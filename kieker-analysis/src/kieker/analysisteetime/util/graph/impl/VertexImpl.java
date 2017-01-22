package kieker.analysisteetime.util.graph.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

class VertexImpl extends GraphElementImpl implements Vertex {

	protected Map<String, Edge> outEdges = new HashMap<String, Edge>();
	protected Map<String, Edge> inEdges = new HashMap<String, Edge>();
	private Graph childGraph;

	protected VertexImpl(final String id, final GraphImpl graph) {
		super(id, graph);
	}

	@Override
	public Graph addChildGraph() {
		this.childGraph = new GraphImpl(this);
		return getChildGraph();
	}

	@Override
	public boolean hasChildGraph() {
		return this.childGraph != null;
	}

	@Override
	public Graph getChildGraph() {
		return childGraph;
	}

	@Override
	public void removeChildGraph() {
		childGraph = null;
	}

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
			ArrayList<Edge> edges = new ArrayList<>(this.outEdges.values());
			edges.addAll(this.inEdges.values());
			return edges;
		}
	}

	@Override
	public Iterable<Vertex> getVertices(final Direction direction) {

		if (direction.equals(Direction.BOTH)) {
			ArrayList<Vertex> vertices = (ArrayList<Vertex>) getVertices(Direction.IN);
			vertices.addAll((ArrayList<Vertex>) getVertices(Direction.OUT));
			return vertices;
		}

		ArrayList<Vertex> vertices = new ArrayList<>();
		for (Edge edge : getEdges(direction)) {
			vertices.add(edge.getVertex(direction.opposite()));
		}
		return vertices;
	}

	@Override
	public void remove() {
		graph.removeVertex(this);
	}

	@Override
	public Edge addEdge(final Vertex inVertex) {
		return addEdge(null, inVertex);
	}

	@Override
	public Edge addEdge(final Object id, final Vertex inVertex) {
		return this.graph.addEdge(id, this, inVertex);
	}

	protected void addOutEdge(final Edge edge) {
		this.outEdges.put(edge.getId().toString(), edge);
	}

	protected void addInEdge(final Edge edge) {
		this.inEdges.put(edge.getId().toString(), edge);
	}

	protected void removeInEdge(final Edge edge) {
		this.inEdges.remove(edge.getId().toString());
	}

	protected void removeOutEdge(final Edge edge) {
		this.outEdges.remove(edge.getId().toString());
	}

}
