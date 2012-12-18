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

package kieker.analysis;

import kieker.analysis.AnalysisController.STATE;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.model.analysisMetaModel.MIProject;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public interface IProjectContext {

	/**
	 * Delivers the value for the given (global) property within the analysis.
	 * 
	 * @return The value for the given property if it exists, null otherwise.
	 * 
	 * @since 1.7
	 */
	public String getProperty(final String key);

	/**
	 * This method delivers the current configuration of this instance as an instance of <code>MIProject</code>.
	 * 
	 * @return A filled meta model instance.
	 * @throws AnalysisConfigurationException
	 *             If the current configuration is somehow invalid.
	 */
	public MIProject getCurrentConfiguration() throws AnalysisConfigurationException;

	/**
	 * Delivers the current name of the project.
	 * 
	 * @return The current project name.
	 */
	public String getProjectName();

	/**
	 * Delivers the current state of the analysis controller.
	 * 
	 * @return The current state.
	 */
	public STATE getState();
}
