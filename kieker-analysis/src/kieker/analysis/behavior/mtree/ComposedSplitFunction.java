package kieker.analysis.behavior.mtree;

import java.util.Set;

import kieker.analysis.behavior.mtree.utils.Pair;

/**
 * A {@linkplain SplitFunction split function} that is defined by composing
 * a {@linkplain PromotionFunction promotion function} and a
 * {@linkplain PartitionFunction partition function}.
 *
 * @param <DATA>
 *            The type of the data objects.
 */
public class ComposedSplitFunction<DATA> implements SplitFunction<DATA> {

	private final PromotionFunction<DATA> promotionFunction;
	private final PartitionFunction<DATA> partitionFunction;

	/**
	 * The constructor of a {@link SplitFunction} composed by a
	 * {@link PromotionFunction} and a {@link PartitionFunction}.
	 */
	public ComposedSplitFunction(
			final PromotionFunction<DATA> promotionFunction,
			final PartitionFunction<DATA> partitionFunction) {
		this.promotionFunction = promotionFunction;
		this.partitionFunction = partitionFunction;
	}

	@Override
	public SplitResult<DATA> process(final Set<DATA> dataSet, final DistanceFunction<? super DATA> distanceFunction) {
		final Pair<DATA> promoted = this.promotionFunction.process(dataSet, distanceFunction);
		final Pair<Set<DATA>> partitions = this.partitionFunction.process(promoted, dataSet, distanceFunction);
		return new SplitResult<>(promoted, partitions);
	}

}
