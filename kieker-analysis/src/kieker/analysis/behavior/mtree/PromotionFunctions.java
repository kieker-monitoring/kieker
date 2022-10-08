package kieker.analysis.behavior.mtree;

import java.util.List;
import java.util.Set;

import kieker.analysis.behavior.mtree.utils.Pair;
import kieker.analysis.behavior.mtree.utils.Utils;

/**
 * Some pre-defined implementations of {@linkplain PromotionFunction promotion
 * functions}.
 */
public final class PromotionFunctions {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private PromotionFunctions() {}

	/**
	 * A {@linkplain PromotionFunction promotion function} object that randomly
	 * chooses ("promotes") two data objects.
	 *
	 * @param <DATA>
	 *            The type of the data objects.
	 */
	public static class RandomPromotion<DATA> implements PromotionFunction<DATA> {
		@Override
		public Pair<DATA> process(final Set<DATA> dataSet,
				final DistanceFunction<? super DATA> distanceFunction) {
			final List<DATA> promotedList = Utils.randomSample(dataSet, 2);
			return new Pair<>(promotedList.get(0), promotedList.get(1));
		}
	}

}
