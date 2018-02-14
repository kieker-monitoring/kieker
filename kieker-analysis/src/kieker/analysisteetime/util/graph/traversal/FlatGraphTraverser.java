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

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class FlatGraphTraverser extends AbstractGraphTraverser {

	public FlatGraphTraverser() {
		super();
	}

	public FlatGraphTraverser(final List<VertexVisitor> vertexVisitors, final List<EdgeVisitor> edgeVisitors) {
		super(vertexVisitors, edgeVisitors);
	}

	public FlatGraphTraverser(final VertexVisitor vertexVisitor, final EdgeVisitor edgeVisitor) {
		super(vertexVisitor, edgeVisitor);
	}

	@Override
	public void traverse(final Graph graph) {

		for (final Vertex vertex : graph.getVertices()) {
			for (final VertexVisitor visitor : this.vertexVisitors) {
				visitor.visitVertex(vertex);
			}
		}

		for (final Edge edge : graph.getEdges()) {
			for (final EdgeVisitor visitor : this.edgeVisitors) {
				visitor.visitEdge(edge);
			}
		}

	}

}
