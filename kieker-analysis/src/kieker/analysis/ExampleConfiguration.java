/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis;

import java.io.File;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;

import kieker.analysis.graph.dependency.DependencyGraphCreatorStage;
import kieker.analysis.graph.dependency.DeploymentLevelOperationDependencyGraphBuilderFactory;
import kieker.analysis.graph.dependency.IDependencyGraphBuilderConfiguration;
import kieker.analysis.graph.dependency.dot.DotExportConfigurationFactory;
import kieker.analysis.graph.dependency.vertextypes.IVertexTypeMapper;
import kieker.analysis.graph.export.dot.DotExportConfiguration;
import kieker.analysis.graph.export.dot.DotFileWriterStage;
import kieker.analysis.signature.NameBuilder;
import kieker.analysis.signature.SignatureExtractor;
import kieker.analysis.source.file.DirectoryReaderStage;
import kieker.analysis.source.file.DirectoryScannerStage;
import kieker.analysis.stage.model.CallEvent2OperationCallStage;
import kieker.analysis.stage.model.ExecutionModelAssembler;
import kieker.analysis.stage.model.ExecutionModelAssemblerStage;
import kieker.analysis.stage.model.ModelObjectFromOperationCallAccessorUtils;
import kieker.analysis.stage.model.ModelRepository;
import kieker.analysis.stage.model.OperationAndCallGeneratorStage;
import kieker.analysis.stage.model.OperationPresentInModelEventReleaseControlStage;
import kieker.analysis.stage.model.StaticModelsAssemblerStage;
import kieker.analysis.stage.model.data.OperationCallDurationEvent;
import kieker.analysis.statistics.CallStatisticsStage;
import kieker.analysis.statistics.FullResponseTimeStatisticsStage;
import kieker.analysis.trace.graph.TraceToGraphTransformerStage;
import kieker.analysis.trace.graph.dot.DotTraceGraphFileWriterStage;
import kieker.analysis.trace.reconstruction.FlowRecordTraceReconstructionStage;
import kieker.analysis.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysis.util.stage.AllowedRecordsFilter;
import kieker.analysis.util.stage.trigger.TriggerOnTerminationStage;
import kieker.common.record.flow.IFlowRecord;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.sources.SourcesFactory;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 *
 * Example configuration for testing the current development.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ExampleConfiguration extends Configuration {

	private static final String DYNAMIC_SOURCE = "dynamic-source";

	private final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
	private final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
	private final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = StatisticsFactory.eINSTANCE.createStatisticsModel();
	private final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();
	private final SignatureExtractor signatureExtractor = SignatureExtractor.forJava();

	public ExampleConfiguration(final File importDirectory, final Path exportDirectory) {

		final TemporalUnit timeUnitOfRecods = ChronoUnit.NANOS;
		final Function<OperationCallDurationEvent, EObject> statisticsObjectAccesor = ModelObjectFromOperationCallAccessorUtils.DEPLOYED_OPERATION;
		final DeploymentLevelOperationDependencyGraphBuilderFactory deploymentGraphBuilderFactory = new DeploymentLevelOperationDependencyGraphBuilderFactory();
		final DotExportConfiguration dependencyGraphDotExportConfiguration = new DotExportConfigurationFactory(
				NameBuilder.forJavaShortOperations(), IVertexTypeMapper.TO_STRING)
						.createForDeploymentLevelOperationDependencyGraph();

		final ModelRepository repository = new ModelRepository("Example");
		repository.register(TypeModel.class, this.typeModel);
		repository.register(AssemblyModel.class, this.assemblyModel);
		repository.register(DeploymentModel.class, this.deploymentModel);
		repository.register(ExecutionModel.class, this.executionModel);
		repository.register(StatisticsModel.class, this.statisticsModel);
		repository.register(SourceModel.class, this.sourceModel);

		// Create the stages
		final DirectoryScannerStage directoryScannerStage = new DirectoryScannerStage(importDirectory);
		final DirectoryReaderStage directoryReaderStage = new DirectoryReaderStage(false, 80860);
		// BETTER consider if KiekerMetadataRecord has to be processed
		// final DebugStage<IMonitoringRecord> debugRecordsStage = new
		// DebugStage<>();
		final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();

		final Distributor<IFlowRecord> flowRecordDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final OperationAndCallGeneratorStage operationAndCallGeneratorStage = new OperationAndCallGeneratorStage(true);

		final StaticModelsAssemblerStage staticModelsAssemblerStage = new StaticModelsAssemblerStage(this.typeModel,
				this.assemblyModel, this.deploymentModel, this.sourceModel, DYNAMIC_SOURCE, this.signatureExtractor);

		final OperationPresentInModelEventReleaseControlStage operationPresentInModelEventReleaseControlStage = new OperationPresentInModelEventReleaseControlStage(
				this.deploymentModel);

		final CallEvent2OperationCallStage callEvent2OperationCallStage = new CallEvent2OperationCallStage(repository.getModel(DeploymentModel.class));
		final ExecutionModelAssemblerStage executionModelAssemblerStage = new ExecutionModelAssemblerStage(
				new ExecutionModelAssembler(this.executionModel, this.sourceModel, DYNAMIC_SOURCE));
		final CallStatisticsStage callStatisticsStage = new CallStatisticsStage(this.statisticsModel,
				this.executionModel);

		// Works on a trace
		final FlowRecordTraceReconstructionStage traceReconstructor = new FlowRecordTraceReconstructionStage(this.deploymentModel,
				timeUnitOfRecods);
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final FullResponseTimeStatisticsStage fullStatisticsDecorator = new FullResponseTimeStatisticsStage(
				this.statisticsModel, statisticsObjectAccesor);

		final TraceToGraphTransformerStage traceToGraphTransformer = new TraceToGraphTransformerStage();
		final DotTraceGraphFileWriterStage dotTraceGraphFileWriter = DotTraceGraphFileWriterStage
				.create(exportDirectory);
		// alternative output stage
		// final GraphMLFileWriterStage graphMLTraceGraphFileWriter = new GraphMLFileWriterStage(exportDirectory);
		final TriggerOnTerminationStage<ModelRepository> onTerminationTrigger = new TriggerOnTerminationStage(repository);

		final DependencyGraphCreatorStage<IDependencyGraphBuilderConfiguration> dependencyGraphCreator = new DependencyGraphCreatorStage<>(
				new IDependencyGraphBuilderConfiguration() {
				},
				deploymentGraphBuilderFactory);
		final DotFileWriterStage dotDepGraphFileWriter = new DotFileWriterStage(exportDirectory,
				dependencyGraphDotExportConfiguration);

		// final AbstractConsumerStage<Graph> debugStage = new
		// GraphPrinterStage();

		// Connect the stages
		super.connectPorts(directoryScannerStage.getOutputPort(), directoryReaderStage.getInputPort());
		super.connectPorts(directoryReaderStage.getOutputPort(), allowedRecordsFilter.getInputPort());
		super.connectPorts(allowedRecordsFilter.getOutputPort(), flowRecordDistributor.getInputPort());

		super.connectPorts(flowRecordDistributor.getNewOutputPort(), operationAndCallGeneratorStage.getInputPort());
		super.connectPorts(operationAndCallGeneratorStage.getOperationOutputPort(), staticModelsAssemblerStage.getInputPort());

		super.connectPorts(flowRecordDistributor.getNewOutputPort(), operationPresentInModelEventReleaseControlStage.getInputPort());
		super.connectPorts(operationPresentInModelEventReleaseControlStage.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
		super.connectPorts(traceStatisticsDecorator.getOutputPort(), traceToGraphTransformer.getInputPort());
		super.connectPorts(traceToGraphTransformer.getOutputPort(), dotTraceGraphFileWriter.getInputPort());
		// alterante output
		// super.connectPorts(traceToGraphTransformer.getOutputPort(), graphMLTraceGraphFileWriter.getInputPort());

		super.connectPorts(operationAndCallGeneratorStage.getCallOutputPort(), callEvent2OperationCallStage.getInputPort());
		super.connectPorts(callEvent2OperationCallStage.getOutputPort(), executionModelAssemblerStage.getInputPort());
		super.connectPorts(executionModelAssemblerStage.getOutputPort(), callStatisticsStage.getInputPort());
		super.connectPorts(callStatisticsStage.getOutputPort(), fullStatisticsDecorator.getInputPort());
		super.connectPorts(fullStatisticsDecorator.getOutputPort(), onTerminationTrigger.getInputPort());

		super.connectPorts(onTerminationTrigger.getOutputPort(), dependencyGraphCreator.getInputPort());
		// super.connectPorts(dependencyGraphCreator.getOutputPort(),
		// debugStage.getInputPort());
		super.connectPorts(dependencyGraphCreator.getOutputPort(), dotDepGraphFileWriter.getInputPort());

	}

	public DeploymentModel getDeploymentModel() {
		return this.deploymentModel;
	}

	public ExecutionModel getExecutionModel() {
		return this.executionModel;
	}

	public StatisticsModel getStatisticsModel() {
		return this.statisticsModel;
	}

}
