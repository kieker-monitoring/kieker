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

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph.Visitor;
import kieker.tools.traceAnalysis.filter.visualization.util.dot.DotFactory;

/**
 * Abstract base class for dependency-graph-formatting visitors. This base class already provides the common
 * algorithm for formatting edges.
 * 
 * @author Holger Knoche
 * 
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 */
public abstract class AbstractDependencyGraphFormatterVisitor<V extends DependencyGraphNode<?>, E extends WeightedBidirectionalDependencyGraphEdge<?>> implements
		Visitor<V, E> {

	private final StringBuilder builder;

	private final boolean includeWeights;
	private final boolean plotLoops;

	/**
	 * Creates a new formatter visitor using the given arguments.
	 * 
	 * @param builder
	 *            The string builder to send the generated output to
	 * @param includeWeights
	 *            Indicates whether weights should be printed at the edges
	 * @param plotLoops
	 *            Indicates whether self-loops should be displayed
	 */
	public AbstractDependencyGraphFormatterVisitor(final StringBuilder builder, final boolean includeWeights, final boolean plotLoops) {
		this.builder = builder;
		this.includeWeights = includeWeights;
		this.plotLoops = plotLoops;
	}

	public void visitEdge(final E edge) {
		final DependencyGraphNode<?> sourceNode = edge.getSource();
		final DependencyGraphNode<?> destinationNode = edge.getTarget();

		if ((sourceNode.equals(destinationNode)) && !this.plotLoops) {
			return;
		}

		final String lineStyle = (edge.isAssumed()) ? DotFactory.DOT_STYLE_DASHED : DotFactory.DOT_STYLE_SOLID; // NOCS (inline ?)

		if (this.includeWeights) {
			this.builder.append(DotFactory.createConnection("", AbstractDependencyGraphFormatter.createNodeId(sourceNode),
					AbstractDependencyGraphFormatter.createNodeId(destinationNode),
					Integer.toString(edge.getTargetWeight().getValue()), lineStyle, DotFactory.DOT_ARROWHEAD_OPEN));
		} else {
			this.builder.append(DotFactory.createConnection("", AbstractDependencyGraphFormatter.createNodeId(sourceNode),
					AbstractDependencyGraphFormatter.createNodeId(destinationNode), lineStyle,
					DotFactory.DOT_ARROWHEAD_OPEN));
		}

		this.builder.append("\n");
	}
}
