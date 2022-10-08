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
package kieker.tools.behavior.analysis;

import kieker.analysis.behavior.ClusterMedoidSink;
import kieker.analysis.behavior.ClusteringSink;
import kieker.analysis.behavior.ModelGenerationCompositeStage;
import kieker.analysis.behavior.clustering.ClusteringCompositeStage;
import kieker.analysis.behavior.clustering.GraphEditDistance;
import kieker.analysis.behavior.clustering.IParameterWeighting;
import kieker.analysis.behavior.clustering.NaiveMediodGenerator;
import kieker.analysis.generic.source.time.TimeReaderStage;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.ParameterEvaluationUtils;
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.Configuration;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorAnalysisConfiguration extends Configuration {

	public BehaviorAnalysisConfiguration(final BehaviorAnalysisSettings settings, final kieker.common.configuration.Configuration configuration)
			throws ConfigurationException {

		final LogsReaderCompositeStage reader = new LogsReaderCompositeStage(configuration);

		final ModelGenerationCompositeStage modelGeneration = new ModelGenerationCompositeStage(settings.getEntryCallAcceptanceMatcher(),
				settings.getTraceSignatureCleanupRewriter(),
				settings.getModelGenerationFilterFactory());

		this.connectPorts(reader.getOutputPort(), modelGeneration.getInputPort());

		final ClusteringCompositeStage clustering = new ClusteringCompositeStage(settings.getClusteringDistance(),
				settings.getMinPts(), settings.getMaxAmount());

		final TimeReaderStage timerStage = new TimeReaderStage(1l, 1l);

		this.connectPorts(modelGeneration.getModelOutputPort(), clustering.getModelInputPort());
		this.connectPorts(timerStage.getOutputPort(), clustering.getTimerInputPort());

		// configure sink. The only one of the clustering sinks should be enabled. This can be
		// improved
		// with a sink factory

		if (settings.isReturnClustering()) {
			final ClusteringSink sink = new ClusteringSink(settings.getOutputUrl());
			this.connectPorts(clustering.getOutputPort(), sink.getInputPort());
		}

		if (settings.isReturnMedoids()) {
			final IParameterWeighting weighting = ParameterEvaluationUtils.createFromConfiguration(IParameterWeighting.class, configuration,
					ConfigurationKeys.PARAMETER_WEIGHTING, "missing parameter weighting function.");

			final double nodeInsertCost = configuration.getDoubleProperty(ConfigurationKeys.NODE_INSERTION_COST, 10);

			final double edgeInsertCost = configuration.getDoubleProperty(ConfigurationKeys.EDGE_INSERTION_COST, 5);

			final double eventGroupInsertCost = configuration
					.getDoubleProperty(ConfigurationKeys.EVENT_GROUP_INSERTION_COST, 4);

			final GraphEditDistance graphEditDistance = new GraphEditDistance(nodeInsertCost, edgeInsertCost, eventGroupInsertCost, weighting);

			final NaiveMediodGenerator medoid = new NaiveMediodGenerator(graphEditDistance);
			final ClusterMedoidSink sink = new ClusterMedoidSink(settings.getOutputUrl());

			this.connectPorts(clustering.getOutputPort(), medoid.getInputPort());
			this.connectPorts(medoid.getOutputPort(), sink.getInputPort());
		}

	}
}
