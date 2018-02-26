/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.filter.visualization.IWebVisualizationFilterPlugin;
import kieker.analysis.plugin.reader.IReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.misc.KiekerMetadataRecord;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the class {@link kieker.analysis.plugin.filter.AbstractFilterPlugin} or
 * {@link kieker.analysis.plugin.reader.AbstractReaderPlugin}.
 *
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.5
 */
@Plugin
public abstract class AbstractPlugin extends AbstractAnalysisComponent implements IPlugin {

	private final ConcurrentHashMap<String, List<PluginInputPortReference>> registeredMethods;
	private final ConcurrentHashMap<String, AbstractRepository> registeredRepositories;
	private final Map<OutputPort, Class<?>[]> outputPortTypes; // NOCS
	private final Map<String, RepositoryPort> repositoryPorts;
	private final Map<String, OutputPort> outputPorts;
	private final Map<String, InputPort> inputPorts;

	// Shutdown mechanism
	private final List<AbstractPlugin> incomingPlugins;
	private final List<AbstractPlugin> outgoingPlugins;
	private volatile STATE state = STATE.READY;

	/**
	 * Each Plugin requires a constructor with a Configuration object and an IProjectContext.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public AbstractPlugin(final Configuration configuration, final IProjectContext projectContext) {
		// Registering will happen in the subclass
		super(configuration, projectContext);

		// Get all repository and output ports.
		this.repositoryPorts = new ConcurrentHashMap<>();
		this.outputPorts = new ConcurrentHashMap<>();
		this.outputPortTypes = new ConcurrentHashMap<>(); // NOCS
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final RepositoryPort repoPort : annotation.repositoryPorts()) {
			if (this.repositoryPorts.put(repoPort.name(), repoPort) != null) {
				this.logger.error("Two RepositoryPorts use the same name: {}", repoPort.name());
			}
		}
		// ignore possible outputPorts for IWebVisualizationFilters
		if (!(this instanceof IWebVisualizationFilterPlugin)) {
			for (final OutputPort outputPort : annotation.outputPorts()) {
				if (this.outputPorts.put(outputPort.name(), outputPort) != null) {
					this.logger.error("Two OutputPorts use the same name: {}", outputPort.name());
				}
				Class<?>[] outTypes = outputPort.eventTypes();
				if (outTypes.length == 0) {
					outTypes = new Class<?>[] { Object.class };
				}
				this.outputPortTypes.put(outputPort, outTypes);
			}
		} else {
			// But inform the user about these invalid ports
			for (final OutputPort outputPort : annotation.outputPorts()) {
				this.logger.warn("Invalid port for visualization filter detected. Port is ignored: {}", outputPort.name());
			}
		}

		// Get all input ports.
		this.inputPorts = new ConcurrentHashMap<>();
		// ignore possible inputPorts for IReaderPlugins
		if (!(this instanceof IReaderPlugin)) {
			for (final Method method : this.getClass().getMethods()) {
				final InputPort inputPort = method.getAnnotation(InputPort.class);
				if ((inputPort != null) && (this.inputPorts.put(inputPort.name(), inputPort) != null)) {
					this.logger.error("Two InputPorts use the same name: {}", inputPort.name());
				}
				if (inputPort != null) {
					final Class<?>[] parameters = method.getParameterTypes();
					if (parameters.length != 1) {
						this.logger.error("The input port {} has to provide exactly one parameter of the correct type.", inputPort.name());
					} else {
						Class<?>[] eventTypes = inputPort.eventTypes();
						if (eventTypes.length == 0) { // NOPMD (nested if)
							eventTypes = new Class<?>[] { Object.class };
						}
						for (final Class<?> event : eventTypes) {
							if (!parameters[0].isAssignableFrom(event)) { // NOPMD (nested if)
								this.logger.error("The event type {} of the input port {} is not accepted by the parameter of type ", event.getName(),
										inputPort.name(), parameters[0].getName());
							}
						}
					}
				}
			}
		} else {
			// But inform the user about these invalid ports
			for (final Method method : this.getClass().getMethods()) {
				final InputPort inputPort = method.getAnnotation(InputPort.class);
				if (inputPort != null) {
					this.logger.warn("Invalid port for reader detected. Port is ignored: {}", inputPort.name());
				}
			}
		}
		this.registeredRepositories = new ConcurrentHashMap<>(this.repositoryPorts.size());

		// Now create a linked queue for every output port of the class, to store the registered methods.
		this.registeredMethods = new ConcurrentHashMap<>();
		for (final OutputPort outputPort : annotation.outputPorts()) {
			this.registeredMethods.put(outputPort.name(), new ArrayList<PluginInputPortReference>(1));
		}
		// and a List for every incoming and outgoing plugin
		this.incomingPlugins = new ArrayList<>(1); // usually only one incoming
		this.outgoingPlugins = new ArrayList<>(1); // usually only one outgoing
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
		// discard this kind of record when encountered ...
		if (data instanceof KiekerMetadataRecord) {
			((AnalysisController) this.projectContext).handleKiekerMetadataRecord((KiekerMetadataRecord) data);
			return true;
		}

		// First step: Get the output port.
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return false;
		}

		// Second step: Check whether the data fits the event types.
		final Class<?>[] outTypes = this.outputPortTypes.get(outputPort);
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

		// Third step: Send everything to the registered ports.
		final List<PluginInputPortReference> registeredMethodsOfPort = this.registeredMethods.get(outputPortName);

		for (final PluginInputPortReference pluginInputPortReference : registeredMethodsOfPort) {
			// Check whether the data fits the event types.
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
							this.logger.warn("Caught exception when sending data from {}: OutputPort {} to {}'s InputPort {}", this.getClass().getName(),
									outputPort.name(), pluginInputPortReference.getPlugin().getClass().getName(),
									pluginInputPortReference.getInputPortMethod().getName(), cause);
						}
					} catch (final Exception e) { // NOPMD NOCS (catch multiple)
						// This is an exception wrapped by invoke
						this.logger.error("Caught exception when invoking {}'s InputPort {}", pluginInputPortReference.getPlugin().getClass().getName(),
								pluginInputPortReference.getInputPortMethod().getName(), e);
					}
					break; // for
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		for (final Method m : dst.getClass().getMethods()) {
			final InputPort ip = m.getAnnotation(InputPort.class);
			if ((ip != null) && (m.getParameterTypes().length == 1) && ip.name().equals(inputPortName)) {
				src.outputPorts.get(outputPortName).eventTypes();
				java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
					@Override
					public Object run() {
						m.setAccessible(true);
						return null;
					}
				});
				src.registeredMethods.get(outputPortName).add(new PluginInputPortReference(dst, inputPortName, m, dst.inputPorts.get(inputPortName).eventTypes()));
				src.outgoingPlugins.add(dst);
				dst.incomingPlugins.add(src);

				// Fire notification events
				src.notifyNewOutgoingConnection(outputPortName, dst, inputPortName);
				dst.notifyNewIncomingConnection(inputPortName, src, outputPortName);

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
		// First step: Check whether the plugins are valid.
		if ((src == null) || (dst == null) || (dst instanceof IReaderPlugin)) {
			LOGGER.warn("Plugins are invalid or null.");
			return false;
		}
		if (src.state != STATE.READY) {
			LOGGER.warn("Plugin: {} not in {} state, but in state {}.", src.getClass().getName(), STATE.READY, src.state);
			return false;
		}
		if (dst.state != STATE.READY) {
			LOGGER.warn("Plugin: {} not in {} state, but in state {}.", dst.getClass().getName(), STATE.READY, dst.state);
			return false;
		}

		// Second step: Check whether the ports exist.
		final OutputPort outputPort = src.outputPorts.get(output);
		if (outputPort == null) {
			LOGGER.warn("Output port does not exist. Plugin: {}; output: {}", src.getClass().getName(), output);
			return false;
		}
		final InputPort inputPort = dst.inputPorts.get(input);
		if (inputPort == null) {
			LOGGER.warn("Input port does not exist. Plugin: {}; output: {}", dst.getClass().getName(), input);
			return false;
		}

		// Third step: Make sure the ports are compatible.
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
			// log.warn("Third step: Make sure the ports are compatible. But the input port cannot get everything.");
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
			final String allowedOutputTypes = Arrays.toString(outputPort.eventTypes());
			final String allowedInputTypes = Arrays.toString(inputPort.eventTypes());
			LOGGER.warn("Output port '{}' ({}) is not compatible with input port '{}' ({}).", output, allowedOutputTypes, input, allowedInputTypes);
			return false;
		}

		// Seems like the connection is okay.
		return true;
	}

	/**
	 * This method delivers an instance of {@code Configuration} containing the default properties for this class.
	 *
	 * @return The default properties.
	 */
	@Override
	protected final Configuration getDefaultConfiguration() {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getPluginName() {
		final String pluginName = this.getClass().getAnnotation(Plugin.class).name();
		if (pluginName.equals(Plugin.NO_NAME)) {
			return this.getClass().getSimpleName();
		} else {
			return pluginName;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getPluginDescription() {
		return this.getClass().getAnnotation(Plugin.class).description();
	}

	/**
	 * This method checks whether all repository ports of the current plugin are connected.
	 *
	 * @return true if and only if all plugin ports (defined in the annotation) are connected to a repository.
	 */
	public final boolean areAllRepositoryPortsConnected() {
		// Run through all port names and check them.
		final Iterator<String> repositoryNameIter = this.repositoryPorts.keySet().iterator();
		while (repositoryNameIter.hasNext()) {
			if (!this.registeredRepositories.containsKey(repositoryNameIter.next())) {
				// The current port is not connected.
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Map<String, AbstractRepository> getCurrentRepositories() {
		return Collections.unmodifiableMap(this.registeredRepositories);
	}

	/**
	 * Delivers the registered repository for the given name or null, if it doesn't exist.
	 *
	 * @param reponame
	 *            The name (key) of the repository.
	 * @return The registered repository instance.
	 */
	protected final AbstractRepository getRepository(final String reponame) {
		return this.registeredRepositories.get(reponame);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] getAllOutputPortNames() {
		final List<String> outputNames = new LinkedList<>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final OutputPort outputPort : annotation.outputPorts()) {
			outputNames.add(outputPort.name());
		}
		return outputNames.toArray(new String[outputNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] getAllInputPortNames() {
		final List<String> inputNames = new LinkedList<>();
		for (final Method method : this.getClass().getMethods()) {
			final InputPort inputPort = method.getAnnotation(InputPort.class);
			if ((inputPort != null) && (method.getParameterTypes().length == 1)) {
				inputNames.add(inputPort.name());
			}
		}
		return inputNames.toArray(new String[inputNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] getAllDisplayNames() {
		final List<String> displayNames = new LinkedList<>();
		for (final Method method : this.getClass().getMethods()) {
			final Display display = method.getAnnotation(Display.class);
			if (display != null) {
				displayNames.add(display.name());
			}
		}
		return displayNames.toArray(new String[displayNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] getAllRepositoryPortNames() {
		final List<String> repositoryNames = new LinkedList<>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final RepositoryPort repositoryPort : annotation.repositoryPorts()) {
			repositoryNames.add(repositoryPort.name());
		}
		return repositoryNames.toArray(new String[repositoryNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<PluginInputPortReference> getConnectedPlugins(final String outputPortName) {
		// Make sure that the output port exists
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return null;
		}
		// Now get the connections.
		final List<PluginInputPortReference> result = new ArrayList<>();
		for (final PluginInputPortReference ref : this.registeredMethods.get(outputPortName)) {
			result.add(ref);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final STATE getState() {
		return this.state;
	}

	/**
	 * Starts this plugin.
	 *
	 * @return true if and only if the start procedure was sucesful.
	 */
	public final boolean start() {
		if (this.state != STATE.READY) {
			return false;
		}
		this.state = STATE.RUNNING;
		return this.init();
	}

	/**
	 * Initializes a shutdown of this and all incoming plugins.
	 *
	 * @param error
	 *            A flag determining whether this plugin has to be shutdown due to an error or not.
	 */
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

	/**
	 * Returns the plugins which provide data to this plugin.
	 *
	 * @param transitive
	 *            Denotes whether indirect (i.e. non-immediate) providers should be returned
	 * @return A set of plugins which directly or indirectly (see above) provide data for this plugin
	 */
	public Set<AbstractPlugin> getIncomingPlugins(final boolean transitive) {
		final Set<AbstractPlugin> knownIncomingPlugins = new HashSet<>();
		this.addIncomingPlugins(knownIncomingPlugins, transitive);
		return knownIncomingPlugins;
	}

	private void addIncomingPlugins(final Set<AbstractPlugin> knownIncomingPlugins, final boolean transitive) {
		for (final AbstractPlugin plugin : this.incomingPlugins) {
			knownIncomingPlugins.add(plugin);
			if (transitive) {
				plugin.addIncomingPlugins(knownIncomingPlugins, transitive);
			}
		}
	}

	/**
	 * Notification method which is called when a new incoming connection to this plugin is established.
	 *
	 * @param inputPortName
	 *            The input port name to which the connection was established
	 * @param connectedPlugin
	 *            The plugin that was connected
	 * @param outputPortName
	 *            The opposing plugin's output port from which the connection was established
	 * @throws AnalysisConfigurationException
	 *             If an error occurs while processing of this notification
	 */
	protected void notifyNewIncomingConnection(final String inputPortName, final AbstractPlugin connectedPlugin, // NOPMD
			final String outputPortName) throws AnalysisConfigurationException {
		// Do nothing by default
	}

	/**
	 * Notification method which is called when a new outgoing connection from this plugin is established.
	 *
	 * @param outputPortName
	 *            The output port name to which the connection was established
	 * @param connectedPlugin
	 *            The plugin that was connected
	 * @param inputPortName
	 *            The opposing plugin's input port from which the connection was established
	 * @throws AnalysisConfigurationException
	 *             If an error occurs while processing of this notification
	 */
	protected void notifyNewOutgoingConnection(final String outputPortName, final AbstractPlugin connectedPlugin, // NOPMD
			final String inputPortName) throws AnalysisConfigurationException {
		// Do nothing by default
	}

}
