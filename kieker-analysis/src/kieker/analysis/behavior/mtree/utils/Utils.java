package kieker.analysis.behavior.mtree.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Some utilities.
 */
public final class Utils {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private Utils() {}

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
	 * @param n
	 *            The number of elements to choose.
	 * @param <T>
	 *            The type of the elements.
	 * @return A list with the chosen elements.
	 */
	public static <T> List<T> randomSample(final Collection<T> collection, int n) {
		final List<T> list = new ArrayList<>(collection);
		final List<T> sample = new ArrayList<>(n);
		final Random random = new Random();
		while (n > 0 && !list.isEmpty()) {
			final int index = random.nextInt(list.size());
			sample.add(list.get(index));
			final int indexLast = list.size() - 1;
			final T last = list.remove(indexLast);
			if (index < indexLast) {
				list.set(index, last);
			}
			n--;
		}
		return sample;
	}

}
