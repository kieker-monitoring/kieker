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
package kieker.tools.trace.analysis.filter.visualization.dependencyGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.VisualizationConstants;
import kieker.tools.trace.analysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * Formatter for operation dependency graphs on the assembly level (see {@link OperationAssemblyDependencyGraph}).
 *
 * @author Holger Knoche
 *
 * @since 1.6
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
public class OperationAssemblyDependencyGraphFormatter extends AbstractOperationDependencyGraphFormatter<OperationAssemblyDependencyGraph> {

	private static final String DEFAULT_FILE_NAME = VisualizationConstants.ASSEMBLY_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX + VisualizationConstants.DOT_FILE_SUFFIX;

	/**
	 * Creates a new formatter.
	 */
	public OperationAssemblyDependencyGraphFormatter() {
		// default empty constructor
	}

	private ConcurrentMap<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> groupNodesByComponent(
			final OperationAssemblyDependencyGraph graph) {
		final ConcurrentMap<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> grouping = new ConcurrentHashMap<>();

		for (final DependencyGraphNode<AssemblyComponentOperationPair> vertex : graph.getVertices()) {
			final AssemblyComponentOperationPair pair = vertex.getEntity();
			final AssemblyComponent assemblyComponent = pair.getAssemblyComponent();

			List<DependencyGraphNode<AssemblyComponentOperationPair>> nodes = grouping.get(assemblyComponent);
			if (nodes == null) {
				nodes = new ArrayList<>();
				grouping.put(assemblyComponent, nodes);
			}
			nodes.add(vertex);
		}

		return grouping;
	}

	private static String createComponentNodeLabel(final AssemblyComponent component, final boolean useShortLabels) {
		final StringBuilder builder = new StringBuilder();

		builder.append(AbstractDependencyGraphFormatter.STEREOTYPE_ASSEMBLY_COMPONENT).append("\\n")
				.append(component.getName()).append(':');

		if (useShortLabels) {
			builder.append("..");
		} else {
			builder.append(component.getType().getPackageName()).append('.');
		}

		builder.append(component.getType().getTypeName());

		return builder.toString();
	}

	private void createGraph(final StringBuilder builder, final Map<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> grouping,
			final boolean useShortLabels) {
		for (final Entry<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> entry : grouping.entrySet()) {
			final AssemblyComponent assemblyComponent = entry.getKey();

			if (assemblyComponent.isRootComponent()) {
				builder.append(DotFactory.createNode("",
						AbstractDependencyGraphFormatter.createNodeId(assemblyComponent.getId()),
						assemblyComponent.getName(),
						DotFactory.DOT_SHAPE_NONE,
						null, // style
						null, // framecolor
						null, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null, // misc
						null));
				continue;
			}

			builder.append(DotFactory.createCluster("",
					AbstractDependencyGraphFormatter.createAssemblyComponentId(assemblyComponent),
					OperationAssemblyDependencyGraphFormatter.createComponentNodeLabel(assemblyComponent, useShortLabels),
					DotFactory.DOT_SHAPE_BOX, // shape
					DotFactory.DOT_STYLE_FILLED, // style
					null, // framecolor
					DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
					null, // fontcolor
					DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
					null)); // misc

			for (final DependencyGraphNode<AssemblyComponentOperationPair> node : entry.getValue()) {
				final Operation operation = node.getEntity().getOperation();

				builder.append(DotFactory.createNode("",
						AbstractDependencyGraphFormatter.createNodeId(node),
						this.createOperationNodeLabel(operation, node),
						DotFactory.DOT_SHAPE_OVAL,
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
	protected String formatDependencyGraph(final OperationAssemblyDependencyGraph graph, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		final StringBuilder builder = new StringBuilder();

		this.appendGraphHeader(builder);
		final ConcurrentMap<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> grouping = this.groupNodesByComponent(graph);
		this.createGraph(builder, grouping, useShortLabels);
		graph.traverseWithVerticesFirst(new EdgeVisitor(builder, includeWeights, plotLoops, useShortLabels));
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
	private static class EdgeVisitor extends AbstractDependencyGraphFormatterVisitor<AssemblyComponentOperationPair> {
		public EdgeVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops, final boolean useShortLabels) {
			super(builder, includeWeights, plotLoops, useShortLabels);
		}

		@Override
		public void visitVertex(final DependencyGraphNode<AssemblyComponentOperationPair> vertex) {
			// Do nothing
		}
	}
}
