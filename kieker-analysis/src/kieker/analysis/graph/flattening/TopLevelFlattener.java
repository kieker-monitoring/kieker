/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.flattening;

import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.IVertex;
import kieker.analysis.graph.traversal.AbstractGraphTraverser;
import kieker.analysis.graph.traversal.FlatGraphTraverser;
import kieker.analysis.graph.traversal.IEdgeVisitor;
import kieker.analysis.graph.traversal.IVertexVisitor;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TopLevelFlattener implements IGraphFlattener, IVertexVisitor, IEdgeVisitor {

	private final AbstractGraphTraverser traverser = new FlatGraphTraverser(this, this);

	public TopLevelFlattener() {
		// create flattener
	}

	@Override
	public void flatten(final IGraph graph) {
		this.traverser.traverse(graph);
	}

	@Override
	public void visitVertex(final IVertex vertex) {
		vertex.removeChildGraph();
	}

	@Override
	public void visitEdge(final IEdge edge) {
		// Do nothing
	}

}
