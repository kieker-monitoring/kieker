package kieker.analysis.generic.graph.mtree.query;

/**
 * The type of the results for nearest-neighbor queries.
 */
public final class ResultItem<T> {

	/** A nearest-neighbor. */
	private final T data;

	/**
	 * The distance from the nearest-neighbor to the query data object
	 * parameter.
	 */
	private final double distance;

	public ResultItem(final T data, final double distance) {
		this.data = data;
		System.err.println("ResultItem data " + data + " " + distance);
		this.distance = distance;
	}

	public T getData() {
		return this.data;
	}

	public double getDistance() {
		return this.distance;
	}
}
