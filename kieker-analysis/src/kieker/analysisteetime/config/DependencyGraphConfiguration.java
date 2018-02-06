/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.config;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import kieker.analysisteetime.OperationCallExtractorStage;
import kieker.analysisteetime.dependencygraphs.DependencyGraphBuilderFactory;
import kieker.analysisteetime.dependencygraphs.DependencyGraphCreatorStage;
import kieker.analysisteetime.dependencygraphs.DeploymentLevelOperationDependencyGraphBuilderFactory;
import kieker.analysisteetime.dependencygraphs.dot.DotExportConfigurationFactory;
import kieker.analysisteetime.model.ExecutionModelAssemblerStage;
import kieker.analysisteetime.model.ModelObjectFromOperationCallAccessors;
import kieker.analysisteetime.model.StaticModelsAssemblerStage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.recordreading.AllowedRecordsFilter;
import kieker.analysisteetime.recordreading.ReadingComposite;
import kieker.analysisteetime.signature.NameBuilder;
import kieker.analysisteetime.signature.SignatureExtractor;
import kieker.analysisteetime.statistics.CallStatisticsStage;
import kieker.analysisteetime.statistics.FullReponseTimeStatisticsStage;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.analysisteetime.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.export.dot.DotFileWriterStage;
import kieker.analysisteetime.util.graph.export.graphml.GraphMLFileWriterStage;
import kieker.analysisteetime.util.stage.trigger.TriggerOnTerminationStage;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 * This is an executable TeeTime {@link Configuration} to create dependency graphs.
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class DependencyGraphConfiguration extends Configuration {
	private static final DotExportConfigurationFactory DOT_EXPORT_CONFIGURATION_FACTORY = new DotExportConfigurationFactory(
			NameBuilder.forJavaShortOperations());

	private final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
	private final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
	private final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = new StatisticsModel();
	private final SignatureExtractor signatureExtractor = SignatureExtractor.forJava();

	public DependencyGraphConfiguration(final File importDirectory, final TemporalUnit timeUnitOfRecods, final File exportDirectory) {
		final DependencyGraphBuilderFactory graphBuilderFactory = new DeploymentLevelOperationDependencyGraphBuilderFactory();

		final ReadingComposite reader = new ReadingComposite(importDirectory);
		final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final StaticModelsAssemblerStage staticModelsAssembler = new StaticModelsAssemblerStage(this.typeModel,
				this.assemblyModel, this.deploymentModel, this.signatureExtractor);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(this.deploymentModel, timeUnitOfRecods);
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final OperationCallExtractorStage operationCallExtractor = new OperationCallExtractorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(
				this.executionModel);
		final FullReponseTimeStatisticsStage fullStatisticsDecorator = new FullReponseTimeStatisticsStage(
				this.statisticsModel, ModelObjectFromOperationCallAccessors.DEPLOYED_OPERATION);
		final CallStatisticsStage callStatisticsDecorator = new CallStatisticsStage(this.statisticsModel,
				this.executionModel);

		final TriggerOnTerminationStage onTerminationTrigger = new TriggerOnTerminationStage();
		final DependencyGraphCreatorStage dependencyGraphCreator = new DependencyGraphCreatorStage(this.executionModel,
				this.statisticsModel, graphBuilderFactory);

		// graph export stages
		final Distributor<Graph> distributor = new Distributor<>(new CopyByReferenceStrategy());
		final DotFileWriterStage dotFileWriterStage = new DotFileWriterStage(exportDirectory.getPath(),
				DOT_EXPORT_CONFIGURATION_FACTORY.createForDeploymentLevelOperationDependencyGraph());
		final GraphMLFileWriterStage graphMLFileWriterStage = new GraphMLFileWriterStage(exportDirectory.getPath());

		super.connectPorts(reader.getOutputPort(), allowedRecordsFilter.getInputPort());
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

		final DependencyGraphConfiguration configuration = new DependencyGraphConfiguration(importDirectory, ChronoUnit.NANOS, exportDirectory);
		final Execution<DependencyGraphConfiguration> execution = new Execution<>(configuration);
		execution.executeBlocking();
	}
}
