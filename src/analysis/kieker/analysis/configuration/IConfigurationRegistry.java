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

package kieker.analysis.configuration;

import java.util.Map;

import kieker.analysis.configuration.exception.PluginNotFoundException;
import kieker.common.configuration.Configuration;

/**
 * @since 1.10
 * @author Markus Fischer
 * 
 *         Registry for updateable filters
 */
public interface IConfigurationRegistry {

	/**
	 * @since 1.10
	 * @return a map containing all updateable FilterPlugins.
	 */
	public abstract Map<String, AbstractUpdateableFilterPlugin> getUpdateableFilters();

	/**
	 * Register an AbstractUpdateableFilterPlugin to the registry.
	 * 
	 * @since 1.10
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
	 * @since 1.10
	 * @param id
	 *            id of the plugin to be updated
	 * @param configuration
	 *            Configuration containing the new values
	 * @param update
	 *            If false, set all properties, else overwrite only properties that are marked as updateable
	 * @throws PluginNotFoundException
	 *             plugin was not found
	 */
	public abstract void updateConfiguration(String id,
			Configuration configuration, boolean update) throws PluginNotFoundException;

}
