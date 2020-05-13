/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.graph.export.blueprints;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.IEdge;
import kieker.analysisteetime.util.graph.IGraph;
import kieker.analysisteetime.util.graph.IVertex;
import kieker.analysisteetime.util.graph.export.AbstractTransformer;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class BlueprintsTransformer extends AbstractTransformer<com.tinkerpop.blueprints.Graph> {

	private static final String LABEL_PROPERTY = "label";

	private final com.tinkerpop.blueprints.Graph transformedGraph = new TinkerGraph();
	private final Map<IVertex, com.tinkerpop.blueprints.Vertex> mappedVertices = new HashMap<>(); // NOPMD (no concurrent access intended)

	public BlueprintsTransformer(final IGraph graph) {
		super(graph);
	}

	@Override
	protected void transformVertex(final IVertex vertex) {
		final com.tinkerpop.blueprints.Vertex mappedVertex = this.transformedGraph.addVertex(vertex.getId());
		this.mappedVertices.put(vertex, mappedVertex);
		for (final String propertyKey : vertex.getPropertyKeys()) {
			mappedVertex.setProperty(propertyKey, vertex.getProperty(propertyKey));
		}

	}

	@Override
	protected void transformEdge(final IEdge edge) {
		final com.tinkerpop.blueprints.Vertex mappedInVertex = this.mappedVertices.get(edge.getVertex(Direction.IN));
		final com.tinkerpop.blueprints.Vertex mappedOutVertex = this.mappedVertices.get(edge.getVertex(Direction.OUT));
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
