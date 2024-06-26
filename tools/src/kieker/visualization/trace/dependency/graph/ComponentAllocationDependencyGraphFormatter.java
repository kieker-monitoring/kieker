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
package kieker.visualization.trace.dependency.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.AllocationComponent;
import kieker.model.system.model.ExecutionContainer;
import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.tools.trace.analysis.filter.visualization.util.dot.DotFactory;

/**
 * Formatter class for component dependency graphs on the allocation level (see {@link ComponentAllocationDependencyGraph}) .
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
public class ComponentAllocationDependencyGraphFormatter extends AbstractComponentDependencyGraphFormatter<ComponentAllocationDependencyGraph> {

	private static final String DEFAULT_FILE_NAME = VisualizationConstants.ALLOCATION_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX + VisualizationConstants.DOT_FILE_SUFFIX;

	/**
	 * Creates a new formatter.
	 */
	public ComponentAllocationDependencyGraphFormatter() {
		// empty default Constructor
	}

	private static ConcurrentMap<ExecutionContainer, List<DependencyGraphNode<AllocationComponent>>> groupNodesByComponent(
			final ComponentAllocationDependencyGraph graph) {
		final ConcurrentMap<ExecutionContainer, List<DependencyGraphNode<AllocationComponent>>> nodeMap = new ConcurrentHashMap<>();

		for (final DependencyGraphNode<AllocationComponent> node : graph.getNodes()) {
			final ExecutionContainer container = node.getEntity().getExecutionContainer();

			List<DependencyGraphNode<AllocationComponent>> nodes = nodeMap.get(container);
			if (nodes == null) {
				nodes = new ArrayList<>();
				nodeMap.put(container, nodes);
			}

			nodes.add(node);
		}

		return nodeMap;
	}

	private void handleContainerEntry(final Entry<ExecutionContainer, List<DependencyGraphNode<AllocationComponent>>> entry, final StringBuilder builder,
			final boolean useShortLabels) {
		final ExecutionContainer container = entry.getKey();

		if (container.isRootContainer()) {
			builder.append(DotFactory.createNode("", AbstractDependencyGraphFormatter.createNodeId(DependencyGraphNode.ROOT_NODE_ID),
					SystemModelRepository.ROOT_NODE_LABEL, DotFactory.DOT_SHAPE_NONE, null, // style
					null, // framecolor
					null, // fillcolor
					null, // fontcolor
					DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
					null, // imagefilename
					null, // misc
					null)); // tooltip
		} else {
			builder.append(DotFactory.createCluster("", AbstractDependencyGraphFormatter.createContainerId(container),
					AbstractDependencyGraphFormatter.STEREOTYPE_EXECUTION_CONTAINER + "\\n" + container.getName(), DotFactory.DOT_SHAPE_BOX, // shape
					DotFactory.DOT_STYLE_FILLED, // style
					null, // framecolor
					DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
					null, // fontcolor
					DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
					null)); // misc
			// dot code for contained components
			for (final DependencyGraphNode<AllocationComponent> node : entry.getValue()) {
				builder.append(DotFactory.createNode("",
						AbstractDependencyGraphFormatter.createNodeId(node),
						AbstractComponentDependencyGraphFormatter.createComponentNodeLabel(node, useShortLabels,
								AbstractDependencyGraphFormatter.STEREOTYPE_ALLOCATION_COMPONENT),
						DotFactory.DOT_SHAPE_BOX,
						DotFactory.DOT_STYLE_FILLED, // style
						AbstractGraphFormatter.getDotRepresentation(node.getColor()), // framecolor
						AbstractDependencyGraphFormatter.getNodeFillColor(node), // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null, // misc
						node.getDescription())); // tooltip
			}
			builder.append("}\n");
		}
	}

	@Override
	protected String formatDependencyGraph(final ComponentAllocationDependencyGraph graph, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		final StringBuilder builder = new StringBuilder();

		this.appendGraphHeader(builder);

		// Group nodes by execution containers
		final ConcurrentMap<ExecutionContainer, List<DependencyGraphNode<AllocationComponent>>> nodeMap = ComponentAllocationDependencyGraphFormatter
				.groupNodesByComponent(graph);
		for (final Entry<ExecutionContainer, List<DependencyGraphNode<AllocationComponent>>> entry : nodeMap.entrySet()) {
			this.handleContainerEntry(entry, builder, useShortLabels);
		}
		// Format the graph's edges
		graph.traverseWithVerticesFirst(new EdgeFormattingVisitor(builder, includeWeights, plotLoops, useShortLabels));

		this.appendGraphFooter(builder);

		return builder.toString();
	}

	@Override
	public String getDefaultFileName() {
		return DEFAULT_FILE_NAME;
	}

	/**
	 * @author Holger Knoche
	 */
	private static class EdgeFormattingVisitor extends
			AbstractDependencyGraphFormatterVisitor<AllocationComponent> {

		public EdgeFormattingVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops, final boolean useShortLabels) {
			super(builder, includeWeights, plotLoops, useShortLabels);
		}

		@Override
		public void visitVertex(final DependencyGraphNode<AllocationComponent> vertex) {
			// Do nothing
		}

	}
}
