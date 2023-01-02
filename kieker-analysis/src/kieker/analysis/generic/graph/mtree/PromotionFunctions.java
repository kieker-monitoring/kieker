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

import java.util.List;
import java.util.Set;

import kieker.analysis.generic.graph.mtree.utils.Pair;
import kieker.analysis.generic.graph.mtree.utils.Utils;

/**
 * Some pre-defined implementations of {@linkplain IPromotionFunction promotion
 * functions}.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class PromotionFunctions {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private PromotionFunctions() {}

	/**
	 * A {@linkplain IPromotionFunction promotion function} object that randomly
	 * chooses ("promotes") two data objects.
	 *
	 * @param <T>
	 *            The type of the data objects.
	 */
	public static class RandomPromotion<T> implements IPromotionFunction<T> {

		public RandomPromotion() {
			// default constructor
		}

		@Override
		public Pair<T> process(final Set<T> dataSet,
				final IDistanceFunction<? super T> distanceFunction) {
			final List<T> promotedList = Utils.randomSample(dataSet, 2);
			return new Pair<>(promotedList.get(0), promotedList.get(1));
		}
	}

}
