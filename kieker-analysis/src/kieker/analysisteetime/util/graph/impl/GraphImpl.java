package kieker.analysisteetime.util.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.Iterables;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

public class GraphImpl extends ElementImpl implements Graph {

	protected String name = "G";

	protected Map<Object, Vertex> vertices = new HashMap<>();
	protected Map<Object, Edge> edges = new HashMap<>();

	protected long currentDefaultId = 0l;

	protected VertexImpl parentVertex = null;

	public GraphImpl() {}

	protected GraphImpl(final VertexImpl parentVertex) {
		this.parentVertex = parentVertex;
	}

	@Override
	public Vertex addVertex(Object id) {
		if (id == null) {
			do {
				id = this.getDefaultId();
			} while (this.vertices.containsKey(id));
		} else {
			if (this.vertices.containsKey(id)) {
				throw ExceptionFactory.vertexWithIdAlreadyExists(id);
			}
		}

		final Vertex vertex = new VertexImpl(id, this);
		this.vertices.put(vertex.getId(), vertex);
		return vertex;
	}

	@Override
	public Vertex addVertexIfAbsent(final Object id) {
		Vertex vertex = this.getVertex(id);
		if (vertex == null) {
			vertex = this.addVertex(id);
		}
		return vertex;
	}

	@Override
	public Vertex getVertex(final Object id) {
		if (id == null) {
			throw ExceptionFactory.vertexIdCanNotBeNull();
		}
		return this.vertices.get(id);
	}

	@Override
	public Iterable<Vertex> getVertices() {
		return Iterables.unmodifiableIterable(this.vertices.values());
	}

	@Override
	public void removeVertex(final Vertex vertex) {
		if (!this.vertices.containsKey(vertex.getId())) {
			throw ExceptionFactory.vertexWithIdDoesNotExist(vertex.getId());
		}

		for (final Edge edge : vertex.getEdges(Direction.IN)) {
			this.removeEdge(edge);
		}
		for (final Edge edge : vertex.getEdges(Direction.OUT)) {
			this.removeEdge(edge);
		}

		this.vertices.remove(vertex.getId());
	}

	@Override
	public Edge addEdge(final Object id, final Vertex outVertex, final Vertex inVertex) {

		// BETTER Throw Exception if Vertices are null

		final Stack<VertexImpl> outVertexParents = new Stack<>();
		for (VertexImpl parent = (VertexImpl) outVertex; parent != null; parent = parent.graph.parentVertex) {
			outVertexParents.push(parent);
		}
		final Stack<VertexImpl> inVertexParents = new Stack<>();
		for (VertexImpl parent = (VertexImpl) inVertex; parent != null; parent = parent.graph.parentVertex) {
			inVertexParents.push(parent);
		}

		if (outVertexParents.peek().graph != inVertexParents.peek().graph) {
			throw ExceptionFactory.verticesAreNotInSameGraph(outVertex.getId(), inVertex.getId());
		}

		GraphImpl firstEqualParent = null;
		while (!outVertexParents.isEmpty() && !inVertexParents.isEmpty() && (outVertexParents.peek().graph == inVertexParents.pop().graph)) {
			firstEqualParent = outVertexParents.pop().graph;
		}
		return firstEqualParent.addEdgeChecked(id, outVertex, inVertex);
	}

	@Override
	public Edge addEdgeIfAbsent(final Object id, final Vertex outVertex, final Vertex inVertex) {
		Edge edge = this.getEdge(id);
		if (edge == null) {
			edge = this.addEdge(id, outVertex, inVertex);
		}
		return edge;
	}

	protected Edge addEdgeChecked(Object id, final Vertex outVertex, final Vertex inVertex) {
		if (id == null) {
			do {
				id = this.getDefaultId();
			} while (this.edges.containsKey(id));
		} else {
			if (this.edges.containsKey(id)) {
				throw ExceptionFactory.edgeWithIdAlreadyExists(id);
			}
		}

		final Edge edge = new EdgeImpl(id, outVertex, inVertex, this);
		this.edges.put(edge.getId(), edge);
		((VertexImpl) outVertex).addOutEdge(edge);
		((VertexImpl) inVertex).addInEdge(edge);

		return edge;
	}

	@Override
	public Edge getEdge(final Object id) {
		if (id == null) {
			throw ExceptionFactory.edgeIdCanNotBeNull();
		}
		return this.edges.get(id);
	}

	@Override
	public Iterable<Edge> getEdges() {
		return Iterables.unmodifiableIterable(this.edges.values());
	}

	@Override
	public void removeEdge(final Edge edge) {
		if (!this.edges.containsKey(edge.getId())) {
			throw ExceptionFactory.edgeWithIdDoesNotExist(edge.getId());
		}

		((VertexImpl) edge.getVertex(Direction.IN)).removeInEdge(edge);
		((VertexImpl) edge.getVertex(Direction.OUT)).removeOutEdge(edge);

		this.edges.remove(edge.getId());
	}

	private Object getDefaultId() {
		long id;
		do {
			id = this.currentDefaultId;
			this.currentDefaultId++;
		} while (this.vertices.containsKey(id) || this.edges.containsKey(id));
		return id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		if (name != null) {
			this.name = name;
		}
	}

}
