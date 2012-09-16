/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.util.graph;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

/**
 * A plugin which receives one or more graphs and stores them for later access. This plugin is primarily
 * intended for unit tests of graph-producing plugins.
 * 
 * @author Holger Knoche
 * 
 */
@Plugin(description = "A plugin that receives one or more graphs and provides access them via methods.")
public class GraphReceiverPlugin extends AbstractFilterPlugin {

	/**
	 * The name of the input port which receives graphs.
	 */
	public static final String INPUT_PORT_NAME_GRAPHS = "inputGraph";

	private final List<AbstractGraph<?, ?, ?>> receivedGraphs = new CopyOnWriteArrayList<AbstractGraph<?, ?, ?>>();

	/**
	 * Creates a new receiver plugin with the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to use
	 */
	public GraphReceiverPlugin(final Configuration configuration) {
		super(configuration);
	}

	public Configuration getCurrentConfiguration() {
		return null;
	}

	@InputPort(name = INPUT_PORT_NAME_GRAPHS,
			description = "Receives graphs to store them",
			eventTypes = { AbstractGraph.class })
	public void receiveGraph(final AbstractGraph<?, ?, ?> graph) {
		this.receivedGraphs.add(graph);
	}

	/**
	 * Returns the number of graphs received so far.
	 * 
	 * @return See above
	 */
	public int getNumberOfReceivedGraphs() {
		return this.receivedGraphs.size();
	}

	/**
	 * Returns the first received graph, if any.
	 * 
	 * @return See above
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractGraph<?, ?, ?>> T getFirstGraph() {
		return (T) this.receivedGraphs.get(0);
	}

	/**
	 * Returns the ({@code index} + 1)th received graph, if any.
	 * 
	 * @param index
	 *            The (zero-based) index of the desired graph
	 * @return See above
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractGraph<?, ?, ?>> T getGraphAt(final int index) {
		return (T) this.receivedGraphs.get(index);
	}

}
