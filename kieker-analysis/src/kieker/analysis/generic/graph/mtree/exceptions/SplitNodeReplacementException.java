package kieker.analysis.generic.graph.mtree.exceptions;

public final class SplitNodeReplacementException extends Exception {
	// A subclass of Throwable cannot be generic. :-(
	// So, we have newNodes declared as Object[] instead of Node[].
	private final Object[] newNodes;

	public SplitNodeReplacementException(final Object... newNodes) {
		this.newNodes = newNodes;
	}

	public Object[] getNewNodes() {
		return this.newNodes;
	}
}
