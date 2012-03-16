/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
package kieker.analysis.plugin;

import java.util.List;
import java.util.Map;

import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke, Jan Waller
 */
public interface IPlugin {

	/**
	 * This method should deliver a {@code Configuration} object containing the current configuration of this instance. In other words: The constructor should be
	 * able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return A completely filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * This method delivers the plugin name of this plugin. The name should be unique, e.g., the classname.
	 * 
	 * @return The name of the plugin.
	 */
	public abstract String getPluginName();

	/**
	 * This method delivers the description of this plugin.
	 * 
	 * @return The description of the plugin.
	 */
	public abstract String getPluginDescription();

	/**
	 * This method delivers the current name of this plugin. The name does not have to be unique.
	 * 
	 * @return The current name of the plugin.
	 */
	public abstract String getName();

	public abstract boolean connect(final String name, final AbstractRepository repo);

	/**
	 * This method delivers an array of {@code AbstractRepository} containing the current repositories of this instance. In other words: The constructor should
	 * be able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return An (possible empty) array of repositories.
	 */
	public abstract Map<String, AbstractRepository> getCurrentRepositories();

	public abstract String[] getAllOutputPortNames();

	public abstract String[] getAllInputPortNames();

	/**
	 * Delivers the plugins with their ports which are connected with the given output port.
	 * 
	 * @param outputPortName
	 *            The name of the output port.
	 * @return An array of pairs, whereat the first element is the plugin and the second one the name of the input port. If the given output port is invalid, null is
	 *         returned
	 */
	public abstract List<PluginInputPortReference> getConnectedPlugins(final String outputPortName);
}
