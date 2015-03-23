/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

import kieker.test.tools.util.GenericTestSetup;

/**
 * Data transfer class for a graph test setup. It contains of a prepared analysis controller (which just
 * needs to be run) and a reference to a plugin which allows to access the produced graphs.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class GraphTestSetup extends GenericTestSetup<AbstractGraph<?, ?, ?>, GraphReceiverPlugin> {

	/**
	 * Creates a new graph test setup with the given data.
	 * 
	 * @param analysisController
	 *            The analysis controller to use
	 * @param resultCollectionPlugin
	 *            The graph receiver plugin to use
	 */
	public GraphTestSetup(final AnalysisController analysisController, final GraphReceiverPlugin resultCollectionPlugin) {
		super(analysisController, resultCollectionPlugin);
	}

}
