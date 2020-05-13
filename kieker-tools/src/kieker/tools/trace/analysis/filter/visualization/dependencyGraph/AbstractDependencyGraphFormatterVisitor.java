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

import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph.IGraphVisitor;
import kieker.tools.trace.analysis.filter.visualization.util.dot.DotFactory;
import kieker.tools.trace.analysis.systemModel.ISystemModelElement;

/**
 * Abstract base class for dependency-graph-formatting visitors. This base class already provides the common
 * algorithm for formatting edges.
 * 
 * @author Holger Knoche
 * 
 * @param <T> subtype of an ISystemModelElement
 * 
 * @since 1.6
 */
public abstract class AbstractDependencyGraphFormatterVisitor<T extends ISystemModelElement> implements
		IGraphVisitor<DependencyGraphNode<T>, WeightedBidirectionalDependencyGraphEdge<T>> {

	protected final StringBuilder builder; // NOPMD (AvoidStringBufferField)
	/** The flag determining whether to include weights or not. */
	protected final boolean includeWeights;
	/** The flag determining whether to plot loops or not. */
	protected final boolean plotLoops;
	/** The flag determining whether to use short labels or not. */
	protected final boolean useShortLabels;

	/**
	 * Creates a new formatter visitor using the given arguments.
	 * 
	 * @param builder
	 *            The string builder to send the generated output to
	 * @param includeWeights
	 *            Indicates whether weights should be printed at the edges
	 * @param plotLoops
	 *            Indicates whether self-loops should be displayed
	 * @param useShortLabels
	 *            Indicates whether short labels should be used
	 */
	public AbstractDependencyGraphFormatterVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops,
			final boolean useShortLabels) {
		this.builder = builder;
		this.includeWeights = includeWeights;
		this.plotLoops = plotLoops;
		this.useShortLabels = useShortLabels;
	}

	@Override
	public void visitEdge(final WeightedBidirectionalDependencyGraphEdge<T> edge) {
		final DependencyGraphNode<T> sourceNode = edge.getSource();
		final DependencyGraphNode<T> destinationNode = edge.getTarget();

		if ((sourceNode.equals(destinationNode)) && !this.plotLoops) {
			return;
		}

		final String lineStyle = (edge.isAssumed()) ? DotFactory.DOT_STYLE_DASHED : DotFactory.DOT_STYLE_SOLID; // NOCS (inline ?)
		final String color = AbstractGraphFormatter.getDotRepresentation(edge.getColor());

		if (this.includeWeights) {
			this.builder.append(DotFactory.createConnection("", AbstractDependencyGraphFormatter.createNodeId(sourceNode),
					AbstractDependencyGraphFormatter.createNodeId(destinationNode),
					Integer.toString(edge.getTargetWeight().get()), lineStyle, DotFactory.DOT_ARROWHEAD_OPEN, color));
		} else {
			this.builder.append(DotFactory.createConnection("", AbstractDependencyGraphFormatter.createNodeId(sourceNode),
					AbstractDependencyGraphFormatter.createNodeId(destinationNode), lineStyle,
					DotFactory.DOT_ARROWHEAD_OPEN, color));
		}

		this.builder.append("\n");
	}
}
