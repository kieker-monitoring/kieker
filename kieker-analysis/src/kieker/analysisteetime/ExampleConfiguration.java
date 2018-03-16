/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;

import kieker.analysisteetime.dependencygraphs.DependencyGraphCreatorStage;
import kieker.analysisteetime.dependencygraphs.DeploymentLevelOperationDependencyGraphBuilderFactory;
import kieker.analysisteetime.dependencygraphs.dot.DotExportConfigurationFactory;
import kieker.analysisteetime.dependencygraphs.vertextypes.IVertexTypeMapper;
import kieker.analysisteetime.model.ExecutionModelAssemblerStage;
import kieker.analysisteetime.model.ModelObjectFromOperationCallAccessors;
import kieker.analysisteetime.model.StaticModelsAssemblerStage;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.recordreading.AllowedRecordsFilter;
import kieker.analysisteetime.recordreading.ReadingComposite;
import kieker.analysisteetime.signature.NameBuilder;
import kieker.analysisteetime.signature.SignatureExtractor;
import kieker.analysisteetime.statistics.CallStatisticsStage;
import kieker.analysisteetime.statistics.FullReponseTimeStatisticsStage;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.trace.graph.TraceToGraphTransformerStage;
import kieker.analysisteetime.trace.graph.dot.DotTraceGraphFileWriterStage;
import kieker.analysisteetime.trace.reconstruction.TraceReconstructorStage;
import kieker.analysisteetime.trace.reconstruction.TraceStatisticsDecoratorStage;
import kieker.analysisteetime.util.graph.export.dot.DotExportConfiguration;
import kieker.analysisteetime.util.graph.export.dot.DotFileWriterStage;
import kieker.analysisteetime.util.stage.trigger.TriggerOnTerminationStage;

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

	private final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
	private final AssemblyModel assemblyModel = AssemblyFactory.eINSTANCE.createAssemblyModel();
	private final DeploymentModel deploymentModel = DeploymentFactory.eINSTANCE.createDeploymentModel();
	private final ExecutionModel executionModel = ExecutionFactory.eINSTANCE.createExecutionModel();
	private final StatisticsModel statisticsModel = new StatisticsModel();
	private final SignatureExtractor signatureExtractor = SignatureExtractor.forJava();

	public ExampleConfiguration(final File importDirectory, final File exportDirectory) {

		final TemporalUnit timeUnitOfRecods = ChronoUnit.NANOS;
		final Function<OperationCall, Object> statisticsObjectAccesor = ModelObjectFromOperationCallAccessors.DEPLOYED_OPERATION;
		final DeploymentLevelOperationDependencyGraphBuilderFactory deploymentGraphBuilderFactory = new DeploymentLevelOperationDependencyGraphBuilderFactory();
		final DotExportConfiguration dependencyGraphDotExportConfiguration = new DotExportConfigurationFactory(
				NameBuilder.forJavaShortOperations(), IVertexTypeMapper.TO_STRING)
						.createForDeploymentLevelOperationDependencyGraph();

		// Create the stages
		final ReadingComposite reader = new ReadingComposite(importDirectory);
		// BETTER consider if KiekerMetadataRecord has to be processed
		// final DebugStage<IMonitoringRecord> debugRecordsStage = new
		// DebugStage<>();
		final AllowedRecordsFilter allowedRecordsFilter = new AllowedRecordsFilter();
		final StaticModelsAssemblerStage staticModelsAssembler = new StaticModelsAssemblerStage(this.typeModel,
				this.assemblyModel, this.deploymentModel, this.signatureExtractor);
		final TraceReconstructorStage traceReconstructor = new TraceReconstructorStage(this.deploymentModel, timeUnitOfRecods);
		final TraceStatisticsDecoratorStage traceStatisticsDecorator = new TraceStatisticsDecoratorStage();

		final OperationCallExtractorStage operationCallExtractor = new OperationCallExtractorStage();
		final ExecutionModelAssemblerStage executionModelAssembler = new ExecutionModelAssemblerStage(
				this.executionModel);
		final FullReponseTimeStatisticsStage fullStatisticsDecorator = new FullReponseTimeStatisticsStage(
				this.statisticsModel, statisticsObjectAccesor);
		final CallStatisticsStage callStatisticsDecorator = new CallStatisticsStage(this.statisticsModel,
				this.executionModel);

		final TraceToGraphTransformerStage traceToGraphTransformer = new TraceToGraphTransformerStage();
		final DotTraceGraphFileWriterStage dotTraceGraphFileWriter = DotTraceGraphFileWriterStage
				.create(exportDirectory);
		// final GraphMLFileWriterStage graphMLTraceGraphFileWriter = new GraphMLFileWriterStage(exportDirectory.getPath());
		final Distributor<Trace> traceDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final TriggerOnTerminationStage onTerminationTrigger = new TriggerOnTerminationStage();

		final DependencyGraphCreatorStage dependencyGraphCreator = new DependencyGraphCreatorStage(this.executionModel,
				this.statisticsModel, deploymentGraphBuilderFactory);
		final DotFileWriterStage dotDepGraphFileWriter = new DotFileWriterStage(exportDirectory.getPath(),
				dependencyGraphDotExportConfiguration);

		// final AbstractConsumerStage<Graph> debugStage = new
		// GraphPrinterStage();

		// Connect the stages
		super.connectPorts(reader.getOutputPort(), allowedRecordsFilter.getInputPort());
		super.connectPorts(allowedRecordsFilter.getOutputPort(), staticModelsAssembler.getInputPort());
		super.connectPorts(staticModelsAssembler.getOutputPort(), traceReconstructor.getInputPort());
		super.connectPorts(traceReconstructor.getOutputPort(), traceStatisticsDecorator.getInputPort());
		super.connectPorts(traceStatisticsDecorator.getOutputPort(), traceDistributor.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), traceToGraphTransformer.getInputPort());
		super.connectPorts(traceToGraphTransformer.getOutputPort(), dotTraceGraphFileWriter.getInputPort());
		// super.connectPorts(traceToGraphTransformer.getOutputPort(),
		// graphMLTraceGraphFileWriter.getInputPort());
		super.connectPorts(traceDistributor.getNewOutputPort(), operationCallExtractor.getInputPort());
		super.connectPorts(operationCallExtractor.getOutputPort(), executionModelAssembler.getInputPort());
		super.connectPorts(executionModelAssembler.getOutputPort(), fullStatisticsDecorator.getInputPort());
		super.connectPorts(fullStatisticsDecorator.getOutputPort(), callStatisticsDecorator.getInputPort());
		super.connectPorts(callStatisticsDecorator.getOutputPort(), onTerminationTrigger.getInputPort());
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
