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
package kieker.analysis.generic.graph.mtree.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Some utilities.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class MTreeUtils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private MTreeUtils() {}

	/**
	 * Identifies the minimum and maximum elements from an iterable, according
	 * to the natural ordering of the elements.
	 *
	 * @param items
	 *            An {@link Iterable} object with the elements
	 * @param <T>
	 *            The type of the elements.
	 * @return A pair with the minimum and maximum elements.
	 */
	public static <T extends Comparable<T>> Pair<T> minMax(final Iterable<T> items) {
		final Iterator<T> iterator = items.iterator();
		if (!iterator.hasNext()) {
			return null;
		}

		T min = iterator.next();
		T max = min;

		while (iterator.hasNext()) {
			final T item = iterator.next();
			if (item.compareTo(min) < 0) {
				min = item;
			}
			if (item.compareTo(max) > 0) {
				max = item;
			}
		}

		return new Pair<>(min, max);
	}

	/**
	 * Randomly chooses elements from the collection.
	 *
	 * @param collection
	 *            The collection.
	 * @param numberOfElements
	 *            The number of elements to choose.
	 * @param <T>
	 *            The type of the elements.
	 * @return A list with the chosen elements.
	 */
	public static <T> List<T> randomSample(final Collection<T> collection, final int numberOfElements) {
		final List<T> list = new ArrayList<>(collection);
		final List<T> sample = new ArrayList<>(numberOfElements);
		final Random random = new Random();
		int count = numberOfElements;
		while (count > 0 && !list.isEmpty()) {
			final int index = random.nextInt(list.size());
			sample.add(list.get(index));
			final int indexLast = list.size() - 1;
			final T last = list.remove(indexLast);
			if (index < indexLast) {
				list.set(index, last);
			}
			count--;
		}
		return sample;
	}

}
