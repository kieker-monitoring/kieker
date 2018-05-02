/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

/**
 * A plugin which receives one or more graphs and stores them for later access. This plugin is primarily
 * intended for unit tests of graph-producing plugins.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
@Plugin(description = "A plugin that receives one or more graphs and provides access them via methods.")
public class GraphReceiverPlugin extends ListCollectionFilter<AbstractGraph<?, ?, ?>> {

	/**
	 * The name of the input port which receives graphs.
	 */
	public static final String INPUT_PORT_NAME_GRAPHS = ListCollectionFilter.INPUT_PORT_NAME;

	/**
	 * Creates a new receiver plugin with the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context to use.
	 */
	public GraphReceiverPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Returns the number of graphs received so far.
	 * 
	 * @return See above
	 */
	public int getNumberOfReceivedGraphs() {
		return this.size();
	}

	/**
	 * Returns the first received graph, if any.
	 * 
	 * @return See above
	 * 
	 * @param <T>
	 *            The type of the graph.
	 */
	public <T extends AbstractGraph<?, ?, ?>> T getFirstGraph() {
		return this.<T>getGraphAt(0); // NOCS Explicit bound is required to work around a possible javac bug
	}

	/**
	 * Returns the ({@code index} + 1)th received graph, if any.
	 * 
	 * @param index
	 *            The (zero-based) index of the desired graph
	 * @return See above
	 * 
	 * @param <T>
	 *            The type of the graph.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractGraph<?, ?, ?>> T getGraphAt(final int index) {
		return (T) this.getList().get(index);
	}

}
