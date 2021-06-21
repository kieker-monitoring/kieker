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

package kieker.test.tools.util;

import java.util.List;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;

/**
 * Generic test setups encapsulate a configured analysis controller (i.e. with a fully configured pipes-and-filters structure)
 * together with a list collection filter which collects the produced data.
 *
 * @author Holger Knoche
 * @since 1.10
 *
 * @param <T>
 *            The type of the result data
 * @param <P>
 *            The type of the collection filter that is used
 */
public class GenericTestSetup<T, P extends ListCollectionFilter<T>> {

	private final AnalysisController analysisController;
	private final P resultCollectionPlugin;

	/**
	 * Creates a new test setup using the given data.
	 *
	 * @param analysisController
	 *            The analysis controller to use for this setup
	 * @param resultCollectionPlugin
	 *            The result collection plugin for this setup
	 */
	public GenericTestSetup(final AnalysisController analysisController, final P resultCollectionPlugin) {
		this.analysisController = analysisController;
		this.resultCollectionPlugin = resultCollectionPlugin;
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
	 * Returns the result collection plugin for this setup.
	 *
	 * @return See above
	 */
	public P getResultCollectionPlugin() {
		return this.resultCollectionPlugin;
	}

	/**
	 * Returns the result produced by this setup. Note that this method does not invoke {@link #run()} automatically.
	 *
	 * @return See above
	 */
	public List<T> getResult() {
		return this.getResultCollectionPlugin().getList();
	}

	/**
	 * Runs the setup by starting the enclosed analysis controller.
	 *
	 * @throws AnalysisConfigurationException
	 *             If an invalid configuration is detected
	 */
	public void run() throws AnalysisConfigurationException {
		this.analysisController.run();
	}

}
