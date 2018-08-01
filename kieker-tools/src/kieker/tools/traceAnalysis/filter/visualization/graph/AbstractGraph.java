/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.graph;

import java.util.Collection;

/**
 * Generic superclass for all graphs in the visualization package.
 * 
 * @author Holger Knoche
 * 
 * @param <V>
 *            The type of the graph's vertices
 * @param <E>
 *            The type of the graph's edges
 * @param <O>
 *            The type of object from which the graph's elements originate
 * 
 * @since 1.6
 */
public abstract class AbstractGraph<V extends AbstractVertex<V, E, O>, E extends AbstractEdge<V, E, O>, O> {

	/**
	 * Returns the vertices contained in this graph.
	 * 
	 * @return See above
	 */
	public abstract Collection<V> getVertices();

	/**
	 * Traverses this graph using the given visitor. Outgoing edges are traversed immediately after their owning node.
	 * 
	 * @param visitor
	 *            The visitor to call during traversal
	 */
	public void traverse(final IGraphVisitor<V, E> visitor) {
		for (final V vertex : this.getVertices()) {
			visitor.visitVertex(vertex);

			for (final E edge : vertex.getOutgoingEdges()) {
				visitor.visitEdge(edge);
			}
		}
	}

	/**
	 * Traverses this graph using the given visitor. All vertices are visited before the first edge.
	 * 
	 * @param visitor
	 *            The visitor to call during traversal
	 */
	public void traverseWithVerticesFirst(final IGraphVisitor<V, E> visitor) {
		for (final V vertex : this.getVertices()) {
			visitor.visitVertex(vertex);
		}

		for (final V vertex : this.getVertices()) {
			for (final E edge : vertex.getOutgoingEdges()) {
				visitor.visitEdge(edge);
			}
		}
	}

	/**
	 * Interface for abstract-graph visitors. These visitors can be used in conjunction with the
	 * graph's traversal methods.
	 * 
	 * @author Holger Knoche
	 * 
	 * @param <V>
	 *            The type of the graph's vertices
	 * @param <E>
	 *            The type of the graph's edges
	 * 
	 * @since 1.6
	 */
	public interface IGraphVisitor<V, E> {

		/**
		 * Call-back operation that is invoked when a vertex is encountered during graph traversal.
		 * 
		 * @param vertex
		 *            The encountered vertex
		 * 
		 * @since 1.6
		 */
		public void visitVertex(V vertex);

		/**
		 * Call-back operation that is invoked when an edge is encountered during graph traversal.
		 * 
		 * @param edge
		 *            The encountered edge
		 * 
		 * @since 1.6
		 */
		public void visitEdge(E edge);

	}
}
