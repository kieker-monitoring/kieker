package kieker.analysis.generic.graph.mtree.exceptions;

import kieker.analysis.generic.graph.mtree.nodes.AbstractNode;

public final class RootNodeReplacementException extends Exception {
	// A subclass of Throwable cannot be generic. :-(
	// So, we have newRoot declared as Object instead of Node.
	private final AbstractNode<?> newRoot;

	public RootNodeReplacementException(final AbstractNode<?> newRoot) {
		this.newRoot = newRoot;
	}

	public AbstractNode<?> getNewRoot() {
		return this.newRoot;
	}
}
