/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.sink.graph;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.traversal.AbstractGraphTraverser;
import kieker.analysis.generic.graph.traversal.FlatGraphTraverser;
import kieker.analysis.generic.graph.traversal.IEdgeVisitor;
import kieker.analysis.generic.graph.traversal.INodeVisitor;

/**
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 * @param <O>
 *            Output format of the transformation
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public abstract class AbstractTransformer<O, N extends INode, E extends IEdge> implements INodeVisitor<N>, IEdgeVisitor<E> {

	protected IGraph<N, E> graph;

	private final AbstractGraphTraverser<N, E> graphTraverser = new FlatGraphTraverser<>(this, this);

	protected AbstractTransformer(final IGraph<N, E> graph) {
		this.graph = graph;
	}

	protected AbstractTransformer(final MutableNetwork<N, E> graph, final String label) {
		this.graph = GraphFactory.createGraph(label, graph);
	}

	public final O transform() {
		this.beforeTransformation();

		this.graphTraverser.traverse(this.graph.getGraph());

		this.afterTransformation();

		return this.getTransformation();
	}

	protected abstract void beforeTransformation();

	protected abstract void afterTransformation();

	protected abstract void transformVertex(N vertex);

	protected abstract void transformEdge(E edge);

	protected abstract O getTransformation();

	@Override
	public void visitNode(final N vertex) {
		this.transformVertex(vertex);
	}

	@Override
	public void visitEdge(final E edge) {
		this.transformEdge(edge);
	}

}
