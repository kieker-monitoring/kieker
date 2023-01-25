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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kieker.analysis.generic.graph.mtree.utils.Pair;

/**
 * A {@linkplain IPartitionFunction partition function} that tries to
 * distribute the data objects equally between the promoted data objects,
 * associating to each promoted data objects the nearest data objects.
 *
 * @param <T>
 *            The type of the data objects.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class BalancedPartitionFunction<T> implements IPartitionFunction<T> {

	public BalancedPartitionFunction() {
		// default constructor
	}

	/**
	 * Processes the balanced partition.
	 *
	 * <p>
	 * The algorithm is roughly equivalent to this:
	 *
	 * <pre>
	 *     While dataSet is not Empty:
	 *         X := The object in dataSet which is nearest to promoted.<b>first</b>
	 *         Remove X from dataSet
	 *         Add X to result.<b>first</b>
	 *
	 *         Y := The object in dataSet which is nearest to promoted.<b>second</b>
	 *         Remove Y from dataSet
	 *         Add Y to result.<b>second</b>
	 *
	 *     Return result
	 * </pre>
	 *
	 * @see mtree.IPartitionFunction#process(mtree.utils.Pair, java.util.Set, mtree.IDistanceFunction)
	 */
	@Override
	public Pair<Set<T>> process(
			final Pair<T> promoted,
			final Set<T> dataSet,
			final IDistanceFunction<? super T> distanceFunction) {
		final List<T> queue1 = new ArrayList<>(dataSet);
		// Sort by distance to the first promoted data
		Collections.sort(queue1, new Comparator<T>() {
			@Override
			public int compare(final T data1, final T data2) {
				final double distance1 = distanceFunction.calculate(data1, promoted.getFirst());
				final double distance2 = distanceFunction.calculate(data2, promoted.getFirst());
				return Double.compare(distance1, distance2);
			}
		});

		final List<T> queue2 = new ArrayList<>(dataSet);
		// Sort by distance to the second promoted data
		Collections.sort(queue2, new Comparator<T>() {
			@Override
			public int compare(final T data1, final T data2) {
				final double distance1 = distanceFunction.calculate(data1, promoted.getSecond());
				final double distance2 = distanceFunction.calculate(data2, promoted.getSecond());
				return Double.compare(distance1, distance2);
			}
		});

		final Pair<Set<T>> partitions = new Pair<>(new HashSet<T>(), new HashSet<T>());

		int index1 = 0;
		int index2 = 0;

		while (index1 < queue1.size() || index2 != queue2.size()) {
			while (index1 < queue1.size()) {
				final T data = queue1.get(index1++);
				if (!partitions.getSecond().contains(data)) {
					partitions.getFirst().add(data);
					break;
				}
			}

			while (index2 < queue2.size()) {
				final T data = queue2.get(index2++);
				if (!partitions.getFirst().contains(data)) {
					partitions.getSecond().add(data);
					break;
				}
			}
		}

		return partitions;
	}
}
