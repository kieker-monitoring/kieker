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

import kieker.analysis.AnalysisController;

/**
 * Data transfer class for a graph test setup. It contains of a prepared analysis controller (which just
 * needs to be run) and a reference to a plugin which allows to access the produced graphs.
 * 
 * @author Holger Knoche
 * 
 */
public class GraphTestSetup {

	private final AnalysisController analysisController;
	private final GraphReceiverPlugin graphReceiverPlugin;

	/**
	 * Creates a new graph test setup from the given data.
	 * 
	 * @param analysisController
	 *            A prepared analysis controller for the setup
	 * @param graphReceiverPlugin
	 *            The plugin that collects the graphs produced by the controller
	 */
	public GraphTestSetup(final AnalysisController analysisController, final GraphReceiverPlugin graphReceiverPlugin) {
		this.analysisController = analysisController;
		this.graphReceiverPlugin = graphReceiverPlugin;
	}

	/**
	 * Returns the analysis controller for this setup.
	 * 
	 * @return See above
	 */
	public AnalysisController getAnalysisController() {
		return this.analysisController;
	}

	/**
	 * Returns the graph receiver plugin for this setup.
	 * 
	 * @return See above
	 */
	public GraphReceiverPlugin getGraphReceiverPlugin() {
		return this.graphReceiverPlugin;
	}

}
