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

package kieker.analysisteetime.experimental.hotspotdetection;

import java.io.File;
import java.time.temporal.ChronoUnit;

import kieker.analysisteetime.model.AssemblyModelAssemblerStage;
import kieker.analysisteetime.model.DeploymentModelAssemblerStage;
import kieker.analysisteetime.model.TypeModelAssemblerStage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.recordreading.ReadingComposite;
import kieker.analysisteetime.signature.JavaComponentSignatureExtractor;
import kieker.analysisteetime.signature.JavaOperationSignatureExtractor;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.Configuration;
import teetime.stage.InstanceOfFilter;

/**
 * Configuration for a hotspot detection based on the longest execution time of methods without children.
 *
 * @author Sören Henning, Stephan Lenga
 *
 * @since 1.14
 */
public class HotspotDetectionConfiguration extends Configuration {

	public HotspotDetectionConfiguration(final File importDirectory) {

		final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
		final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
		final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();

		// Create the stages
		final ReadingComposite reader = new ReadingComposite(importDirectory);
		// BETTER consider if KiekerMetadataRecord has to be processed
		// final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<>(IFlowRecord.class);
		final TypeModelAssemblerStage typeModelAssembler = new TypeModelAssemblerStage(typeModel, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());
		final AssemblyModelAssemblerStage assemblyModelAssembler = new AssemblyModelAssemblerStage(typeModel, assemblyModel);
		final DeploymentModelAssemblerStage deploymentModelAssembler = new DeploymentModelAssemblerStage(assemblyModel, deploymentModel);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(deploymentModel, ChronoUnit.NANOS);
		final HotspotDetectionStage hotspotDetector = new HotspotDetectionStage();

		// Connect the stages
		super.connectPorts(reader.getOutputPort(), instanceOfFilter.getInputPort());
		super.connectPorts(instanceOfFilter.getMatchedOutputPort(), typeModelAssembler.getInputPort());
		super.connectPorts(typeModelAssembler.getOutputPort(), assemblyModelAssembler.getInputPort());
		super.connectPorts(assemblyModelAssembler.getOutputPort(), deploymentModelAssembler.getInputPort());
		super.connectPorts(deploymentModelAssembler.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), hotspotDetector.getInputPort());

	}

}
