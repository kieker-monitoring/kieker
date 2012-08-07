/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.filter.visualization;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractEdge;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertex;

/**
 * Abstract superclass for all graph filters.
 * 
 * @author Holger Knoche
 * 
 */

@Plugin
public abstract class AbstractGraphFilter<G extends AbstractGraph<V, E, O>, V extends AbstractVertex<V, E, O>, E extends AbstractEdge<V, E, O>, O> extends
		AbstractFilterPlugin<Configuration> implements IGraphOutputtingFilter<G> {

	/**
	 * The name of the filter's graph input port.
	 */
	public static final String INPUT_PORT_NAME_GRAPH = "graphs";

	private final Configuration configuration;

	/**
	 * Creates a new filter with the given configuration;
	 * 
	 * @param configuration
	 *            The filter configuration to use
	 */
	public AbstractGraphFilter(final Configuration configuration) {
		super(configuration);

		this.configuration = configuration;
	}

	public Configuration getCurrentConfiguration() {
		return this.configuration;
	}

	@InputPort(name = INPUT_PORT_NAME_GRAPH,
			description = "Graphs to process",
			eventTypes = { AbstractGraph.class })
	public void processGraph(final G graph) {
		final G processedGraph = this.performConcreteGraphProcessing(graph);
		this.deliver(this.getGraphOutputPortName(), processedGraph);
	}

	public String getGraphOutputPortName() {
		return OUTPUT_PORT_NAME_GRAPH;
	}

	public String getGraphInputPortName() {
		return INPUT_PORT_NAME_GRAPH;
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
