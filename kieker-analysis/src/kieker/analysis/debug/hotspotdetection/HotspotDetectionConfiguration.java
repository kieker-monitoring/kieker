/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.debug.hotspotdetection;

import java.io.File;
import java.time.temporal.ChronoUnit;

import kieker.analysis.model.AssemblyModelAssemblerStage;
import kieker.analysis.model.DeploymentModelAssemblerStage;
import kieker.analysis.model.OperationAndCallGeneratorStage;
import kieker.analysis.model.TypeModelAssemblerStage;
import kieker.analysis.model.data.OperationEvent;
import kieker.analysis.signature.JavaComponentSignatureExtractor;
import kieker.analysis.signature.JavaOperationSignatureExtractor;
import kieker.analysis.source.file.DirectoryReaderStage;
import kieker.analysis.source.file.DirectoryScannerStage;
import kieker.analysis.stage.flow.FlowTraceEventMatcher;
import kieker.analysis.stage.general.ControlledEventReleaseStage;
import kieker.analysis.trace.reconstruction.FlowRecordTraceReconstructionStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.sources.SourcesFactory;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

import teetime.framework.Configuration;
import teetime.stage.InstanceOfFilter;
import teetime.stage.basic.distributor.Distributor;

/**
 * Configuration for a hotspot detection based on the longest execution time of
 * methods without children.
 *
 * @author Sören Henning, Stephan Lenga
 *
 * @since 1.14
 */
public class HotspotDetectionConfiguration extends Configuration {

	private static final String DYNAMIC_SOURCE = null;

	public HotspotDetectionConfiguration(final File importDirectory) {

		final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
		final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
		final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
		final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();

		// Create the stages
		final DirectoryScannerStage directoryScannerStage = new DirectoryScannerStage(importDirectory);
		final DirectoryReaderStage directoryReaderStage = new DirectoryReaderStage(false, 80860);
		// BETTER consider if KiekerMetadataRecord has to be processed
		// final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<>(
				IFlowRecord.class);

		final Distributor<IFlowRecord> flowRecordDistributor = new Distributor<>();

		final OperationAndCallGeneratorStage operationAndCallGeneratorStage = new OperationAndCallGeneratorStage(true);

		final TypeModelAssemblerStage typeModelAssembler = new TypeModelAssemblerStage(typeModel, sourceModel, DYNAMIC_SOURCE,
				new JavaComponentSignatureExtractor(), new JavaOperationSignatureExtractor());
		final AssemblyModelAssemblerStage assemblyModelAssembler = new AssemblyModelAssemblerStage(typeModel,
				assemblyModel, sourceModel, DYNAMIC_SOURCE);
		final DeploymentModelAssemblerStage deploymentModelAssemblerStage = new DeploymentModelAssemblerStage(assemblyModel,
				deploymentModel, sourceModel, DYNAMIC_SOURCE);

		final ControlledEventReleaseStage<OperationEvent, IFlowRecord> flowRecordMerger = new ControlledEventReleaseStage<>(new FlowTraceEventMatcher());

		final FlowRecordTraceReconstructionStage traceReconstructor = new FlowRecordTraceReconstructionStage(deploymentModel,
				ChronoUnit.NANOS);
		final HotspotDetectionStage hotspotDetector = new HotspotDetectionStage();

		// Connect the stages
		super.connectPorts(directoryScannerStage.getOutputPort(), directoryReaderStage.getInputPort());
		super.connectPorts(directoryReaderStage.getOutputPort(), instanceOfFilter.getInputPort());
		super.connectPorts(instanceOfFilter.getMatchedOutputPort(), flowRecordDistributor.getInputPort());

		super.connectPorts(flowRecordDistributor.getNewOutputPort(), operationAndCallGeneratorStage.getInputPort());
		super.connectPorts(operationAndCallGeneratorStage.getOperationOutputPort(), typeModelAssembler.getInputPort());
		super.connectPorts(typeModelAssembler.getOutputPort(), assemblyModelAssembler.getInputPort());
		super.connectPorts(assemblyModelAssembler.getOutputPort(), deploymentModelAssemblerStage.getInputPort());
		super.connectPorts(deploymentModelAssemblerStage.getOutputPort(), flowRecordMerger.getControlInputPort());
		super.connectPorts(flowRecordDistributor.getNewOutputPort(), flowRecordMerger.getBaseInputPort());
		super.connectPorts(flowRecordMerger.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), hotspotDetector.getInputPort());
	}

}
