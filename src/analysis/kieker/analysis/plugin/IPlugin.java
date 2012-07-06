/***************************************************************************
 * Copyright 2012 by
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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke, Jan Waller
 */
public interface IPlugin<C extends Configuration> {

	/**
	 * This method should deliver a {@code Configuration} object containing the current configuration of this instance. In other words: The constructor should be
	 * able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return A completely filled configuration object.
	 */
	public abstract C getCurrentConfiguration();

	/**
	 * This method delivers the plugin name of this plugin. The name should be unique, e.g., the classname.
	 * 
	 * @return The name of the plugin.
	 */
	public abstract String getPluginName();

	/**
	 * This method delivers the description of this plugin type.
	 * 
	 * @return The description of the plugin type.
	 */
	public abstract String getPluginDescription();

	/**
	 * This method delivers the current name of this plugin instance. The name does not have to be unique.
	 * 
	 * @return The current name of the plugin instance.
	 */
	public abstract String getName();

	/**
	 * Connects the given repository to this plugin via the given name. <b>DO NOT USE THIS METHOD!</b> Use <code>AnalysisController.connect</code> instead!
	 * 
	 * @param name
	 *            The name of the port to connect the repository.
	 * @param repo
	 *            The repository which should be used.
	 * @throws AnalysisConfigurationException
	 *             if the repository-port is invalid, the repository itself is incompatible or the port is already used.
	 */
	public abstract void connect(final String name, final AbstractRepository<?> repo) throws AnalysisConfigurationException;

	/**
	 * This method delivers an array of {@code AbstractRepository} containing the current repositories of this instance. In other words: The constructor should
	 * be able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return An (possible empty) array of repositories.
	 */
	public abstract Map<String, AbstractRepository<?>> getCurrentRepositories();

	public abstract String[] getAllOutputPortNames();

	public abstract String[] getAllInputPortNames();

	public abstract String[] getAllDisplayNames();

	/**
	 * Delivers the plugins with their ports which are connected with the given output port.
	 * 
	 * @param outputPortName
	 *            The name of the output port.
	 * @return An array of pairs, whereat the first element is the plugin and the second one the name of the input port. If the given output port is invalid, null is
	 *         returned
	 */
	public abstract List<PluginInputPortReference> getConnectedPlugins(final String outputPortName);

	/**
	 * 
	 * @author Nils Christian Ehmke
	 */
	public static final class PluginInputPortReference {
		private final IPlugin<?> plugin;
		private final String inputPortName;
		private final Method inputPortMethod;
		private final Class<?>[] eventTypes;

		public PluginInputPortReference(final IPlugin<?> plugin, final String inputPortName, final Method inputPortMethod, final Class<?>[] eventTypes) {
			this.plugin = plugin;
			this.inputPortName = inputPortName;
			this.inputPortMethod = inputPortMethod;
			this.eventTypes = eventTypes.clone();
		}

		public final IPlugin<?> getPlugin() {
			return this.plugin;
		}

		public final Method getInputPortMethod() {
			return this.inputPortMethod;
		}

		public final Class<?>[] getEventTypes() {
			return this.eventTypes.clone();
		}

		public final String getInputPortName() {
			return this.inputPortName;
		}
	}
}
