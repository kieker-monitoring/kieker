/**
 *
 */
package kieker.analysisteetime;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import kieker.analysisteetime.dependencygraphs.DependencyGraphCreatorStage;
import kieker.analysisteetime.dependencygraphs.DeploymentLevelOperationDependencyGraphBuilderFactory;
import kieker.analysisteetime.dependencygraphs.dot.DotExportConfigurationFactory;
import kieker.analysisteetime.dependencygraphs.vertextypes.VertexTypeMapper;
import kieker.analysisteetime.experimental.DebugStage;
import kieker.analysisteetime.experimental.GraphPrinterStage;
import kieker.analysisteetime.model.ExecutionModelAssemblerStage;
import kieker.analysisteetime.model.ModelObjectFromOperationCallAccesors;
import kieker.analysisteetime.model.StaticModelsAssemblerStage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.recordreading.AllowedRecordsFilter;
import kieker.analysisteetime.recordreading.ReadingComposite;
import kieker.analysisteetime.signature.JavaFullComponentNameBuilder;
import kieker.analysisteetime.signature.JavaShortOperationNameBuilder;
import kieker.analysisteetime.signature.SignatureExtractor;
import kieker.analysisteetime.statistics.CallStatisticsStage;
import kieker.analysisteetime.statistics.FullStatisticsDecoratorStage;
import kieker.analysisteetime.statistics.Statistics;
import kieker.analysisteetime.trace.graph.TraceToGraphTransformerStage;
import kieker.analysisteetime.trace.graph.dot.DotTraceGraphFileWriterStage;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.analysisteetime.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.export.dot.DotFileWriterStage;
import kieker.analysisteetime.util.graph.export.graphml.GraphMLFileWriterStage;
import kieker.analysisteetime.util.stage.trigger.TriggerOnTerminationStage;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.Configuration;
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
	private final Map<Object, Statistics> statisticsModel = new HashMap<>();
	private final SignatureExtractor signatureExtractor = SignatureExtractor.forJava();

	public ExampleConfiguration(final File importDirectory, final File exportDirectory) {

		// Create the stages
		final ReadingComposite reader = new ReadingComposite(importDirectory);
		// TODO consider if KiekerMetadataRecord has to be processed
		final DebugStage<IMonitoringRecord> debugRecordsStage = new DebugStage<>();
		final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final StaticModelsAssemblerStage staticModelsAssembler = new StaticModelsAssemblerStage(this.typeModel, this.assemblyModel, this.deploymentModel,
				this.signatureExtractor);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(this.deploymentModel, false, ChronoUnit.NANOS); // TODO second parameter,
																																		// NANOS temp
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final OperationCallExtractorStage operationCallExtractor = new OperationCallExtractorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(this.executionModel);
		final FullStatisticsDecoratorStage fullStatisticsDecorator = new FullStatisticsDecoratorStage(this.statisticsModel,
				ModelObjectFromOperationCallAccesors.DEPLOYED_OPERATION);
		final CallStatisticsStage callStatisticsDecorator = new CallStatisticsStage(this.statisticsModel, this.executionModel);

		final TraceToGraphTransformerStage traceToGraphTransformer = new TraceToGraphTransformerStage();
		final DotTraceGraphFileWriterStage dotTraceGraphFileWriter = DotTraceGraphFileWriterStage.create(exportDirectory);
		final GraphMLFileWriterStage graphMLTraceGraphFileWriter = new GraphMLFileWriterStage(exportDirectory.getPath());
		final Distributor<Trace> traceDistributor = new Distributor<>();
		final TriggerOnTerminationStage onTerminationTrigger = new TriggerOnTerminationStage();
		final DependencyGraphCreatorStage dependencyGraphCreator = new DependencyGraphCreatorStage(this.executionModel, this.statisticsModel,
				new DeploymentLevelOperationDependencyGraphBuilderFactory());
		final DotFileWriterStage dotDepGraphFileWriter = new DotFileWriterStage(exportDirectory.getPath(),
				(new DotExportConfigurationFactory(new JavaFullComponentNameBuilder(), new JavaShortOperationNameBuilder(), VertexTypeMapper.DEFAULT))
						.createForDeploymentLevelOperationDependencyGraph());

		final AbstractConsumerStage<Graph> debugStage = new GraphPrinterStage();

		// Connect the stages
		super.connectPorts(reader.getOutputPort(), debugRecordsStage.getInputPort());
		super.connectPorts(debugRecordsStage.getOutputPort(), allowedRecordsFilter.getInputPort());
		super.connectPorts(allowedRecordsFilter.getOutputPort(), staticModelsAssembler.getInputPort());
		super.connectPorts(staticModelsAssembler.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
		super.connectPorts(traceStatisticsDecorator.getOutputPort(), traceDistributor.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), traceToGraphTransformer.getInputPort());
		super.connectPorts(traceToGraphTransformer.getOutputPort(), dotTraceGraphFileWriter.getInputPort());
		// super.connectPorts(traceToGraphTransformer.getOutputPort(), graphMLTraceGraphFileWriter.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), operationCallExtractor.getInputPort());
		super.connectPorts(operationCallExtractor.getOutputPort(), executionModelAssembler.getInputPort());
		super.connectPorts(executionModelAssembler.getOutputPort(), fullStatisticsDecorator.getInputPort());
		super.connectPorts(fullStatisticsDecorator.getOutputPort(), callStatisticsDecorator.getInputPort());
		super.connectPorts(callStatisticsDecorator.getOutputPort(), onTerminationTrigger.getInputPort());
		super.connectPorts(onTerminationTrigger.getOutputPort(), dependencyGraphCreator.getInputPort());
		// super.connectPorts(dependencyGraphCreator.getOutputPort(), debugStage.getInputPort());
		super.connectPorts(dependencyGraphCreator.getOutputPort(), dotDepGraphFileWriter.getInputPort());

	}

	public DeploymentModel getDeploymentModel() {
		return this.deploymentModel;
	}

}
