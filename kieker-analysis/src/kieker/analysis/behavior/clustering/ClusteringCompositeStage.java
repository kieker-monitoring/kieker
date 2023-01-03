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

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.BasicCostFunction;
import kieker.analysis.generic.graph.clustering.Clustering;
import kieker.analysis.generic.graph.clustering.DataCollectorStage;
import kieker.analysis.generic.graph.clustering.ExtractDBScanClustersStage;
import kieker.analysis.generic.graph.clustering.MTreeGeneratorStage;
import kieker.analysis.generic.graph.clustering.OpticsData;
import kieker.analysis.generic.graph.clustering.OpticsData.OPTICSDataGED;
import kieker.analysis.generic.graph.clustering.OpticsStage;

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

	private final InputPort<IGraph<N, E>> modelInputPort;
	private final InputPort<Long> timerInputPort;
	private final OutputPort<Clustering<MutableNetwork<N, E>>> outputPort;

	public ClusteringCompositeStage(final double clusteringDistance, final int minPts, final int maxAmount,
			final BasicCostFunction<N, E> costFunction) {

		final OPTICSDataGED<N, E> distanceFunction = new OPTICSDataGED<>(costFunction);

		final BehaviorModelToOpticsDataTransformation<N, E> modelToOptics = new BehaviorModelToOpticsDataTransformation<>(distanceFunction);

		final OpticsStage<N, E> optics = new OpticsStage<>(clusteringDistance, minPts);

		final MTreeGeneratorStage<OpticsData<N, E>> mTreeGenerator = new MTreeGeneratorStage<>(distanceFunction);

		final DataCollectorStage<OpticsData<N, E>> collector;

		if (maxAmount != -1) {
			collector = new DataCollectorStage<>(maxAmount);

		} else {
			collector = new DataCollectorStage<>();
		}

		final ExtractDBScanClustersStage<N, E> clustering = new ExtractDBScanClustersStage<>(clusteringDistance);

		this.modelInputPort = modelToOptics.getInputPort();

		this.timerInputPort = collector.getTimeTriggerInputPort();

		this.connectPorts(modelToOptics.getOutputPort(), collector.getDataInputPort());

		this.connectPorts(collector.getmTreeOutputPort(), mTreeGenerator.getInputPort());

		this.connectPorts(mTreeGenerator.getOutputPort(), optics.getMTreeInputPort());

		this.connectPorts(collector.getOpticsOutputPort(), optics.getModelsInputPort());

		this.connectPorts(optics.getOutputPort(), clustering.getInputPort());

		this.outputPort = clustering.getOutputPort();
	}

	public InputPort<IGraph<N, E>> getModelInputPort() {
		return this.modelInputPort;
	}

	public InputPort<Long> getTimerInputPort() {
		return this.timerInputPort;
	}

	public OutputPort<Clustering<MutableNetwork<N, E>>> getOutputPort() {
		return this.outputPort;
	}

}
