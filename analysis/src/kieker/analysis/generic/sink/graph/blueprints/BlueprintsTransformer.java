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

package kieker.analysis.generic.sink.graph.blueprints;

import java.util.HashMap;
import java.util.Map;

import com.google.common.graph.EndpointPair;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.sink.graph.AbstractTransformer;

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
public class BlueprintsTransformer<N extends INode, E extends IEdge> extends AbstractTransformer<com.tinkerpop.blueprints.Graph, N, E> {

	private static final String LABEL_PROPERTY = "label";

	private final com.tinkerpop.blueprints.Graph transformedGraph = new TinkerGraph();
	private final Map<INode, com.tinkerpop.blueprints.Vertex> mappedVertices = new HashMap<>(); // NOPMD (no concurrent access intended)

	public BlueprintsTransformer(final IGraph<N, E> graph) {
		super(graph);
	}

	@Override
	protected void transformVertex(final N vertex) {
		final com.tinkerpop.blueprints.Vertex mappedVertex = this.transformedGraph.addVertex(vertex.getId());
		this.mappedVertices.put(vertex, mappedVertex);
		for (final String propertyKey : vertex.getPropertyKeys()) {
			mappedVertex.setProperty(propertyKey, vertex.getProperty(propertyKey));
		}

	}

	@Override
	protected void transformEdge(final E edge) {
		final EndpointPair<N> pair = this.graph.getGraph().incidentNodes(edge);
		final com.tinkerpop.blueprints.Vertex mappedInVertex = this.mappedVertices.get(pair.target());
		final com.tinkerpop.blueprints.Vertex mappedOutVertex = this.mappedVertices.get(pair.source());
		String label = edge.getProperty(LABEL_PROPERTY);
		if (label == null) {
			label = "";
		}
		final com.tinkerpop.blueprints.Edge mappedEdge = this.transformedGraph.addEdge(edge.getId(), mappedOutVertex, mappedInVertex, label);
		for (final String propertyKey : edge.getPropertyKeys()) {
			mappedEdge.setProperty(propertyKey, edge.getProperty(propertyKey));
		}
	}

	@Override
	protected com.tinkerpop.blueprints.Graph getTransformation() {
		return this.transformedGraph;
	}

	@Override
	protected void beforeTransformation() {
		// Do nothing
	}

	@Override
	protected void afterTransformation() {
		// Do nothing
	}

}
