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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;

/**
 * Formatter for container dependency graphs.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class ContainerDependencyGraphFormatter extends AbstractDependencyGraphFormatter<ContainerDependencyGraph> {

	private static final String DEFAULT_FILE_NAME = Constants.CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX + Constants.DOT_FILE_SUFFIX;

	/**
	 * Creates a new formatter.
	 */
	public ContainerDependencyGraphFormatter() {
		// empty default constructor
	}

	static String createExecutionContainerNodeLabel(final ExecutionContainer container) { // NOPMD package for outer class
		return AbstractDependencyGraphFormatter.STEREOTYPE_EXECUTION_CONTAINER + "\\n" + container.getName();
	}

	@Override
	protected String formatDependencyGraph(final ContainerDependencyGraph graph, final boolean includeWeights, final boolean useShortLabels,
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
	private static class FormatterVisitor extends AbstractDependencyGraphFormatterVisitor<ExecutionContainer> {

		public FormatterVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops, final boolean useShortLabels) {
			super(builder, includeWeights, plotLoops, useShortLabels);
		}

		@Override
		public void visitVertex(final DependencyGraphNode<ExecutionContainer> vertex) {
			final ExecutionContainer container = vertex.getEntity();

			if (container.isRootContainer()) {
				this.builder.append(DotFactory.createNode("",
						AbstractDependencyGraphFormatter.createNodeId(vertex),
						container.getName(),
						DotFactory.DOT_SHAPE_NONE,
						null,
						null, // framecolor
						null, // fillcolor // NOPMD (null) // NOCS
						null, // fontcolor
						DotFactory.DOT_DEFAULT_FONTSIZE, // fontsize
						null, // imagefilename
						null, // misc
						null)); // tooltip
			} else {
				this.builder.append(DotFactory.createNode("",
						AbstractDependencyGraphFormatter.createNodeId(vertex),
						ContainerDependencyGraphFormatter.createExecutionContainerNodeLabel(container),
						DotFactory.DOT_SHAPE_BOX3D, // NOCS (AvoidInlineConditionalsCheck)
						DotFactory.DOT_STYLE_FILLED, // style // NOPMD (null) // NOCS (AvoidInlineConditionalsCheck)
						AbstractGraphFormatter.getDotRepresentation(vertex.getColor()), // framecolor
						DotFactory.DOT_FILLCOLOR_WHITE, // fillcolor // NOPMD (null) // NOCS
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
