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

import java.util.Set;

import kieker.analysis.generic.graph.mtree.utils.Pair;

/**
 * An object that chooses a pair from a set of data objects.
 *
 * @param <T>
 *            The type of the data objects.
 * 
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public interface IPromotionFunction<T> {

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
	Pair<T> process(Set<T> dataSet, IDistanceFunction<? super T> distanceFunction);

}
