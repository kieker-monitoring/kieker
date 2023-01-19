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

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

import teetime.stage.basic.AbstractTransformation;

/**
 * The algorithm extracts clusters, which are equivalent to DBScan clusters from the OPTICS plot.
 * The algorithm was proposed in the optics paper.
 *
 * @param <N>
 *            node class type
 * @param <E>
 *            edge class type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ExtractDBScanClustersStage<N extends INode, E extends IEdge>
		extends AbstractTransformation<List<OpticsData<MutableNetwork<N, E>>>, Clustering<MutableNetwork<N, E>>> {

	private final double clusteringDistance;

	public ExtractDBScanClustersStage(final double clusteringDistance) {
		this.clusteringDistance = clusteringDistance;
	}

	@Override
	protected void execute(final List<OpticsData<MutableNetwork<N, E>>> opticsResults) throws Exception {

		this.logger.debug("received optics result");
		for (final OpticsData<MutableNetwork<N, E>> model : opticsResults) {
			this.logger.debug(Double.toString(model.getReachabilityDistance()) + " and core: "
					+ Double.toString(model.getCoreDistance()));
		}
		final Clustering<MutableNetwork<N, E>> clustering = new Clustering<>();

		Set<MutableNetwork<N, E>> currentCluster = clustering.getNoise();

		for (final OpticsData<MutableNetwork<N, E>> model : opticsResults) {
			if (model.getReachabilityDistance() == OpticsData.UNDEFINED
					|| model.getReachabilityDistance() > this.clusteringDistance) {
				if (model.getCoreDistance() <= this.clusteringDistance
						&& model.getCoreDistance() != OpticsData.UNDEFINED) {
					final Set<MutableNetwork<N, E>> newCluster = new HashSet<>();
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
		this.logger.info("generated " + clustering.getClusters().size() + " clusters.");

		this.getOutputPort().send(clustering);
	}
}
