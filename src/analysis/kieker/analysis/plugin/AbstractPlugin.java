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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the class {@link AbstractAnalysisPlugin} or {@link AbstractReaderPlugin}.
 * 
 * @author Nils Christian Ehmke, Jan Waller
 */
@Plugin
public abstract class AbstractPlugin implements IPlugin {
	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	public static final String CONFIG_NAME = "name-hiddenAndNeverExportedProperty";

	private final String name;

	protected final Configuration configuration;
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<PluginInputPortReference>> registeredMethods;
	private final ConcurrentHashMap<String, AbstractRepository> registeredRepositories;
	private final HashMap<String, RepositoryPort> repositoryPorts;
	private final HashMap<String, OutputPort> outputPorts;
	private final HashMap<String, InputPort> inputPorts;

	/**
	 * Each Plugin requires a constructor with a single Configuration object and an array of repositories!
	 */
	public AbstractPlugin(final Configuration configuration) {
		try {
			// TODO: somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration(); // NOPMD
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			AbstractPlugin.LOG.error("Unable to set plugin default properties", ex);
		}
		this.configuration = configuration;

		/* try to determine name */
		this.name = configuration.getStringProperty(AbstractPlugin.CONFIG_NAME);

		/* KEEP IN MIND: Although we use "this" in the following code, it points to the actual class. Not to AbstractPlugin!! */

		/* Get all repository and output ports. */
		this.repositoryPorts = new HashMap<String, RepositoryPort>();
		this.outputPorts = new HashMap<String, OutputPort>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final RepositoryPort repoPort : annotation.repositoryPorts()) {
			if (this.repositoryPorts.put(repoPort.name(), repoPort) != null) {
				AbstractPlugin.LOG.error("Two RepositoryPorts use the same name: " + repoPort.name());
			}
		}
		for (final OutputPort outputPort : annotation.outputPorts()) {
			if (this.outputPorts.put(outputPort.name(), outputPort) != null) {
				AbstractPlugin.LOG.error("Two OutputPorts use the same name: " + outputPort.name());
			}
		}
		/* Get all input ports. */
		this.inputPorts = new HashMap<String, InputPort>();
		final Method[] allMethods = this.getClass().getMethods();
		for (final Method method : allMethods) {
			final InputPort inputPort = method.getAnnotation(InputPort.class);
			if ((inputPort != null) && (this.inputPorts.put(inputPort.name(), inputPort) != null)) {
				AbstractPlugin.LOG.error("Two InputPorts use the same name: " + inputPort.name());
			}
		}

		this.registeredRepositories = new ConcurrentHashMap<String, AbstractRepository>(this.repositoryPorts.size());

		/* Now create a linked queue for every output port of the class, to store the registered methods. */
		this.registeredMethods = new ConcurrentHashMap<String, ConcurrentLinkedQueue<PluginInputPortReference>>();
		for (final OutputPort outputPort : annotation.outputPorts()) {
			this.registeredMethods.put(outputPort.name(), new ConcurrentLinkedQueue<PluginInputPortReference>());
		}
	}

	/**
	 * This method should deliver an instance of {@code Properties} containing the default properties for this class. In other words: Every class inheriting from
	 * {@code AbstractPlugin} should implement this method to deliver an object which can be used for the constructor of this class.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getCurrentConfiguration()
	 */
	@Override
	public abstract Configuration getCurrentConfiguration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getName()
	 */
	@Override
	public final String getName() {
		return this.name;
	}

	/**
	 * Delivers the given data to all registered input ports of the given output port.
	 * 
	 * @param outputPortName
	 *            The output port to be used to send the given data.
	 * @param data
	 *            The data to be send; must not be null.
	 * @return true if and only if the given output port does exist and if the data is not null and if it suits the port's event types.
	 */
	protected final boolean deliver(final String outputPortName, final Object data) {
		if (data == null) {
			return false;
		}

		/* First step: Get the output port. */
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return false;
		}

		/* Second step: Check whether the data fits the event types. */
		Class<?>[] outTypes = outputPort.eventTypes();
		if (outTypes.length == 0) {
			outTypes = new Class<?>[] { Object.class };
		}
		boolean outTypeMatch = false;
		for (final Class<?> eventType : outTypes) {
			if (eventType.isInstance(data)) {
				outTypeMatch = true;
				break; // for
			}
		}
		if (!outTypeMatch) {
			return false;
		}

		/* Third step: Send everything to the registered ports. */
		final ConcurrentLinkedQueue<PluginInputPortReference> registeredMethods = this.registeredMethods.get(outputPortName);

		for (final PluginInputPortReference pluginInputPortReference : registeredMethods) {
			/* Check whether the data fits the event types. */
			Class<?>[] eventTypes = pluginInputPortReference.getEventTypes();
			if (eventTypes.length == 0) {
				eventTypes = new Class<?>[] { Object.class };
			}
			for (final Class<?> eventType : eventTypes) {
				if (eventType.isAssignableFrom(data.getClass())) { // data instanceof eventType
					try {
						pluginInputPortReference.getInputPortMethod().invoke(pluginInputPortReference.getPlugin(), data);
					} catch (final Exception e) {
						AbstractPlugin.LOG.warn(this.getClass().getName() + ": OutputPort " + outputPort.name() + " couldn't send data to "
								+ pluginInputPortReference.getPlugin().getClass().getName() + "'s InputPort " +
								pluginInputPortReference.getInputPortMethod().getName(), e);
					}
					break; // for
				}
			}
		}
		return true;
	}

	/**
	 * This method checks whether two plugins can be connected.
	 * 
	 * @param src
	 *            The source plugin.
	 * @param output
	 *            The output port of the source plugin.
	 * @param dst
	 *            The destination plugin.
	 * @param input
	 *            The input port of the destination port.
	 * @return true if and only if both given plugins are valid, the output and input ports exist and if they are compatible. Furthermore the destination plugin must
	 *         not be a reader.
	 */
	public static final boolean isConnectionAllowed(final AbstractPlugin src, final String output, final AbstractPlugin dst, final String input) {
		/* First step: Check whether the plugins are valid. */
		if ((src == null) || (dst == null) || (dst instanceof IReaderPlugin)) {
			AbstractPlugin.LOG.warn("First step: Check whether the plugins are valid.");
			return false;
		}

		/* Second step: Check whether the ports exist. */
		final OutputPort outputPort = src.outputPorts.get(output);
		final InputPort inputPort = dst.inputPorts.get(input);
		if (outputPort == null) {
			AbstractPlugin.LOG.warn("Output port does not exist. " + "Plugin: " + src.getClass().getName() + "; output: " + output);
			return false;
		}
		if (inputPort == null) {
			AbstractPlugin.LOG.warn("Input port does not exist. " + "Plugin: " + dst.getClass().getName() + "; input: " + input);
			return false;
		}

		/* Third step: Make sure the ports are compatible. */
		if (inputPort.eventTypes().length != 0) {
			final Class<?>[] outEventTypes;
			if (outputPort.eventTypes().length == 0) {
				outEventTypes = new Class<?>[] { Object.class };
			} else {
				outEventTypes = outputPort.eventTypes();
			}
			// // Output port can deliver everything
			// if (!Arrays.asList(inputPort.eventTypes()).contains(Object.class)) {
			// // But the input port cannot get everything.
			// AbstractPlugin.LOG.warn("Third step: Make sure the ports are compatible. But the input port cannot get everything.");
			// return false;
			// }
			for (final Class<?> srcEventType : outEventTypes) {
				for (final Class<?> dstEventType : inputPort.eventTypes()) {
					// FIXME: We now also accept "downcasts" as compatible. This could perhaps be colored differently in the GUI
					if (dstEventType.isAssignableFrom(srcEventType) || srcEventType.isAssignableFrom(dstEventType)) {
						return true;
					}
				}
				AbstractPlugin.LOG.warn("Third step: Make sure the ports are compatible. Not compatible.");
				return false;
			}
		}

		/* Seems like the connection is okay. */
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#connect(java.lang.String, kieker.analysis.repository.AbstractRepository)
	 */
	@Override
	public final boolean connect(final String name, final AbstractRepository repo) {
		final RepositoryPort port = this.repositoryPorts.get(name);
		if (port == null) {
			AbstractPlugin.LOG.error("Unknown repository port: " + name);
			return false;
		}
		final Class<? extends AbstractRepository> repositoryType = port.repositoryType();
		if (!repositoryType.isAssignableFrom(repo.getClass())) {
			AbstractPlugin.LOG.error("Expected RepositoryType: " + repositoryType.getName() + " Found: " + repo.getClass().getName());
			return false;
		}
		synchronized (this) {
			if (this.registeredRepositories.containsKey(name)) {
				AbstractPlugin.LOG.error("RepositoryPort already connected: " + name);
				return false;
			}
			this.registeredRepositories.put(name, repo);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getCurrentRepositories()
	 */
	@Override
	public final Map<String, AbstractRepository> getCurrentRepositories() {
		return Collections.unmodifiableMap(this.registeredRepositories);
	}

	protected final AbstractRepository getRepository(final String name) {
		return this.registeredRepositories.get(name);
	}

	/**
	 * This method connects two plugins. <b>DO NOT USE THIS METHOD!</b> Use <code>AnalysisController.connect</code> instead!
	 * 
	 * @param src
	 *            The source plugin.
	 * @param outputPortName
	 *            The output port of the source plugin.
	 * @param dst
	 *            The destination plugin.
	 * @param inputPortName
	 *            The input port of the destination port.
	 * @return true if and only if both given plugins are valid, the output and input ports exist and if they are compatible. Furthermore the destination plugin must
	 *         not be a reader.
	 */
	public static final boolean connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst, final String inputPortName) {
		if (!AbstractPlugin.isConnectionAllowed(src, outputPortName, dst, inputPortName)) {
			AbstractPlugin.LOG.warn("Could not connect: " + src.getClass().getName() + " to " + dst.getClass().getName());
			return false;
		}

		/* Connect the ports. */
		// FIXME: add a better check for the parameter of the method (currently only if 1 parameter present)
		for (final Method m : dst.getClass().getMethods()) {
			final InputPort ip = m.getAnnotation(InputPort.class);
			if ((ip != null) && (m.getParameterTypes().length == 1) && ip.name().equals(inputPortName)) {
				m.setAccessible(true);
				src.registeredMethods.get(outputPortName).add(new PluginInputPortReference(dst, inputPortName, m, dst.inputPorts.get(inputPortName).eventTypes()));
				return true;
			}
		}
		AbstractPlugin.LOG.warn("Could not connect: " + src.getClass().getName() + " to " + dst.getClass().getName());
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getAllOutputPortNames()
	 */
	@Override
	public final String[] getAllOutputPortNames() {
		final List<String> outputNames = new LinkedList<String>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final OutputPort outputPort : annotation.outputPorts()) {
			outputNames.add(outputPort.name());
		}
		return outputNames.toArray(new String[outputNames.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getAllInputPortNames()
	 */
	@Override
	public final String[] getAllInputPortNames() {
		final List<String> inputNames = new LinkedList<String>();
		for (final Method method : this.getClass().getMethods()) {
			final InputPort inputPort = method.getAnnotation(InputPort.class);
			if ((inputPort != null) && (method.getParameterTypes().length == 1)) {
				inputNames.add(inputPort.name());
			}
		}
		return inputNames.toArray(new String[inputNames.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getConnectedPlugins(java.lang.String)
	 */
	@Override
	public final List<PluginInputPortReference> getConnectedPlugins(final String outputPortName) {
		/* Make sure that the output port exists */
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return null;
		}

		/* Now get the connections. */
		final ConcurrentLinkedQueue<PluginInputPortReference> currRegisteredMethods = this.registeredMethods.get(outputPortName);
		final List<PluginInputPortReference> result = new ArrayList<PluginInputPortReference>();
		for (final PluginInputPortReference ref : currRegisteredMethods) {
			result.add(ref);
		}

		return result;
	}
}
