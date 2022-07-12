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

package kieker.analysis.generic.sink.graph;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.traversal.AbstractGraphTraverser;
import kieker.analysis.generic.graph.traversal.FlatGraphTraverser;
import kieker.analysis.generic.graph.traversal.IEdgeVisitor;
import kieker.analysis.generic.graph.traversal.IVertexVisitor;

/**
 *
 *
 * @param <O>
 *            Output format of the transformation
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public abstract class AbstractTransformer<O> implements IVertexVisitor, IEdgeVisitor {

	protected IGraph graph;

	private final AbstractGraphTraverser graphTraverser = new FlatGraphTraverser(this, this);

	protected AbstractTransformer(final IGraph graph) {
		this.graph = graph;
	}

	protected AbstractTransformer(final MutableNetwork<INode, IEdge> graph, final String label) {
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

	protected abstract void transformVertex(INode vertex);

	protected abstract void transformEdge(IEdge edge);

	protected abstract O getTransformation();

	@Override
	public void visitVertex(final INode vertex) {
		this.transformVertex(vertex);
	}

	@Override
	public void visitEdge(final IEdge edge) {
		this.transformEdge(edge);
	}

}
