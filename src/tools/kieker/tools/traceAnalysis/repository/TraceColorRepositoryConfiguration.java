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
 * Configuration wrapper for the trace color repository ({@link TraceColorRepository}).
 * 
 * @author Holger Knoche
 * 
 */
public class TraceColorRepositoryConfiguration extends ConfigurationWrapper {

	/**
	 * Name of the configuration property that contains the file name of the trace color file.
	 */
	public static final String CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME = "traceColorFileName";

	/**
	 * Creates a new configuration wrapper using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to wrap
	 */
	public TraceColorRepositoryConfiguration(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * Returns the trace color file name.
	 * 
	 * @return See above
	 */
	public String getTraceColorFileName() {
		return this.getWrappedConfiguration().getStringProperty(CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME);
	}

	/**
	 * Sets the trace color file name to the given value.
	 * 
	 * @param fileName
	 *            The file name to set
	 */
	public void setTraceColorFileName(final String fileName) {
		this.getWrappedConfiguration().setProperty(CONFIG_PROPERTY_NAME_TRACE_COLOR_FILE_NAME, fileName);
	}

}
