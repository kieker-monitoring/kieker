/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.exception;

import kieker.common.configuration.Configuration;

/**
 *
 * @author Markus Fischer
 * @since 1.10
 * @deprecated since 1.15.1 plugin API
 *
 *
 *             The Plugin requested with the ID was not found in the registry.
 *
 */
@Deprecated
public class PluginNotFoundException extends Exception {

	private static final long serialVersionUID = -8803287298408230964L;
	private final int pluginID;
	private final Configuration configuration;

	/**
	 * Constructor.
	 *
	 * @param id
	 *            requesting ID
	 * @param configuration
	 *            requesting configuration
	 */
	public PluginNotFoundException(final int id, final Configuration configuration) {
		this.pluginID = id;
		this.configuration = configuration;
	}

	/**
	 * @return the pluginID
	 */
	public int getPluginID() {
		return this.pluginID;
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return this.configuration;
	}
}
