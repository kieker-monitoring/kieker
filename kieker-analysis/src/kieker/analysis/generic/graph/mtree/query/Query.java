package kieker.analysis.generic.graph.mtree.query;

import java.util.Iterator;

import kieker.analysis.generic.graph.mtree.MTree;

/**
 * An {@link Iterable} class which can be iterated to fetch the results of a
 * nearest-neighbors query.
 *
 * <p>
 * The neighbors are presented in non-decreasing order from the {@code
 * queryData} argument to the {@link MTree#getNearest(Object, double, int)
 * getNearest*()}
 * call.
 *
 * <p>
 * The query on the M-Tree is executed during the iteration, as the
 * results are fetched. It means that, by the time when the <i>n</i>-th
 * result is fetched, the next result may still not be known, and the
 * resources allocated were only the necessary to identify the <i>n</i>
 * first results.
 */
public final class Query<T> implements Iterable<ResultItem<T>> {

	private final T data;
	private final double range;
	private final int limit;
	private final MTree<T> mtree;

	public Query(final MTree<T> mtree, final T data, final double range, final int limit) {
		this.mtree = mtree;
		this.data = data;
		this.range = range;
		this.limit = limit;
	}

	@Override
	public Iterator<ResultItem<T>> iterator() {
		return new ResultsIterator<T>(this.mtree, this);
	}

	public T getData() {
		return this.data;
	}

	public int getLimit() {
		return this.limit;
	}

	public double getRange() {
		return this.range;
	}
}
