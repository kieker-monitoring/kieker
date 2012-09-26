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

package kieker.common.configuration;

/**
 * Base class for plugin-specific configuration wrappers. These wrappers are commonly used to provide a
 * convenient access to configuration properties, e.g. via accessor methods.
 * 
 * @author Holger Knoche
 * 
 */
public class ConfigurationWrapper {

	private final Configuration wrappedConfiguration;

	/**
	 * Creates a new wrapper around the given configuration.
	 */
	protected ConfigurationWrapper(final Configuration configuration) {
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

}
