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

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.mtree.IDistanceFunction;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage calculates the medoid of the clusters using the trimed algorithm.
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class MedoidGenerator<N extends INode, E extends IEdge> extends AbstractTransformation<Clustering<MutableNetwork<N, E>>, MutableNetwork<N, E>> {

	private final IDistanceFunction<MutableNetwork<N, E>> distanceFunction;

	public MedoidGenerator(final IDistanceFunction<MutableNetwork<N, E>> distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	@Override
	protected void execute(final Clustering<MutableNetwork<N, E>> clustering) throws Exception {

		for (final Set<MutableNetwork<N, E>> clusterSet : clustering.getClusters()) {

			final MutableNetwork<N, E>[] cluster = clusterSet.toArray(new MutableNetwork[clusterSet.size()]);
			// The trimed algorithm needs at least one element.
			if (cluster.length == 0) {
				this.logger.warn("Empty cluster received");
				return;
			}

			final TrimmedAlgorithm<MutableNetwork<N, E>> trimed = new TrimmedAlgorithm<>(cluster, this.distanceFunction);

			this.outputPort.send(trimed.calculate());
		}
		this.logger.info("gernerated all mediods of a clustering");
	}

}
