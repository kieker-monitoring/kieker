/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.configuration;

import kieker.analysis.exception.PluginNotFoundException;
import kieker.analysis.plugin.AbstractUpdateableFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.util.registry.Registry;

/**
 * This is a global accesible singleton class to update the configuration of registered filters during runtime.
 * 
 * @author Markus Fischer, Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class GlobalConfigurationRegistry {

	private static final GlobalConfigurationRegistry INSTANCE = new GlobalConfigurationRegistry();

	private final Registry<AbstractUpdateableFilterPlugin> updateableFilters = new Registry<AbstractUpdateableFilterPlugin>();

	private GlobalConfigurationRegistry() {}

	public static GlobalConfigurationRegistry getInstance() {
		return INSTANCE;
	}

	/**
	 * Registers an AbstractUpdateableFilterPlugin to the registry.
	 * 
	 * @param plugin
	 *            plugin to be registered
	 */
	public int registerUpdateableFilterPlugin(final AbstractUpdateableFilterPlugin plugin) {
		return this.updateableFilters.get(plugin);
	}

	/**
	 * Updates the configuration of a FilterPlugin identified by its id.
	 * 
	 * @param id
	 *            id of the plugin to be updated
	 * @param configuration
	 *            Configuration containing the new values
	 * @param update
	 *            If false, set all properties, else overwrite only properties that are marked as updateable
	 * 
	 * @throws PluginNotFoundException
	 *             plugin was not found
	 */
	public void updateConfiguration(final int id, final Configuration configuration, final boolean update) throws PluginNotFoundException {
		final AbstractUpdateableFilterPlugin plugin = this.updateableFilters.get(id);

		if (null != plugin) {
			plugin.setCurrentConfiguration(configuration, update);
		} else {
			throw new PluginNotFoundException(id, configuration);
		}
	}

}
