/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.model.MetaModelHandler;
import kieker.analysis.model.MetaModelHandler.PluginConnection;
import kieker.analysis.model.MetaModelHandler.RepositoryConnection;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.IReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.misc.KiekerMetadataRecord;

/**
 * The <code>AnalysisController</code> can be used to configure, control, save
 * and load an analysis instance. It is responsible for the life cycle of the
 * readers, filters and repositories.
 *
 * @author Andre van Hoorn, Matthias Rohr, Nils Christian Ehmke, Jan Waller
 *
 * @since 0.95a
 * @deprecated 1.15 can be removed when all filter and tools have been ported to
 *             TeeTime
 */
@Deprecated
@kieker.analysis.annotation.AnalysisController(configuration = {
	@Property(name = IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, defaultValue = "NANOSECONDS"),
	@Property(name = IProjectContext.CONFIG_PROPERTY_NAME_PROJECT_NAME, defaultValue = "AnalysisProject") })
public final class AnalysisController implements IAnalysisController { // NOPMD (really long class)

	static final Logger LOG = LoggerFactory.getLogger(AnalysisController.class); // NOPMD package for inner class

	private final String projectName;

	/**
	 * This list contains the dependencies of the project. Currently this field is
	 * only being used when loading an instance of the model.
	 */
	private final Collection<MIDependency> dependencies = new CopyOnWriteArrayList<>();
	/**
	 * This list contains all registered readers within this controller.
	 */
	private final Collection<AbstractReaderPlugin> readers = new CopyOnWriteArrayList<>();
	/**
	 * This list contains all registered filters within this controller.
	 */
	private final Collection<AbstractFilterPlugin> filters = new CopyOnWriteArrayList<>();
	/**
	 * This list contains all registered repositories within this controller.
	 */
	private final Collection<AbstractRepository> repos = new CopyOnWriteArrayList<>();
	/**
	 * This list contains all registered state observers within this controller.
	 */
	private final Collection<IStateObserver> stateObservers = new CopyOnWriteArrayList<>();

	private final CountDownLatch initializationLatch = new CountDownLatch(1);

	private final Set<String> registeredComponentNames = new CopyOnWriteArraySet<>();

	/**
	 * This map is used to store the mapping between a given instance of
	 * {@link MIProject} and an actual instantiation of an analysis. It is not
	 * modified after creation and should only be used by the factory method.
	 */
	private Map<MIPlugin, AbstractPlugin> pluginModelMap;
	/**
	 * This map is used to store the mapping between a given instance of
	 * {@link MIProject} and an actual instantiation of an analysis. It is not
	 * modified after creation and should only be used by the factory method.
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
	 * This constructors creates an {@link AnalysisController} instance, using the
	 * given file to load an analysis model. The given file should therefore be an
	 * instance of the analysis meta model.
	 *
	 * @param file
	 *            The configuration file for the analysis.
	 *
	 * @throws IOException
	 *             If the given file could not be loaded
	 *             or is not a valid kax-configuration
	 *             file.
	 * @throws AnalysisConfigurationException
	 *             If one or more plugins or repositories
	 *             could not be created, one or more
	 *             properties of the plugins are invalid
	 *             or if a connection between two filters
	 *             is not allowed.
	 */
	public AnalysisController(final File file) throws IOException, AnalysisConfigurationException {
		this(file, AnalysisController.class.getClassLoader());
	}

	/**
	 * This constructors creates an {@link AnalysisController} instance, using the
	 * given file to load an analysis model and the given classloader to initialize
	 * the objects. The given file should therefore be an instance of the analysis
	 * meta model.
	 *
	 * @param file
	 *            The configuration file for the analysis.
	 * @param classLoader
	 *            The classloader used to initialize the plugins etc.
	 *
	 * @throws IOException
	 *             If the given file could not be loaded
	 *             or is not a valid kax-configuration
	 *             file.
	 * @throws AnalysisConfigurationException
	 *             If one or more plugins or repositories
	 *             could not be created, one or more
	 *             properties of the plugins are invalid
	 *             or if a connection between two filters
	 *             is not allowed.
	 */
	public AnalysisController(final File file, final ClassLoader classLoader)
			throws IOException, AnalysisConfigurationException {
		this(AnalysisController.loadFromFile(file), classLoader);
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses the
	 * given instance of {@link MIProject} to construct the analysis.
	 *
	 * @param project
	 *            The project instance for the analysis.
	 * @throws AnalysisConfigurationException
	 *             If the given project could not be
	 *             loaded.
	 * @throws NullPointerException
	 *             If the project is null.
	 */
	public AnalysisController(final MIProject project) throws AnalysisConfigurationException {
		this(project, AnalysisController.class.getClassLoader());
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses the
	 * given instance of @link{Project} to construct the analysis.
	 *
	 * @param project
	 *            The project instance for the analysis.
	 * @param classLoader
	 *            The class loader used for the initializing.
	 * @throws NullPointerException
	 *             If the project is null.
	 * @throws AnalysisConfigurationException
	 *             If the given project could not be
	 *             loaded.
	 */
	public AnalysisController(final MIProject project, final ClassLoader classLoader)
			throws AnalysisConfigurationException {
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
	 *            The global configuration of this analysis. All plugins
	 *            can indirectly access it.
	 */
	public AnalysisController(final Configuration configuration) {
		this.globalConfiguration = this.validateConfiguration(configuration.flatten(this.getDefaultConfiguration()));
		this.projectName = this.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_PROJECT_NAME);
	}

	/**
	 * This simple helper method validates the configuration object.
	 *
	 * @param configuration
	 *            The configuration object to check and (perhaps) update.
	 * @return The configuration object.
	 */
	private Configuration validateConfiguration(final Configuration configuration) {
		final String stringProperty = configuration
				.getStringProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		try {
			TimeUnit.valueOf(stringProperty);
		} catch (final IllegalArgumentException ignore) {
			AnalysisController.LOG.warn("{} is no valid TimeUnit! Using NANOSECONDS instead.", stringProperty);
			configuration.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT,
					TimeUnit.NANOSECONDS.name());
		}
		return configuration;
	}

	/**
	 * This simple helper method create a new configuration object which is empty
	 * except for an entry containing the given project name.
	 *
	 * @param projectName
	 *            The project name to be stored in the new configuration
	 *            object.
	 * @return The configuration object.
	 */
	private static final Configuration createConfigurationWithProjectName(final String projectName) {
		final Configuration configuration = new Configuration();
		configuration.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_PROJECT_NAME, projectName);
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
		final kieker.analysis.annotation.AnalysisController annotation = this.getClass()
				.getAnnotation(kieker.analysis.annotation.AnalysisController.class);
		if (null != annotation) {
			// Run through the available properties and put them into our configuration
			for (final Property property : annotation.configuration()) {
				defaultConfiguration.setProperty(property.name(), property.defaultValue());
			}
		}
		return defaultConfiguration;
	}

	/**
	 * Called whenever an {@link KiekerMetadataRecord} is found inside the filters
	 * network.
	 *
	 * Currently this method only logs all details.
	 *
	 * @param record
	 *            the KiekerMetadataRecord containing the information
	 */
	public final void handleKiekerMetadataRecord(final KiekerMetadataRecord record) {
		AnalysisController.LOG.info(
				"Kieker metadata: version='{}', controllerName='{}', hostname='{}', "
						+ "experimentId='{}', debugMode='{}', timeOffset='{}', timeUnit='{}', numberOfRecords='{}'",
				record.getVersion(), record.getControllerName(), record.getHostname(), record.getExperimentId(),
				record.isDebugMode(), record.getTimeOffset(), record.getTimeUnit(), record.getNumberOfRecords());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void registerStateObserver(final IStateObserver stateObserver) {
		this.stateObservers.add(stateObserver);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void unregisterStateObserver(final IStateObserver stateObserver) {
		this.stateObservers.remove(stateObserver);
	}

	/**
	 * This method runs through all observers and informs them about the current
	 * state.
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
	@Override
	public final String getProperty(final String key) {
		return this.globalConfiguration.getStringProperty(key);
	}

	/**
	 * This method can be used to load the configuration from a given meta model
	 * instance.
	 *
	 * @param mProject
	 *            The instance to be used for configuration.
	 * @param classLoader
	 *            The instance of {@link java.lang.ClassLoader} used for
	 *            creating the necessary components.
	 * @throws AnalysisConfigurationException
	 *             If the given project represents
	 *             somehow an invalid configuration.
	 */
	private final void loadFromModelProject(final MIProject mProject, final ClassLoader classLoader)
			throws AnalysisConfigurationException {
		final Map<MIRepository, AbstractRepository> repositoryMap = new HashMap<>(); // NOPMD (no concurrent access)
		final Map<MIPlugin, AbstractPlugin> pluginMap = new HashMap<>(); // NOPMD (no concurrent access)
		final Collection<PluginConnection> pluginConnections = new ArrayList<>();
		final Collection<RepositoryConnection> repositoryConnections = new ArrayList<>();

		MetaModelHandler.metaModelToJava(mProject, this, pluginConnections, repositoryConnections, this.dependencies,
				classLoader, this.globalConfiguration, repositoryMap, pluginMap);

		for (final PluginConnection connection : pluginConnections) {
			this.connect(connection.getSource(), connection.getOutputName(), connection.getDestination(),
					connection.getInputName());
		}
		for (final RepositoryConnection connection : repositoryConnections) {
			this.connect(connection.getSource(), connection.getOutputName(), connection.getRepository());
		}

		// Remember the mapping!
		this.pluginModelMap = pluginMap;
		this.repositoryModelMap = repositoryMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void saveToFile(final File file) throws IOException, AnalysisConfigurationException {
		final MIProject mProject = this.getCurrentConfiguration();
		AnalysisController.saveToFile(file, mProject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void saveToFile(final String pathname) throws IOException, AnalysisConfigurationException {
		this.saveToFile(new File(pathname));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst,
			final String inputPortName) throws AnalysisConfigurationException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to connect readers and filters after starting analysis.");
		}
		if ((src == null) || (dst == null) || (inputPortName == null) || (outputPortName == null)) {
			throw new AnalysisConfigurationException("Unable to connect null values.");
		}
		// check whether dst is a reader
		if (dst instanceof IReaderPlugin) {
			throw new AnalysisConfigurationException("The plugin '" + dst.getName() + "' (" + dst.getPluginName()
					+ ") is a reader and can not be connected to.");
		}
		// Make sure that the plugins are registered. */
		if (!(this.filters.contains(src) || this.readers.contains(src))) {
			throw new AnalysisConfigurationException(
					"The plugin '" + src.getName() + "' (" + src.getPluginName() + ") is not registered.");
		}
		if (!this.filters.contains(dst)) {
			throw new AnalysisConfigurationException(
					"The plugin '" + dst.getName() + "' (" + dst.getPluginName() + ") is not registered.");
		}
		// Use the method of AbstractPlugin (This should be the only allowed call to
		// this method) to check the connection.
		AbstractPlugin.connect(src, outputPortName, dst, inputPortName); // throws AnalysisConfigurationException
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void connect(final AbstractPlugin plugin, final String repositoryPort,
			final AbstractRepository repository) throws AnalysisConfigurationException {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to connect repositories after starting analysis.");
		}
		if (repository == null) {
			throw new AnalysisConfigurationException(
					"Plugin '" + plugin.getName() + "' (" + plugin.getPluginName() + ") has unconnected repositories.");
		}
		// Make sure that the plugin is registered.
		if (!(this.filters.contains(plugin) || this.readers.contains(plugin))) {
			throw new AnalysisConfigurationException(
					"The plugin '" + plugin.getName() + "' (" + plugin.getPluginName() + ") is not registered.");
		}
		// Make sure that the repository is registered.
		if (!this.repos.contains(repository)) {
			throw new AnalysisConfigurationException("The repository '" + repository.getName() + "' ("
					+ repository.getRepositoryName() + ") is not registered.");
		}
		// Use the method of AbstractPlugin (This should be the only allowed call to
		// this method) to check the connections.
		plugin.connect(repositoryPort, repository); // throws AnalysisConfigurationException
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final MIProject getCurrentConfiguration() throws AnalysisConfigurationException {
		return MetaModelHandler.javaToMetaModel(this.readers, this.filters, this.repos, this.dependencies,
				this.projectName, this.globalConfiguration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void run() throws AnalysisConfigurationException {
		try {
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
					throw new AnalysisConfigurationException("Reader '" + reader.getName() + "' ("
							+ reader.getPluginName() + ") has unconnected repositories.");
				}
				if (!reader.start()) {
					this.terminate(true);
					throw new AnalysisConfigurationException(
							"Reader '" + reader.getName() + "' (" + reader.getPluginName() + ") failed to initialize.");
				}
			}
			for (final AbstractFilterPlugin filter : this.filters) {
				// Make also sure that all repository ports of all plugins are connected.
				if (!filter.areAllRepositoryPortsConnected()) {
					this.terminate(true);
					throw new AnalysisConfigurationException("Plugin '" + filter.getName() + "' ("
							+ filter.getPluginName() + ") has unconnected repositories.");
				}
				if (!filter.start()) {
					this.terminate(true);
					throw new AnalysisConfigurationException(
							"Plugin '" + filter.getName() + "' (" + filter.getPluginName() + ") failed to initialize.");
				}
			}
			// Start reading
			final CountDownLatch readerLatch = new CountDownLatch(this.readers.size());
			for (final AbstractReaderPlugin reader : this.readers) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (!reader.read()) {
								// here we started and won't throw any exceptions!
								AnalysisController.LOG.error("Calling read() on Reader '{}' ({})  returned false.",
										reader.getName(), reader.getPluginName());
								AnalysisController.this.terminate(true);
							}
						} catch (final Throwable t) { // NOPMD NOCS (we also want errors)
							AnalysisController.LOG.error("Exception while reading on Reader '{}' ({}).",
									reader.getName(), reader.getPluginName(), t);
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
				AnalysisController.LOG.warn("Interrupted while waiting for readers to finish", ex);
			}
		} finally {
			this.initializationLatch.countDown(); // just to make sure
			this.terminate();
		}
	}

	/**
	 * Causes the calling thread to wait until the analysis controller has been
	 * initialized.
	 */
	public final void awaitInitialization() {
		try {
			this.initializationLatch.await();
		} catch (final InterruptedException ex) {
			AnalysisController.LOG.warn("Interrupted while waiting for initialization of analysis controller.", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void terminate() {
		this.terminate(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void terminate(final boolean error) {
		try {
			synchronized (this) {
				if (this.state != STATE.RUNNING) {
					return;
				}
				this.state = STATE.TERMINATING;
			}
			if (error) {
				AnalysisController.LOG.info("Error during analysis. Terminating ...");
			} else {
				AnalysisController.LOG.info("Terminating analysis.");
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
			AnalysisController.LOG.error("Error during shutdown.", t);
		} finally {
			this.notifyStateObservers();
		}
	}

	/**
	 * Registers the given reader with this analysis instance. <b>This method is for
	 * internal use only!</b>
	 *
	 * @param reader
	 *            The reader to register with this analysis.
	 *
	 * @throws IllegalStateException
	 *             If the analysis has already been started when
	 *             this method is called.
	 */
	public final void registerReader(final AbstractReaderPlugin reader) {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to register filter after starting analysis.");
		}
		if (this.readers.contains(reader)) {
			AnalysisController.LOG.warn("Reader {} already registered.", reader.getName());
			return;
		}
		this.readers.add(reader);
		AnalysisController.LOG.debug("Registered reader {}", reader);
	}

	/**
	 * Registers the given filter with this analysis instance. <b>This method is for
	 * internal use only!</b>
	 *
	 * @param filter
	 *            The filter to register with this analysis.
	 *
	 * @throws IllegalStateException
	 *             If the analysis has already been started when
	 *             this method is called.
	 */
	public final void registerFilter(final AbstractFilterPlugin filter) {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to register filter after starting analysis.");
		}
		if (this.filters.contains(filter)) {
			AnalysisController.LOG.warn("Filter '{}' ({}) already registered.", filter.getName(),
					filter.getPluginName());
			return;
		}
		this.filters.add(filter);
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered plugin " + filter);
		}
	}

	/**
	 * Registers the given repository with this analysis instance. <b>This method is
	 * for internal use only!</b>
	 *
	 * @param repository
	 *            The repository to register with this analysis.
	 *
	 * @throws IllegalStateException
	 *             If the analysis has already been started when
	 *             this method is called.
	 */
	public final void registerRepository(final AbstractRepository repository) {
		if (this.state != STATE.READY) {
			throw new IllegalStateException("Unable to register respository after starting analysis.");
		}
		if (this.repos.contains(repository)) {
			AnalysisController.LOG.warn("Repository '{}' ({}) already registered.", repository.getName(),
					repository.getRepositoryName());
			return;
		}
		this.repos.add(repository);
		AnalysisController.LOG.debug("Registered repository '{}' ({})", repository.getName(),
				repository.getRepositoryName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getProjectName() {
		return this.projectName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Collection<AbstractReaderPlugin> getReaders() {
		return Collections.unmodifiableCollection(this.readers);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Collection<AbstractFilterPlugin> getFilters() {
		return Collections.unmodifiableCollection(this.filters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Collection<AbstractRepository> getRepositories() {
		return Collections.unmodifiableCollection(this.repos);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
			// Some exceptions like the XMIException can be thrown during loading although
			// it cannot be seen. Catch this situation.
			final IOException newEx = new IOException(
					"The given file '" + file.getAbsolutePath() + "' is not a valid kax-configuration file.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (cause is set)
		}
	}

	/**
	 * This method can be used to save the given instance of <code>MIProject</code>
	 * within a given file.
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
			final IOException newEx = new IOException(
					"Unable to save configuration file '" + file.getAbsolutePath() + "'.");
			newEx.initCause(ex);
			throw newEx; // NOPMD (cause is set)
		}
	}

	/**
	 * This is a factory method which can be used to create a new instance of
	 * {@link AnalysisController}, but delivers the mapping between the
	 * {@link MIProject} and the actual analysis. It calls the constructor
	 * {@code AnalysisController(MIProject, ClassLoader)}.
	 *
	 * @param project
	 *            The project to be loaded.
	 * @param classLoader
	 *            The class loader used to load the instances.
	 * @return The newly created controller and the mapping.
	 * @throws NullPointerException
	 *             If the project is null.
	 * @throws AnalysisConfigurationException
	 *             If the given project could not be
	 *             loaded.
	 */
	public static final AnalysisControllerWithMapping createAnalysisController(final MIProject project,
			final ClassLoader classLoader) throws AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController(project, classLoader);

		return new AnalysisControllerWithMapping(controller, controller.pluginModelMap, controller.repositoryModelMap);
	}

	/**
	 * This method tries to atomically register the given name for a component.
	 *
	 * @param name
	 *            The component name to register
	 *
	 * @return true if and only if the given name is not already used and could now
	 *         be registered.
	 */
	public boolean tryRegisterComponentName(final String name) {
		return this.registeredComponentNames.add(name);
	}

	/**
	 * This is a wrapper for the {@link AnalysisController} which contains a mapping
	 * between the model instances and the actual objects as well. This is necessary
	 * if one wants to create an analysis based on an instance of {@link MIProject}
	 * and needs to map from the model instances to the actual created objects.
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
		 *            The mapping between actual plugins and their model
		 *            counterparts.
		 * @param repositoryMap
		 *            The mapping between actual repositories and their model
		 *            counterparts.
		 */
		public AnalysisControllerWithMapping(final AnalysisController controller,
				final Map<MIPlugin, AbstractPlugin> pluginMap,
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
	 * This interface can be used for observers which want to get notified about
	 * state changes of an analysis controller.
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
		void update(final AnalysisController controller, final STATE state);
	}

}
