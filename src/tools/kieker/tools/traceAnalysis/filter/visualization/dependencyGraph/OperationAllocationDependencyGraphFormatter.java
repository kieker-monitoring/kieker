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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * Formatter for operation dependency graphs on the allocation level (see {@link OperationAllocationDependencyGraph}).
 * 
 * @author Holger Knoche
 * 
 */
public class OperationAllocationDependencyGraphFormatter extends AbstractOperationDependencyGraphFormatter<OperationAllocationDependencyGraph> {

	private static class ElementGrouping {

		private final Map<ExecutionContainer, Set<AllocationComponent>> allocationComponentGrouping;
		private final Map<AllocationComponent, Set<DependencyGraphNode<AllocationComponentOperationPair>>> operationGrouping;

		public ElementGrouping(final Map<ExecutionContainer, Set<AllocationComponent>> allocationComponentGrouping,
				final Map<AllocationComponent, Set<DependencyGraphNode<AllocationComponentOperationPair>>> operationGrouping) {
			this.allocationComponentGrouping = allocationComponentGrouping;
			this.operationGrouping = operationGrouping;
		}

		public Map<ExecutionContainer, Set<AllocationComponent>> getAllocationComponentGrouping() {
			return this.allocationComponentGrouping;
		}

		public Map<AllocationComponent, Set<DependencyGraphNode<AllocationComponentOperationPair>>> getOperationGrouping() {
			return this.operationGrouping;
		}

	}

	private static class EdgeVisitor extends AbstractDependencyGraphFormatterVisitor<AllocationComponentOperationPair> {

		public EdgeVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops, final boolean useShortLabels) {
			super(builder, includeWeights, plotLoops, useShortLabels);
		}

		public void visitVertex(final DependencyGraphNode<AllocationComponentOperationPair> vertex) {
			// Do nothing
		}

	}

	private ElementGrouping groupElements(final OperationAllocationDependencyGraph graph) {
		final Map<ExecutionContainer, Set<AllocationComponent>> allocationComponentGrouping = new HashMap<ExecutionContainer, Set<AllocationComponent>>();
		final Map<AllocationComponent, Set<DependencyGraphNode<AllocationComponentOperationPair>>> operationGrouping = new HashMap<AllocationComponent, Set<DependencyGraphNode<AllocationComponentOperationPair>>>();

		for (final DependencyGraphNode<AllocationComponentOperationPair> vertex : graph.getVertices()) {
			final AllocationComponent allocationComponent = vertex.getEntity().getAllocationComponent();
			final ExecutionContainer executionContainer = allocationComponent.getExecutionContainer();

			// Update map execution container -> allocation components
			Set<AllocationComponent> allocationComponents = allocationComponentGrouping.get(executionContainer);
			if (allocationComponents == null) {
				allocationComponents = new HashSet<AllocationComponent>();
				allocationComponentGrouping.put(executionContainer, allocationComponents);
			}
			allocationComponents.add(allocationComponent);

			// Update map allocation component -> operations
			Set<DependencyGraphNode<AllocationComponentOperationPair>> operations = operationGrouping.get(allocationComponent);
			if (operations == null) {
				operations = new HashSet<DependencyGraphNode<AllocationComponentOperationPair>>();
				operationGrouping.put(allocationComponent, operations);
			}
			operations.add(vertex);
		}

		return new ElementGrouping(allocationComponentGrouping, operationGrouping);
	}

	private static String createContainerNodeLabel(final ExecutionContainer container) {
		return (STEREOTYPE_EXECUTION_CONTAINER + "\\n" + container.getName());
	}

	private static String createAllocationComponentNodeLabel(final AllocationComponent component, final boolean useShortLabels) {
		final StringBuilder builder = new StringBuilder();

		builder.append(AbstractDependencyGraphFormatter.STEREOTYPE_ALLOCATION_COMPONENT).append("\\n");
		builder.append(component.getAssemblyComponent().getName()).append(":");

		if (useShortLabels) {
			builder.append("..");
		} else {
			builder.append(component.getAssemblyComponent().getType().getPackageName()).append(".");
		}

		builder.append(component.getAssemblyComponent().getType().getTypeName());

		return builder.toString();
	}

	private void createGraph(final ElementGrouping grouping, final StringBuilder builder, final boolean useShortLabels) {
		final Map<ExecutionContainer, Set<AllocationComponent>> allocationComponentGrouping = grouping.getAllocationComponentGrouping();
		final Map<AllocationComponent, Set<DependencyGraphNode<AllocationComponentOperationPair>>> operationGrouping = grouping.getOperationGrouping();

		for (final Entry<ExecutionContainer, Set<AllocationComponent>> containerComponentEntry : allocationComponentGrouping.entrySet()) {
			final ExecutionContainer executionContainer = containerComponentEntry.getKey();

			// If the current execution container is the root container, just build a simple node
			// and go on
			if (executionContainer.isRootContainer()) {
				builder.append(DotFactory.createNode("",
						AbstractDependencyGraphFormatter.createNodeId(executionContainer.getId()),
						executionContainer.getName(),
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

			// If it is a common container, create the cluster for the execution container...
			builder.append(DotFactory.createCluster("",
					AbstractDependencyGraphFormatter.createContainerId(executionContainer),
					OperationAllocationDependencyGraphFormatter.createContainerNodeLabel(executionContainer),
					DotFactory.DOT_SHAPE_BOX, // shape
					DotFactory.DOT_STYLE_FILLED, // style
					null, // framecolor
					DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
					null, // fontcolor
					DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
					null)); // misc

			// ...then, create clusters for the contained allocation components.
			for (final AllocationComponent allocationComponent : containerComponentEntry.getValue()) {
				builder.append(DotFactory.createCluster("",
						AbstractDependencyGraphFormatter.createAllocationComponentId(allocationComponent),
						OperationAllocationDependencyGraphFormatter.createAllocationComponentNodeLabel(allocationComponent, useShortLabels),
						DotFactory.DOT_SHAPE_BOX,
						DotFactory.DOT_STYLE_FILLED, // style
						null, // framecolor
						DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null // misc
						));

				// Print the nodes for the operations
				for (final DependencyGraphNode<AllocationComponentOperationPair> node : operationGrouping.get(allocationComponent)) {
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
			builder.append("}\n");
		}
	}

	@Override
	protected String formatDependencyGraph(final OperationAllocationDependencyGraph graph, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		final StringBuilder builder = new StringBuilder();

		this.appendGraphHeader(builder);
		final ElementGrouping grouping = this.groupElements(graph);
		this.createGraph(grouping, builder, useShortLabels);
		graph.traverseWithVerticesFirst(new EdgeVisitor(builder, includeWeights, plotLoops, useShortLabels));
		this.appendGraphFooter(builder);

		return builder.toString();
	}
}
