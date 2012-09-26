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

package kieker.tools.traceAnalysis.repository;

import kieker.common.configuration.Configuration;
import kieker.common.configuration.ConfigurationWrapper;

/**
 * Configuration wrapper for the description repository ({@link DescriptionRepository}).
 * 
 * @author Holger Knoche
 * 
 */
public class DescriptionRepositoryConfiguration extends ConfigurationWrapper {

	/**
	 * Name of the configuration property that contains the file name of the description file.
	 */
	public static final String CONFIG_PROPERTY_NAME_DESCRIPTION_FILE_NAME = "descriptionFileName";

	/**
	 * Creates a new configuration wrapper using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to use
	 */
	public DescriptionRepositoryConfiguration(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * Returns the description file name.
	 * 
	 * @return See above
	 */
	public String getDescriptionFileName() {
		return this.getWrappedConfiguration().getStringProperty(CONFIG_PROPERTY_NAME_DESCRIPTION_FILE_NAME);
	}

	/**
	 * Sets the description file name.
	 * 
	 * @param fileName
	 *            The file name to set
	 */
	public void setDescriptionFileName(final String fileName) {
		this.getWrappedConfiguration().setProperty(CONFIG_PROPERTY_NAME_DESCRIPTION_FILE_NAME, fileName);
	}

}
