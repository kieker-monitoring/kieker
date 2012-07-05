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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * Formatter for operation dependency graphs on the assembly level (see {@link OperationAssemblyDependencyGraph}).
 * 
 * @author Holger Knoche
 * 
 */
public class OperationAssemblyDependencyGraphFormatter extends AbstractOperationDependencyGraphFormatter<OperationAssemblyDependencyGraph> {

	private static class EdgeVisitor extends AbstractDependencyGraphFormatterVisitor<AssemblyComponentOperationPair> {

		public EdgeVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops, final boolean useShortLabels) {
			super(builder, includeWeights, plotLoops, useShortLabels);
		}

		public void visitVertex(final DependencyGraphNode<AssemblyComponentOperationPair> vertex) {
			// Do nothing
		}

	}

	private Map<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> groupNodesByComponent(
			final OperationAssemblyDependencyGraph graph) {
		final Map<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> grouping = new HashMap<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>>();

		for (final DependencyGraphNode<AssemblyComponentOperationPair> vertex : graph.getVertices()) {
			final AssemblyComponentOperationPair pair = vertex.getEntity();
			final AssemblyComponent assemblyComponent = pair.getAssemblyComponent();

			List<DependencyGraphNode<AssemblyComponentOperationPair>> nodes = grouping.get(assemblyComponent);
			if (nodes == null) {
				nodes = new ArrayList<DependencyGraphNode<AssemblyComponentOperationPair>>();
				grouping.put(assemblyComponent, nodes);
			}
			nodes.add(vertex);
		}

		return grouping;
	}

	private static String createComponentNodeLabel(final AssemblyComponent component, final boolean useShortLabels) {
		final StringBuilder builder = new StringBuilder();

		builder.append(AbstractDependencyGraphFilter.STEREOTYPE_ASSEMBLY_COMPONENT).append("\\n");
		builder.append(component.getName()).append(":");

		if (useShortLabels) {
			builder.append("..");
		} else {
			builder.append(component.getType().getPackageName()).append(".");
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
						AbstractDependencyGraphFormatter.createAssemblyComponentId(assemblyComponent),
						assemblyComponent.getName(),
						DotFactory.DOT_SHAPE_NONE,
						null, // style
						null, // framecolor
						null, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null // misc
						));
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
						null, // framecolor
						AbstractDependencyGraphFormatter.getNodeFillColor(node), // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null // misc
						));
			}
			builder.append("}\n");
		}
	}

	@Override
	protected String formatDependencyGraph(final OperationAssemblyDependencyGraph graph, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		final StringBuilder builder = new StringBuilder();

		this.appendGraphHeader(builder);
		final Map<AssemblyComponent, List<DependencyGraphNode<AssemblyComponentOperationPair>>> grouping = this.groupNodesByComponent(graph);
		this.createGraph(builder, grouping, useShortLabels);
		graph.traverseWithVerticesFirst(new EdgeVisitor(builder, includeWeights, plotLoops, useShortLabels));
		this.appendGraphFooter(builder);

		return builder.toString();
	}

}
