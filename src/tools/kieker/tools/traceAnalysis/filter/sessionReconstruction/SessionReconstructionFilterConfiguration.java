/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.sessionReconstruction;

import kieker.common.configuration.Configuration;

/**
 * Specific configuration wrapper for the {@link SessionReconstructionFilter}.
 * 
 * @author Holger Knoche
 * @since 1.10
 * 
 */
// TODO: #699: Remove this class
public class SessionReconstructionFilterConfiguration {

	/**
	 * Name of the configuration parameter which accepts the maximum think time (i.e. the time interval
	 * after which a new session is started)
	 */
	public static final String CONFIGURATION_NAME_MAX_THINK_TIME = "maxThinkTime";

	private final Configuration wrappedConfiguration;

	/**
	 * Creates a new configuration which wraps the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to wrap
	 */
	public SessionReconstructionFilterConfiguration(final Configuration configuration) {
		this.wrappedConfiguration = configuration;
	}

	/**
	 * Returns the wrapped configuration.
	 * 
	 * @return See above
	 */
	public Configuration getWrappedConfiguration() {
		return this.wrappedConfiguration;
	}

	/**
	 * Returns the value of the maximum think time property. The maximum think time is measured in milliseconds.
	 * 
	 * @return See above
	 */
	public long getMaxThinkTime() {
		return this.wrappedConfiguration.getLongProperty(CONFIGURATION_NAME_MAX_THINK_TIME);
	}

	/**
	 * Sets the value of the maximum think time property. The maximum think time is measured in milliseconds.
	 * 
	 * @param value
	 *            The value to set
	 */
	public void setMaxThinkTime(final long value) {
		this.wrappedConfiguration.setProperty(CONFIGURATION_NAME_MAX_THINK_TIME, String.valueOf(value));
	}

}
