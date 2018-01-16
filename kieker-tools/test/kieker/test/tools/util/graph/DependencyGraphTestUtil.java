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

package kieker.test.tools.util.graph;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.filter.AbstractGraphProducingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFilter;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.AbstractDependencyGraph;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.DependencyGraphNode;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.WeightedBidirectionalDependencyGraphEdge;
import kieker.tools.traceAnalysis.systemModel.ISystemModelElement;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class provides utility functions for dependency graph tests.
 * 
 * @author Holger Knoche, Nils Christian Ehmke
 * 
 * @since 1.6
 */
public final class DependencyGraphTestUtil {

	private DependencyGraphTestUtil() {
		// private constructor for static class
	}

	/**
	 * Utility function to create a node lookup table for a given dependency graph.
	 * 
	 * @param graph
	 *            The graph whose nodes shall be indexed
	 * @return A map which associates the node's identifier (see {@link kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraphElement#getIdentifier()})
	 *         to the actual identifier
	 * 
	 * @param <T>
	 *            The type of the entities within the dependency graph.
	 */
	public static <T extends ISystemModelElement> ConcurrentMap<String, DependencyGraphNode<T>> createNodeLookupTable(final AbstractDependencyGraph<T> graph) {
		final ConcurrentMap<String, DependencyGraphNode<T>> map = new ConcurrentHashMap<String, DependencyGraphNode<T>>();

		for (final DependencyGraphNode<T> node : graph.getNodes()) {
			map.put(node.getIdentifier(), node);
		}

		return map;
	}

	/**
	 * Prepares a test setup (especially the filter structure) to test a given graph-producing filter. The created
	 * structure contains all necessary plugins to process the given list of operation execution records using the given filter.
	 * A {@link GraphReceiverPlugin} is attached to the plugin's output port to make the created graph available for
	 * inspection.
	 * 
	 * @param graphProducer
	 *            The graph-producing filter
	 * @param inputPortName
	 *            The input port name that accepts message traces
	 * @param systemModelRepositoryPortName
	 *            The repository port's name to which the system model repository must be connected.
	 *            If the plugin does not need access to the system model repository, this parameter should be {@code null}
	 * @param executionRecords
	 *            The execution records that shall be processed
	 * @param analysisController
	 *            The analysis controller which will be used to register this component.
	 * 
	 * @return A fully-initialized {@link GraphTestSetup} instance
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the process yields an invalid analysis configuration
	 */
	public static GraphTestSetup prepareEnvironmentForProducerTest(final AnalysisController analysisController, final AbstractGraphProducingFilter<?> graphProducer,
			final String inputPortName, final String systemModelRepositoryPortName, final List<OperationExecutionRecord> executionRecords) throws
			AnalysisConfigurationException {
		return DependencyGraphTestUtil.prepareEnvironment(analysisController, graphProducer, inputPortName, systemModelRepositoryPortName, executionRecords);
	}

	/**
	 * Prepares a test setup (especially the filter structure) to test graph filters in conjunction with a given graph-producing filter.
	 * The created structure contains all necessary plugins to process the given list of operation execution records using the given filter.
	 * A {@link GraphReceiverPlugin} is attached to the plugin's output port to make the created graph available for
	 * inspection.
	 * 
	 * @param graphProducer
	 *            The graph-producing filter
	 * @param inputPortName
	 *            The input port name that accepts message traces
	 * @param systemModelRepositoryPortName
	 *            The repository port's name to which the system model repository must be connected.
	 *            If the plugin does not need access to the system model repository, this parameter should be {@code null}
	 * @param executionRecords
	 *            The execution records that shall be processed
	 * @param graphFilters
	 *            The graph filters in the order they should be attached to the producer
	 * @param analysisController
	 *            The analysis controller which will be used to register this component.
	 * 
	 * @return A fully-initialized {@link GraphTestSetup} instance
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the process yields an invalid analysis configuration
	 */
	public static GraphTestSetup prepareEnvironmentForGraphFilterTest(final AnalysisController analysisController,
			final AbstractGraphProducingFilter<?> graphProducer, final String inputPortName, final String systemModelRepositoryPortName,
			final List<OperationExecutionRecord> executionRecords, final AbstractGraphFilter<?, ?, ?, ?>... graphFilters) throws AnalysisConfigurationException {
		return DependencyGraphTestUtil.prepareEnvironment(analysisController, graphProducer, inputPortName, systemModelRepositoryPortName, executionRecords,
				graphFilters);
	}

	private static GraphTestSetup prepareEnvironment(final AnalysisController analysisController, final AbstractGraphProducingFilter<?> graphProducer,
			final String inputPortName, final String systemModelRepositoryPortName, final List<OperationExecutionRecord> executionRecords,
			final AbstractGraphFilter<?, ?, ?, ?>... graphFilters) throws AnalysisConfigurationException {

		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration(), analysisController);

		final ListReader<OperationExecutionRecord> readerPlugin = new ListReader<OperationExecutionRecord>(new Configuration(), analysisController);
		readerPlugin.addAllObjects(executionRecords);

		final ExecutionRecordTransformationFilter transformationFilter = new ExecutionRecordTransformationFilter(new Configuration(), analysisController);
		final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(new Configuration(), analysisController);

		// Correct the behavior of the extended list filter as properties are not inherited.
		final Configuration graphReceiverConfiguration = new Configuration();
		graphReceiverConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES,
				ListCollectionFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES);
		final GraphReceiverPlugin graphReceiver = new GraphReceiverPlugin(graphReceiverConfiguration, analysisController);

		// Connect repositories
		analysisController.connect(transformationFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisController.connect(traceReconstructionFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		if (systemModelRepositoryPortName != null) {
			analysisController.connect(graphProducer, systemModelRepositoryPortName, systemModelRepository);
		}

		// Connect plugins
		analysisController.connect(readerPlugin, ListReader.OUTPUT_PORT_NAME,
				transformationFilter, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		analysisController.connect(transformationFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				traceReconstructionFilter, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
		analysisController.connect(traceReconstructionFilter, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE,
				graphProducer, inputPortName);

		DependencyGraphTestUtil.connectGraphFilters(analysisController, graphProducer, graphFilters, graphReceiver);

		return new GraphTestSetup(analysisController, graphReceiver);
	}

	private static void connectGraphFilters(final AnalysisController analysisController, final AbstractGraphProducingFilter<?> producer,
			final AbstractGraphFilter<?, ?, ?, ?>[] graphFilters, final GraphReceiverPlugin graphReceiver) throws AnalysisConfigurationException {
		AbstractGraphFilter<?, ?, ?, ?> lastFilter = null;

		// Connect graph filters
		for (final AbstractGraphFilter<?, ?, ?, ?> filter : graphFilters) {
			if (lastFilter == null) {
				analysisController.connect(producer, producer.getGraphOutputPortName(), filter, filter.getGraphInputPortName());
			} else {
				analysisController.connect(lastFilter, lastFilter.getGraphOutputPortName(), filter, filter.getGraphInputPortName());
			}
			lastFilter = filter;
		}

		// Attach the graph receiver at the appropriate position
		if (lastFilter == null) {
			DependencyGraphTestUtil.attachGraphReceiver(analysisController, producer, graphReceiver);
		} else {
			DependencyGraphTestUtil.attachGraphReceiver(analysisController, lastFilter, graphReceiver);
		}
	}

	private static <P extends AbstractPlugin & IGraphOutputtingFilter<?>> void attachGraphReceiver(final AnalysisController analysisController,
			final P deliveringPlugin, final GraphReceiverPlugin graphReceiver) throws AnalysisConfigurationException {
		analysisController.connect(deliveringPlugin, deliveringPlugin.getGraphOutputPortName(), graphReceiver, GraphReceiverPlugin.INPUT_PORT_NAME_GRAPHS);
	}

	/**
	 * Utility function to test whether an edge with the given target weight exists between the given nodes.
	 * 
	 * @param source
	 *            The expected source node
	 * @param expectedWeight
	 *            The expected target weight of the edge
	 * @param target
	 *            The expected target node
	 * @return {@code True} if such an edge exists, {@code false} otherwise
	 */
	public static boolean dependencyEdgeExists(final DependencyGraphNode<?> source, final int expectedWeight, final DependencyGraphNode<?> target) {
		boolean edgeFound = false;

		for (final WeightedBidirectionalDependencyGraphEdge<?> edge : source.getOutgoingEdges()) {
			if (edge.getTarget().equals(target) && (edge.getTargetWeight().get() == expectedWeight)) {
				edgeFound = true;
			}
		}

		return edgeFound;
	}
}
