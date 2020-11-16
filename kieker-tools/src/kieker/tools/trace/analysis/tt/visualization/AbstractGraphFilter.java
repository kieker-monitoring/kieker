/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis.tt.visualization;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractEdge;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractVertex;
import kieker.tools.trace.analysis.filter.visualization.graph.IOriginRetentionPolicy;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Abstract superclass for all graph filters.
 *
 * @param <G>
 *            The graph that is processed by this filter
 * @param <V>
 *            The vertex type of the graph
 * @param <E>
 *            The edge type of the graph
 * @param <O>
 *            The type of the graph's elements origins
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
public abstract class AbstractGraphFilter<G extends AbstractGraph<V, E, O>, V extends AbstractVertex<V, E, O>, E extends AbstractEdge<V, E, O>, O>
		extends AbstractConsumerStage<G> {

	private final OutputPort<G> outputPort = this.createOutputPort();

	/**
	 * Creates a new filter with the given configuration.
	 */
	public AbstractGraphFilter() {
		// nothing to be done here
	}

	protected abstract IOriginRetentionPolicy getDesiredOriginRetentionPolicy() throws AnalysisConfigurationException;

	/**
	 * Processes the given graph.
	 *
	 * @param graph
	 *            The graph to process
	 */
	@Override
	protected void execute(final G graph) throws Exception {
		final G processedGraph = this.performConcreteGraphProcessing(graph);
		this.outputPort.send(processedGraph);
	}

	public OutputPort<G> getOutputPort() {
		return this.outputPort;
	}

	/**
	 * This method encapsulates the concrete graph processing performed by the concrete filters.
	 *
	 * @param graph
	 *            The graph to process
	 * @return The processed graph, which may be the same as the input graph
	 */
	protected abstract G performConcreteGraphProcessing(G graph);
}
