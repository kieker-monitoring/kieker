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
package kieker.analysis.behavior.mtree;

import java.util.Set;

import kieker.analysis.behavior.mtree.utils.Pair;

/**
 * A {@linkplain ISplitFunction split function} that is defined by composing
 * a {@linkplain IPromotionFunction promotion function} and a
 * {@linkplain IPartitionFunction partition function}.
 *
 * @param <T>
 *            The type of the data objects.
 * 
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class ComposedSplitFunction<T> implements ISplitFunction<T> {

	private final IPromotionFunction<T> promotionFunction;
	private final IPartitionFunction<T> partitionFunction;

	/**
	 * The constructor of a {@link ISplitFunction} composed by a
	 * {@link IPromotionFunction} and a {@link IPartitionFunction}.
	 */
	public ComposedSplitFunction(
			final IPromotionFunction<T> promotionFunction,
			final IPartitionFunction<T> partitionFunction) {
		this.promotionFunction = promotionFunction;
		this.partitionFunction = partitionFunction;
	}

	@Override
	public SplitResult<T> process(final Set<T> dataSet, final IDistanceFunction<? super T> distanceFunction) {
		final Pair<T> promoted = this.promotionFunction.process(dataSet, distanceFunction);
		final Pair<Set<T>> partitions = this.partitionFunction.process(promoted, dataSet, distanceFunction);
		return new SplitResult<>(promoted, partitions);
	}

}
