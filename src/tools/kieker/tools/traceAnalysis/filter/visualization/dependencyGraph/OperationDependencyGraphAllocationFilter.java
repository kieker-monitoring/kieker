/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.SynchronousReplyMessage;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationComponentOperationPairFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AllocationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.OperationRepository;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * Refactored copy from LogAnalysis-legacy tool<br>
 * 
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn, Lena St&ouml;ver, Matthias Rohr,
 */
@Plugin(repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, repositoryType = SystemModelRepository.class),
		outputPorts = @OutputPort(name = OperationDependencyGraphAllocationFilter.OUTPUT_PORT_NAME, eventTypes = { AbstractGraph.class }))
public class OperationDependencyGraphAllocationFilter extends AbstractDependencyGraphFilter<AllocationComponentOperationPair> {
	public static final String CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE = "dotOutputFn";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	public static final String CONFIG_PROPERTY_NAME_SHORT_LABELS = "shortLabels";
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS = "includeSelfLoops";

	public static final String OUTPUT_PORT_NAME = "graphOutput";

	private final String dotOutputFile;
	private final boolean includeWeights;
	private final boolean shortLabels;
	private final boolean includeSelfLoops;

	/**
	 * Creates a new instance of this class using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance.
	 */
	public OperationDependencyGraphAllocationFilter(final Configuration configuration) {
		/* Call the mandatory "default" constructor. */
		super(configuration);

		/* Initialize the necessary fields from the inherited class. */
		super.setDependencyGraph(new OperationAllocationDependencyGraph(new AllocationComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
				OperationRepository.ROOT_OPERATION,
				AllocationRepository.ROOT_ALLOCATION_COMPONENT)));

		/* Initialize from the given configuration. */
		this.dotOutputFile = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE);
		this.includeWeights = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.shortLabels = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS);
		this.includeSelfLoops = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS);
	}

	@Override
	public void terminate(final boolean error) {
		if (!error) {
			this.deliver(OUTPUT_PORT_NAME, this.dependencyGraph);
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, "./OperationDependencyGraph");
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.TRUE.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS, Boolean.FALSE.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.TRUE.toString());

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_DOT_OUTPUT_FILE, this.dotOutputFile);
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, Boolean.toString(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_SELF_LOOPS, Boolean.toString(this.includeSelfLoops));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORT_LABELS, Boolean.toString(this.shortLabels));
		return configuration;
	}

	@Override
	@InputPort(name = AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES, description = "Receives the message traces to be processed", eventTypes = { MessageTrace.class })
	public void inputMessageTraces(final MessageTrace t) {
		for (final AbstractMessage m : t.getSequenceAsVector()) {
			if (m instanceof SynchronousReplyMessage) {
				continue;
			}
			final AllocationComponent senderComponent = m.getSendingExecution().getAllocationComponent();
			final AllocationComponent receiverComponent = m.getReceivingExecution().getAllocationComponent();
			final int rootOperationId = OperationRepository.ROOT_OPERATION.getId();
			final Operation senderOperation = m.getSendingExecution().getOperation();
			final Operation receiverOperation = m.getReceivingExecution().getOperation();
			/* The following two get-calls to the factory return s.th. in either case */
			final AllocationComponentOperationPairFactory pairFactory = this.getSystemEntityFactory().getAllocationPairFactory();
			final AllocationComponentOperationPair senderPair = (senderOperation.getId() == rootOperationId) ? OperationDependencyGraphAllocationFilter.this.dependencyGraph // NOCS
					.getRootNode().getEntity()
					: pairFactory.getPairInstanceByPair(senderComponent, senderOperation);
			final AllocationComponentOperationPair receiverPair = (receiverOperation.getId() == rootOperationId) ? OperationDependencyGraphAllocationFilter.this.dependencyGraph // NOCS
					.getRootNode().getEntity()
					: pairFactory.getPairInstanceByPair(receiverComponent, receiverOperation);

			DependencyGraphNode<AllocationComponentOperationPair> senderNode = OperationDependencyGraphAllocationFilter.this.dependencyGraph.getNode(senderPair
					.getId());
			DependencyGraphNode<AllocationComponentOperationPair> receiverNode = OperationDependencyGraphAllocationFilter.this.dependencyGraph
					.getNode(receiverPair.getId());
			if (senderNode == null) {
				senderNode = new DependencyGraphNode<AllocationComponentOperationPair>(senderPair.getId(), senderPair, t);

				if (m.getSendingExecution().isAssumed()) {
					senderNode.setAssumed();
				}

				OperationDependencyGraphAllocationFilter.this.dependencyGraph.addNode(senderNode.getId(), senderNode);
			}
			else {
				senderNode.addOrigin(t);
			}

			if (receiverNode == null) {
				receiverNode = new DependencyGraphNode<AllocationComponentOperationPair>(receiverPair.getId(), receiverPair, t);

				if (m.getReceivingExecution().isAssumed()) {
					receiverNode.setAssumed();
				}

				OperationDependencyGraphAllocationFilter.this.dependencyGraph.addNode(receiverNode.getId(), receiverNode);
			}
			else {
				receiverNode.addOrigin(t);
			}

			final boolean assumed = this.isDependencyAssumed(senderNode, receiverNode);

			senderNode.addOutgoingDependency(receiverNode, assumed, t);
			receiverNode.addIncomingDependency(senderNode, assumed, t);

			this.invokeDecorators(m, senderNode, receiverNode);
		}
		OperationDependencyGraphAllocationFilter.this.reportSuccess(t.getTraceId());
	}

}
