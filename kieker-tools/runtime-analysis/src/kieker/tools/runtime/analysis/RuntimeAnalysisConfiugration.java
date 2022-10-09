/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.runtime.analysis;

import kieker.analysis.generic.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.util.classpath.InstantiationFactory;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;

import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeallocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.analysis.feature.IContainerManagementSinkStage;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.general.DynamicEventDispatcher;
import org.iobserve.stages.general.IEventMatcher;
import org.iobserve.stages.general.ImplementsEventMatcher;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a generic configuration for all analyses.
 *
 * Note: We already added lines to deallocation. However, deallocation is currently missing.
 *
 * @author Reiner Jung
 *
 */
public class RuntimeAnalysisConfiguration extends Configuration {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeAnalysisConfiguration.class);

	private static final String DELIMETER = ",";

	private AllocationStage allocationStage;

	private DeploymentCompositeStage deploymentStage;

	private UndeploymentCompositeStage undeploymentStage;

	private final DynamicEventDispatcher eventDispatcher;

	private DeallocationStage deallocationStage;

	/**
     * Create an analysis configuration, configured by a configuration object.
     *
     * @param configuration
     *            Kieker configuration
     * @param repositoryModelResource
     *            repository resource
     * @param resourceEnvironmentModelResource
     *            resource environment
     * @param systemModelResource
     *            system model
     * @param allocationModelResource
     *            allocation model
     * @param usageModelResource
     *            usage model
     * @param correspondenceModelResource
     *            correspondence model
     * @throws ConfigurationException
     *             on error
     */
    public AnalysisConfiguration(final kieker.common.configuration.Configuration configuration,
            final Neo4JModelResource<Repository> repositoryModelResource,
            final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource,
            final Neo4JModelResource<System> systemModelResource,
            final Neo4JModelResource<Allocation> allocationModelResource,
            final Neo4JModelResource<UsageModel> usageModelResource,
            final Neo4JModelResource<CorrespondenceModel> correspondenceModelResource) throws ConfigurationException {
        this.eventDispatcher = new DynamicEventDispatcher(null, true, true, false);

        /** Source stage. */
        final String sourceClassName = configuration.getStringProperty(ConfigurationKeys.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createWithConfiguration(ISourceCompositeStage.class, sourceClassName, configuration);

            this.connectPorts(sourceCompositeStage.getOutputPort(), this.eventDispatcher.getInputPort());

            this.traceProcessing(configuration);
            this.geoLocation(configuration);

            this.containerManagement(configuration, resourceEnvironmentModelResource, systemModelResource,
                    allocationModelResource, correspondenceModelResource);
        } else {
            AnalysisConfiguration.LOGGER.error("Initialization incomplete: No source stage specified.");
            throw new ConfigurationException("Initialization incomplete: No source stage specified.");
        }
    }

	/**
     * Model less config.
     *
     * @param configuration
     *            configuration
     * @throws ConfigurationException
     *             on error
     */
    public AnalysisConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        this(configuration, null, null, null, null, null, null);
    }

	/**
	 * Create all stages necessary for the container management.
	 *
	 * @param configuration
	 *            potential configuration parameter for filters
	 * @param resourceEnvironmentModelResource
	 * @param systemModelResource
	 * @param allocationModelResource
	 * @param correspondenceModelResource
	 *
	 * @throws ConfigurationException
	 *             when configuration fails
	 */
	private void containerManagement(final kieker.common.configuration.Configuration configuration,
			final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource,
			final Neo4JModelResource<System> systemModelResource,
			final Neo4JModelResource<Allocation> allocationModelResource,
			final Neo4JModelResource<CorrespondenceModel> correspondenceModelResource) throws ConfigurationException {
		if (configuration.getBooleanProperty(ConfigurationKeys.CONTAINER_MANAGEMENT, false)) {

			/** allocation. */
			this.allocationStage = new AllocationStage(resourceEnvironmentModelResource);
			final IEventMatcher<IAllocationEvent> allocationMatcher = new ImplementsEventMatcher<>(
					IAllocationEvent.class, null);
			/** connect ports. */
			this.eventDispatcher.registerOutput(allocationMatcher);
			this.connectPorts(allocationMatcher.getOutputPort(), this.allocationStage.getInputPort());

			/** deallocation. */
			this.deallocationStage = new DeallocationStage(resourceEnvironmentModelResource);
			/** connect ports. */
			final IEventMatcher<IDeallocationEvent> deallocationMatcher = new ImplementsEventMatcher<>(
					IDeallocationEvent.class, null);
			this.eventDispatcher.registerOutput(deallocationMatcher);
			this.connectPorts(deallocationMatcher.getOutputPort(), this.deallocationStage.getInputPort());

			/** deployment. */
			final IEventMatcher<IDeployedEvent> deployedEventMatcher = new ImplementsEventMatcher<>(
					IDeployedEvent.class, null);
			this.eventDispatcher.registerOutput(deployedEventMatcher);
			this.deploymentStage = new DeploymentCompositeStage(resourceEnvironmentModelResource, systemModelResource,
					allocationModelResource, correspondenceModelResource);
			/** connect ports. */
			this.connectPorts(deployedEventMatcher.getOutputPort(), this.deploymentStage.getDeployedInputPort());

			/** undeployment. */
			final IEventMatcher<IUndeployedEvent> undeployedEventMatcher = new ImplementsEventMatcher<>(
					IUndeployedEvent.class, null);
			this.eventDispatcher.registerOutput(undeployedEventMatcher);
			this.undeploymentStage = new UndeploymentCompositeStage(resourceEnvironmentModelResource,
					systemModelResource, allocationModelResource, correspondenceModelResource);
			/** connect ports. */
			this.connectPorts(undeployedEventMatcher.getOutputPort(), this.undeploymentStage.getUndeployedInputPort());

			/** dependent features. */
			this.createContainerManagementSink(configuration, resourceEnvironmentModelResource, systemModelResource,
					allocationModelResource);
		}

	}

	/**
	 * Create sinks for container management visualization.
	 *
	 * @param configuration
	 *            configuration object
	 * @param resourceEnvironmentModelResource
	 * @param allocationModelResource
	 * @param systemModelResource
	 *
	 * @throws ConfigurationException
	 *             when configuration fails
	 */
	private void createContainerManagementSink(final kieker.common.configuration.Configuration configuration,
			final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource,
			final Neo4JModelResource<System> systemModelResource,
			final Neo4JModelResource<Allocation> allocationModelResource) throws ConfigurationException {
		final String[] containerManagementSinks = configuration
				.getStringArrayProperty(ConfigurationKeys.CONTAINER_MANAGEMENT_SINK, AnalysisConfiguration.DELIMETER);
		if (containerManagementSinks.length == 1) {
			final IContainerManagementSinkStage containerManagementSinksStage = InstantiationFactory
					.createWithConfiguration(IContainerManagementSinkStage.class, containerManagementSinks[0],
							configuration);
			/** connect ports. */
			this.connectPorts(this.allocationStage.getAllocationNotifyOutputPort(),
					containerManagementSinksStage.getAllocationInputPort());
			this.connectPorts(this.deallocationStage.getDeallocationNotifyOutputPort(),
					containerManagementSinksStage.getDeallocationInputPort());
			this.connectPorts(this.deploymentStage.getDeployedOutputPort(),
					containerManagementSinksStage.getDeployedInputPort());
			this.connectPorts(this.undeploymentStage.getUndeployedOutputPort(),
					containerManagementSinksStage.getUndeployedInputPort());
		} else if (containerManagementSinks.length > 1) {
			/** In case of multiple outputs, we require distributors. */
			final Distributor<ResourceContainer> allocationDistributor = new Distributor<>(
					new CopyByReferenceStrategy());
			final Distributor<ResourceContainer> deallocationDistributor = new Distributor<>(
					new CopyByReferenceStrategy());
			final Distributor<PCMDeployedEvent> deploymentDistributor = new Distributor<>(
					new CopyByReferenceStrategy());
			final Distributor<PCMUndeployedEvent> undeploymentDistributor = new Distributor<>(
					new CopyByReferenceStrategy());

			/** link distributor to container management. */
			this.connectPorts(this.allocationStage.getAllocationNotifyOutputPort(),
					allocationDistributor.getInputPort());
			this.connectPorts(this.deallocationStage.getDeallocationNotifyOutputPort(),
					deallocationDistributor.getInputPort());
			this.connectPorts(this.deploymentStage.getDeployedOutputPort(), deploymentDistributor.getInputPort());
			this.connectPorts(this.undeploymentStage.getUndeployedOutputPort(), undeploymentDistributor.getInputPort());

			/** Create and connect sinks. */
			for (final String containerManagementSink : containerManagementSinks) {
				final IContainerManagementSinkStage containerManagementSinksStage = InstantiationFactory
						.getInstance(configuration).create(IContainerManagementSinkStage.class, containerManagementSink,
								IContainerManagementSinkStage.SIGNATURE, configuration,
								resourceEnvironmentModelResource, allocationModelResource, systemModelResource);
				/** connect ports. */
				this.connectPorts(allocationDistributor.getNewOutputPort(),
						containerManagementSinksStage.getAllocationInputPort());
				this.connectPorts(deallocationDistributor.getNewOutputPort(),
						containerManagementSinksStage.getDeallocationInputPort());
				this.connectPorts(deploymentDistributor.getNewOutputPort(),
						containerManagementSinksStage.getDeployedInputPort());
				this.connectPorts(undeploymentDistributor.getNewOutputPort(),
						containerManagementSinksStage.getUndeployedInputPort());
			}
		} else {
			AnalysisConfiguration.LOGGER.warn(
					"No container management sinks specified. Therefore, deployment and allocation changes will not be communicated.");
		}
	}

	/**
	 * Create trace related filter chains.
	 *
	 * @param configuration
	 *            filter configurations
	 * @throws ConfigurationException
	 *             when configuration fails
	 */
	private void traceProcessing(final kieker.common.configuration.Configuration configuration)
			throws ConfigurationException {
		if (configuration.getBooleanProperty(ConfigurationKeys.TRACES, false)) {
			final TraceReconstructionCompositeStage traceReconstructionStage = new TraceReconstructionCompositeStage(
					configuration);

			OutputPort<EventBasedTrace> behaviorClusteringEventBasedTracePort = traceReconstructionStage
					.getTraceValidOutputPort();
			OutputPort<EventBasedTrace> dataFlowEventBasedTracePort = traceReconstructionStage
					.getTraceValidOutputPort();

			/** Connect ports. */
			final IEventMatcher<IFlowRecord> flowRecordMatcher = new ImplementsEventMatcher<>(IFlowRecord.class, null);
			this.eventDispatcher.registerOutput(flowRecordMatcher);
			this.connectPorts(flowRecordMatcher.getOutputPort(), traceReconstructionStage.getInputPort());

			/** Include distributor to support tow simultaneous sinks. */
			if (configuration.getBooleanProperty(ConfigurationKeys.DATA_FLOW, false)
					&& !configuration.getStringProperty(ConfigurationKeys.BEHAVIOR_CLUSTERING).isEmpty()) {
				final IDistributorStrategy strategy = new CopyByReferenceStrategy();
				final Distributor<EventBasedTrace> distributor = new Distributor<>(strategy);
				this.connectPorts(traceReconstructionStage.getTraceValidOutputPort(), distributor.getInputPort());

				behaviorClusteringEventBasedTracePort = distributor.getNewOutputPort();
				dataFlowEventBasedTracePort = distributor.getNewOutputPort();
			}

			/** Initialize depending features. */
			this.behaviorClustering(configuration, behaviorClusteringEventBasedTracePort);
			this.dataflow(configuration, dataFlowEventBasedTracePort);
		}
	}

	/**
	 * Create data flow processing setup.
	 *
	 * @param configuration
	 * @param eventBasedTraceOutputPort
	 */
	private void dataflow(final kieker.common.configuration.Configuration configuration,
			final OutputPort<EventBasedTrace> eventBasedTraceOutputPort) {
		if (configuration.getBooleanProperty(ConfigurationKeys.DATA_FLOW, false)) {
			/** connect ports. */
			// this.connectPorts(eventBasedTraceOutputPort, targetPort);
			AnalysisConfiguration.LOGGER.warn("Configuration for dataflow analysis missing.");
		}
	}

	/**
	 * Create the behavioral clustering.
	 *
	 * @param configuration
	 *            analysis configuration
	 * @throws ConfigurationException
	 *             when filter configuration fails
	 */
	private void behaviorClustering(final kieker.common.configuration.Configuration configuration,
			final OutputPort<EventBasedTrace> eventBasedTraceOutputPort) throws ConfigurationException {
		final String behaviorClustringClassName = configuration
				.getStringProperty(ConfigurationKeys.BEHAVIOR_CLUSTERING);
		if (!behaviorClustringClassName.isEmpty()) {
			final IBehaviorCompositeStage behavior = InstantiationFactory
					.createWithConfiguration(IBehaviorCompositeStage.class, behaviorClustringClassName, configuration);

			final IEventMatcher<ISessionEvent> sessionMatcher = new ImplementsEventMatcher<>(ISessionEvent.class, null);
			this.eventDispatcher.registerOutput(sessionMatcher);

			this.connectPorts(eventBasedTraceOutputPort, behavior.getEventBasedTraceInputPort());
			this.connectPorts(sessionMatcher.getOutputPort(), behavior.getSessionEventInputPort());

			if (configuration.getBooleanProperty(ConfigurationKeys.BEHAVIOR_CLUSTERING_SINK)) {
				// TODO needs visualization trigger
				AnalysisConfiguration.LOGGER.warn("Configuration for behavior sink missing.");
			}
		}
	}

	private void geoLocation(final kieker.common.configuration.Configuration configuration) {
		if (configuration.getBooleanProperty(ConfigurationKeys.GEO_LOCATION, false)) {
			AnalysisConfiguration.LOGGER.warn("Configuration for geolocation.");
		}
	}

	public DynamicEventDispatcher getEventDispatcher() {
		return this.eventDispatcher;
	}
}
