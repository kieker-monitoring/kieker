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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import teetime.stage.basic.AbstractTransformation;

/**
 * The algorithm extracts clusters, which are equivalent to DBScan clusters from the OPTICS plot.
 * The algorithm was proposed in the optics paper.
 *
 * @param <T> optics data type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ExtractDBScanClustersStage<T>
		extends AbstractTransformation<List<OpticsData<T>>, Clustering<T>> {

	private final double clusteringDistance;

	public ExtractDBScanClustersStage(final double clusteringDistance) {
		this.clusteringDistance = clusteringDistance;
	}

	@Override
	protected void execute(final List<OpticsData<T>> opticsResults) throws Exception {

		this.logger.debug("received optics result");
		for (final OpticsData<T> model : opticsResults) {
			this.logger.debug("{} and core: {}", Double.toString(model.getReachabilityDistance()),
					Double.toString(model.getCoreDistance()));
		}
		final Clustering<T> clustering = new Clustering<>();

		Set<T> currentCluster = clustering.getNoise();

		for (final OpticsData<T> model : opticsResults) {
			if (model.getReachabilityDistance() == OpticsData.UNDEFINED
					|| model.getReachabilityDistance() > this.clusteringDistance) {
				if (model.getCoreDistance() <= this.clusteringDistance
						&& model.getCoreDistance() != OpticsData.UNDEFINED) {
					final Set<T> newCluster = new HashSet<>();
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
		this.logger.debug("generated {} clusters.", clustering.getClusters().size());

		this.getOutputPort().send(clustering);
	}
}
