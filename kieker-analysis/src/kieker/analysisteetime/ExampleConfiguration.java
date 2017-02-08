/**
 *
 */
package kieker.analysisteetime;

import java.io.File;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.recordreading.ReadingComposite;
import kieker.analysisteetime.trace.graph.TraceToGraphTransformerStage;
import kieker.analysisteetime.trace.graph.dot.DotTraceGraphFileWriterStage;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.analysisteetime.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysisteetime.util.stage.trigger.TriggerOnTerminationStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.Configuration;
import teetime.stage.InstanceOfFilter;
import teetime.stage.basic.distributor.Distributor;

/**
 *
 * Example configuration for testing the current development.
 *
 * @author Sören Henning
 */
public class ExampleConfiguration extends Configuration {

	private final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
	private final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
	private final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();

	public ExampleConfiguration(final File importDirectory, final File exportDirectory) {

		// TODO Model creation should be available as composite stage

		// Create the stages
		final ReadingComposite reader = new ReadingComposite(importDirectory);
		// TODO consider if KiekerMetadataRecord has to be processed
		// final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<>(IFlowRecord.class);
		final TypeModelAssemblerStage typeModelAssembler = new TypeModelAssemblerStage(this.typeModel, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());
		final AssemblyModelAssemblerStage assemblyModelAssembler = new AssemblyModelAssemblerStage(this.typeModel, this.assemblyModel);
		final DeploymentModelAssemblerStage deploymentModelAssembler = new DeploymentModelAssemblerStage(this.assemblyModel, this.deploymentModel);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(this.deploymentModel, false); // TODO second parameter
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(this.executionModel);
		final TraceToGraphTransformerStage traceToGraphTransformer = new TraceToGraphTransformerStage();
		final DotTraceGraphFileWriterStage dotTraceGraphFileWriter = DotTraceGraphFileWriterStage.create(exportDirectory);
		final Distributor<Trace> traceDistributor = new Distributor<>();
		final TriggerOnTerminationStage triggerOnTerminationStage = new TriggerOnTerminationStage();

		// Connect the stages
		super.connectPorts(reader.getOutputPort(), instanceOfFilter.getInputPort());
		super.connectPorts(instanceOfFilter.getMatchedOutputPort(), typeModelAssembler.getInputPort());
		super.connectPorts(typeModelAssembler.getOutputPort(), assemblyModelAssembler.getInputPort());
		super.connectPorts(assemblyModelAssembler.getOutputPort(), deploymentModelAssembler.getInputPort());
		super.connectPorts(deploymentModelAssembler.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
		super.connectPorts(traceStatisticsDecorator.getOutputPort(), traceDistributor.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), traceToGraphTransformer.getInputPort());
		super.connectPorts(traceToGraphTransformer.getOutputPort(), dotTraceGraphFileWriter.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), executionModelAssembler.getInputPort());
		super.connectPorts(executionModelAssembler.getOutputPort(), triggerOnTerminationStage.getInputPort());

	}

	public DeploymentModel getDeploymentModel() {
		return this.deploymentModel;
	}

}
