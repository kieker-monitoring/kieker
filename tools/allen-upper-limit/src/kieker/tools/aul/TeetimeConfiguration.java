/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.aul;

import java.io.IOException;

import org.mosim.refactorlizar.architecture.evaluation.codemetrics.Complexity;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.HyperGraphSize;
import org.mosim.refactorlizar.architecture.evaluation.graphs.Node;

import com.google.common.graph.Graph;

import kieker.analysis.architecture.ModelRepositoryProducerStage;
import kieker.analysis.generic.graph.selector.AllSelector;
import kieker.analysis.generic.source.NumberGeneratorProducer;
import kieker.analysis.metrics.graph.entropy.AllenDeployedMaximalInterconnectedGraphStage;
import kieker.analysis.metrics.graph.entropy.ComputeAllenComplexityMetrics;
import kieker.analysis.metrics.graph.entropy.KiekerArchitectureModelSystemGraphUtils;
import kieker.analysis.metrics.graph.entropy.SaveMultipleResultsAllenMetricSink;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.tools.aul.stages.CreateArchitectureModularGraphStage;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class TeetimeConfiguration extends Configuration {
	public TeetimeConfiguration(final Settings settings) throws IOException {

		final OutputPort<Graph<Node<DeployedComponent>>> graphOutputPort;

		if (settings.getNodes() != null) {
			final NumberGeneratorProducer numberGeneratorProducer = new NumberGeneratorProducer(1, settings.getNodes());
			final CreateArchitectureModularGraphStage createGraphStage = new CreateArchitectureModularGraphStage(
					settings.getCreatorMode());

			this.connectPorts(numberGeneratorProducer.getOutputPort(), createGraphStage.getInputPort());

			graphOutputPort = createGraphStage.getOutputPort();
		} else {
			final ModelRepositoryProducerStage readerStage = new ModelRepositoryProducerStage(
					settings.getInputDirectory());
			final AllenDeployedMaximalInterconnectedGraphStage allenArchitectureModularGraphStage = new AllenDeployedMaximalInterconnectedGraphStage(
					new AllSelector());

			this.connectPorts(readerStage.getOutputPort(), allenArchitectureModularGraphStage.getInputPort());

			graphOutputPort = allenArchitectureModularGraphStage.getOutputPort();
		}

		/** setup allen metrics. */
		final ComputeAllenComplexityMetrics<DeployedComponent> computeAllenComplexityStage = new ComputeAllenComplexityMetrics<>(
				new KiekerArchitectureModelSystemGraphUtils(), HyperGraphSize.class, Complexity.class);
		final SaveMultipleResultsAllenMetricSink saveAllenDataStage = new SaveMultipleResultsAllenMetricSink(
				settings.getOutputDirectory().resolve("allen-metrics.csv"), "\n", ";", HyperGraphSize.class,
				Complexity.class);

		this.connectPorts(graphOutputPort, computeAllenComplexityStage.getInputPort());
		this.connectPorts(computeAllenComplexityStage.getOutputPort(), saveAllenDataStage.getInputPort());
	}
}
