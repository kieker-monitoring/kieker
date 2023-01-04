/***************************************************************************
 * Copyright (c) 2012-2013 Eduardo R. D'Avila (https://github.com/erdavila)
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
package kieker.analysis.generic.graph.mtree;

import java.util.Set;

import kieker.analysis.generic.graph.mtree.utils.Pair;

/**
 * Defines an object to be used to split a node in an M-Tree. A node must be
 * split when it has reached its maximum capacity and a new child node would be
 * added to it.
 *
 * <p>
 * The splitting consists in choosing a pair of "promoted" data objects from
 * the children and then partition the set of children in two partitions
 * corresponding to the two promoted data objects.
 *
 * @param <T>
 *            The type of the data objects.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public interface ISplitFunction<T> {

	/**
	 * Processes the splitting of a node.
	 *
	 * @param dataSet
	 *            A set of data that are keys to the children of the node
	 *            to be split.
	 * @param distanceFunction
	 *            A {@linkplain IDistanceFunction distance function}
	 *            that can be used to help splitting the node.
	 * @return A {@link SplitResult} object with a pair of promoted data objects
	 *         and a pair of corresponding partitions of the data objects.
	 */
	SplitResult<T> process(Set<T> dataSet, IDistanceFunction<? super T> distanceFunction);

	/**
	 * An object used as the result for the
	 * {@link ISplitFunction#process(Set, IDistanceFunction)} method.
	 *
	 * @param <R>
	 *            The type of the data objects.
	 */
	public static class SplitResult<R> {

		/**
		 * A pair of promoted data objects.
		 */
		private final Pair<R> promoted;

		/**
		 * A pair of partitions corresponding to the {@code promoted} data
		 * objects.
		 */
		private final Pair<Set<R>> partitions;

		/**
		 * The constructor for a {@link SplitResult} object.
		 *
		 * @param promoted
		 *            promoted
		 * @param partitions
		 *            partitions
		 */
		public SplitResult(final Pair<R> promoted, final Pair<Set<R>> partitions) {
			this.promoted = promoted;
			this.partitions = partitions;
		}

		public Pair<R> getPromoted() {
			return this.promoted;
		}

		public Pair<Set<R>> getPartitions() {
			return this.partitions;
		}

	}

}
