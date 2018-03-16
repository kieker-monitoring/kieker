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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Iterables;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.IEdge;
import kieker.analysisteetime.util.graph.IGraph;
import kieker.analysisteetime.util.graph.IVertex;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
class VertexImpl extends GraphElementImpl implements IVertex {

	protected Map<Object, IEdge> outEdges = new HashMap<>(); // NOPMD (no concurrent access intended)
	protected Map<Object, IEdge> inEdges = new HashMap<>(); // NOPMD (no concurrent access intended)
	private IGraph childGraph;

	protected VertexImpl(final Object id, final GraphImpl graph) {
		super(id, graph);
	}

	@Override
	public IGraph addChildGraph() {
		this.childGraph = new GraphImpl(this);
		return this.getChildGraph();
	}

	@Override
	public IGraph addChildGraphIfAbsent() {
		return this.childGraph != null ? this.childGraph : this.addChildGraph(); // NOCS (declarative)
	}

	@Override
	public boolean hasChildGraph() {
		return this.childGraph != null;
	}

	@Override
	public IGraph getChildGraph() {
		return this.childGraph;
	}

	@Override
	public void removeChildGraph() {
		// BETTER could be refactored to a dummy (null) object
		this.childGraph = null; // NOPMD (setting to null intended by data structure)
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
	public Iterable<IEdge> getEdges(final Direction direction) {
		if (direction.equals(Direction.OUT)) {
			return Iterables.unmodifiableIterable(this.outEdges.values());
		} else if (direction.equals(Direction.IN)) {
			return Iterables.unmodifiableIterable(this.inEdges.values());
		} else {
			return Iterables.unmodifiableIterable(Iterables.concat(this.outEdges.values(), this.inEdges.values()));
		}
	}

	@Override
	public Iterable<IVertex> getVertices(final Direction direction) {

		if (direction.equals(Direction.BOTH)) {
			final List<IVertex> vertices = (List<IVertex>) this.getVertices(Direction.IN);
			vertices.addAll((List<IVertex>) this.getVertices(Direction.OUT));
			return vertices;
		}

		final List<IVertex> vertices = new ArrayList<>();
		for (final IEdge edge : this.getEdges(direction)) {
			vertices.add(edge.getVertex(direction.opposite()));
		}
		return vertices;
	}

	@Override
	public void remove() {
		this.graph.removeVertex(this);
	}

	@Override
	public IEdge addEdge(final IVertex inVertex) {
		return this.addEdge(null, inVertex);
	}

	@Override
	public IEdge addEdge(final Object id, final IVertex inVertex) {
		return this.graph.addEdge(id, this, inVertex);
	}

	@Override
	public IEdge addEdgeIfAbsent(final Object id, final IVertex inVertex) {
		return this.graph.addEdgeIfAbsent(id, this, inVertex);
	}

	protected void addOutEdge(final IEdge edge) {
		this.outEdges.put(edge.getId(), edge);
	}

	protected void addInEdge(final IEdge edge) {
		this.inEdges.put(edge.getId(), edge);
	}

	protected void removeInEdge(final IEdge edge) {
		this.inEdges.remove(edge.getId());
	}

	protected void removeOutEdge(final IEdge edge) {
		this.outEdges.remove(edge.getId());
	}

}
