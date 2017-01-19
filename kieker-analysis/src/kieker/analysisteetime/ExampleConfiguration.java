/**
 *
 */
package kieker.analysisteetime;

import java.io.File;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentRoot;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeRoot;
import kieker.analysisteetime.recordreading.ReadingComposite;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.analysisteetime.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.Configuration;
import teetime.stage.InstanceOfFilter;

/**
 *
 * Example configuration for testing the current development.
 *
 * @author Sören Henning
 */
public class ExampleConfiguration extends Configuration {

	private final TypeRoot typeRoot = TypeFactory.eINSTANCE.createTypeRoot();
	private final AssemblyRoot assemblyRoot = AssemblyFactory.eINSTANCE.createAssemblyRoot();
	private final DeploymentRoot deploymentRoot = DeploymentFactory.eINSTANCE.createDeploymentRoot();
	private final ExecutionRoot executionRoot = ExecutionFactory.eINSTANCE.createExecutionRoot();

	public ExampleConfiguration(final File importDirectory) {

		// TODO Model creation should be available as composite stage

		// Create the stages
		final ReadingComposite reader = new ReadingComposite(importDirectory);
		// TODO consider if KiekerMetadataRecord has to be processed
		// final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<>(IFlowRecord.class);
		final TypeModelAssemblerStage typeModelAssembler = new TypeModelAssemblerStage(this.typeRoot);
		final AssemblyModelAssemblerStage assemblyModelAssembler = new AssemblyModelAssemblerStage(this.typeRoot, this.assemblyRoot);
		final DeploymentModelAssemblerStage deploymentModelAssembler = new DeploymentModelAssemblerStage(this.assemblyRoot, this.deploymentRoot);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(this.deploymentRoot, false); // TODO second parameter
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(this.executionRoot);

		// Connect the stages
		super.connectPorts(reader.getOutputPort(), instanceOfFilter.getInputPort());
		super.connectPorts(instanceOfFilter.getMatchedOutputPort(), typeModelAssembler.getInputPort());
		super.connectPorts(typeModelAssembler.getOutputPort(), assemblyModelAssembler.getInputPort());
		super.connectPorts(assemblyModelAssembler.getOutputPort(), deploymentModelAssembler.getInputPort());
		super.connectPorts(deploymentModelAssembler.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
		super.connectPorts(traceStatisticsDecorator.getOutputPort(), executionModelAssembler.getInputPort());

	}

	public DeploymentRoot getDeploymentRoot() {
		return this.deploymentRoot;
	}

}
