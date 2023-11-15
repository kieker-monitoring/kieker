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
package kieker.analysis.generic.clustering.mtree;

import java.util.Set;

import kieker.analysis.generic.clustering.mtree.utils.Pair;

/**
 * An object with partitions a set of data into two sub-sets.
 *
 * @param <T>
 *            The type of the data on the sets.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public interface IPartitionFunction<T> {

	/**
	 * Executes the partitioning.
	 *
	 * @param promoted
	 *            The pair of data objects that will guide the partition
	 *            process.
	 * @param dataSet
	 *            The original set of data objects to be partitioned.
	 * @param distanceFunction
	 *            A {@linkplain IDistanceFunction distance function}
	 *            to be used on the partitioning.
	 * @return A pair of partition sub-sets. Each sub-set must correspond to one
	 *         of the {@code promoted} data objects.
	 */
	Pair<Set<T>> process(Pair<T> promoted, Set<T> dataSet, IDistanceFunction<? super T> distanceFunction);

}
