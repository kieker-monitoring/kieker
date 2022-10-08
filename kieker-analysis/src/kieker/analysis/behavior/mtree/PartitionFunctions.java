package kieker.analysis.behavior.mtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kieker.analysis.behavior.mtree.utils.Pair;

/**
 * Some pre-defined implementations of {@linkplain PartitionFunction partition
 * functions}.
 */
public final class PartitionFunctions {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private PartitionFunctions() {}

	/**
	 * A {@linkplain PartitionFunction partition function} that tries to
	 * distribute the data objects equally between the promoted data objects,
	 * associating to each promoted data objects the nearest data objects.
	 *
	 * @param <DATA>
	 *            The type of the data objects.
	 */
	public static class BalancedPartition<DATA> implements PartitionFunction<DATA> {

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
		 * @see mtree.PartitionFunction#process(mtree.utils.Pair, java.util.Set, mtree.DistanceFunction)
		 */
		@Override
		public Pair<Set<DATA>> process(
				final Pair<DATA> promoted,
				final Set<DATA> dataSet,
				final DistanceFunction<? super DATA> distanceFunction) {
			final List<DATA> queue1 = new ArrayList<>(dataSet);
			// Sort by distance to the first promoted data
			Collections.sort(queue1, new Comparator<DATA>() {
				@Override
				public int compare(final DATA data1, final DATA data2) {
					final double distance1 = distanceFunction.calculate(data1, promoted.first);
					final double distance2 = distanceFunction.calculate(data2, promoted.first);
					return Double.compare(distance1, distance2);
				}
			});

			final List<DATA> queue2 = new ArrayList<>(dataSet);
			// Sort by distance to the second promoted data
			Collections.sort(queue2, new Comparator<DATA>() {
				@Override
				public int compare(final DATA data1, final DATA data2) {
					final double distance1 = distanceFunction.calculate(data1, promoted.second);
					final double distance2 = distanceFunction.calculate(data2, promoted.second);
					return Double.compare(distance1, distance2);
				}
			});

			final Pair<Set<DATA>> partitions = new Pair<>(new HashSet<DATA>(), new HashSet<DATA>());

			int index1 = 0;
			int index2 = 0;

			while (index1 < queue1.size() || index2 != queue2.size()) {
				while (index1 < queue1.size()) {
					final DATA data = queue1.get(index1++);
					if (!partitions.second.contains(data)) {
						partitions.first.add(data);
						break;
					}
				}

				while (index2 < queue2.size()) {
					final DATA data = queue2.get(index2++);
					if (!partitions.first.contains(data)) {
						partitions.second.add(data);
						break;
					}
				}
			}

			return partitions;
		}
	}
}
