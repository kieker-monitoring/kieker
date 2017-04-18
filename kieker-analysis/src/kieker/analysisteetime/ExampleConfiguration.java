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
import kieker.analysisteetime.statistics.FullStatisticsDecoratorStage;
import kieker.analysisteetime.statistics.Statistics;
import kieker.analysisteetime.trace.graph.TraceToGraphTransformerStage;
import kieker.analysisteetime.trace.graph.dot.DotTraceGraphFileWriterStage;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.analysisteetime.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.export.dot.DotFileWriterStage;
import kieker.analysisteetime.util.graph.export.graphml.GraphMLFileWriterStage;
import kieker.analysisteetime.util.stage.trigger.TriggerOnTerminationStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.AbstractConsumerStage;
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
	private final Map<Object, Statistics> deploymedOperationsStatisticsModel = new HashMap<>();

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
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(this.deploymentModel, false, ChronoUnit.NANOS); // TODO second parameter,
																																		// NANOS temp
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final OperationCallExtractorStage operationCallExtractor = new OperationCallExtractorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(this.executionModel);
		final FullStatisticsDecoratorStage fullStatisticsDecorator = new FullStatisticsDecoratorStage(this.deploymedOperationsStatisticsModel,
				ModelObjectFromOperationCallAccesors.DEPLOYED_OPERATION);

		final TraceToGraphTransformerStage traceToGraphTransformer = new TraceToGraphTransformerStage();
		final DotTraceGraphFileWriterStage dotTraceGraphFileWriter = DotTraceGraphFileWriterStage.create(exportDirectory);
		final GraphMLFileWriterStage graphMLTraceGraphFileWriter = new GraphMLFileWriterStage(exportDirectory.getPath());
		final Distributor<Trace> traceDistributor = new Distributor<>();
		final TriggerOnTerminationStage onTerminationTrigger = new TriggerOnTerminationStage();
		final DependencyGraphCreatorStage dependencyGraphCreator = new DependencyGraphCreatorStage(this.executionModel,
				new DeploymentLevelOperationDependencyGraphBuilderFactory());
		final DotFileWriterStage dotDepGraphFileWriter = new DotFileWriterStage(exportDirectory.getPath(),
				(new DotExportConfigurationFactory(new JavaFullComponentNameBuilder(), new JavaShortOperationNameBuilder(), VertexTypeMapper.DEFAULT))
						.createForDeploymentLevelOperationDependencyGraph());
		final AbstractConsumerStage<Graph> debugStage = new AbstractConsumerStage<Graph>() {
			@Override
			protected void execute(final Graph graph) {
				for (final Vertex vertex : graph.getVertices()) {
					System.out.println("Vertices:");
					System.out.println(vertex.getId());
					System.out.println("    Vertices:");
					for (final Vertex vertex1 : vertex.getChildGraph().getVertices()) {
						System.out.println("    " + vertex1.getId());
						System.out.println("        Vertices:");
						for (final Vertex vertex2 : vertex1.getChildGraph().getVertices()) {
							System.out.println("        " + vertex2.getId());
						}
						System.out.println("        Edges:");
						for (final Edge edge2 : vertex1.getChildGraph().getEdges()) {
							System.out.println("        " + edge2.getVertex(Direction.OUT).getId() + "->" + edge2.getVertex(Direction.IN).getId());
						}
					}
					System.out.println("    Edges:");
					for (final Edge edge1 : vertex.getChildGraph().getEdges()) {
						System.out.println("    " + edge1.getVertex(Direction.OUT).getId() + "->" + edge1.getVertex(Direction.IN).getId());
					}
				}
				System.out.println("Edges:");
				for (final Edge edge : graph.getEdges()) {
					System.out.println(edge.getVertex(Direction.OUT).getId() + "->" + edge.getVertex(Direction.IN).getId());
				}
			}
		};

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
		// super.connectPorts(traceToGraphTransformer.getOutputPort(), graphMLTraceGraphFileWriter.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), operationCallExtractor.getInputPort());
		super.connectPorts(operationCallExtractor.getOutputPort(), executionModelAssembler.getInputPort());
		super.connectPorts(executionModelAssembler.getOutputPort(), fullStatisticsDecorator.getInputPort());
		super.connectPorts(fullStatisticsDecorator.getOutputPort(), onTerminationTrigger.getInputPort());
		super.connectPorts(onTerminationTrigger.getOutputPort(), dependencyGraphCreator.getInputPort());
		// super.connectPorts(dependencyGraphCreator.getOutputPort(), debugStage.getInputPort());
		// super.connectPorts(dependencyGraphCreator.getOutputPort(), dotDepGraphFileWriter.getInputPort());

	}

	public DeploymentModel getDeploymentModel() {
		return this.deploymentModel;
	}

}
