package kieker.analysisteetime.util.graph.impl;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Vertex;

class EdgeImpl extends GraphElementImpl implements Edge {

	private final Vertex outVertex;
	private final Vertex inVertex;

	protected EdgeImpl(final String id, final Vertex outVertex, final Vertex inVertex, final GraphImpl graph) {
		super(id, graph);
		this.outVertex = outVertex;
		this.inVertex = inVertex;
	}

	@Override
	public void remove() {
		this.graph.removeEdge(this);
	}

	@Override
	public Vertex getVertex(final Direction direction) throws IllegalArgumentException {
		if (direction.equals(Direction.IN)) {
			return this.inVertex;
		} else if (direction.equals(Direction.OUT)) {
			return this.outVertex;
		} else {
			throw ExceptionFactory.bothIsNotSupported();
		}
	}

}
