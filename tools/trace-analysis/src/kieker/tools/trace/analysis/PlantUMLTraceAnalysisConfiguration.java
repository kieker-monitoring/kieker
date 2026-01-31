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
package kieker.tools.trace.analysis;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.MessageTrace;
import kieker.tools.common.TraceAnalysisParameters;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.visualization.trace.SequenceDiagramFilter.SDModes;
import kieker.visualization.trace.call.tree.plantuml.PlantUMLAggregatedAllocationComponentOperationCallTreeFilter;
import kieker.visualization.trace.call.tree.plantuml.PlantUMLAggregatedAssemblyComponentOperationCallTreeFilter;
import kieker.visualization.trace.call.tree.plantuml.PlantUMLTraceCallTreeFilter;
import kieker.visualization.trace.dependency.graph.ComponentDependencyGraphAllocationFilter;
import kieker.visualization.trace.dependency.graph.ComponentDependencyGraphAssemblyFilter;
import kieker.visualization.trace.dependency.graph.ContainerDependencyGraphFilter;
import kieker.visualization.trace.dependency.graph.OperationDependencyGraphAllocationFilter;
import kieker.visualization.trace.dependency.graph.OperationDependencyGraphAssemblyFilter;
import kieker.visualization.trace.plantuml.PlantUMLFileGenerator;
import kieker.visualization.trace.plantuml.PlantUMLGraphWriterPlugin;
import kieker.visualization.trace.plantuml.PlantUMLSequenceDiagramFilter;
import teetime.stage.basic.distributor.Distributor;


/**
 * Configuration for PlantUML-based trace analysis visualizations.
 * 
 * @author Yorrick Josuttis
 */
public class PlantUMLTraceAnalysisConfiguration extends AbstractTraceAnalysisConfiguration {

	public PlantUMLTraceAnalysisConfiguration(final TraceAnalysisParameters parameters, final SystemModelRepository systemRepository) {
		super(parameters, systemRepository);
	}

	@Override
	protected void createPlotSequenceDiagrams(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final String outputFilename, final SDModes sequenceDiagramMode, final boolean shortLabels) {
		final PlantUMLSequenceDiagramFilter.SDModes plantMode = PlantUMLSequenceDiagramFilter.SDModes.valueOf(sequenceDiagramMode.name());
		final PlantUMLSequenceDiagramFilter plantUMLSequenceDiagramFilter = new PlantUMLSequenceDiagramFilter(systemRepository, plantMode,
				pathPrefix + File.separator + outputFilename, shortLabels);
		final PlantUMLFileGenerator plantUMLFileGenerator = new PlantUMLFileGenerator(this.parameters);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), plantUMLSequenceDiagramFilter.getInputPort());
		this.connectPorts(plantUMLSequenceDiagramFilter.getOutputPort(), plantUMLFileGenerator.getInputPort());
	}

	@Override
	protected void createPlotDeploymentComponentDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels, final boolean plotLoops) {
		final ComponentDependencyGraphAllocationFilter graphFilter = new ComponentDependencyGraphAllocationFilter(systemRepository,
				TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final PlantUMLGraphWriterPlugin writerStage = new PlantUMLGraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
		this.connectPorts(writerStage.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createPlotAssemblyComponentDependencyGraph(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName, final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final ComponentDependencyGraphAssemblyFilter graphFilter = new ComponentDependencyGraphAssemblyFilter(systemRepository, TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final PlantUMLGraphWriterPlugin writerStage = new PlantUMLGraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
		this.connectPorts(writerStage.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createPlotContainerDependencyGraph(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String outputPathName, final String outputFileName, final boolean includeWeights, final boolean shortLabels, final boolean plotLoops) {
		final ContainerDependencyGraphFilter graphFilter = new ContainerDependencyGraphFilter(systemRepository, TimeUnit.NANOSECONDS);
		final PlantUMLGraphWriterPlugin writerStage = new PlantUMLGraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
		this.connectPorts(writerStage.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createPlotDeploymentOperationDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final OperationDependencyGraphAllocationFilter graphFilter = new OperationDependencyGraphAllocationFilter(systemRepository, TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final PlantUMLGraphWriterPlugin writerStage = new PlantUMLGraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
		this.connectPorts(writerStage.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createPlotAssemblyOperationDependencyGraph(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final OperationDependencyGraphAssemblyFilter graphFilter = new OperationDependencyGraphAssemblyFilter(systemRepository, TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final PlantUMLGraphWriterPlugin writerStage = new PlantUMLGraphWriterPlugin(outputPathName, outputFileName, includeWeights, shortLabels, plotLoops);
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
		this.connectPorts(writerStage.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createTraceCallTreeFilter(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean shortLabels) {
		final PlantUMLTraceCallTreeFilter componentPlotTraceCallTrees = new PlantUMLTraceCallTreeFilter(systemRepository, shortLabels,
				pathPrefix + AbstractTraceAnalysisConfiguration.CALL_TREE_FN_PREFIX);
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotTraceCallTrees.getInputPort());
		this.connectPorts(componentPlotTraceCallTrees.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createPlotAggregatedDeploymentCallTree(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels) {
		final PlantUMLAggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = new PlantUMLAggregatedAllocationComponentOperationCallTreeFilter(
				systemRepository, includeWeights, shortLabels, pathPrefix + VisualizationConstants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".puml");
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotAggregatedCallTree.getInputPort());
		this.connectPorts(componentPlotAggregatedCallTree.getOutputPort(), fileGenerator.getInputPort());
	}

	@Override
	protected void createAggrAssemblyCompOpCallTreeFilter(final SystemModelRepository systemRepository, final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels) {
		final PlantUMLAggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = new PlantUMLAggregatedAssemblyComponentOperationCallTreeFilter(
				systemRepository, includeWeights, shortLabels, pathPrefix + VisualizationConstants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".puml");
		final PlantUMLFileGenerator fileGenerator = new PlantUMLFileGenerator(this.parameters);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotAssemblyCallTree.getInputPort());
		this.connectPorts(componentPlotAssemblyCallTree.getOutputPort(), fileGenerator.getInputPort());
	}

}
