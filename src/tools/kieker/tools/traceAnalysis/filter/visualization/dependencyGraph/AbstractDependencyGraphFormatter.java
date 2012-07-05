/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;

/**
 * Abstract superclass for all dependency-graph formatters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The graph type this formatter is for
 */
public abstract class AbstractDependencyGraphFormatter<G extends DependencyGraph<?>> extends AbstractGraphFormatter<G> {

	/**
	 * Name of the configuration property indicating that weights should be included.
	 */
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	/**
	 * Name of the configuration property indicating that short labels should be used.
	 */
	public static final String CONFIG_PROPERTY_NAME_SHORTLABELS = "shortLabels";
	/**
	 * Name of the configuration property indicating that self-loops should be displayed.
	 */
	public static final String CONFIG_PROPERTY_NAME_SELFLOOPS = "selfLoops";

	protected static final String STEREOTYPE_EXECUTION_CONTAINER = "<<execution container>>";
	protected static final String STEREOTYPE_ASSEMBLY_COMPONENT = "<<assembly component>>";
	protected static final String STEREOTYPE_ALLOCATION_COMPONENT = "<<deployment component>>";

	private static final String NODE_ID_PREFIX = "depNode_";
	private static final String CONTAINER_NODE_ID_PREFIX = "container";

	@Override
	protected String formatGraph(final G graph, final Configuration configuration) {
		final boolean includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		final boolean useShortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
		final boolean plotLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);

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
		builder.append("}");
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
		return (node.isAssumed()) ? DotFactory.DOT_FILLCOLOR_GRAY : DotFactory.DOT_FILLCOLOR_WHITE;
	}

	protected abstract String formatDependencyGraph(G graph, boolean includeWeights, boolean useShortLabels, boolean plotLoops);
}
