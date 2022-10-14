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

import kieker.analysis.behavior.model.BehaviorModel;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * A composite stage, to perform the entire clustering. First the models are converted into the
 * wrapper class OpticsData. These are ordered in an M-Tree with the Graph-Edit-Distance metric
 * Then, the optics algorithm sorts the models and assigns reachability-distances The
 * ExtractDBScanClusters stage is used to extract the clusters from the optisc result
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ClusteringCompositeStage extends CompositeStage {

	private final InputPort<BehaviorModel> modelInputPort;
	private final InputPort<Long> timerInputPort;
	private final OutputPort<Clustering<BehaviorModel>> outputPort;

	public ClusteringCompositeStage(final double clusteringDistance, final int minPts, final int maxAmount) {

		final BehaviorModelToOpticsDataTransformation modelToOptics = new BehaviorModelToOpticsDataTransformation();

		final OpticsStage optics = new OpticsStage(clusteringDistance, minPts);

		final MTreeGeneratorStage<OpticsData> mTreeGenerator = new MTreeGeneratorStage<>(OpticsData.getDistanceFunction());

		final DataCollectorStage<OpticsData> collector;

		if (maxAmount != -1) {
			collector = new DataCollectorStage<>(maxAmount);

		} else {
			collector = new DataCollectorStage<>();
		}

		final ExtractDBScanClustersStage clustering = new ExtractDBScanClustersStage(clusteringDistance);

		this.modelInputPort = modelToOptics.getInputPort();

		this.timerInputPort = collector.getTimeTriggerInputPort();

		this.connectPorts(modelToOptics.getOutputPort(), collector.getDataInputPort());

		this.connectPorts(collector.getmTreeOutputPort(), mTreeGenerator.getInputPort());

		this.connectPorts(mTreeGenerator.getOutputPort(), optics.getMTreeInputPort());

		this.connectPorts(collector.getOpticsOutputPort(), optics.getModelsInputPort());

		this.connectPorts(optics.getOutputPort(), clustering.getInputPort());

		this.outputPort = clustering.getOutputPort();

	}

	public InputPort<BehaviorModel> getModelInputPort() {
		return this.modelInputPort;
	}

	public InputPort<Long> getTimerInputPort() {
		return this.timerInputPort;
	}

	public OutputPort<Clustering<BehaviorModel>> getOutputPort() {
		return this.outputPort;
	}

}
