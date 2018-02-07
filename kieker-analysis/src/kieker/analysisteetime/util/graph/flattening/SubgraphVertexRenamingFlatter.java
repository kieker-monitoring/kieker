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

package kieker.analysisteetime.util.graph.flattening;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.traversal.AbstractGraphTraverser;
import kieker.analysisteetime.util.graph.traversal.DeepGraphTraverser;
import kieker.analysisteetime.util.graph.traversal.EdgeVisitor;
import kieker.analysisteetime.util.graph.traversal.VertexVisitor;

/**
 * {@link GraphFlattener} that pulls vertices from subgraphs to the top level graph by renaming them.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
// TODO not implemented so far
public class SubgraphVertexRenamingFlatter implements GraphFlattener, VertexVisitor, EdgeVisitor {

	private final AbstractGraphTraverser traverser = new DeepGraphTraverser(this, this);

	public SubgraphVertexRenamingFlatter() {
		// create Flattener
	}

	@Override
	public void flatten(final Graph graph) {
		this.traverser.traverse(graph);
	}

	@Override
	public void visitVertex(final Vertex vertex) {

		// vertex2 = vertex
		// while (vertex2.graph.parentVertex != null) {
		//
		// }

	}

	@Override
	public void visitEdge(final Edge edge) {
		// Do nothing
	}

}
