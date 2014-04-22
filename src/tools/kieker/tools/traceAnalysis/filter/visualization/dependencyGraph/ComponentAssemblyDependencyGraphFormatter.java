/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;

/**
 * Formatter class for component dependency graphs on the assembly level (see {@link ComponentAssemblyDependencyGraph}).
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class ComponentAssemblyDependencyGraphFormatter extends AbstractComponentDependencyGraphFormatter<ComponentAssemblyDependencyGraph> {

	private static final String DEFAULT_FILE_NAME = Constants.ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX + Constants.DOT_FILE_SUFFIX;

	/**
	 * Creates a new formatter.
	 */
	public ComponentAssemblyDependencyGraphFormatter() {
		// empty default constructor
	}

	@Override
	protected String formatDependencyGraph(final ComponentAssemblyDependencyGraph graph, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		final StringBuilder builder = new StringBuilder();

		this.appendGraphHeader(builder);
		graph.traverseWithVerticesFirst(new FormatterVisitor(builder, includeWeights, plotLoops, useShortLabels));
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
	private static class FormatterVisitor extends AbstractDependencyGraphFormatterVisitor<AssemblyComponent> {

		public FormatterVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops, final boolean useShortLabels) {
			super(builder, includeWeights, plotLoops, useShortLabels);
		}

		private String createNodeLabel(final DependencyGraphNode<AssemblyComponent> vertex, final AssemblyComponent component) {
			final StringBuilder builder = new StringBuilder();

			builder.append(AbstractDependencyGraphFormatter.STEREOTYPE_ASSEMBLY_COMPONENT).append("\\n");
			builder.append(component.getName()).append(':');

			if (this.useShortLabels) {
				builder.append("..").append(component.getType().getTypeName());
			} else {
				builder.append(component.getType().getFullQualifiedName());
			}

			AbstractGraphFormatter.formatDecorations(builder, vertex);

			return builder.toString();
		}

		@Override
		public void visitVertex(final DependencyGraphNode<AssemblyComponent> vertex) {
			final AssemblyComponent component = vertex.getEntity();

			if (component.isRootComponent()) {
				this.builder.append(DotFactory.createNode("", AbstractDependencyGraphFormatter.createNodeId(vertex),
						component.getName(),
						DotFactory.DOT_SHAPE_NONE, // NOCS
						null, // style // NOCS // NOPMD (null)
						null, // framecolor
						null, // fillcolor // NOCS //NOPMD (null)
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null, // misc
						null)); // tooltip
			} else {
				this.builder.append(DotFactory.createNode("", AbstractDependencyGraphFormatter.createNodeId(vertex),
						this.createNodeLabel(vertex, component), // NOCS
						DotFactory.DOT_SHAPE_BOX, // NOCS
						DotFactory.DOT_STYLE_FILLED, // style // NOCS // NOPMD (null)
						AbstractGraphFormatter.getDotRepresentation(vertex.getColor()), // framecolor
						AbstractDependencyGraphFormatter.getNodeFillColor(vertex), // fillcolor // NOCS //NOPMD (null)
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null, // misc
						vertex.getDescription())); // tooltip
			}

			this.builder.append("\n");
		}
	}
}
