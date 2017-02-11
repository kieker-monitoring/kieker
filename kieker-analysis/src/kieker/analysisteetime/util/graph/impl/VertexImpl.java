package kieker.analysisteetime.util.graph.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

class VertexImpl extends GraphElementImpl implements Vertex {

	protected Map<Object, Edge> outEdges = new HashMap<>();
	protected Map<Object, Edge> inEdges = new HashMap<>();
	private Graph childGraph;

	protected VertexImpl(final Object id, final GraphImpl graph) {
		super(id, graph);
	}

	@Override
	public Graph addChildGraph() {
		this.childGraph = new GraphImpl(this);
		return this.getChildGraph();
	}

	@Override
	public Graph addChildGraphIfAbsent() {
		return this.childGraph != null ? this.childGraph : this.addChildGraph();
	}

	@Override
	public boolean hasChildGraph() {
		return this.childGraph != null;
	}

	@Override
	public Graph getChildGraph() {
		return this.childGraph;
	}

	@Override
	public void removeChildGraph() {
		this.childGraph = null;
	}

	@Override
	public int getDepth() {
		int depth = 0;
		GraphImpl graph = this.graph;
		while (graph.parentVertex != null) {
			graph = graph.parentVertex.graph;
			depth++;
		}
		return depth;
	}

	@Override
	public Iterable<Edge> getEdges(final Direction direction) {
		if (direction.equals(Direction.OUT)) {
			return new ArrayList<>(this.outEdges.values());
		} else if (direction.equals(Direction.IN)) {
			return new ArrayList<>(this.inEdges.values());
		} else {
			final List<Edge> edges = new ArrayList<>(this.outEdges.values());
			edges.addAll(this.inEdges.values());
			return edges;
		}
	}

	@Override
	public Iterable<Vertex> getVertices(final Direction direction) {

		if (direction.equals(Direction.BOTH)) {
			final List<Vertex> vertices = (List<Vertex>) this.getVertices(Direction.IN);
			vertices.addAll((List<Vertex>) this.getVertices(Direction.OUT));
			return vertices;
		}

		final List<Vertex> vertices = new ArrayList<>();
		for (final Edge edge : this.getEdges(direction)) {
			vertices.add(edge.getVertex(direction.opposite()));
		}
		return vertices;
	}

	@Override
	public void remove() {
		this.graph.removeVertex(this);
	}

	@Override
	public Edge addEdge(final Vertex inVertex) {
		return this.addEdge(null, inVertex);
	}

	@Override
	public Edge addEdge(final Object id, final Vertex inVertex) {
		return this.graph.addEdge(id, this, inVertex);
	}

	@Override
	public Edge addEdgeIfAbsent(final Object id, final Vertex inVertex) {
		return this.graph.addEdgeIfAbsent(id, this, inVertex);
	}

	protected void addOutEdge(final Edge edge) {
		this.outEdges.put(edge.getId(), edge);
	}

	protected void addInEdge(final Edge edge) {
		this.inEdges.put(edge.getId(), edge);
	}

	protected void removeInEdge(final Edge edge) {
		this.inEdges.remove(edge.getId());
	}

	protected void removeOutEdge(final Edge edge) {
		this.outEdges.remove(edge.getId());
	}

}
