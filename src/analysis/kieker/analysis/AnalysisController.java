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

package kieker.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.model.MetaModelHandler;
import kieker.analysis.model.MetaModelHandler.PluginConnection;
import kieker.analysis.model.MetaModelHandler.RepositoryConnection;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIRepository;
<<<<<<< .mine
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage;
=======


>>>>>>> .theirs
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.IReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.misc.KiekerMetadataRecord;

/**
 * The <code>AnalysisController</code> can be used to configure, control, save and load an analysis instance.
 * It is responsible for the life cycle of the readers, filters and repositories.
 * 
 * @author Andre van Hoorn, Matthias Rohr, Nils Christian Ehmke, Jan Waller
 * 
 * @since 0.95a
 */
@kieker.analysis.annotation.AnalysisController(
		configuration = {
			@Property(name = IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, defaultValue = "NANOSECONDS"),
			@Property(name = IProjectContext.CONFIG_PROPERTY_NAME_PROJECT_NAME, defaultValue = "AnalysisProject")
		})
public final class AnalysisController implements IAnalysisController { // NOPMD (really long class)

	static final Log LOG = LogFactory.getLog(AnalysisController.class); // NOPMD package for inner class

	private final String projectName;

	/**
	 * This list contains the dependencies of the project. Currently this field is only being used when loading an instance of the model.
	 */
	private final Collection<MIDependency> dependencies = new CopyOnWriteArrayList<MIDependency>();
	/**
	 * This list contains all registered readers within this controller.
	 */
	private final Collection<AbstractReaderPlugin> readers = new CopyOnWriteArrayList<AbstractReaderPlugin>();
	/**
	 * This list contains all registered filters within this controller.
	 */
	private final Collection<AbstractFilterPlugin> filters = new CopyOnWriteArrayList<AbstractFilterPlugin>();
	/**
	 * This list contains all registered repositories within this controller.
	 */
	private final Collection<AbstractRepository> repos = new CopyOnWriteArrayList<AbstractRepository>();
	/**
	 * This list contains all registered state observers within this controller.
	 */
	private final Collection<IStateObserver> stateObservers = new CopyOnWriteArrayList<IStateObserver>();

	private final CountDownLatch initializationLatch = new CountDownLatch(1);
	private volatile CountDownLatch filterLatch;

	/**
	 * This map is used to store the mapping between a given instance of {@link MIProject} and an actual instantiation of an analysis. It is not modified after
	 * creation and should only be used by the factory method.
	 */
	private Map<MIPlugin, AbstractPlugin> pluginModelMap;
	/**
	 * This map is used to store the mapping between a given instance of {@link MIProject} and an actual instantiation of an analysis. It is not modified after
	 * creation and should only be used by the factory method.
	 */
	private Map<MIRepository, AbstractRepository> repositoryModelMap;

	/**
	 * This field contains the current state of the controller.
	 */
	private volatile STATE state = STATE.READY;

	/**
	 * This field contains the global configuration for the analysis.
	 */
	private final Configuration globalConfiguration;

	/**
	 * Constructs an {@link AnalysisController} instance.
	 */
	public AnalysisController() {
		this(new Configuration());
	}

	/**
	 * Constructs an {@link AnalysisController} instance using the given parameter.
	 * 
	 * @param projectName
	 *            The name of the project.
	 */
	public AnalysisController(final String projectName) {
		this(AnalysisController.createConfigurationWithProjectName(projectName));
	}

	/**
	 * This constructors creates an {@link AnalysisController} instance, using the given file to load an analysis model. The given file should therefore be an
	 * instance of the analysis meta model.
	 * 
	 * @param file
	 *            The configuration file for the analysis.
	 * 
	 * @throws IOException
	 *             If the given file could not be loaded or is not a valid kax-configuration file.
	 * @throws AnalysisConfigurationException
	 *             If one or more plugins or repositories could not be created, one or
	 *             more properties of the plugins are invalid or if a connection between two filters is not allowed.
	 */
	public AnalysisController(final File file) throws IOException, AnalysisConfigurationException {
		this(file, AnalysisController.class.getClassLoader());
	}

	/**
	 * This constructors creates an {@link AnalysisController} instance, using the given file to load an analysis model and the given classloader to initialize the
	 * objects. The given file should therefore be an instance of the analysis meta model.
	 * 
	 * @param file
	 *            The configuration file for the analysis.
	 * @param classLoader
	 *            The classloader used to initialize the plugins etc.
	 * 
	 * @throws IOException
	 *             If the given file could not be loaded or is not a valid kax-configuration file.
	 * @throws AnalysisConfigurationException
	 *             If one or more plugins or repositories could not be created, one or
	 *             more properties of the plugins are invalid or if a connection between two filters is not allowed.
	 */
	public AnalysisController(final File file, final ClassLoader classLoader) throws IOException, AnalysisConfigurationException {
		this(AnalysisController.loadFromFile(file), classLoader);
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses the given instance of {@link MIProject} to construct the analysis.
	 * 
	 * @param project
	 *            The project instance for the analysis.
	 * @throws AnalysisConfigurationException
	 *             If the given project could not be loaded.
	 * @throws NullPointerException
	 *             If the project is null.
	 */
	public AnalysisController(final MIProject project) throws NullPointerException, AnalysisConfigurationException {
		this(project, AnalysisController.class.getClassLoader());
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses the given instance of @link{Project} to construct the analysis.
	 * 
	 * @param project
	 *            The project instance for the analysis.
	 * @param classLoader
	 *            The class loader used for the initializing.
	 * @throws NullPointerException
	 *             If the project is null.
	 * @throws AnalysisConfigurationException
	 *             If the given project could not be loaded.
	 */
	public AnalysisController(final MIProject project, final ClassLoader classLoader) throws NullPointerException, AnalysisConfigurationException {
		if (project == null) {
			throw new NullPointerException("Can not load project null.");
		} else {
			this.globalConfiguration = this.validateConfiguration(new Configuration(this.getDefaultConfiguration()));
			this.loadFromModelProject(project, classLoader);
			this.projectName = project.getName();
		}
	}

	/**
	 * Constructs an {@link AnalysisController} instance using the given parameter.
	 * 
	 * @param configuration
	 *            The global configuration of this analysis. All plugins can indirectly access it.
	 */
	public AnalysisController(final Configuration configuration) {
		this.globalConfiguration = this.validateConfiguration(configuration.flatten(this.getDefaultConfiguration()));
		this.projectName = this.getProperty(CONFIG_PROPERTY_NAME_PROJECT_NAME);
	}

	public void initializeDistributedMode(final String name, final boolean supervisorNode) {

	}

	/**
	 * This simple helper method validates the configuration object.
	 * 
	 * @param configuration
	 *            The configuration object to check and (perhaps) update.
	 * @return The configuration object.
	 */
	private Configuration validateConfiguration(final Configuration configuration) {
		final String stringProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		try {
			TimeUnit.valueOf(stringProperty);
		} catch (final IllegalArgumentException ignore) {
			LOG.warn(stringProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			configuration.setProperty(CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, TimeUnit.NANOSECONDS.name());
		}
		return configuration;
	}

	/**
	 * This simple helper method create a new configuration object which is empty except for an entry containing the given project name.
	 * 
	 * @param projectName
	 *            The project name to be stored in the new configuration object.
	 * @return The configuration object.
	 */
	private static final Configuration createConfigurationWithProjectName(final String projectName) {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_PROJECT_NAME, projectName);
		return configuration;
	}

	/**
	 * This method provides the default properties, as supplied by the annotation.
	 * 
	 * @return The default configuration.
	 */
	private final Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		// Get the (potential) annotation of our class
		final kieker.analysis.annotation.AnalysisController annotation = this.getClass().getAnnotation(kieker.analysis.annotation.AnalysisController.class);
		if (null != annotation) {
			// Run through the available properties and put them into our configuration
			for (final Property property : annotation.configuration()) {
				defaultConfiguration.setProperty(property.name(), property.defaultValue());
			}
		}
		return defaultConfiguration;
	}

	/**
	 * Called whenever an {@link KiekerMetadataRecord} is found inside the filters network.
	 * 
	 * Currently this method only logs all details.
	 * 
	 * @param record
	 *            the KiekerMetadataRecord containing the information
	 */
	public final void handleKiekerMetadataRecord(final KiekerMetadataRecord record) {
		LOG.info(record.toFormattedString());
	}

	/**
	 * {@inheritDoc}
	 */
	public final void registerStateObserver(final IStateObserver stateObserver) {
		this.stateObservers.add(stateObserver);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void unregisterStateObserver(final IStateObserver stateObserver) {
		this.stateObservers.remove(stateObserver);
	}

	/**
	 * This method runs through all observers and informs them about the current state.
	 */
	private final void notifyStateObservers() {
		// Get the current state to make sure that all observers get the same state.
		final STATE currState = this.state;
		// Now inform the observers.
		for (final IStateObserver observer : this.stateObservers) {
			observer.update(this, currState);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getProperty(final String key) {
		return this.globalConfiguration.getStringProperty(key);
	}

	/**
	 * This method can be used to load the configuration from a given meta model instance.
	 * 
	 * @param mProject
	 *            The instance to be used for configuration.
	 * @param classLoader
	 *            The instance of {@link java.lang.ClassLoader} used for creating the necessary components.
	 * @throws AnalysisConfigurationException
	 *             If the given project represents somehow an invalid configuration.
	 */
	private final void loadFromModelProject(final MIProject mProject, final ClassLoader classLoader) throws AnalysisConfigurationException {
		final Map<MIRepository, AbstractRepository> repositoryMap = new HashMap<MIRepository, AbstractRepository>(); // NOPMD (no concurrent access)
		final Map<MIPlugin, AbstractPlugin> pluginMap = new HashMap<MIPlugin, AbstractPlugin>(); // NOPMD (no concurrent access)
		final Collection<PluginConnection> pluginConnections = new ArrayList<PluginConnection>();
		final Collection<RepositoryConnection> repositoryConnections = new ArrayList<RepositoryConnection>();

		MetaModelHandler.metaModelToJava(mProject, this, pluginConnections, repositoryConnections, this.dependencies, classLoader, this.globalConfiguration,
				repositoryMap, pluginMap);

		for (final PluginConnection connection : pluginConnections) {
			this.connect(connection.getSource(), connection.getOutputName(), connection.getDestination(), connection.getInputName());
		}
<<<<<<< .mine
		// Now we have all plugins. We can start to assemble the wiring.
		for (final MIPlugin mPlugin : mPlugins) {
			// Check whether the ports exist and log this if necessary.
			AnalysisController.checkPorts(mPlugin, pluginMap.get(mPlugin));
			// //final EList<MIRepositoryConnector> mPluginRPorts = mPlugin.getRepositories();
			// for (final MIRepositoryConnector mPluginRPort : mPluginRPorts) {
			// this.connect(pluginMap.get(mPlugin), mPluginRPort.getName(), repositoryMap.get(mPluginRPort.getRepository()));
			// }
			final EList<MIOutputPort> mPluginOPorts = mPlugin.getOutputPorts();
			for (final MIOutputPort mPluginOPort : mPluginOPorts) {
				final String outputPortName = mPluginOPort.getName();
				final AbstractPlugin srcPlugin = pluginMap.get(mPlugin);
				// Get all ports which should be subscribed to this port.
				final EList<MIInputPort> mSubscribers = mPluginOPort.getSubscribers();
				for (final MIInputPort mSubscriber : mSubscribers) {
					// Find the mapping and subscribe
					final String inputPortName = mSubscriber.getName();
					final AbstractPlugin dstPlugin = pluginMap.get(mSubscriber.getParent());
					this.connect(srcPlugin, outputPortName, dstPlugin, inputPortName);
				}
			}
=======
		for (final RepositoryConnection connection : repositoryConnections) {
			this.connect(connection.getSource(), connection.getOutputName(), connection.getRepository());



















>>>>>>> .theirs
		}

		// Remember the mapping!
		this.pluginModelMap = pluginMap;
		this.repositoryModelMap = repositoryMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void saveToFile(final File file) throws IOException, AnalysisConfigurationException {
		final MIProject mProject = this.getCurrentConfiguration();
		AnalysisController.saveToFile(file, mProject);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void saveToFile(final String pathname) throws IOException, AnalysisConfigurationException {
		this.saveToFile(new File(pathname));
	}

	/**
	 * {@inheritDoc}
	 */
	public final void connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst, final String inputPortName)
			throws IllegalStateException, AnalysisConfigurationException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to connect readers and filters after starting analysis.");
		}
		if ((src == null) || (dst == null) || (inputPortName == null) || (outputPortName == null)) {
			throw new AnalysisConfigurationException("Unable to connect null values.");
		}
		// check whether dst is a reader
		if (dst instanceof IReaderPlugin) {
			throw new AnalysisConfigurationException("The plugin '" + dst.getName() + "' (" + dst.getPluginName() + ") is a reader and can not be connected to.");
		}
		// Make sure that the plugins are registered. */
		if (!(this.filters.contains(src) || this.readers.contains(src))) {
			throw new AnalysisConfigurationException("The plugin '" + src.getName() + "' (" + src.getPluginName() + ") is not registered.");
		}
		if (!this.filters.contains(dst)) {
			throw new AnalysisConfigurationException("The plugin '" + dst.getName() + "' (" + dst.getPluginName() + ") is not registered.");
		}
		// Use the method of AbstractPlugin (This should be the only allowed call to this method) to check the connection.
		AbstractPlugin.connect(src, outputPortName, dst, inputPortName); // throws AnalysisConfigurationException
	}

	/**
	 * {@inheritDoc}
	 */
	public final void connect(final AbstractPlugin plugin, final String repositoryPort, final AbstractRepository repository)
			throws IllegalStateException, AnalysisConfigurationException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to connect repositories after starting analysis.");
		}
		// Make sure that the plugin is registered.
		if (!(this.filters.contains(plugin) || this.readers.contains(plugin))) {
			throw new AnalysisConfigurationException("The plugin '" + plugin.getName() + "' (" + plugin.getPluginName() + ") is not registered.");
		}
		// Make sure that the repository is registered.
		if (!this.repos.contains(repository)) {
			throw new AnalysisConfigurationException("The repository '" + repository.getName() + "' (" + repository.getRepositoryName() + ") is not registered.");
		}
		// Use the method of AbstractPlugin (This should be the only allowed call to this method) to check the connections.
		plugin.connect(repositoryPort, repository); // throws AnalysisConfigurationException
	}

	/**
	 * {@inheritDoc}
	 */
	public final MIProject getCurrentConfiguration() throws AnalysisConfigurationException {
<<<<<<< .mine
		try {
			// Create a factory to create all other model instances.
			final MAnalysisMetaModelFactory factory = new MAnalysisMetaModelFactory();
			final MIProject mProject = factory.createProject();
			mProject.setName(this.projectName);
			final Map<AbstractPlugin, MIPlugin> pluginMap = new HashMap<AbstractPlugin, MIPlugin>(); // NOPMD (no concurrent access)
			final Map<AbstractRepository, MIRepository> repositoryMap = new HashMap<AbstractRepository, MIRepository>(); // NOPMD (no concurrent access)
			// Store the libraries.
			mProject.getDependencies().addAll(this.dependencies);
			// Run through all repositories and create the model-counterparts.
			for (final AbstractRepository repo : this.repos) {
				final MIRepository mRepo = factory.createRepository();
				mRepo.setClassname(repo.getClass().getName());
				mRepo.getProperties().addAll(AnalysisController.convertProperties(repo.getCurrentConfiguration(), factory));
				mProject.getRepositories().add(mRepo);
				repositoryMap.put(repo, mRepo);
			}
			// Run through all plugins and create the model-counterparts.
			final List<AbstractPlugin> plugins = new ArrayList<AbstractPlugin>(this.readers);
			for (final AbstractFilterPlugin filter : this.filters) {
				plugins.add(filter);
			}
			for (final AbstractPlugin plugin : plugins) {
				MIPlugin mPlugin;
				if (plugin instanceof AbstractReaderPlugin) {
					mPlugin = factory.createReader();
				} else {
					mPlugin = factory.createFilter();
				}
				// Remember the mapping.
				pluginMap.put(plugin, mPlugin);
				mPlugin.setClassname(plugin.getClass().getName());
				mPlugin.setName(plugin.getName());
				mPlugin.getProperties().addAll(AnalysisController.convertProperties(plugin.getCurrentConfiguration(), factory));

				// Add additional properties containing the asynchronous ports
				final MIProperty propAsyncInputPorts = factory.createProperty();
				propAsyncInputPorts.setName(AbstractPlugin.CONFIG_ASYNC_INPUT_PORTS);
				propAsyncInputPorts.setValue(Configuration.toProperty(plugin.getAllAsynchronousInputPorts()));
				final MIProperty propAsyncOutputPorts = factory.createProperty();
				propAsyncOutputPorts.setName(AbstractPlugin.CONFIG_ASYNC_OUTPUT_PORTS);
				propAsyncOutputPorts.setValue(Configuration.toProperty(plugin.getAllAsynchronousOutputPorts()));
				mPlugin.getProperties().add(propAsyncOutputPorts);
				mPlugin.getProperties().add(propAsyncInputPorts);

				// Extract the repositories.
				for (final Entry<String, AbstractRepository> repoEntry : plugin.getCurrentRepositories().entrySet()) {
					// Try to find the repository within our map.
					final AbstractRepository repository = repoEntry.getValue();
					final MIRepository mRepository = repositoryMap.get(repository);
					// If it doesn't exist, we have a problem...
					if (mRepository == null) {
						throw new AnalysisConfigurationException("Repository '" + repository.getName() + "' (" + repository.getRepositoryName()
								+ ") not contained in project. Maybe the repository has not been registered.");
					}
					// Now the connector.
					// final MIRepositoryConnector mRepositoryConn = factory.createRepositoryConnector();
					// mRepositoryConn.setName(repoEntry.getKey());
					// mRepositoryConn.setRepository(mRepository);
					// mPlugin.getRepositories().add(mRepositoryConn);
				}
				// Create the ports.
				final String[] outs = plugin.getAllOutputPortNames();
				for (final String out : outs) {
					final MIOutputPort mOutputPort = factory.createOutputPort();
					mOutputPort.setName(out);
					mPlugin.getOutputPorts().add(mOutputPort);
				}
				final String[] ins = plugin.getAllInputPortNames();
				for (final String in : ins) {
					final MIInputPort mInputPort = factory.createInputPort();
					mInputPort.setName(in);
					((MIFilter) mPlugin).getInputPorts().add(mInputPort);
				}
				mProject.getPlugins().add(mPlugin);
			}
			// Now connect the plugins.
			for (final IPlugin plugin : plugins) {
				final MIPlugin mOutputPlugin = pluginMap.get(plugin);
				// Check all output ports of the original plugin.
				for (final String outputPortName : plugin.getAllOutputPortNames()) {
					// Get the corresponding port of the model counterpart and get also the plugins which are currently connected with the original plugin.
					final EList<MIInputPort> subscribers = AnalysisController.findOutputPort(mOutputPlugin, outputPortName).getSubscribers();
					// Run through all connected plugins.
					for (final PluginInputPortReference subscriber : plugin.getConnectedPlugins(outputPortName)) {
						final IPlugin subscriberPlugin = subscriber.getPlugin();
						final MIPlugin mSubscriberPlugin = pluginMap.get(subscriberPlugin);
						// If it doesn't exist, we have a problem...
						if (mSubscriberPlugin == null) {
							throw new AnalysisConfigurationException("Plugin '" + subscriberPlugin.getName() + "' (" + subscriberPlugin.getPluginName()
									+ ") not contained in project. Maybe the plugin has not been registered.");
						}
						final MIInputPort mInputPort = AnalysisController.findInputPort((MIFilter) mSubscriberPlugin, subscriber.getInputPortName());
						subscribers.add(mInputPort);
					}
				}
			}

			// Now put our global configuration into the model instance
			final Set<Entry<Object, Object>> properties = this.globalConfiguration.entrySet();
			for (final Entry<Object, Object> property : properties) {
				final MIProperty mProperty = factory.createProperty();
				mProperty.setName((String) property.getKey());
				mProperty.setValue((String) property.getValue());

				mProject.getProperties().add(mProperty);
			}

			// We are finished. Return the finished project.
			return mProject;
		} catch (final Exception ex) { // NOPMD NOCS (catch any remaining problems)
			throw new AnalysisConfigurationException("Failed to retrieve current configuration of AnalysisCopntroller.", ex);
		}
=======
		return MetaModelHandler.javaToMetaModel(this.readers, this.filters, this.repos, this.dependencies, this.projectName, this.globalConfiguration);
















































































































>>>>>>> .theirs
	}

	/**
	 * {@inheritDoc}
	 */
	public final void run() throws IllegalStateException, AnalysisConfigurationException {
		synchronized (this) {
			if (this.state != STATE.READY) {
				throw new IllegalStateException("AnalysisController may be executed only once.");
			}
			this.state = STATE.RUNNING;
			this.notifyStateObservers();
		}
		// Make sure that a log reader exists.
		if (this.readers.size() == 0) {
			this.terminate(true);
			throw new AnalysisConfigurationException("No log reader registered.");
		}
		// Call init() method of all plug-ins.
		for (final AbstractReaderPlugin reader : this.readers) {
			// Make also sure that all repository ports of all plugins are connected.
			if (!reader.areAllRepositoryPortsConnected()) {
				this.terminate(true);
				throw new AnalysisConfigurationException("Reader '" + reader.getName() + "' (" + reader.getPluginName() + ") has unconnected repositories.");
			}
			reader.startInitializationSequence();
			if (!reader.start()) {
				this.terminate(true);
				throw new AnalysisConfigurationException("Reader '" + reader.getName() + "' (" + reader.getPluginName() + ") failed to initialize.");
			}
		}

		for (final AbstractFilterPlugin filter : this.filters) {
			// Make also sure that all repository ports of all plugins are connected.
			if (!filter.areAllRepositoryPortsConnected()) {
				this.terminate(true);
				throw new AnalysisConfigurationException("Plugin '" + filter.getName() + "' (" + filter.getPluginName() + ") has unconnected repositories.");
			}
			if (!filter.start()) {
				this.terminate(true);
				throw new AnalysisConfigurationException("Plugin '" + filter.getName() + "' (" + filter.getPluginName() + ") failed to initialize.");
			}
		}
		// Start reading
		final CountDownLatch readerLatch = new CountDownLatch(this.readers.size());
		this.filterLatch = new CountDownLatch(this.filters.size());
		for (final AbstractReaderPlugin reader : this.readers) {
			new Thread(new Runnable() {
				public void run() {
					try {
						if (!reader.read()) {
							// here we started and won't throw any exceptions!
							LOG.error("Calling read() on Reader '" + reader.getName() + "' (" + reader.getPluginName() + ")  returned false.");
							AnalysisController.this.terminate(true);
						}
					} catch (final Throwable t) { // NOPMD NOCS (we also want errors)
						LOG.error("Exception while reading on Reader '" + reader.getName() + "' (" + reader.getPluginName() + ").", t);
						AnalysisController.this.terminate(true);
					} finally {
						readerLatch.countDown();
					}
				}
			}).start();
		}
		// wait until all threads are finished
		try {
			this.initializationLatch.countDown();
			readerLatch.await();
		} catch (final InterruptedException ex) {
			LOG.warn("Interrupted while waiting for readers to finish", ex);
		}

		this.terminate();

		// wait for the remaining filters
		try {
			this.filterLatch.await();
		} catch (final InterruptedException ex) {
			LOG.warn("Interrupted while waiting for filters to finish", ex);
		}

		LOG.info("Analysis terminated");
	}

	public final void notifyFilterTermination(final AbstractPlugin plugin) {
		if (plugin instanceof AbstractFilterPlugin) {
			this.filterLatch.countDown();
		}
	}

	/**
	 * Causes the calling thread to wait until the analysis controller has been initialized.
	 */
	protected final void awaitInitialization() {
		try {
			this.initializationLatch.await();
		} catch (final InterruptedException ex) {
			LOG.warn("Interrupted while waiting for initialization of analysis controller.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void terminate() {
		this.terminate(false);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void terminate(final boolean error) {
		try {
			synchronized (this) {
				if (this.state != STATE.RUNNING) {
					return;
				}
				this.state = STATE.TERMINATING;
			}
			if (error) {
				LOG.info("Error during analysis. Terminating ...");
			} else {
				LOG.info("Terminating analysis.");
			}
			for (final AbstractReaderPlugin reader : this.readers) {
				reader.shutdown(error);
			}
			for (final AbstractFilterPlugin filter : this.filters) {
				filter.shutdown(error);
			}
			if (error) {
				this.state = STATE.FAILED;
			} else {
				this.state = STATE.TERMINATED;
			}
		} catch (final Throwable t) { // NOPMD NOCS (Catch errors and exceptions)
			// Make sure that neither an exception nor an error can crash the application
			// Even if the logging or the notify method fails, we have a correct state now!
			this.state = STATE.FAILED;
			LOG.error("Error during shutdown.", t);
		} finally {
			this.notifyStateObservers();
		}
<<<<<<< .mine
		for (final AbstractReaderPlugin reader : this.readers) {
			reader.startTerminationSequence(error);
		}
=======



>>>>>>> .theirs
	}

	/**
	 * Registers the given reader with this analysis instance. <b>This method is for internal use only!</b>
	 * 
	 * @param reader
	 *            The reader to register with this analysis.
	 * 
	 * @throws IllegalStateException
	 *             If the analysis has already been started when this method is called.
	 */
	public final void registerReader(final AbstractReaderPlugin reader) throws IllegalStateException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to register filter after starting analysis.");
		}
		if (this.readers.contains(reader)) {
			LOG.warn("Reader " + reader.getName() + " already registered.");
			return;
		}
		this.readers.add(reader);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Registered reader " + reader);
		}
	}

	/**
	 * Registers the given filter with this analysis instance. <b>This method is for internal use only!</b>
	 * 
	 * @param filter
	 *            The filter to register with this analysis.
	 * 
	 * @throws IllegalStateException
	 *             If the analysis has already been started when this method is called.
	 */
	public final void registerFilter(final AbstractFilterPlugin filter) throws IllegalStateException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to register filter after starting analysis.");
		}
		if (this.filters.contains(filter)) {
			LOG.warn("Filter '" + filter.getName() + "' (" + filter.getPluginName() + ") already registered.");
			return;
		}
		this.filters.add(filter);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Registered plugin " + filter);
		}
	}

	/**
	 * Registers the given repository with this analysis instance. <b>This method is for internal use only!</b>
	 * 
	 * @param repository
	 *            The repository to register with this analysis.
	 * 
	 * @throws IllegalStateException
	 *             If the analysis has already been started when this method is called.
	 */
	public final void registerRepository(final AbstractRepository repository) throws IllegalStateException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to register respository after starting analysis.");
		}
		if (this.repos.contains(repository)) {
			LOG.warn("Repository '" + repository.getName() + "' (" + repository.getRepositoryName() + ") already registered.");
			return;
		}
		this.repos.add(repository);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Registered Repository '" + repository.getName() + "' (" + repository.getRepositoryName() + ")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getProjectName() {
		return this.projectName;
	}

	/**
	 * {@inheritDoc}
	 */
	public final Collection<AbstractReaderPlugin> getReaders() {
		return Collections.unmodifiableCollection(this.readers);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Collection<AbstractFilterPlugin> getFilters() {
		return Collections.unmodifiableCollection(this.filters);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Collection<AbstractRepository> getRepositories() {
		return Collections.unmodifiableCollection(this.repos);
	}

	/**
	 * {@inheritDoc}
	 */
	public final STATE getState() {
		return this.state;
	}

	/**
	 * This method can be used to load a meta model instance from a given file.
	 * 
	 * @param file
	 *            The file to be loaded.
	 * @return An instance of <code>MIProject</code> if everything went well.
	 * @throws IOException
	 *             If something during loading went wrong.
	 */
	public static final MIProject loadFromFile(final File file) throws IOException {
		try {
			return MetaModelHandler.loadProjectFromFile(file);
		} catch (final IOException ex) {
			final IOException newEx = new IOException("Error loading file '" + file.getAbsolutePath() + "'.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (cause is set)
		} catch (final Exception ex) { // NOPMD NOCS (illegal catch)
			// Some exceptions like the XMIException can be thrown during loading although it cannot be seen. Catch this situation.
			final IOException newEx = new IOException("The given file '" + file.getAbsolutePath() + "' is not a valid kax-configuration file.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (cause is set)
		}
	}

	/**
	 * This method can be used to save the given instance of <code>MIProject</code> within a given file.
	 * 
	 * @param file
	 *            The file to be used for the storage.
	 * @param project
	 *            The project to be stored.
	 * @throws IOException
	 *             In case of errors.
	 */
	public static final void saveToFile(final File file, final MIProject project) throws IOException {
		try {
			MetaModelHandler.saveProjectToFile(file, project);
		} catch (final IOException ex) {
			final IOException newEx = new IOException("Unable to save configuration file '" + file.getAbsolutePath() + "'.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (cause is set)
		}
	}

	/**
	 * This is a factory method which can be used to create a new instance of {@link AnalysisController}, but delivers the mapping between the {@link MIProject} and
	 * the actual analysis. It calls the constructor {@code AnalysisController(MIProject, ClassLoader)}.
	 * 
	 * @param project
	 *            The project to be loaded.
	 * @param classLoader
	 *            The class loader used to load the instances.
	 * @return The newly created controller and the mapping.
	 * @throws NullPointerException
	 *             If the project is null.
	 * @throws AnalysisConfigurationException
	 *             If the given project could not be loaded.
	 */
	public static final AnalysisControllerWithMapping createAnalysisController(final MIProject project, final ClassLoader classLoader) throws NullPointerException,
			AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController(project, classLoader);

		return new AnalysisControllerWithMapping(controller, controller.pluginModelMap, controller.repositoryModelMap);
	}

	/**
	 * This is a wrapper for the {@link AnalysisController} which contains a mapping between the model instances and the actual objects as well. This is necessary if
	 * one wants to create an analysis based on an instance of {@link MIProject} and needs to map from the model instances to the actual created objects.
	 * 
	 * @author Andre van Hoorn, Nils Christian Ehmke, Jan Waller
	 * 
	 * @since 1.6
	 */
	public static final class AnalysisControllerWithMapping {

		private final Map<MIPlugin, AbstractPlugin> pluginMap;
		private final Map<MIRepository, AbstractRepository> repositoryMap;
		private final AnalysisController controller;

		/**
		 * Creates a new instance of this class using the given parameters.
		 * 
		 * @param controller
		 *            The analysis controller to be stored in this container.
		 * @param pluginMap
		 *            The mapping between actual plugins and their model counterparts.
		 * @param repositoryMap
		 *            The mapping between actual repositories and their model counterparts.
		 */
		public AnalysisControllerWithMapping(final AnalysisController controller, final Map<MIPlugin, AbstractPlugin> pluginMap,
				final Map<MIRepository, AbstractRepository> repositoryMap) {
			this.controller = controller;
			this.pluginMap = pluginMap;
			this.repositoryMap = repositoryMap;
		}

		/**
		 * Getter for the property {@link AnalysisControllerWithMapping#pluginMap}.
		 * 
		 * @return The current value of the property.
		 */
		public Map<MIPlugin, AbstractPlugin> getPluginMap() {
			return this.pluginMap;
		}

		/**
		 * Getter for the property {@link AnalysisControllerWithMapping#repositoryMap}.
		 * 
		 * @return The current value of the property.
		 */
		public Map<MIRepository, AbstractRepository> getRepositoryMap() {
			return this.repositoryMap;
		}

		/**
		 * Getter for the property {@link AnalysisControllerWithMapping#controller}.
		 * 
		 * @return The current value of the property.
		 */
		public AnalysisController getController() {
			return this.controller;
		}

	}

	/**
	 * An enumeration used to describe the state of an {@link AnalysisController}.
	 * 
	 * @author Jan Waller
	 * 
	 * @since 1.5
	 */
	public static enum STATE {
		/**
		 * The controller has been initialized and is ready to be configured.
		 */
		READY,
		/**
		 * The analysis is currently running.
		 */
		RUNNING,
		/**
		 * The controller has initiated a termination.
		 */
		TERMINATING,
		/**
		 * The controller has been terminated without errors.
		 */
		TERMINATED,
		/**
		 * The analysis failed.
		 */
		FAILED,
	}

	/**
	 * This interface can be used for observers which want to get notified about state changes of an analysis controller.
	 * 
	 * @author Nils Christian Ehmke
	 * 
	 * @since 1.5
	 */
	public static interface IStateObserver {

		/**
		 * This method will be called for every update of the state.
		 * 
		 * @param controller
		 *            The controller which updated its state.
		 * @param state
		 *            The new state of the given controller.
		 * 
		 * @since 1.5
		 */
		public void update(final AnalysisController controller, final STATE state);
	}
}
