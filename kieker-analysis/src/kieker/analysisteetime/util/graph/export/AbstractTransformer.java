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

package kieker.analysisteetime.util.graph.export;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.traversal.AbstractGraphTraverser;
import kieker.analysisteetime.util.graph.traversal.EdgeVisitor;
import kieker.analysisteetime.util.graph.traversal.FlatGraphTraverser;
import kieker.analysisteetime.util.graph.traversal.VertexVisitor;

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
public abstract class AbstractTransformer<O> implements VertexVisitor, EdgeVisitor {

	protected Graph graph;

	private final AbstractGraphTraverser graphTraverser = new FlatGraphTraverser(this, this);

	protected AbstractTransformer(final Graph graph) {
		this.graph = graph;
	}

	public final O transform() {

		this.beforeTransformation();

		this.graphTraverser.traverse(this.graph);

		this.afterTransformation();

		return this.getTransformation();
	}

	protected abstract void beforeTransformation();

	protected abstract void afterTransformation();

	protected abstract void transformVertex(Vertex vertex);

	protected abstract void transformEdge(Edge edge);

	protected abstract O getTransformation();

	@Override
	public void visitVertex(final Vertex vertex) {
		this.transformVertex(vertex);
	}

	@Override
	public void visitEdge(final Edge edge) {
		this.transformEdge(edge);
	}

}
