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

package kieker.analysis.configuration;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import kieker.analysis.OperationCallExtractorStage;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.dependency.DependencyGraphCreatorStage;
import kieker.analysis.graph.dependency.DeploymentLevelOperationDependencyGraphBuilderFactory;
import kieker.analysis.graph.dependency.IDependencyGraphBuilderFactory;
import kieker.analysis.graph.dependency.dot.DotExportConfigurationFactory;
import kieker.analysis.graph.export.dot.DotFileWriterStage;
import kieker.analysis.graph.export.graphml.GraphMLFileWriterStage;
import kieker.analysis.model.ExecutionModelAssembler;
import kieker.analysis.model.ExecutionModelAssemblerStage;
import kieker.analysis.model.ModelObjectFromOperationCallAccessors;
import kieker.analysis.model.StaticModelsAssemblerStage;
import kieker.analysis.signature.NameBuilder;
import kieker.analysis.signature.SignatureExtractor;
import kieker.analysis.source.file.DirectoryReaderStage;
import kieker.analysis.source.file.DirectoryScannerStage;
import kieker.analysis.statistics.CallStatisticsStage;
import kieker.analysis.statistics.FullReponseTimeStatisticsStage;
import kieker.analysis.statistics.StatisticsModel;
import kieker.analysis.trace.reconstruction.FlowRecordTraceReconstructionStage;
import kieker.analysis.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysis.util.stage.AllowedRecordsFilter;
import kieker.analysis.util.stage.trigger.TriggerOnTerminationStage;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
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

	private final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
	private final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
	private final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = new StatisticsModel();
	private final SignatureExtractor signatureExtractor = SignatureExtractor.forJava();

	public DependencyGraphConfiguration() {
		this(DependencyGraphConfiguration.KEY_IMPORT_DIRECTORY, DependencyGraphConfiguration.KEY_TIME_UNIT_OF_RECODS,
				DependencyGraphConfiguration.KEY_EXPORT_DIRECTORY);
	}

	public DependencyGraphConfiguration(final String importDirectory, final String timeUnitOfRecods,
			final String exportDirectory) {
		this(new File(importDirectory), ChronoUnit.valueOf(timeUnitOfRecods), new File(exportDirectory));
	}

	public DependencyGraphConfiguration(final File importDirectory, final TemporalUnit timeUnitOfRecods,
			final File exportDirectory) {
		final IDependencyGraphBuilderFactory graphBuilderFactory = new DeploymentLevelOperationDependencyGraphBuilderFactory();

		final DirectoryScannerStage directoryScannerStage = new DirectoryScannerStage(importDirectory);
		final DirectoryReaderStage directoryReaderStage = new DirectoryReaderStage(false, 80860);
		final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final StaticModelsAssemblerStage staticModelsAssembler = new StaticModelsAssemblerStage(this.typeModel,
				this.assemblyModel, this.deploymentModel, this.signatureExtractor);
		final FlowRecordTraceReconstructionStage traceReconstructor = new FlowRecordTraceReconstructionStage(this.deploymentModel,
				timeUnitOfRecods);
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final OperationCallExtractorStage operationCallExtractor = new OperationCallExtractorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(
				this.executionModel, new ExecutionModelAssembler(this.executionModel));
		final FullReponseTimeStatisticsStage fullStatisticsDecorator = new FullReponseTimeStatisticsStage(
				this.statisticsModel, ModelObjectFromOperationCallAccessors.DEPLOYED_OPERATION);
		final CallStatisticsStage callStatisticsDecorator = new CallStatisticsStage(this.statisticsModel,
				this.executionModel);

		final TriggerOnTerminationStage onTerminationTrigger = new TriggerOnTerminationStage();
		final DependencyGraphCreatorStage dependencyGraphCreator = new DependencyGraphCreatorStage(this.executionModel,
				this.statisticsModel, graphBuilderFactory);

		// graph export stages
		final Distributor<IGraph> distributor = new Distributor<>(new CopyByReferenceStrategy());
		final DotFileWriterStage dotFileWriterStage = new DotFileWriterStage(exportDirectory.getPath(),
				DependencyGraphConfiguration.DOT_EXPORT_CONFIGURATION_FACTORY
						.createForDeploymentLevelOperationDependencyGraph());
		final GraphMLFileWriterStage graphMLFileWriterStage = new GraphMLFileWriterStage(exportDirectory.getPath());

		super.connectPorts(directoryScannerStage.getOutputPort(), directoryReaderStage.getInputPort());
		super.connectPorts(directoryReaderStage.getOutputPort(), allowedRecordsFilter.getInputPort());
		super.connectPorts(allowedRecordsFilter.getOutputPort(), staticModelsAssembler.getInputPort());
		super.connectPorts(staticModelsAssembler.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
		super.connectPorts(traceStatisticsDecorator.getOutputPort(), operationCallExtractor.getInputPort());
		super.connectPorts(operationCallExtractor.getOutputPort(), executionModelAssembler.getInputPort());
		super.connectPorts(executionModelAssembler.getOutputPort(), fullStatisticsDecorator.getInputPort());
		super.connectPorts(fullStatisticsDecorator.getOutputPort(), callStatisticsDecorator.getInputPort());
		super.connectPorts(callStatisticsDecorator.getOutputPort(), onTerminationTrigger.getInputPort());
		super.connectPorts(onTerminationTrigger.getOutputPort(), dependencyGraphCreator.getInputPort());

		super.connectPorts(dependencyGraphCreator.getOutputPort(), distributor.getInputPort());
		super.connectPorts(distributor.getNewOutputPort(), dotFileWriterStage.getInputPort());
		super.connectPorts(distributor.getNewOutputPort(), graphMLFileWriterStage.getInputPort());
	}

	public static void main(final String[] args) {
		final File importDirectory = new File(args[0]);
		final File exportDirectory = new File(args[1]);

		final DependencyGraphConfiguration configuration = new DependencyGraphConfiguration(importDirectory,
				ChronoUnit.NANOS, exportDirectory);
		final Execution<DependencyGraphConfiguration> execution = new Execution<>(configuration);
		execution.executeBlocking();
	}
}
