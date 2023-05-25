/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.generic.graph.clustering;

import java.util.Set;

import kieker.analysis.generic.graph.mtree.IDistanceFunction;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage calculates the medoid of the clusters using the trimmed algorithm.
 * A medoid is a representative object of a cluster where the medoid has the least
 * difference to all other objects in the cluster.
 *
 * @param <T>
 *            data type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class MedoidGenerator<T> extends AbstractTransformation<Clustering<T>, T> {

	private final IDistanceFunction<T> distanceFunction;

	public MedoidGenerator(final IDistanceFunction<T> distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	@Override
	protected void execute(final Clustering<T> clustering) throws Exception {

		for (final Set<T> clusterSet : clustering.getClusters()) {

			@SuppressWarnings("unchecked")
			final T[] cluster = (T[]) clusterSet.toArray(); // NOPMD
			// The trimmed algorithm needs at least one element.
			if (cluster.length == 0) {
				this.logger.warn("Empty cluster received");
				return;
			}

			final TrimmedAlgorithm<T> trimed = new TrimmedAlgorithm<>(cluster, this.distanceFunction);

			this.outputPort.send(trimed.calculate());
		}
		this.logger.info("gernerated all mediods of a clustering");
	}

}
