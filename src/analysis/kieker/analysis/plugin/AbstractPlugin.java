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

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.port.AbstractInputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from {@link AbstractAnalysisPlugin} or {@link AbstractMonitoringReader}.
 * 
 * @author Nils Christian Ehmke
 */
public abstract class AbstractPlugin {
	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	private final Map<String, AbstractInputPort> inputPorts = new ConcurrentHashMap<String, AbstractInputPort>();
	private final Map<String, OutputPort> outputPorts = new ConcurrentHashMap<String, OutputPort>();
	private String name = null;

	protected final Configuration configuration;

	/**
	 * Each Plugin requires a constructor with a single Configuration object!
	 */
	public AbstractPlugin(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Properties defaultProps = this.getDefaultProperties(); // NOPMD
			if (defaultProps != null) {
				configuration.setDefaultProperties(defaultProps);
			}
		} catch (final IllegalAccessException ex) {
			AbstractPlugin.LOG.error("Unable to set plugin default properties");
		}
		this.configuration = configuration;
	}

	/**
	 * This method should deliver an instance of {@code Properties} containing
	 * the default properties for this class. In other words: Every class
	 * inheriting from {@code AbstractPlugin} should implement this method to
	 * deliver an object which can be used for the constructor of this clas.
	 * 
	 * @return The default properties.
	 */
	protected abstract Properties getDefaultProperties();

	/**
	 * This method should deliver a {@code Configuration} object containing the
	 * current configuration of this instance. In other words: The constructor
	 * should be able to use the given object to initialize a new instance of
	 * this class with the same intern properties.
	 * 
	 * @return A complete filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * Delivers the input port with the given name.
	 * 
	 * @param name
	 *            The name of the input port.
	 * @return The input port with the given name or null if the name is invalid.
	 */
	public final AbstractInputPort getInputPort(final String name) {
		return this.inputPorts.get(name);
	}

	/**
	 * Delivers the output port with the given name.
	 * 
	 * @param name
	 *            The name of the output port.
	 * @return The output port with the given name or null if the name is invalid.
	 */
	public final OutputPort getOutputPort(final String name) {
		return this.outputPorts.get(name);
	}

	/**
	 * This method registers the given input port and makes sure that other
	 * classes calling the getter-methods can find this port.
	 * 
	 * @param name
	 *            The name under which the port should be accessible. If the
	 *            name is already in use, the old port will be replaced.
	 * @param port
	 *            The port to be registered.
	 */
	protected void registerInputPort(final String name, final AbstractInputPort port) {
		this.inputPorts.put(name, port);
	}

	/**
	 * This method registers the given output port and makes sure that other
	 * classes calling the getter-methods can find this port.
	 * 
	 * @param name
	 *            The name under which the port should be accessible. If the
	 *            name is already in use, the old port will be replaced.
	 * @param port
	 *            The port to be registered.
	 */
	protected void registerOutputPort(final String name, final OutputPort port) {
		this.outputPorts.put(name, port);
	}

	/**
	 * Delivers an array containing all input ports of this instance.
	 * 
	 * @return An array with all input ports.
	 */
	public final AbstractInputPort[] getAllInputPorts() {
		return this.inputPorts.values().toArray(new AbstractInputPort[0]);
	}

	/**
	 * Delivers an array containing all output ports of this instance.
	 * 
	 * @return An array with all output ports.
	 */
	public final OutputPort[] getAllOutputPorts() {
		return this.outputPorts.values().toArray(new OutputPort[0]);
	}

	/**
	 * Delivers an array containing all output port names of this instance.
	 * 
	 * @return An array with the names of all output ports.
	 */
	public final String[] getAllOutputPortNames() {
		return this.outputPorts.keySet().toArray(new String[0]);
	}

	/**
	 * Delivers an array containing all input port names of this instance.
	 * 
	 * @return An array with the names of all input ports.
	 */
	public String[] getAllInputPortNames() {
		return this.inputPorts.keySet().toArray(new String[0]);
	}

	/**
	 * This method delivers the current name of this plugin. The name does not
	 * have to be unique.
	 * 
	 * @return The name of the plugin.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * This method sets the current name of this plugin. The name does not
	 * have to be unique.
	 * 
	 * @param name
	 *            The new name of the plugin.
	 */
	public final void setName(final String name) {
		this.name = name;
	}
}
