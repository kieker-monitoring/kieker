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

package kieker.analysisteetime.util.graph.traversal;

import java.util.List;

import kieker.analysisteetime.util.graph.IEdge;
import kieker.analysisteetime.util.graph.IGraph;
import kieker.analysisteetime.util.graph.IVertex;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class FlatGraphTraverser extends AbstractGraphTraverser {

	public FlatGraphTraverser() {
		super();
	}

	public FlatGraphTraverser(final List<IVertexVisitor> vertexVisitors, final List<IEdgeVisitor> edgeVisitors) {
		super(vertexVisitors, edgeVisitors);
	}

	public FlatGraphTraverser(final IVertexVisitor vertexVisitor, final IEdgeVisitor edgeVisitor) {
		super(vertexVisitor, edgeVisitor);
	}

	@Override
	public void traverse(final IGraph graph) {

		for (final IVertex vertex : graph.getVertices()) {
			for (final IVertexVisitor visitor : this.vertexVisitors) {
				visitor.visitVertex(vertex);
			}
		}

		for (final IEdge edge : graph.getEdges()) {
			for (final IEdgeVisitor visitor : this.edgeVisitors) {
				visitor.visitEdge(edge);
			}
		}

	}

}
