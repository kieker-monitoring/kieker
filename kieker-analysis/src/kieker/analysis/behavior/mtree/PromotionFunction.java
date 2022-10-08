package kieker.analysis.behavior.mtree;

import java.util.Set;

import kieker.analysis.behavior.mtree.utils.Pair;

/**
 * An object that chooses a pair from a set of data objects.
 *
 * @param <DATA>
 *            The type of the data objects.
 */
public interface PromotionFunction<DATA> {

	/**
	 * Chooses (promotes) a pair of objects according to some criteria that is
	 * suitable for the application using the M-Tree.
	 *
	 * @param dataSet
	 *            The set of objects to choose a pair from.
	 * @param distanceFunction
	 *            A function that can be used for choosing the
	 *            promoted objects.
	 * @return A pair of chosen objects.
	 */
	Pair<DATA> process(Set<DATA> dataSet, DistanceFunction<? super DATA> distanceFunction);

}
