/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.traceAnalysis.filter.visualization.GraphWriterConfiguration;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * Abstract superclass for all dependency-graph formatters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The graph type this formatter is for
 */
public abstract class AbstractDependencyGraphFormatter<G extends AbstractDependencyGraph<?>> extends AbstractGraphFormatter<G> {

	protected static final String STEREOTYPE_EXECUTION_CONTAINER = "<<execution container>>";
	protected static final String STEREOTYPE_ASSEMBLY_COMPONENT = "<<assembly component>>";
	protected static final String STEREOTYPE_ALLOCATION_COMPONENT = "<<deployment component>>";

	private static final String NODE_ID_PREFIX = "depNode_";
	private static final String CONTAINER_NODE_ID_PREFIX = "container";
	private static final String COMPONENT_NODE_ID_PREFIX = "component_";

	@Override
	protected String formatGraph(final G graph, final GraphWriterConfiguration configuration) {
		final boolean includeWeights = configuration.doIncludeWeights();
		final boolean useShortLabels = configuration.doUseShortLabels();
		final boolean plotLoops = configuration.doPlotLoops();

		return this.formatDependencyGraph(graph, includeWeights, useShortLabels, plotLoops);
	}

	/**
	 * Outputs the default graph header to the given builder.
	 * 
	 * @param builder
	 *            The builder to use
	 */
	protected void appendGraphHeader(final StringBuilder builder) {
		builder.append("digraph G {\n rankdir=" + DotFactory.DOT_DOT_RANKDIR_LR + ";\n");
	}

	/**
	 * Outputs the default graph footer to the given builder.
	 * 
	 * @param builder
	 *            The builder to use
	 */
	protected void appendGraphFooter(final StringBuilder builder) {
		builder.append("}\n");
	}

	/**
	 * Utility function to create a textual container ID for an execution container.
	 * 
	 * @param container
	 *            The container to create the ID for
	 * @return The created ID
	 */
	protected static String createContainerId(final ExecutionContainer container) {
		return CONTAINER_NODE_ID_PREFIX + container.getId();
	}

	/**
	 * Utility function to create a textual container ID for an allocation component.
	 * 
	 * @param component
	 *            The allocation component to create the ID for
	 * @return The created ID
	 */
	protected static String createAllocationComponentId(final AllocationComponent component) {
		return COMPONENT_NODE_ID_PREFIX + component.getId();
	}

	/**
	 * Utility function to create a textual container ID for an assembly component.
	 * 
	 * @param component
	 *            The assembly component to create the ID for
	 * @return The created ID
	 */
	protected static String createAssemblyComponentId(final AssemblyComponent component) {
		return COMPONENT_NODE_ID_PREFIX + component.getId();
	}

	/**
	 * Utility function to create a textual node ID for an operation.
	 * 
	 * @param operation
	 *            The operation to create the ID for
	 * @return The created ID
	 */
	protected static String createOperationNodeId(final Operation operation) {
		return AbstractDependencyGraphFormatter.createNodeId(operation.getId());
	}

	/**
	 * Utility function to create a textual node ID from a given numeric node ID.
	 * 
	 * @param nodeId
	 *            The numeric node ID
	 * @return The created ID
	 */
	protected static String createNodeId(final int nodeId) {
		return NODE_ID_PREFIX + nodeId;
	}

	/**
	 * Utility function to create a textual node ID for a given node.
	 * 
	 * @param node
	 *            The node to create the ID for
	 * @return The created ID
	 */
	protected static String createNodeId(final DependencyGraphNode<?> node) {
		return AbstractDependencyGraphFormatter.createNodeId(node.getId());
	}

	/**
	 * Utility function to determine the fill color to use for a given node.
	 * 
	 * @param node
	 *            The node to determine the color for
	 * @return The color name to use for the given node
	 */
	protected static String getNodeFillColor(final DependencyGraphNode<?> node) {
		if (node.isAssumed()) {
			return DotFactory.DOT_FILLCOLOR_GRAY;
		} else {
			return DotFactory.DOT_FILLCOLOR_WHITE;
		}
	}

	protected abstract String formatDependencyGraph(G graph, boolean includeWeights, boolean useShortLabels, boolean plotLoops);
}
