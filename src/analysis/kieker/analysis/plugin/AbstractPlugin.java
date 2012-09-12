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

package kieker.analysis.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.display.annotation.Display;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.reader.IReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the class {@link kieker.analysis.plugin.filter.AbstractFilterPlugin} or
 * {@link kieker.analysis.plugin.reader.AbstractReaderPlugin}.
 * 
 * @author Nils Christian Ehmke, Jan Waller
 */
@Plugin
public abstract class AbstractPlugin implements IPlugin {

	public static final String CONFIG_NAME = Configuration.CONFIGURATION_NAME_KEY;

	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	protected final Configuration configuration;
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<PluginInputPortReference>> registeredMethods;
	private final ConcurrentHashMap<String, AbstractRepository> registeredRepositories;
	private final Map<String, RepositoryPort> repositoryPorts;
	private final Map<String, OutputPort> outputPorts;
	private final Map<String, InputPort> inputPorts;
	private final String name;

	// Shutdown mechanism
	private final List<AbstractPlugin> incomingPlugins;
	private final List<AbstractPlugin> outgoingPlugins;
	private volatile STATE state = STATE.READY;

	/**
	 * Each Plugin requires a constructor with a single Configuration object and an array of repositories!
	 */
	public AbstractPlugin(final Configuration configuration) {
		try {
			// TODO: somewhat dirty hack...
			configuration.setDefaultConfiguration(this.getDefaultConfiguration());

		} catch (final IllegalAccessException ex) {
			LOG.error("Unable to set plugin default properties", ex);
		}
		this.configuration = configuration;

		/* try to determine name */
		this.name = configuration.getStringProperty(CONFIG_NAME);

		/* KEEP IN MIND: Although we use "this" in the following code, it points to the actual class. Not to AbstractPlugin!! */

		/* Get all repository and output ports. */
		this.repositoryPorts = new ConcurrentHashMap<String, RepositoryPort>();
		this.outputPorts = new ConcurrentHashMap<String, OutputPort>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final RepositoryPort repoPort : annotation.repositoryPorts()) {
			if (this.repositoryPorts.put(repoPort.name(), repoPort) != null) {
				LOG.error("Two RepositoryPorts use the same name: " + repoPort.name());
			}
		}
		for (final OutputPort outputPort : annotation.outputPorts()) {
			if (this.outputPorts.put(outputPort.name(), outputPort) != null) {
				LOG.error("Two OutputPorts use the same name: " + outputPort.name());
			}
		}
		/* Get all input ports. */
		this.inputPorts = new ConcurrentHashMap<String, InputPort>();
		// ignore possible inputPorts for IReaderPlugins
		if (!(this instanceof IReaderPlugin)) {
			for (final Method method : this.getClass().getMethods()) {
				final InputPort inputPort = method.getAnnotation(InputPort.class);
				if ((inputPort != null) && (this.inputPorts.put(inputPort.name(), inputPort) != null)) {
					LOG.error("Two InputPorts use the same name: " + inputPort.name());
				}
			}
		}
		this.registeredRepositories = new ConcurrentHashMap<String, AbstractRepository>(this.repositoryPorts.size());

		/* Now create a linked queue for every output port of the class, to store the registered methods. */
		this.registeredMethods = new ConcurrentHashMap<String, ConcurrentLinkedQueue<PluginInputPortReference>>();
		for (final OutputPort outputPort : annotation.outputPorts()) {
			this.registeredMethods.put(outputPort.name(), new ConcurrentLinkedQueue<PluginInputPortReference>());
		}
		/* and a List for every incoming and outgoing plugin */
		this.incomingPlugins = new ArrayList<AbstractPlugin>(1); // usually only one incoming
		this.outgoingPlugins = new ArrayList<AbstractPlugin>(1); // usually only one outgoing
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
		if (((this.state != STATE.RUNNING) && (this.state != STATE.TERMINATING)) || (data == null)) {
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
		final ConcurrentLinkedQueue<PluginInputPortReference> registeredMethodsOfPort = this.registeredMethods.get(outputPortName);

		for (final PluginInputPortReference pluginInputPortReference : registeredMethodsOfPort) {
			/* Check whether the data fits the event types. */
			Class<?>[] eventTypes = pluginInputPortReference.getEventTypes();
			if (eventTypes.length == 0) {
				eventTypes = new Class<?>[] { Object.class };
			}
			for (final Class<?> eventType : eventTypes) {
				if (eventType.isAssignableFrom(data.getClass())) { // data instanceof eventType
					try {
						pluginInputPortReference.getInputPortMethod().invoke(pluginInputPortReference.getPlugin(), data);
					} catch (final InvocationTargetException e) {
						// This is an exception wrapped by invoke
						final Throwable cause = e.getCause();
						if (cause instanceof Error) {
							// This is a severe case and there is little chance to terminate appropriately
							throw (Error) cause;
						} else {
							LOG.warn("Caught exception when sending data from " + this.getClass().getName() + ": OutputPort " + outputPort.name()
									+ " to "
									+ pluginInputPortReference.getPlugin().getClass().getName() + "'s InputPort "
									+ pluginInputPortReference.getInputPortMethod().getName(), cause);
						}
					} catch (final Exception e) { // NOPMD NOCS (catch multiple)
						// This is an exception wrapped by invoke
						LOG.error("Caught exception when invoking "
								+ pluginInputPortReference.getPlugin().getClass().getName() + "'s InputPort "
								+ pluginInputPortReference.getInputPortMethod().getName(), e);
					}
					break; // for
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#connect(java.lang.String, kieker.analysis.repository.AbstractRepository)
	 */
	public final void connect(final String reponame, final AbstractRepository repository) throws AnalysisConfigurationException {
		if (this.state != STATE.READY) {
			throw new AnalysisConfigurationException("Plugin: " + this.getClass().getName() + " final not in " + STATE.READY + " this.state, but final in state "
					+ this.state + ".");
		}
		final RepositoryPort port = this.repositoryPorts.get(reponame);
		if (port == null) {
			throw new AnalysisConfigurationException("Failed to connect plugin '" + this.getName() + "' (" + this.getPluginName() + ") to repository '"
					+ repository.getName() + "' (" + repository.getRepositoryName() + "). Unknown repository port: " + reponame);
		}
		final Class<? extends AbstractRepository> repositoryType = port.repositoryType();
		if (!repositoryType.isAssignableFrom(repository.getClass())) {
			throw new AnalysisConfigurationException("Failed to connect plugin '" + this.getName() + "' (" + this.getPluginName() + ") to repository '"
					+ repository.getName() + "' (" + repository.getRepositoryName() + "). Expected RepositoryType: " + repositoryType.getName() + " Found: "
					+ repository.getClass().getName());
		}
		synchronized (this) {
			if (this.registeredRepositories.containsKey(reponame)) {
				throw new AnalysisConfigurationException("Failed to connect plugin '" + this.getName() + "' (" + this.getPluginName() + ") to repository '"
						+ repository.getName() + "' (" + repository.getRepositoryName() + "). RepositoryPort already connected: " + reponame);
			}
			this.registeredRepositories.put(reponame, repository);
		}
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
	 * @throws AnalysisConfigurationException
	 *             if any given plugin is invalid, any output or input port doesn't exist or if they are incompatible.
	 *             Furthermore the destination plugin must not be a reader.
	 */
	public static final void connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst, final String inputPortName)
			throws AnalysisConfigurationException {
		if (!AbstractPlugin.isConnectionAllowed(src, outputPortName, dst, inputPortName)) {
			throw new AnalysisConfigurationException("Failed to connect plugin '" + src.getName() + "' (" + src.getPluginName() + ") to plugin '"
					+ dst.getName() + "' (" + dst.getPluginName() + ").");
		}
		// Connect the ports.
		// TODO: add a better check for the parameter of the method (currently only if 1 parameter present)
		for (final Method m : dst.getClass().getMethods()) {
			final InputPort ip = m.getAnnotation(InputPort.class);
			if ((ip != null) && (m.getParameterTypes().length == 1) && ip.name().equals(inputPortName)) {
				java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						m.setAccessible(true);
						return null;
					}
				});
				src.registeredMethods.get(outputPortName).add(new PluginInputPortReference(dst, inputPortName, m, dst.inputPorts.get(inputPortName).eventTypes()));
				src.outgoingPlugins.add(dst);
				dst.incomingPlugins.add(src);
				return;
			}
		}
		throw new AnalysisConfigurationException("Failed to connect plugin '" + src.getName() + "' (" + src.getPluginName() + ") to plugin '"
				+ dst.getName() + "' (" + dst.getPluginName() + ").");
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
			LOG.warn("Plugins are invalid or null.");
			return false;
		}
		if (src.state != STATE.READY) {
			LOG.warn("Plugin: " + src.getClass().getName() + " not in " + STATE.READY + " state, but in state " + src.state + ".");
			return false;
		}
		if (dst.state != STATE.READY) {
			LOG.warn("Plugin: " + dst.getClass().getName() + " not in " + STATE.READY + " state, but in state " + dst.state + ".");
			return false;
		}

		/* Second step: Check whether the ports exist. */
		final OutputPort outputPort = src.outputPorts.get(output);
		if (outputPort == null) {
			LOG.warn("Output port does not exist. " + "Plugin: " + src.getClass().getName() + "; output: " + output);
			return false;
		}
		final InputPort inputPort = dst.inputPorts.get(input);
		if (inputPort == null) {
			LOG.warn("Input port does not exist. " + "Plugin: " + dst.getClass().getName() + "; input: " + input);
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
			// LOG.warn("Third step: Make sure the ports are compatible. But the input port cannot get everything.");
			// return false;
			// }
			for (final Class<?> srcEventType : outEventTypes) {
				for (final Class<?> dstEventType : inputPort.eventTypes()) {
					if (dstEventType.isInterface() || srcEventType.isInterface()
							|| dstEventType.isAssignableFrom(srcEventType) || srcEventType.isAssignableFrom(dstEventType)) {
						return true;
					}
				}
			}
			LOG.warn("Ports are not comaptible with eachother.");
			return false;
		}

		/* Seems like the connection is okay. */
		return true;
	}

	/**
	 * This method delivers an instance of {@code Configuration} containing the default properties for this class.
	 * 
	 * @return The default properties.
	 */
	private final Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		// Get the annotation from the class
		final Plugin pluginAnnotation = this.getClass().getAnnotation(Plugin.class);
		final Property[] propertyAnnotations = pluginAnnotation.configuration();
		// Run through all properties within the annotation and add them to the configuration object
		for (final Property property : propertyAnnotations) {
			defaultConfiguration.setProperty(property.name(), property.defaultValue());
		}
		return defaultConfiguration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getName()
	 */
	public final String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getPluginName()
	 */
	public final String getPluginName() {
		final String pluginName = this.getClass().getAnnotation(Plugin.class).name();
		if (pluginName.equals(Plugin.NO_NAME)) {
			return this.getClass().getSimpleName();
		} else {
			return pluginName;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getPluginDescription()
	 */
	public final String getPluginDescription() {
		return this.getClass().getAnnotation(Plugin.class).description();
	}

	/**
	 * This method checks whether all repository ports of the current plugin are connected.
	 * 
	 * @return true if and only if all plugin ports (defined in the annotation) are connected to a repository.
	 */
	public final boolean areAllRepositoryPortsConnected() {
		/* Run through all port names and check them. */
		final Iterator<String> repositoryNameIter = this.repositoryPorts.keySet().iterator();
		while (repositoryNameIter.hasNext()) {
			if (!this.registeredRepositories.containsKey(repositoryNameIter.next())) {
				/* The current port is not connected. */
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getCurrentRepositories()
	 */
	public final Map<String, AbstractRepository> getCurrentRepositories() {
		return Collections.unmodifiableMap(this.registeredRepositories);
	}

	protected final AbstractRepository getRepository(final String reponame) {
		return this.registeredRepositories.get(reponame);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getAllOutputPortNames()
	 */
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
	 * @see kieker.analysis.plugin.IPlugin#getAllDisplayNames()
	 */
	public final String[] getAllDisplayNames() {
		final List<String> displayNames = new LinkedList<String>();
		for (final Method method : this.getClass().getMethods()) {
			final Display display = method.getAnnotation(Display.class);
			if (display != null) {
				displayNames.add(display.name());
			}
		}
		return displayNames.toArray(new String[displayNames.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getAllOutputPortNames()
	 */
	public final String[] getAllRepositoryPortNames() {
		final List<String> repositoryNames = new LinkedList<String>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final RepositoryPort repositoryPort : annotation.repositoryPorts()) {
			repositoryNames.add(repositoryPort.name());
		}
		return repositoryNames.toArray(new String[repositoryNames.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getConnectedPlugins(java.lang.String)
	 */
	public final List<PluginInputPortReference> getConnectedPlugins(final String outputPortName) {
		/* Make sure that the output port exists */
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return null;
		}
		/* Now get the connections. */
		final List<PluginInputPortReference> result = new ArrayList<PluginInputPortReference>();
		for (final PluginInputPortReference ref : this.registeredMethods.get(outputPortName)) {
			result.add(ref);
		}
		return result;
	}

	public final STATE getState() {
		return this.state;
	}

	public final boolean start() {
		if (this.state != STATE.READY) {
			return false;
		}
		this.state = STATE.RUNNING;
		return this.init();
	}

	public final void shutdown(final boolean error) {
		if ((this.state != STATE.READY) && (this.state != STATE.RUNNING)) { // we terminate only once
			return;
		}
		if (error) {
			this.state = STATE.FAILING;
		} else {
			this.state = STATE.TERMINATING;
		}
		for (final AbstractPlugin plugin : this.incomingPlugins) {
			plugin.shutdown(error);
		}
		// when we arrive here, all incoming plugins are terminated!
		this.terminate(error);
		if (error) {
			this.state = STATE.FAILED;
		} else {
			this.state = STATE.TERMINATED;
		}
		for (final AbstractPlugin plugin : this.outgoingPlugins) {
			plugin.shutdown(error);
		}
	}
}
