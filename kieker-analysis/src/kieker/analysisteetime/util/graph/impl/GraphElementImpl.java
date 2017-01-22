package kieker.analysisteetime.util.graph.impl;

import kieker.analysisteetime.util.graph.GraphElement;

abstract class GraphElementImpl extends ElementImpl implements GraphElement {

	protected final String id;
	protected final GraphImpl graph;

	protected GraphElementImpl(final String id, final GraphImpl graph) {
		super();
		this.graph = graph;
		this.id = id;
	}

	@Override
	abstract public void remove();

	@Override
	public Object getId() {
		return this.id;
	}

}
