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

package kieker.analysisteetime.util.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.Iterables;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.IEdge;
import kieker.analysisteetime.util.graph.IGraph;
import kieker.analysisteetime.util.graph.IVertex;

/**
 * Default implementation of a {@link IGraph}. Instances can be created using the {@link IGraph} {@code create()} method.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphImpl extends ElementImpl implements IGraph {

	protected String name = "G";

	protected Map<Object, IVertex> vertices = new HashMap<>(); // NOPMD (no concurrent access intended)
	protected Map<Object, IEdge> edges = new HashMap<>(); // NOPMD (no concurrent access intended)

	protected long currentDefaultId; // = 0l

	protected VertexImpl parentVertex; // intended to be "null" per default

	protected GraphImpl() {
		super();
	}

	protected GraphImpl(final VertexImpl parentVertex) {
		this.parentVertex = parentVertex;
	}

	@Override
	public IVertex addVertex(final Object id) {
		Object freeId = id;
		if (freeId == null) {
			do {
				freeId = this.getDefaultId();
			} while (this.vertices.containsKey(freeId));
		} else {
			if (this.vertices.containsKey(freeId)) {
				throw ExceptionFactory.vertexWithIdAlreadyExists(freeId);
			}
		}

		final IVertex vertex = new VertexImpl(freeId, this);
		this.vertices.put(vertex.getId(), vertex);
		return vertex;
	}

	@Override
	public IVertex addVertexIfAbsent(final Object id) {
		IVertex vertex = this.getVertex(id);
		if (vertex == null) {
			vertex = this.addVertex(id);
		}
		return vertex;
	}

	@Override
	public IVertex getVertex(final Object id) {
		if (id == null) {
			throw ExceptionFactory.vertexIdCanNotBeNull();
		}
		return this.vertices.get(id);
	}

	@Override
	public Iterable<IVertex> getVertices() {
		return Iterables.unmodifiableIterable(this.vertices.values());
	}

	@Override
	public void removeVertex(final IVertex vertex) {
		if (!this.vertices.containsKey(vertex.getId())) {
			throw ExceptionFactory.vertexWithIdDoesNotExist(vertex.getId());
		}

		for (final IEdge edge : vertex.getEdges(Direction.IN)) {
			this.removeEdge(edge);
		}
		for (final IEdge edge : vertex.getEdges(Direction.OUT)) {
			this.removeEdge(edge);
		}

		this.vertices.remove(vertex.getId());
	}

	@Override
	public IEdge addEdge(final Object id, final IVertex outVertex, final IVertex inVertex) {
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
	public IEdge addEdgeIfAbsent(final Object id, final IVertex outVertex, final IVertex inVertex) {
		IEdge edge = this.getEdge(id);
		if (edge == null) {
			edge = this.addEdge(id, outVertex, inVertex);
		}
		return edge;
	}

	protected IEdge addEdgeChecked(final Object id, final IVertex outVertex, final IVertex inVertex) {
		Object freeId = id;
		if (freeId == null) {
			do {
				freeId = this.getDefaultId();
			} while (this.edges.containsKey(freeId));
		} else {
			if (this.edges.containsKey(freeId)) {
				throw ExceptionFactory.edgeWithIdAlreadyExists(freeId);
			}
		}

		final IEdge edge = new EdgeImpl(freeId, outVertex, inVertex, this);
		this.edges.put(edge.getId(), edge);
		((VertexImpl) outVertex).addOutEdge(edge);
		((VertexImpl) inVertex).addInEdge(edge);

		return edge;
	}

	@Override
	public IEdge getEdge(final Object id) {
		if (id == null) {
			throw ExceptionFactory.edgeIdCanNotBeNull();
		}
		return this.edges.get(id);
	}

	@Override
	public Iterable<IEdge> getEdges() {
		return Iterables.unmodifiableIterable(this.edges.values());
	}

	@Override
	public void removeEdge(final IEdge edge) {
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
