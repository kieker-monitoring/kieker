/**
 *
 */
package kieker.analysisteetime.experimental.hotspotdetection;

import java.io.File;

import kieker.analysisteetime.AssemblyModelAssemblerStage;
import kieker.analysisteetime.DeploymentModelAssemblerStage;
import kieker.analysisteetime.TypeModelAssemblerStage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.recordreading.ReadingComposite;
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
 */
public class HotspotDetectionConfiguration extends Configuration {

	public HotspotDetectionConfiguration(final File importDirectory) {

		final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
		final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
		final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();

		// Create the stages
		final ReadingComposite reader = new ReadingComposite(importDirectory);
		// TODO consider if KiekerMetadataRecord has to be processed
		// final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<>(IFlowRecord.class);
		final TypeModelAssemblerStage typeModelAssembler = new TypeModelAssemblerStage(typeModel);
		final AssemblyModelAssemblerStage assemblyModelAssembler = new AssemblyModelAssemblerStage(typeModel, assemblyModel);
		final DeploymentModelAssemblerStage deploymentModelAssembler = new DeploymentModelAssemblerStage(assemblyModel, deploymentModel);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(deploymentModel, false); // TODO second parameter
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
