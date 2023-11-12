/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis;

import java.io.IOException;

import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Cohesion;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Complexity;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Coupling;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.HyperGraphSize;

import kieker.analysis.architecture.dependency.DependencyGraphCreatorStage;
import kieker.analysis.architecture.recovery.signature.NameBuilder;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.sink.graph.dot.DotFileWriterStage;
import kieker.analysis.generic.sink.graph.graphml.GraphMLFileWriterStage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.tools.mvis.graph.ColorAssemblyLevelComponentDependencyGraphBuilderFactory;
import kieker.tools.mvis.graph.ColorAssemblyLevelOperationDependencyGraphBuilderFactory;
import kieker.tools.mvis.graph.ColoredDotExportConfigurationFactory;
import kieker.tools.mvis.graph.DedicatedFileNameMapper;
import kieker.tools.mvis.graph.IColorDependencyGraphBuilderConfiguration;
import kieker.tools.mvis.stages.graph.ColorDependencyGraphBuilderConfiguration;
import kieker.tools.mvis.stages.graph.ModuleCallGraphStage;
import kieker.tools.mvis.stages.graph.OperationCallGraphStage;
import kieker.tools.mvis.stages.metrics.ModuleNodeCountCouplingEntry;
import kieker.tools.mvis.stages.metrics.ModuleNodeCountCouplingStage;
import kieker.tools.mvis.stages.metrics.NumberOfCallsEntry;
import kieker.tools.mvis.stages.metrics.NumberOfCallsStage;
import kieker.tools.mvis.stages.metrics.OperationNodeCountCouplingStage;
import kieker.tools.mvis.stages.metrics.OperationNodeCountEntry;

import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

import org.oceandsl.analysis.architecture.stages.ModelRepositoryProducerStage;
import org.oceandsl.analysis.generic.stages.TableCsvSink;
import org.oceandsl.analysis.metrics.entropy.AllenDeployedArchitectureGraphStage;
import org.oceandsl.analysis.metrics.entropy.ComputeAllenComplexityMetrics;
import org.oceandsl.analysis.metrics.entropy.KiekerArchitectureModelSystemGraphUtils;
import org.oceandsl.analysis.metrics.entropy.SaveAllenDataStage;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class TeetimeConfiguration extends Configuration {

    private static final String OPERATION_CALLS_CSV = "operation-calls.csv";
    private static final String DISTINCT_OPERATION_DEGREE_CSV = "distinct-operation-degree.csv";
    private static final String DISTINCT_MODULE_DEGREE_CSV = "distinct-module-degree.csv";

    public TeetimeConfiguration(final Settings settings) throws IOException {

        final ModelRepositoryProducerStage readerStage = new ModelRepositoryProducerStage(settings.getInputDirectory());

        final Distributor<ModelRepository> statisticsDistributor = new Distributor<>(new CopyByReferenceStrategy());

        this.connectPorts(readerStage.getOutputPort(), statisticsDistributor.getInputPort());

        this.createNumberOfCallsStatistics(settings, statisticsDistributor);
        this.createOperationCouplingStatistics(settings, statisticsDistributor);
        this.createModuleCouplingStatistics(settings, statisticsDistributor);
        this.createAllenMetricStatistics(settings, statisticsDistributor);

        final Distributor<ModelRepository> triggerDistributor = new Distributor<>(new CopyByReferenceStrategy());

        this.connectPorts(statisticsDistributor.getNewOutputPort(), triggerDistributor.getInputPort());

        this.createOperationGraphs(settings, triggerDistributor);
        this.createComponentGraphs(settings, triggerDistributor);
    }

    private void createComponentGraphs(final Settings settings, final Distributor<ModelRepository> triggerDistributor) {
        if (settings.getOutputGraphs().contains(EOutputGraph.DOT_COMPONENT)) {
            final IColorDependencyGraphBuilderConfiguration configuration = new ColorDependencyGraphBuilderConfiguration(
                    settings.getSelector());

            final DependencyGraphCreatorStage<IColorDependencyGraphBuilderConfiguration> componentDependencyGraphCreatorStage = new DependencyGraphCreatorStage<>(
                    configuration, new ColorAssemblyLevelComponentDependencyGraphBuilderFactory());
            final DotFileWriterStage<INode, IEdge> componentDependencyDotFileWriterStage = new DotFileWriterStage<>(
                    new DedicatedFileNameMapper(settings.getOutputDirectory(), "component", "dot"),
                    new ColoredDotExportConfigurationFactory(NameBuilder.forJavaShortOperations())
                            .createForAssemblyLevelComponentDependencyGraph(false));

            this.connectPorts(triggerDistributor.getNewOutputPort(),
                    componentDependencyGraphCreatorStage.getInputPort());
            this.connectPorts(componentDependencyGraphCreatorStage.getOutputPort(),
                    componentDependencyDotFileWriterStage.getInputPort());
        }
    }

    private void createOperationGraphs(final Settings settings, final Distributor<ModelRepository> triggerDistributor) {
        if (settings.getOutputGraphs().contains(EOutputGraph.DOT_OP)
                || settings.getOutputGraphs().contains(EOutputGraph.GRAPHML)) {
            final IColorDependencyGraphBuilderConfiguration configuration = new ColorDependencyGraphBuilderConfiguration(
                    settings.getSelector());

            final DependencyGraphCreatorStage<IColorDependencyGraphBuilderConfiguration> operationDependencyGraphCreatorStage = new DependencyGraphCreatorStage<>(
                    configuration, new ColorAssemblyLevelOperationDependencyGraphBuilderFactory());

            final Distributor<IGraph<INode, IEdge>> graphsDistributor = new Distributor<>(
                    new CopyByReferenceStrategy());

            this.connectPorts(triggerDistributor.getNewOutputPort(),
                    operationDependencyGraphCreatorStage.getInputPort());
            this.connectPorts(operationDependencyGraphCreatorStage.getOutputPort(), graphsDistributor.getInputPort());

            if (settings.getOutputGraphs().contains(EOutputGraph.DOT_OP)) {
                final DotFileWriterStage<INode, IEdge> dotFileOperationDependencyWriterStage = new DotFileWriterStage<>(
                        new DedicatedFileNameMapper(settings.getOutputDirectory(), "operation", "dot"),
                        new ColoredDotExportConfigurationFactory(NameBuilder.forJavaShortOperations())
                                .createForAssemblyLevelOperationDependencyGraph(false));

                this.connectPorts(graphsDistributor.getNewOutputPort(),
                        dotFileOperationDependencyWriterStage.getInputPort());
            }

            if (settings.getOutputGraphs().contains(EOutputGraph.GRAPHML)) {
                final GraphMLFileWriterStage<INode, IEdge> graphMLFileWriterStage = new GraphMLFileWriterStage<>(
                        settings.getOutputDirectory());

                this.connectPorts(graphsDistributor.getNewOutputPort(), graphMLFileWriterStage.getInputPort());
            }
        }

    }

    private void createNumberOfCallsStatistics(final Settings settings,
            final Distributor<ModelRepository> statisticsDistributor) {
        if (settings.getComputeStatistics() == null) {
            return;
        }
        if (settings.getComputeStatistics().contains(EStatistics.NUM_OF_CALLS)) {
            final NumberOfCallsStage numberOfCallsStage = new NumberOfCallsStage();
            final TableCsvSink<String, NumberOfCallsEntry> operationCallSink = new TableCsvSink<>(
                    settings.getOutputDirectory(),
                    String.format("%s-%s", settings.getSelector().getFilePrefix(),
                            TeetimeConfiguration.OPERATION_CALLS_CSV),
                    NumberOfCallsEntry.class, true, settings.getLineSeparator());

            this.connectPorts(statisticsDistributor.getNewOutputPort(), numberOfCallsStage.getInputPort());
            this.connectPorts(numberOfCallsStage.getOutputPort(), operationCallSink.getInputPort());
        }
    }

    private void createOperationCouplingStatistics(final Settings settings,
            final Distributor<ModelRepository> statisticsDistributor) {
        if (settings.getComputeStatistics() == null) {
            return;
        }
        if (settings.getComputeStatistics().contains(EStatistics.OP_COUPLING)) {
            final OperationCallGraphStage functionCallGraphStage = new OperationCallGraphStage(settings.getSelector(),
                    settings.getGraphGenerationMode());
            final OperationNodeCountCouplingStage functionNodeCouplingStage = new OperationNodeCountCouplingStage();

            final TableCsvSink<String, OperationNodeCountEntry> distinctOperationDegreeSink = new TableCsvSink<>(
                    settings.getOutputDirectory(),
                    String.format("%s-%s", settings.getSelector().getFilePrefix(),
                            TeetimeConfiguration.DISTINCT_OPERATION_DEGREE_CSV),
                    OperationNodeCountEntry.class, true, settings.getLineSeparator());

            this.connectPorts(statisticsDistributor.getNewOutputPort(), functionCallGraphStage.getInputPort());
            this.connectPorts(functionCallGraphStage.getOutputPort(), functionNodeCouplingStage.getInputPort());
            this.connectPorts(functionNodeCouplingStage.getOutputPort(), distinctOperationDegreeSink.getInputPort());
        }
    }

    private void createModuleCouplingStatistics(final Settings settings,
            final Distributor<ModelRepository> statisticsDistributor) {
        if (settings.getComputeStatistics() == null) {
            return;
        }
        if (settings.getComputeStatistics().contains(EStatistics.MODULE_COUPLING)) {
            final ModuleCallGraphStage moduleCallGraphStage = new ModuleCallGraphStage(settings.getSelector(),
                    settings.getGraphGenerationMode());
            final ModuleNodeCountCouplingStage moduleNodeCouplingStage = new ModuleNodeCountCouplingStage();

            final TableCsvSink<String, ModuleNodeCountCouplingEntry> distinctModuleDegreeSink = new TableCsvSink<>(
                    settings.getOutputDirectory(),
                    String.format("%s-%s", settings.getSelector().getFilePrefix(),
                            TeetimeConfiguration.DISTINCT_MODULE_DEGREE_CSV),
                    ModuleNodeCountCouplingEntry.class, true, settings.getLineSeparator());

            this.connectPorts(statisticsDistributor.getNewOutputPort(), moduleCallGraphStage.getInputPort());
            this.connectPorts(moduleCallGraphStage.getOutputPort(), moduleNodeCouplingStage.getInputPort());
            this.connectPorts(moduleNodeCouplingStage.getOutputPort(), distinctModuleDegreeSink.getInputPort());
        }
    }

    private void createAllenMetricStatistics(final Settings settings,
            final Distributor<ModelRepository> statisticsDistributor) {
        if (settings.getComputeStatistics() == null) {
            return;
        }
        if (settings.getComputeStatistics().contains(EStatistics.ALLEN)) {
            final AllenDeployedArchitectureGraphStage allenArchitectureModularGraphStage = new AllenDeployedArchitectureGraphStage(
                    settings.getSelector(), settings.getGraphGenerationMode());
            final ComputeAllenComplexityMetrics<DeployedComponent> computeAllenComplexityStage = new ComputeAllenComplexityMetrics<>(
                    new KiekerArchitectureModelSystemGraphUtils(), HyperGraphSize.class, Complexity.class,
                    Coupling.class, Cohesion.class);
            final SaveAllenDataStage saveAllenDataStage = new SaveAllenDataStage(settings.getOutputDirectory());

            this.connectPorts(statisticsDistributor.getNewOutputPort(),
                    allenArchitectureModularGraphStage.getInputPort());
            this.connectPorts(allenArchitectureModularGraphStage.getOutputPort(),
                    computeAllenComplexityStage.getInputPort());
            this.connectPorts(computeAllenComplexityStage.getOutputPort(), saveAllenDataStage.getInputPort());
        }
    }
}
