package kieker.analysisteetime.util.graph.export.blueprints;

import java.util.HashMap;
import java.util.Map;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.export.AbstractTransformer;

public class BlueprintsTransformer extends AbstractTransformer<com.tinkerpop.blueprints.Graph> {

	private final com.tinkerpop.blueprints.Graph transformedGraph = new TinkerGraph();
	private final Map<Vertex, com.tinkerpop.blueprints.Vertex> mappedVertices = new HashMap<>(); // NOPMD (no concurrent access intended)

	private static final String LABEL_PROPERTY = "label";

	public BlueprintsTransformer(final Graph graph) {
		super(graph);
	}

	@Override
	protected void transformVertex(final Vertex vertex) {
		final com.tinkerpop.blueprints.Vertex mappedVertex = this.transformedGraph.addVertex(vertex.getId());
		this.mappedVertices.put(vertex, mappedVertex);
		for (final String propertyKey : vertex.getPropertyKeys()) {
			mappedVertex.setProperty(propertyKey, vertex.getProperty(propertyKey));
		}

	}

	@Override
	protected void transformEdge(final Edge edge) {
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
