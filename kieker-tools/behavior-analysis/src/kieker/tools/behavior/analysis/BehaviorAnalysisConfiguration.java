/***************************************************************************
 * Copyright (C) 2017 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.behavior.analysis;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.behavior.ModelGenerationCompositeStage;
import kieker.analysis.behavior.acceptance.matcher.GenericEntryCallAcceptanceMatcher;
import kieker.analysis.behavior.acceptance.matcher.IEntryCallAcceptanceMatcher;
import kieker.analysis.behavior.clustering.BehaviorModelToOpticsDataTransformation;
import kieker.analysis.behavior.clustering.UserBehaviorCostFunction;
import kieker.analysis.behavior.model.EntryCallEventSerializer;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.ClusterMedoidFilesSink;
import kieker.analysis.generic.graph.clustering.Clustering;
import kieker.analysis.generic.graph.clustering.ClusteringCompositeStage;
import kieker.analysis.generic.graph.clustering.ClusteringFileSink;
import kieker.analysis.generic.graph.clustering.GraphEditDistance;
import kieker.analysis.generic.graph.clustering.NaiveMedoidGenerator;
import kieker.analysis.generic.graph.clustering.OPTICSDataGED;
import kieker.analysis.util.stage.trigger.TerminationStage;
import kieker.common.exception.ConfigurationException;
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class BehaviorAnalysisConfiguration extends Configuration {

	public BehaviorAnalysisConfiguration(final BehaviorAnalysisSettings settings)
			throws ConfigurationException {

		final UserBehaviorCostFunction costFunction = new UserBehaviorCostFunction(settings.getNodeInsertCost(), settings.getEdgeInsertCost(),
				settings.getEventGroupInsertCost(), settings.getParameterWeighting());

		final LogsReaderCompositeStage reader = new LogsReaderCompositeStage(settings.getDirectories(), settings.isVerbose(), settings.getDataBufferSize());

		final IEntryCallAcceptanceMatcher entryCallAcceptanceMatcher = new GenericEntryCallAcceptanceMatcher(settings.getClassSignatureAcceptancePatterns(),
				settings.getOperationSignatureAcceptancePatterns(),
				settings.getAcceptanceMatcherMode());
		final ModelGenerationCompositeStage modelGeneration = new ModelGenerationCompositeStage(entryCallAcceptanceMatcher,
				settings.getTraceSignatureProcessor(), settings.getUserSessionTimeout());

		final GraphEditDistance<INode, UserBehaviorEdge> gedDistanceFunction = new GraphEditDistance<>(costFunction);

		final OPTICSDataGED<MutableNetwork<INode, UserBehaviorEdge>> distanceFunction = new OPTICSDataGED<>(gedDistanceFunction);

		final BehaviorModelToOpticsDataTransformation<INode, UserBehaviorEdge> behaviorModelToOpticsDataTransformation = new BehaviorModelToOpticsDataTransformation<>(
				distanceFunction);
		final ClusteringCompositeStage<INode, UserBehaviorEdge> clusteringCompositeStage = new ClusteringCompositeStage<>(settings.getClusteringDistance(),
				settings.getMinPts(), settings.getMaxAmount(), distanceFunction);
		final Distributor<Clustering<MutableNetwork<INode, UserBehaviorEdge>>> distributor = new Distributor<>(new CopyByReferenceStrategy());

		// TODO needed to use this during online runtime.
		// final TimeReaderStage timerStage = new TimeReaderStage(1L, 1L);
		final TerminationStage<Long> timerStage = new TerminationStage<>(0L);

		this.connectPorts(reader.getOutputPort(), modelGeneration.getInputPort());

		this.connectPorts(modelGeneration.getModelOutputPort(), behaviorModelToOpticsDataTransformation.getInputPort());
		this.connectPorts(behaviorModelToOpticsDataTransformation.getOutputPort(), clusteringCompositeStage.getInputPort());
		this.connectPorts(timerStage.getOutputPort(), clusteringCompositeStage.getTimerInputPort());
		this.connectPorts(clusteringCompositeStage.getOutputPort(), distributor.getInputPort());

		if (settings.getClusterOutputPath() != null) {
			final ClusteringFileSink<MutableNetwork<INode, UserBehaviorEdge>> sink = new ClusteringFileSink<>(settings.getClusterOutputPath(),
					new EntryCallEventSerializer());
			this.connectPorts(distributor.getNewOutputPort(), sink.getInputPort());
		}

		if (settings.getMedoidOutputPath() != null) {
			final GraphEditDistance<INode, UserBehaviorEdge> graphEditDistance = new GraphEditDistance<>(costFunction);

			final NaiveMedoidGenerator<MutableNetwork<INode, UserBehaviorEdge>> medoid = new NaiveMedoidGenerator<>(graphEditDistance);
			final ClusterMedoidFilesSink<MutableNetwork<INode, UserBehaviorEdge>> sink = new ClusterMedoidFilesSink<>(settings.getMedoidOutputPath(),
					new EntryCallEventSerializer());

			this.connectPorts(distributor.getNewOutputPort(), medoid.getInputPort());
			this.connectPorts(medoid.getOutputPort(), sink.getInputPort());
		}
	}
}
