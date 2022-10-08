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
import kieker.analysis.behavior.mtree.DistanceFunction;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage calculates the medoid of the clusters using the trimed algorithm.
 *
 * @author Lars Jürgenseng
 * @since 2.0.0
 */
public class MedoidGenerator extends AbstractTransformation<Clustering<BehaviorModel>, BehaviorModel> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MedoidGenerator.class);

	private final DistanceFunction<BehaviorModel> dm;

	public MedoidGenerator(final DistanceFunction<BehaviorModel> dm) {
		this.dm = dm;
	}

	@Override
	protected void execute(final Clustering<BehaviorModel> clustering) throws Exception {

		for (final Set<BehaviorModel> clusterSet : clustering.getClusters()) {

			final BehaviorModel[] cluster = clusterSet.toArray(new BehaviorModel[clusterSet.size()]);
			// The trimed algorithm needs at least one element.
			if (cluster.length == 0) {
				MedoidGenerator.LOGGER.warn("Empty cluster received");
				return;
			}

			final TrimedAlgorithm<BehaviorModel> trimed = new TrimedAlgorithm<>(cluster, this.dm);

			this.outputPort.send(trimed.calculate());
		}
		MedoidGenerator.LOGGER.info("gernerated all mediods of a clustering");

	}

}
