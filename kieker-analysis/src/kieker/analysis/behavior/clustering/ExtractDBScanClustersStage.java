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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.behavior.model.BehaviorModel;

import teetime.stage.basic.AbstractTransformation;

/**
 * The algorithm extracts clusters, which are equivalent to DBScan clusters from the OPTICS plot.
 * The algorithm was proposed in the optics paper.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ExtractDBScanClustersStage extends AbstractTransformation<List<OpticsData>, Clustering<BehaviorModel>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractDBScanClustersStage.class);

	private final double clusteringDistance;

	public ExtractDBScanClustersStage(final double clusteringDistance) {
		this.clusteringDistance = clusteringDistance;
	}

	@Override
	protected void execute(final List<OpticsData> opticsResults) throws Exception {

		ExtractDBScanClustersStage.LOGGER.info("received optics result");
		for (final OpticsData model : opticsResults) {
			ExtractDBScanClustersStage.LOGGER.debug(Double.toString(model.getReachabilityDistance()) + " and core: "
					+ Double.toString(model.getCoreDistance()));
		}
		final Clustering<BehaviorModel> clustering = new Clustering<>();

		Set<BehaviorModel> currentCluster = clustering.getNoise();

		for (final OpticsData model : opticsResults) {
			if (model.getReachabilityDistance() == OpticsData.UNDEFINED
					|| model.getReachabilityDistance() > this.clusteringDistance) {
				if (model.getCoreDistance() <= this.clusteringDistance
						&& model.getCoreDistance() != OpticsData.UNDEFINED) {
					final Set<BehaviorModel> newCluster = new HashSet<>();
					clustering.addCluster(newCluster);
					newCluster.add(model.getData());
					currentCluster = newCluster;
				} else {
					clustering.getNoise().add(model.getData());
				}
			} else {
				currentCluster.add(model.getData());
			}
		}
		ExtractDBScanClustersStage.LOGGER.info("generated " + clustering.getClusters().size() + " clusters");

		this.getOutputPort().send(clustering);
	}
}
