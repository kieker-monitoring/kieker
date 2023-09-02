/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.clustering.mtree.query;

import java.util.Iterator;

import kieker.analysis.generic.clustering.mtree.MTree;

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
 * 
 * @param <T>
 *            data type of the values in the query result
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
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
		return new ResultsIterator<T>(this);
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

	public MTree<T> getMTree() {
		return this.mtree;
	}
}
