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

package kieker.tools.configuration;

import java.util.Map;

import kieker.common.configuration.Configuration;
import kieker.tools.configuration.exception.PluginNotFoundException;

/**
 * 
 * @author Markus Fischer
 * 
 *         Registry for updateable filters
 */
public interface IConfigurationRegistry {

	/**
	 * 
	 * @return a map containing all updateable FilterPlugins.
	 */
	public abstract Map<String, AbstractUpdateableFilterPlugin> getUpdateableFilters();

	/**
	 * Register an AbstractUpdateableFilterPlugin to the registry.
	 * 
	 * @param id
	 *            Unique id for the plugin (within the scope of a registry)
	 * @param plugin
	 *            plugin to be registered
	 */
	public abstract void registerUpdateableFilterPlugin(String id,
			AbstractUpdateableFilterPlugin plugin);

	/**
	 * Update the configuration of a FilterPlugin identified by its id.
	 * 
	 * @param id
	 *            id of the plugin to be updated
	 * @param configuration
	 *            Configuration containing the new values
	 * @param update
	 *            boolean value @see {@link AbstractUpdateableFilterPlugin#setCurrentConfiguration(Configuration, boolean)}
	 * 
	 * @throws PluginNotFoundException
	 *             plugin was not found
	 */
	public abstract void updateConfiguration(String id,
			Configuration configuration, boolean update) throws PluginNotFoundException;

}
