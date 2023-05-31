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

package kieker.analysis.generic.graph.traversal;

import java.util.List;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

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
public class DeepGraphTraverser<N extends INode, E extends IEdge> extends AbstractGraphTraverser<N, E> {

	public DeepGraphTraverser() {
		super();
	}

	public DeepGraphTraverser(final List<INodeVisitor<N>> nodeVisitors, final List<IEdgeVisitor<E>> edgeVisitors) {
		super(nodeVisitors, edgeVisitors);
	}

	public DeepGraphTraverser(final INodeVisitor<N> nodeVisitor, final IEdgeVisitor<E> edgeVisitor) {
		super(nodeVisitor, edgeVisitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void traverse(final MutableNetwork<N, E> graph) {
		for (final N node : graph.nodes()) {
			for (final INodeVisitor<N> visitor : this.nodeVisitors) {
				visitor.visitNode(node);
			}
			if (node.getChildGraph() != null) {
				this.traverse((MutableNetwork<N, E>) node.getChildGraph().getGraph());
			}
		}

		for (final E edge : graph.edges()) {
			for (final IEdgeVisitor<E> visitor : this.edgeVisitors) {
				visitor.visitEdge(edge);
			}
		}
	}

}
