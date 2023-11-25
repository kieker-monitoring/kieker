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
package kieker.analysis.generic.graph.flattening;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.traversal.AbstractGraphTraverser;
import kieker.analysis.generic.graph.traversal.FlatGraphTraverser;
import kieker.analysis.generic.graph.traversal.IEdgeVisitor;
import kieker.analysis.generic.graph.traversal.INodeVisitor;

/**
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TopLevelFlattener<N extends INode, E extends IEdge> implements IGraphFlattener<N, E>, INodeVisitor<N>, IEdgeVisitor<E> {

	private final AbstractGraphTraverser<N, E> traverser = new FlatGraphTraverser<>(this, this);

	public TopLevelFlattener() {
		// create flattener
	}

	@Override
	public void flatten(final MutableNetwork<N, E> graph) {
		this.traverser.traverse(graph);
	}

	@Override
	public void visitNode(final INode node) {
		node.removeChildGraph();
	}

	@Override
	public void visitEdge(final IEdge edge) {
		// Do nothing
	}

}
