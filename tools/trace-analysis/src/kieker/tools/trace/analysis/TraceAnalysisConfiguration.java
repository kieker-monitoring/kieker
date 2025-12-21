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

import kieker.tools.common.TraceAnalysisParameters;

import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.MessageTrace;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.visualization.trace.GraphWriterPlugin;
import kieker.visualization.trace.SequenceDiagramFilter;
import kieker.visualization.trace.SequenceDiagramFilter.SDModes;
import kieker.visualization.trace.call.tree.dot.AggregatedAllocationComponentOperationCallTreeFilter;
import kieker.visualization.trace.call.tree.dot.AggregatedAssemblyComponentOperationCallTreeFilter;
import kieker.visualization.trace.call.tree.dot.TraceCallTreeFilter;
import kieker.visualization.trace.dependency.graph.ComponentDependencyGraphAllocationFilter;
import kieker.visualization.trace.dependency.graph.ComponentDependencyGraphAssemblyFilter;
import kieker.visualization.trace.dependency.graph.ContainerDependencyGraphFilter;
import kieker.visualization.trace.dependency.graph.OperationDependencyGraphAllocationFilter;
import kieker.visualization.trace.dependency.graph.OperationDependencyGraphAssemblyFilter;

import teetime.stage.basic.distributor.Distributor;

/**
 * Central teetime pipe and filter configuration for trace analysis.
 *
 * @author Reiner Jung
 * @author Yorrick Josuttis
 * @since 1.15
 *
 */
public class TraceAnalysisConfiguration extends AbstractTraceAnalysisConfiguration {

	public TraceAnalysisConfiguration(final TraceAnalysisParameters parameters,
			final SystemModelRepository systemRepository) {
		super(parameters, systemRepository);
	}

	@Override
	protected void createPlotSequenceDiagrams(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final String outputFilename, final SDModes sequenceDiagramMode,
			final boolean shortLabels) {
		final SequenceDiagramFilter sequenceDiagramFilter = new SequenceDiagramFilter(systemRepository,
				sequenceDiagramMode,
				pathPrefix + File.separator + outputFilename, shortLabels);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), sequenceDiagramFilter.getInputPort());
	}

	@Override
	protected void createPlotDeploymentComponentDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList,
			final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels, final boolean plotLoops) {
		final ComponentDependencyGraphAllocationFilter graphFilter = new ComponentDependencyGraphAllocationFilter(
				systemRepository,
				TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights,
				shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	@Override
	protected void createPlotAssemblyComponentDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final ComponentDependencyGraphAssemblyFilter graphFilter = new ComponentDependencyGraphAssemblyFilter(
				systemRepository, TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights,
				shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	@Override
	protected void createPlotContainerDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String outputPathName, final String outputFileName, final boolean includeWeights,
			final boolean shortLabels, final boolean plotLoops) {
		final ContainerDependencyGraphFilter graphFilter = new ContainerDependencyGraphFilter(systemRepository,
				TimeUnit.NANOSECONDS);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights,
				shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	@Override
	protected void createPlotDeploymentOperationDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor, final List<String> decoratorList,
			final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final OperationDependencyGraphAllocationFilter graphFilter = new OperationDependencyGraphAllocationFilter(
				systemRepository, TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights,
				shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	@Override
	protected void createPlotAssemblyOperationDependencyGraph(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final List<String> decoratorList, final String outputPathName, final String outputFileName,
			final boolean includeWeights, final boolean shortLabels,
			final boolean plotLoops) {
		final OperationDependencyGraphAssemblyFilter graphFilter = new OperationDependencyGraphAssemblyFilter(
				systemRepository, TimeUnit.NANOSECONDS);
		this.addDecorators(decoratorList, graphFilter);
		final GraphWriterPlugin writerStage = new GraphWriterPlugin(outputPathName, outputFileName, includeWeights,
				shortLabels, plotLoops);

		this.connectPorts(messageTraceDistributor.getNewOutputPort(), graphFilter.getInputPort());
		this.connectPorts(graphFilter.getOutputPort(), writerStage.getInputPort());
	}

	@Override
	protected void createTraceCallTreeFilter(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean shortLabels) {
		final TraceCallTreeFilter componentPlotTraceCallTrees = new TraceCallTreeFilter(systemRepository, shortLabels,
				pathPrefix + AbstractTraceAnalysisConfiguration.CALL_TREE_FN_PREFIX);
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotTraceCallTrees.getInputPort());
	}

	@Override
	protected void createPlotAggregatedDeploymentCallTree(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels) {
		final AggregatedAllocationComponentOperationCallTreeFilter componentPlotAggregatedCallTree = new AggregatedAllocationComponentOperationCallTreeFilter(
				systemRepository, includeWeights, shortLabels,
				pathPrefix + VisualizationConstants.AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX + ".dot");
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotAggregatedCallTree.getInputPort());
	}

	@Override
	protected void createAggrAssemblyCompOpCallTreeFilter(final SystemModelRepository systemRepository,
			final Distributor<MessageTrace> messageTraceDistributor,
			final String pathPrefix, final boolean includeWeights, final boolean shortLabels) {
		final AggregatedAssemblyComponentOperationCallTreeFilter componentPlotAssemblyCallTree = new AggregatedAssemblyComponentOperationCallTreeFilter(
				systemRepository, includeWeights, shortLabels,
				pathPrefix + VisualizationConstants.AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX + ".dot");
		this.connectPorts(messageTraceDistributor.getNewOutputPort(), componentPlotAssemblyCallTree.getInputPort());
	}

}
