/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

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
import kieker.analysis.plugin.metasignal.InitializationSignal;
import kieker.analysis.plugin.metasignal.MetaSignal;
import kieker.analysis.plugin.metasignal.TerminationSignal;
import kieker.analysis.plugin.reader.IReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
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

	public static final String CONFIG_ASYNC_INPUT_PORTS = "async-input-ports";
	public static final String CONFIG_ASYNC_OUTPUT_PORTS = "async-output-ports";

	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<PluginInputPortReference>> registeredMethods;
	private final ConcurrentHashMap<String, AbstractRepository> registeredRepositories;
	private final Map<String, RepositoryPort> repositoryPorts;
	private final Map<String, OutputPort> outputPorts;
	private final Map<String, InputPort> inputPorts;

	// Shutdown mechanism
	private final List<AbstractPlugin> incomingPlugins;
	private final List<AbstractPlugin> outgoingPlugins;
	private volatile STATE state = STATE.READY;
	private volatile int metaSignalCounter = 0;

	// Queues and threads for the asynchronous mode
	private final ConcurrentHashMap<String, BlockingQueue<Object>> sendingQueues = new ConcurrentHashMap<String, BlockingQueue<Object>>();
	private final Collection<SendingThread> sendingThreads = new CopyOnWriteArrayList<SendingThread>();
	private final ConcurrentHashMap<String, BlockingQueue<Object>> receivingQueues = new ConcurrentHashMap<String, BlockingQueue<Object>>();
	private final Collection<ReceivingThread> receivingThreads = new CopyOnWriteArrayList<ReceivingThread>();

	/**
	 * Each Plugin requires a constructor with a Configuration object and a IProjectContext.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public AbstractPlugin(final Configuration configuration, final IProjectContext projectContext) {
		// Registering will happen in the subclass
		super(configuration, projectContext);

		// Find out which of the ports should be asynchronous
		final String[] asyncInputPorts = configuration.getStringArrayProperty(CONFIG_ASYNC_INPUT_PORTS);
		final String[] asyncOutputPorts = configuration.getStringArrayProperty(CONFIG_ASYNC_OUTPUT_PORTS);
		Arrays.sort(asyncInputPorts);
		Arrays.sort(asyncOutputPorts);

		// Get all repository and output ports.
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
			} else if (Arrays.binarySearch(asyncOutputPorts, outputPort.name()) >= 0) {
				this.setOutputPortToAsynchronousMode(outputPort.name());
			}
		}
		// Get all input ports.
		this.inputPorts = new ConcurrentHashMap<String, InputPort>();
		// ignore possible inputPorts for IReaderPlugins
		if (!(this instanceof IReaderPlugin)) {
			for (final Method method : this.getClass().getMethods()) {
				final InputPort inputPort = method.getAnnotation(InputPort.class);
				if ((inputPort != null) && (this.inputPorts.put(inputPort.name(), inputPort) != null)) {
					LOG.error("Two InputPorts use the same name: " + inputPort.name());
				} else if ((inputPort != null) && (Arrays.binarySearch(asyncInputPorts, inputPort.name()) >= 0)) {
					this.setInputPortToAsynchronousMode(inputPort.name(), method);
				}
				if (inputPort != null) {
					final Class<?>[] parameters = method.getParameterTypes();
					if (parameters.length != 1) {
						LOG.error("The input port " + inputPort.name() + " has to provide exactly one parameter of the correct type.");
					} else {
						Class<?>[] eventTypes = inputPort.eventTypes();
						if (eventTypes.length == 0) { // NOPMD (nested if)
							eventTypes = new Class<?>[] { Object.class };
						}
						for (final Class<?> event : eventTypes) {
							if (!parameters[0].isAssignableFrom(event)) { // NOPMD (nested if)
								LOG.error("The event type " + event.getName() + " of the input port " + inputPort.name()
										+ " is not accepted by the parameter of type "
										+ parameters[0].getName());
							}
						}
					}
				}
			}
		}
		this.registeredRepositories = new ConcurrentHashMap<String, AbstractRepository>(this.repositoryPorts.size());

		// Now create a linked queue for every output port of the class, to store the registered methods.
		this.registeredMethods = new ConcurrentHashMap<String, ConcurrentLinkedQueue<PluginInputPortReference>>();
		for (final OutputPort outputPort : annotation.outputPorts()) {
			this.registeredMethods.put(outputPort.name(), new ConcurrentLinkedQueue<PluginInputPortReference>());
		}
		// and a List for every incoming and outgoing plugin
		this.incomingPlugins = new ArrayList<AbstractPlugin>(1); // usually only one incoming
		this.outgoingPlugins = new ArrayList<AbstractPlugin>(1); // usually only one outgoing
	}

	private void setOutputPortToAsynchronousMode(final String outputPortName) {
		final BlockingQueue<Object> newQueue = new LinkedBlockingQueue<Object>();
		this.sendingQueues.put(outputPortName, newQueue);
		this.sendingThreads.add(new SendingThread(newQueue, outputPortName));
	}

	private void setInputPortToAsynchronousMode(final String inputPortName, final Method method) {
		final BlockingQueue<Object> newQueue = new LinkedBlockingQueue<Object>();
		this.receivingQueues.put(inputPortName, newQueue);
		this.receivingThreads.add(new ReceivingThread(newQueue, method));
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
		return this.deliver(outputPortName, data, true);
	}

	private final boolean deliver(final String outputPortName, final Object data, final boolean checkForAsynchronousMode) {
		// Check whether the data has to be delivered asynchronously
		if (checkForAsynchronousMode && this.sendingQueues.containsKey(outputPortName)) {
			this.sendingQueues.get(outputPortName).add(data);
			return true;
		}

		// Is this a meta signal?
		if (data instanceof MetaSignal) {
			final ConcurrentLinkedQueue<PluginInputPortReference> registeredMethodsOfPort = this.registeredMethods.get(outputPortName);

			for (final PluginInputPortReference pluginInputPortReference : registeredMethodsOfPort) {
				((AbstractPlugin) pluginInputPortReference.getPlugin()).processMetaSignal((MetaSignal) data);
			}
		}

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

		// Third step: Send everything to the registered ports.
		final ConcurrentLinkedQueue<PluginInputPortReference> registeredMethodsOfPort = this.registeredMethods.get(outputPortName);

		for (final PluginInputPortReference pluginInputPortReference : registeredMethodsOfPort) {
			// Check whether the data fits the event types.
			Class<?>[] eventTypes = pluginInputPortReference.getEventTypes();
			if (eventTypes.length == 0) {
				eventTypes = new Class<?>[] { Object.class };
			}
			for (final Class<?> eventType : eventTypes) {
				if (eventType.isAssignableFrom(data.getClass())) { // data instanceof eventType
					// Check whether we have to send the data asynchronously for this input port
					final AbstractPlugin receivingPlugin = (AbstractPlugin) pluginInputPortReference.getPlugin();
					final Queue<Object> receivingQueue = receivingPlugin.receivingQueues.get(pluginInputPortReference.getInputPortName());
					if (receivingQueue != null) {
						receivingQueue.add(data);
						break;
					}

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

	private boolean processMetaSignal(final MetaSignal data) {
		AbstractPlugin.LOG.info("Plugin " + this.getName() + " received meta signal (" + data + ")");

		if (data instanceof InitializationSignal) {
			this.metaSignalCounter++;
		} else if (data instanceof TerminationSignal) {
			this.metaSignalCounter--;
		}

		if ((this.metaSignalCounter == 0) || ((data instanceof TerminationSignal) && ((TerminationSignal) data).isError())) {
			// Time to shut this filter down!
			this.shutdown(((TerminationSignal) data).isError());
		}

		// Forward the meta signal!
		for (final String outputPort : this.getAllOutputPortNames()) {
			this.deliver(outputPort, data, false);
		}

		return true;
	}

	/**
	 * {@inheritDoc}
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
	public static final void connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst, final String inputPortName) throws
			AnalysisConfigurationException {
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

		// Make sure that components within containers are only connected to components within the same container
		// if (src.containerComponent != dst.containerComponent) {
		// LOG.warn("Components are contained in different containers.");
		// return false;
		// }

		// Second step: Check whether the ports exist.
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
		// They must not be for internal use only - except they belong to an analysis node.
		// TODO Can we avoid to use AnalysisNode here?
		if (outputPort.internalUseOnly() && !(src instanceof AnalysisNode)) {
			LOG.warn("Output port is for internal use only. " + "Plugin: " + src.getClass().getName() + "; output: " + output);
			return false;
		}
		if (inputPort.internalUseOnly() && !(dst instanceof AnalysisNode)) {
			LOG.warn("Input port is for internal use only. " + "Plugin: " + dst.getClass().getName() + "; input: " + input);
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
			final String allowedOutputTypes = Arrays.toString(outputPort.eventTypes());
			final String allowedInputTypes = Arrays.toString(inputPort.eventTypes());
			LOG.warn("Output port '" + output + "' (" + allowedOutputTypes + ") is not compatible with input port '" + input + "' (" + allowedInputTypes + ").");
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
	public final String[] getAllOutputPortNames() {
		final List<String> outputNames = new LinkedList<String>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final OutputPort outputPort : annotation.outputPorts()) {
			outputNames.add(outputPort.name());
		}
		return outputNames.toArray(new String[outputNames.size()]);
	}

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	public final String[] getAllRepositoryPortNames() {
		final List<String> repositoryNames = new LinkedList<String>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final RepositoryPort repositoryPort : annotation.repositoryPorts()) {
			repositoryNames.add(repositoryPort.name());
		}
		return repositoryNames.toArray(new String[repositoryNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public final List<PluginInputPortReference> getConnectedPlugins(final String outputPortName) {
		// Make sure that the output port exists
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return null;
		}
		// Now get the connections.
		final List<PluginInputPortReference> result = new ArrayList<PluginInputPortReference>();
		for (final PluginInputPortReference ref : this.registeredMethods.get(outputPortName)) {
			result.add(ref);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
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
		this.initAsynchronousPorts();
		return this.init();
	}

	private void initAsynchronousPorts() {
		for (final Thread t : this.sendingThreads) {
			t.start();
		}
		for (final Thread t : this.receivingThreads) {
			t.start();
		}
	}

	private void shutdownAsynchronousPorts() throws InterruptedException {
		for (final ReceivingThread t : this.receivingThreads) {
			t.terminate();
			t.join();
		}
		for (final SendingThread t : this.sendingThreads) {
			t.terminate();
			t.join();
		}
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

		// when we arrive here, all incoming plugins are terminated!
		this.terminate(error);
		try {
			this.shutdownAsynchronousPorts();
		} catch (final InterruptedException ex) {
			LOG.warn("Interrupted while waiting for asynchronous ports to finish.");
		}

		((AnalysisController) this.projectContext).notifyFilterTermination(this);
	}

	/**
	 * Returns the plugins which provide data to this plugin.
	 * 
	 * @param transitive
	 *            Denotes whether indirect (i.e. non-immediate) providers should be returned
	 * @return A set of plugins which directly or indirectly (see above) provide data for this plugin
	 */
	public Set<AbstractPlugin> getIncomingPlugins(final boolean transitive) {
		final Set<AbstractPlugin> knownIncomingPlugins = new HashSet<AbstractPlugin>();
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

	private class ReceivingThread extends Thread {

		private final BlockingQueue<Object> queue;
		private final Method method;

		private volatile boolean terminated;

		public ReceivingThread(final BlockingQueue<Object> queue, final Method method) {
			this.queue = queue;
			this.method = method;
			java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					ReceivingThread.this.method.setAccessible(true);
					return null;
				}
			});
		}

		@Override
		public void run() {
			while (!this.terminated) {
				try {
					final Object data = this.queue.take();
					this.method.invoke(AbstractPlugin.this, data);
				} catch (final InterruptedException ex) {
					LOG.info("ReceiverThread interrupted", ex);
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				} catch (final IllegalArgumentException e) {
					e.printStackTrace();
				} catch (final InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		public void terminate() {
			this.terminated = true;
			this.interrupt();
		}

	}

	private class SendingThread extends Thread {

		private final BlockingQueue<Object> queue;
		private final String outputPortName;
		private volatile boolean terminated;

		public SendingThread(final BlockingQueue<Object> queue, final String outputPortName) {
			this.queue = queue;
			this.outputPortName = outputPortName;
		}

		@Override
		public void run() {
			while (!this.terminated) {
				try {
					final Object data = this.queue.take();
					// Make sure that deliver sends the data to the queue but this deliver here should do so, but deliver directly.
					AbstractPlugin.this.deliver(this.outputPortName, data, false);
				} catch (final InterruptedException ex) {
					LOG.info("SendingThread interrupted", ex);
				}
			}
		}

		public void terminate() {
			this.terminated = true;
			this.interrupt();
		}
	}

	public Object[] getAllAsynchronousInputPorts() {
		return this.receivingQueues.keySet().toArray(new String[0]);
	}

	public String[] getAllAsynchronousOutputPorts() {
		return this.sendingQueues.keySet().toArray(new String[0]);
	}
}
