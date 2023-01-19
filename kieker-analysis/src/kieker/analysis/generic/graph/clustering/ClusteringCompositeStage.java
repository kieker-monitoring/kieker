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

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * A composite stage, to perform the entire clustering. First the models are converted into the
 * wrapper class OpticsData. These are ordered in an M-Tree with the Graph-Edit-Distance metric
 * Then, the optics algorithm sorts the models and assigns reachability-distances The
 * ExtractDBScanClusters stage is used to extract the clusters from the optisc result
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ClusteringCompositeStage<N extends INode, E extends IEdge> extends CompositeStage {

	private final InputPort<OpticsData<MutableNetwork<N, E>>> inputPort;
	private final InputPort<Long> timerInputPort;
	private final OutputPort<Clustering<MutableNetwork<N, E>>> outputPort;

	public ClusteringCompositeStage(final double clusteringDistance, final int minPts, final Integer maxAmount,
			final OPTICSDataGED<MutableNetwork<N, E>> distanceFunction) {

		final DataCollectorStage<OpticsData<MutableNetwork<N, E>>> dataCollectorStage;
		if (maxAmount != null) {
			dataCollectorStage = new DataCollectorStage<>(maxAmount);
		} else {
			dataCollectorStage = new DataCollectorStage<>();
		}

		final MTreeGeneratorStage<OpticsData<MutableNetwork<N, E>>> mTreeGeneratorStage = new MTreeGeneratorStage<>(distanceFunction);

		final OpticsStage<N, E> opticsStage = new OpticsStage<>(clusteringDistance, minPts);

		final ExtractDBScanClustersStage<N, E> clustering = new ExtractDBScanClustersStage<>(clusteringDistance);

		this.timerInputPort = dataCollectorStage.getTimeTriggerInputPort();

		this.inputPort = dataCollectorStage.getDataInputPort();

		this.connectPorts(dataCollectorStage.getmTreeOutputPort(), mTreeGeneratorStage.getInputPort());
		this.connectPorts(mTreeGeneratorStage.getOutputPort(), opticsStage.getMTreeInputPort());

		this.connectPorts(dataCollectorStage.getOpticsOutputPort(), opticsStage.getModelsInputPort());
		this.connectPorts(opticsStage.getOutputPort(), clustering.getInputPort());

		this.outputPort = clustering.getOutputPort();
	}

	public InputPort<OpticsData<MutableNetwork<N, E>>> getInputPort() {
		return this.inputPort;
	}

	public InputPort<Long> getTimerInputPort() {
		return this.timerInputPort;
	}

	public OutputPort<Clustering<MutableNetwork<N, E>>> getOutputPort() {
		return this.outputPort;
	}

}
