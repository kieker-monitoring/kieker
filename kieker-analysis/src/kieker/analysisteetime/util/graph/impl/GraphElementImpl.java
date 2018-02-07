package kieker.analysisteetime.util.graph.impl;

import kieker.analysisteetime.util.graph.GraphElement;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
abstract class GraphElementImpl extends ElementImpl implements GraphElement { // NOPMD NOCS (GraphElement is in this context the abstraction of Vertex and Edge))

	protected final Object id;
	protected final GraphImpl graph;

	protected GraphElementImpl(final Object id, final GraphImpl graph) {
		super();
		this.graph = graph;
		this.id = id;
	}

	@Override
	public abstract void remove();

	@Override
	public Object getId() {
		return this.id;
	}

}
