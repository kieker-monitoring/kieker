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

package kieker.tools.iface;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;

/**
 * This class represents a runnable Kieker setup that can be used in external applications.
 * 
 * @author Holger Knoche
 * 
 * @param <T>
 *            The type of the data produced by this setup
 */
public class RunnableKiekerSetup<T> {

	private final AnalysisController analysisController;
	private final ListCollectionFilter<T> collectionFilter;
	private boolean dataAvailable = false;

	/**
	 * Creates a new setup using the given data.
	 * 
	 * @param analysisController
	 *            The analysis controller to use
	 * @param collectionFilter
	 *            The collection filter to use
	 */
	protected RunnableKiekerSetup(final AnalysisController analysisController, final ListCollectionFilter<T> collectionFilter) {
		this.analysisController = analysisController;
		this.collectionFilter = collectionFilter;
	}

	private void ensureData() throws AnalysisConfigurationException {
		if (!this.dataAvailable) {
			this.run(false);
		}
	}

	/**
	 * Explicitly runs this setup.
	 * 
	 * @param force
	 *            Denotes whether the setup should be run even if there is already data available
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public void run(final boolean force) throws AnalysisConfigurationException {
		if (!this.dataAvailable || force) {
			this.analysisController.run();
			this.dataAvailable = true;
		}
	}

	/**
	 * Returns the data produced by this setup. If the setup has not been run yet, it is run implicitly.
	 * 
	 * @return The data produced by this setup
	 * @throws AnalysisConfigurationException
	 *             If an erroneous configuration is detected
	 */
	public Iterable<T> getData() throws AnalysisConfigurationException {
		this.ensureData();

		return this.collectionFilter.getList();
	}

}
