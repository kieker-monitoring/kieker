/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.configuration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import kieker.analysis.architecture.CallEventMatcher;
import kieker.analysis.architecture.dependency.DependencyGraphCreatorStage;
import kieker.analysis.architecture.dependency.DeploymentLevelOperationDependencyGraphBuilderFactory;
import kieker.analysis.architecture.dependency.IDependencyGraphBuilderConfiguration;
import kieker.analysis.architecture.dependency.IDependencyGraphBuilderFactory;
import kieker.analysis.architecture.recovery.CallEvent2OperationCallStage;
import kieker.analysis.architecture.recovery.ExecutionModelAssembler;
import kieker.analysis.architecture.recovery.ExecutionModelAssemblerStage;
import kieker.analysis.architecture.recovery.ModelObjectFromOperationCallAccessorUtils;
import kieker.analysis.architecture.recovery.ModelRepository;
import kieker.analysis.architecture.recovery.OperationAndCallGeneratorStage;
import kieker.analysis.architecture.recovery.StaticModelsAssemblerStage;
import kieker.analysis.architecture.recovery.data.CallEvent;
import kieker.analysis.architecture.recovery.data.OperationEvent;
import kieker.analysis.architecture.recovery.signature.NameBuilder;
import kieker.analysis.architecture.recovery.signature.SignatureExtractor;
import kieker.analysis.architecture.trace.flow.FlowTraceEventMatcher;
import kieker.analysis.architecture.trace.reconstruction.FlowRecordTraceReconstructionStage;
import kieker.analysis.architecture.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysis.generic.ControlledEventReleaseStage;
import kieker.analysis.generic.source.file.DirectoryReaderStage;
import kieker.analysis.generic.source.file.DirectoryScannerStage;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.dependency.dot.DotExportConfigurationFactory;
import kieker.analysis.graph.export.dot.DotFileWriterStage;
import kieker.analysis.graph.export.graphml.GraphMLFileWriterStage;
import kieker.analysis.statistics.CallStatisticsStage;
import kieker.analysis.statistics.FullResponseTimeStatisticsStage;
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
import teetime.framework.Execution;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 * This is an executable TeeTime {@link Configuration} to create dependency
 * graphs.
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public class DependencyGraphConfiguration extends Configuration {

	private static final String PREFIX = DependencyGraphConfiguration.class.getName();
	private static final String KEY_IMPORT_DIRECTORY = System
			.getProperty(DependencyGraphConfiguration.PREFIX + ".importDirectory");
	private static final String KEY_TIME_UNIT_OF_RECODS = System
			.getProperty(DependencyGraphConfiguration.PREFIX + ".timeUnitOfRecods");
	private static final String KEY_EXPORT_DIRECTORY = System
			.getProperty(DependencyGraphConfiguration.PREFIX + ".exportDirectory");

	private static final DotExportConfigurationFactory DOT_EXPORT_CONFIGURATION_FACTORY = new DotExportConfigurationFactory(
			NameBuilder.forJavaShortOperations());
	private static final String DYNAMIC_SOURCE = "dynamic-source";
	private static final String DEFAULT_NAME = "G";

	private final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
	private final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
	private final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = StatisticsFactory.eINSTANCE.createStatisticsModel();
	private final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();
	private final SignatureExtractor signatureExtractor = SignatureExtractor.forJava();

	public DependencyGraphConfiguration() {
		this(DEFAULT_NAME, DependencyGraphConfiguration.KEY_IMPORT_DIRECTORY, DependencyGraphConfiguration.KEY_TIME_UNIT_OF_RECODS,
				Paths.get(DependencyGraphConfiguration.KEY_EXPORT_DIRECTORY));
	}

	public DependencyGraphConfiguration(final String name, final String importDirectory, final String timeUnitOfRecods,
			final Path exportDirectory) {
		this(name, new File(importDirectory), ChronoUnit.valueOf(timeUnitOfRecods), exportDirectory);
	}

	public DependencyGraphConfiguration(final String name, final File importDirectory, final TemporalUnit timeUnitOfRecods,
			final Path exportDirectory) {
		final ModelRepository repository = new ModelRepository(name);
		repository.register(TypeModel.class, this.typeModel);
		repository.register(AssemblyModel.class, this.assemblyModel);
		repository.register(DeploymentModel.class, this.deploymentModel);
		repository.register(ExecutionModel.class, this.executionModel);
		repository.register(StatisticsModel.class, this.statisticsModel);
		repository.register(SourceModel.class, this.sourceModel);

		final IDependencyGraphBuilderFactory<IDependencyGraphBuilderConfiguration> graphBuilderFactory = new DeploymentLevelOperationDependencyGraphBuilderFactory();

		final DirectoryScannerStage directoryScannerStage = new DirectoryScannerStage(importDirectory);

		final DirectoryReaderStage directoryReaderStage = new DirectoryReaderStage(false, 80860);

		final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();

		final Distributor<IFlowRecord> flowRecordDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final OperationAndCallGeneratorStage operationAndCallGeneratorStage = new OperationAndCallGeneratorStage(true);

		final StaticModelsAssemblerStage staticModelsAssembler = new StaticModelsAssemblerStage(this.typeModel,
				this.assemblyModel, this.deploymentModel, this.sourceModel, DYNAMIC_SOURCE, this.signatureExtractor);

		final ControlledEventReleaseStage<OperationEvent, IFlowRecord> flowRecordMerger = new ControlledEventReleaseStage<>(new FlowTraceEventMatcher());
		flowRecordMerger.declareActive();
		final ControlledEventReleaseStage<OperationEvent, CallEvent> controlCallEventStage = new ControlledEventReleaseStage<>(
				new CallEventMatcher());
		controlCallEventStage.declareActive();
		final Distributor<OperationEvent> operationCompleteDistributor = new Distributor<>(new CopyByReferenceStrategy());

		final CallEvent2OperationCallStage callEvent2operationCallStage = new CallEvent2OperationCallStage(repository.getModel(DeploymentModel.class));
		final ExecutionModelAssemblerStage executionModelAssemblerStage = new ExecutionModelAssemblerStage(
				new ExecutionModelAssembler(this.executionModel, this.sourceModel, DYNAMIC_SOURCE));
		final CallStatisticsStage callStatisticsStage = new CallStatisticsStage(this.statisticsModel,
				this.executionModel);

		// Works on a trace
		final FlowRecordTraceReconstructionStage traceReconstructor = new FlowRecordTraceReconstructionStage(this.deploymentModel,
				timeUnitOfRecods);
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final FullResponseTimeStatisticsStage fullStatisticsDecorator = new FullResponseTimeStatisticsStage(
				this.statisticsModel, ModelObjectFromOperationCallAccessorUtils.DEPLOYED_OPERATION);

		final TriggerOnTerminationStage<ModelRepository> onTerminationTrigger = new TriggerOnTerminationStage<>(repository);

		final DependencyGraphCreatorStage<IDependencyGraphBuilderConfiguration> dependencyGraphCreator = new DependencyGraphCreatorStage<>(
				new IDependencyGraphBuilderConfiguration() {
				}, graphBuilderFactory);

		// graph export stages
		final Distributor<IGraph> distributor = new Distributor<>(new CopyByReferenceStrategy());
		final DotFileWriterStage dotFileWriterStage = new DotFileWriterStage(exportDirectory,
				DependencyGraphConfiguration.DOT_EXPORT_CONFIGURATION_FACTORY
						.createForDeploymentLevelOperationDependencyGraph());

		final GraphMLFileWriterStage graphMLFileWriterStage = new GraphMLFileWriterStage(exportDirectory);

		super.connectPorts(directoryScannerStage.getOutputPort(), directoryReaderStage.getInputPort());
		super.connectPorts(directoryReaderStage.getOutputPort(), allowedRecordsFilter.getInputPort());
		super.connectPorts(allowedRecordsFilter.getOutputPort(), flowRecordDistributor.getInputPort());

		super.connectPorts(flowRecordDistributor.getNewOutputPort(), operationAndCallGeneratorStage.getInputPort());
		super.connectPorts(operationAndCallGeneratorStage.getOperationOutputPort(), staticModelsAssembler.getInputPort());
		super.connectPorts(staticModelsAssembler.getOutputPort(), operationCompleteDistributor.getInputPort());

		super.connectPorts(operationCompleteDistributor.getNewOutputPort(), controlCallEventStage.getControlInputPort());
		super.connectPorts(operationAndCallGeneratorStage.getCallOutputPort(), controlCallEventStage.getBaseInputPort());
		super.connectPorts(controlCallEventStage.getOutputPort(), callEvent2operationCallStage.getInputPort());
		super.connectPorts(callEvent2operationCallStage.getOutputPort(), executionModelAssemblerStage.getInputPort());
		super.connectPorts(executionModelAssemblerStage.getOutputPort(), callStatisticsStage.getInputPort());
		super.connectPorts(callStatisticsStage.getOutputPort(), fullStatisticsDecorator.getInputPort());
		super.connectPorts(fullStatisticsDecorator.getOutputPort(), onTerminationTrigger.getInputPort());

		super.connectPorts(onTerminationTrigger.getOutputPort(), dependencyGraphCreator.getInputPort());
		super.connectPorts(dependencyGraphCreator.getOutputPort(), distributor.getInputPort());
		super.connectPorts(distributor.getNewOutputPort(), dotFileWriterStage.getInputPort());
		super.connectPorts(distributor.getNewOutputPort(), graphMLFileWriterStage.getInputPort());

		super.connectPorts(operationCompleteDistributor.getNewOutputPort(), flowRecordMerger.getControlInputPort());
		super.connectPorts(flowRecordDistributor.getNewOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
	}

	public static void main(final String[] args) {
		final File importDirectory = new File(args[0]);
		final Path exportDirectory = Paths.get(args[1]);

		final DependencyGraphConfiguration configuration = new DependencyGraphConfiguration(DEFAULT_NAME, importDirectory,
				ChronoUnit.NANOS, exportDirectory);
		final Execution<DependencyGraphConfiguration> execution = new Execution<>(configuration);
		execution.executeBlocking();
	}
}
