package kieker.analysis.behavior.mtree;

import java.util.Set;

import kieker.analysis.behavior.mtree.utils.Pair;

/**
 * An object with partitions a set of data into two sub-sets.
 *
 * @param <DATA>
 *            The type of the data on the sets.
 */
public interface PartitionFunction<DATA> {

	/**
	 * Executes the partitioning.
	 *
	 * @param promoted
	 *            The pair of data objects that will guide the partition
	 *            process.
	 * @param dataSet
	 *            The original set of data objects to be partitioned.
	 * @param distanceFunction
	 *            A {@linkplain DistanceFunction distance function}
	 *            to be used on the partitioning.
	 * @return A pair of partition sub-sets. Each sub-set must correspond to one
	 *         of the {@code promoted} data objects.
	 */
	Pair<Set<DATA>> process(Pair<DATA> promoted, Set<DATA> dataSet, DistanceFunction<? super DATA> distanceFunction);

}
