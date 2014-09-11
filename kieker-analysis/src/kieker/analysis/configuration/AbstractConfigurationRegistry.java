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

import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.configuration.exception.PluginNotFoundException;
import kieker.common.configuration.Configuration;

/**
 * Abstract class for a ConfigurationRegistry.<br>
 * 
 * {@link AbstractUpdateableFilterPlugin} can register to a Registry and thereby be accessed and updated.
 * 
 * @author Markus Fischer
 * @since 1.10
 * 
 */
public abstract class AbstractConfigurationRegistry implements IConfigurationRegistry {

	private final ConcurrentHashMap<String, AbstractUpdateableFilterPlugin> updateableFilters = new ConcurrentHashMap<String, AbstractUpdateableFilterPlugin>();

	public AbstractConfigurationRegistry() {
		// Empty default constructor
	}

	public ConcurrentHashMap<String, AbstractUpdateableFilterPlugin> getUpdateableFilters() {
		return this.updateableFilters;
	}

	public void registerUpdateableFilterPlugin(final String id, final AbstractUpdateableFilterPlugin plugin) {
		this.getUpdateableFilters().put(id, plugin);
	}

	public void updateConfiguration(final String id, final Configuration configuration, final boolean update)
			throws PluginNotFoundException {
		final AbstractUpdateableFilterPlugin plugin = this.updateableFilters.get(id);
		if (plugin != null) {
			plugin.setCurrentConfiguration(configuration, update);
		} else {

			throw new PluginNotFoundException(id, configuration);
		}

	}
}
