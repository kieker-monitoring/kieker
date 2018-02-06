package kieker.analysisteetime.util.graph.impl;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
class EdgeImpl extends GraphElementImpl implements Edge {

	private final Vertex outVertex;
	private final Vertex inVertex;

	protected EdgeImpl(final Object id, final Vertex outVertex, final Vertex inVertex, final GraphImpl graph) {
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
		switch (direction) {
		case IN:
			return this.inVertex;
		case OUT:
			return this.outVertex;
		default:
			throw ExceptionFactory.bothIsNotSupported();
		}
	}

}
