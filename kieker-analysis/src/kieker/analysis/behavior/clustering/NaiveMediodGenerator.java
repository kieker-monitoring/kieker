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
package kieker.analysis.behavior.clustering;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.mtree.IDistanceFunction;

import teetime.stage.basic.AbstractTransformation;

/**
 * The naive medoid algorithm, where all pairwise distances are calculated
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class NaiveMediodGenerator extends AbstractTransformation<Clustering<BehaviorModel>, BehaviorModel> {
	private static final Logger LOGGER = LoggerFactory.getLogger(NaiveMediodGenerator.class);

	private final IDistanceFunction<BehaviorModel> dm;

	public NaiveMediodGenerator(final IDistanceFunction<BehaviorModel> dm) {
		this.dm = dm;
	}

	@Override
	protected void execute(final Clustering<BehaviorModel> clustering) throws Exception {

		for (final Set<BehaviorModel> clusterSet : clustering.getClusters()) {

			final BehaviorModel[] cluster = clusterSet.toArray(new BehaviorModel[clusterSet.size()]);
			if (cluster.length == 0) {
				NaiveMediodGenerator.LOGGER.warn("Empty cluster received");
				return;
			}

			BehaviorModel medoid = cluster[0];
			double minDistanceSum = Double.MAX_VALUE;

			for (int i = 0; i < cluster.length; i++) {
				double distanceSum = 0;

				// calculate the distance to the other objects
				for (int j = 0; j < cluster.length; j++) {
					if (i != j) {
						distanceSum += this.dm.calculate(cluster[i], cluster[j]);
					}

				}
				// remember this object, if best medoid so far
				if (distanceSum < minDistanceSum) {
					minDistanceSum = distanceSum;
					medoid = cluster[i];
				}

			}

			this.outputPort.send(medoid);
		}

		NaiveMediodGenerator.LOGGER.info("mediod generated");
	}

}
