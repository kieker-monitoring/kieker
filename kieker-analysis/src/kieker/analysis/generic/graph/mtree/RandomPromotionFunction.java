/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.generic.graph.mtree.utils.MTreeUtils;
import kieker.analysis.generic.graph.mtree.utils.Pair;

/**
 * A {@linkplain IPromotionFunction promotion function} object that randomly
 * chooses ("promotes") two data objects.
 *
 * @param <T>
 *            The type of the data objects.
 * 
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class RandomPromotionFunction<T> implements IPromotionFunction<T> {

	public RandomPromotionFunction() {
		// default constructor
	}

	@Override
	public Pair<T> process(final Set<T> dataSet,
			final IDistanceFunction<? super T> distanceFunction) {
		final List<T> promotedList = MTreeUtils.randomSample(dataSet, 2);
		return new Pair<>(promotedList.get(0), promotedList.get(1));
	}
}
