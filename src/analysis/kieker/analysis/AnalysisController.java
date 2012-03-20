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

package kieker.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.AbstractReaderPlugin;
import kieker.analysis.plugin.IPlugin;
import kieker.analysis.plugin.PluginInputPortReference;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * 
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller
 */
public final class AnalysisController implements Runnable {
	private static final Log LOG = LogFactory.getLog(AnalysisController.class);

	private final String projectName;
	/**
	 * This list contains the dependencies of the project. Currently this field is only being used when loading an instance of the model.
	 */
	private final Collection<MIDependency> dependencies = new CopyOnWriteArrayList<MIDependency>();
	private final Collection<AbstractReaderPlugin> readers = new CopyOnWriteArrayList<AbstractReaderPlugin>();
	private final Collection<AbstractAnalysisPlugin> filters = new CopyOnWriteArrayList<AbstractAnalysisPlugin>();
	private final Collection<AbstractRepository> repos = new CopyOnWriteArrayList<AbstractRepository>();

	private final CountDownLatch initializationLatch = new CountDownLatch(1);

	private volatile STATE state = STATE.READY;

	public static enum STATE {
		READY,
		RUNNING,
		TERMINATED,
		FAILED,
	}

	/**
	 * Constructs an {@link AnalysisController} instance.
	 */
	public AnalysisController() {
		this.projectName = "AnalysisProject";
	}

	/**
	 * Constructs an {@link AnalysisController} instance.
	 */
	public AnalysisController(final String projectName) {
		this.projectName = projectName;
	}

	/**
	 * This constructors loads the model from the given file as a configuration and creates an instance of this class.
	 * The file should therefore be an instance of the analysis meta model.
	 * 
	 * @param file
	 *            The configuration file for the analysis.
	 * 
	 * @return A completely initialized instance of {@link AnalysisController}.
	 * 
	 * @throws NullPointerException
	 *             If something went wrong.
	 */
	public AnalysisController(final File file) throws NullPointerException {
		this(AnalysisController.loadFromFile(file));
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses the given instance of @link{Project} to construct the analysis.
	 * 
	 * @param project
	 *            The project instance for the analysis.
	 */
	public AnalysisController(final MIProject project) {
		if (project != null) {
			this.loadFromModelProject(project);
			this.projectName = project.getName();
		} else {
			this.projectName = "";
			AnalysisController.LOG.error("The project could not be loaded.");
		}
	}

	/**
	 * This method can be used to load the configuration from a given meta model instance.
	 * 
	 * @param mproject
	 *            The instance to be used for configuration.
	 */
	private final void loadFromModelProject(final MIProject mproject) {
		/* Remember the libraries. */
		this.dependencies.addAll(mproject.getDependencies());

		/* Create the repositories. */
		final Map<MIRepository, AbstractRepository> repositoryMap = new HashMap<MIRepository, AbstractRepository>();
		for (final MIRepository mRepository : mproject.getRepositories()) {
			/* Extract the necessary informations to create the repository. */
			final Configuration configuration = AnalysisController.modelPropertiesToConfiguration(mRepository.getProperties());
			try {
				final AbstractRepository repository = AnalysisController.createAndInitialize(AbstractRepository.class, mRepository.getClassname(), configuration);
				repositoryMap.put(mRepository, repository);
				this.registerRepository(repository);
			} catch (final Exception ex) {
				AnalysisController.LOG.error("Could not load repository: " + mRepository.getClassname(), ex);
				continue;
			}
		}
		/*
		 * We run through the project and collect all plugins. As we create an actual object for every plugin within the model, we have to remember the mapping
		 * between the plugins within the model and the actual objects we create.
		 */
		final EList<MIPlugin> mPlugins = mproject.getPlugins();
		/* Now run through all plugins. */
		final Map<MIPlugin, AbstractPlugin> pluginMap = new HashMap<MIPlugin, AbstractPlugin>();
		for (final MIPlugin mPlugin : mPlugins) {
			/* Extract the necessary informations to create the plugin. */
			final Configuration configuration = AnalysisController.modelPropertiesToConfiguration(mPlugin.getProperties());
			final String pluginClassname = mPlugin.getClassname();
			configuration.setProperty(AbstractPlugin.CONFIG_NAME, mPlugin.getName());
			/* Create the plugin and put it into our map. */
			try {
				final AbstractPlugin plugin = AnalysisController.createAndInitialize(AbstractPlugin.class, pluginClassname, configuration);
				pluginMap.put(mPlugin, plugin);
				/* Check the used configuration against the actual available config keys. */
				AnalysisController.checkConfiguration(plugin, configuration);
				/* Add the plugin to our controller instance. */
				if (plugin instanceof AbstractReaderPlugin) {
					this.registerReader((AbstractReaderPlugin) plugin);
				} else {
					this.registerFilter((AbstractAnalysisPlugin) plugin);
				}
			} catch (final Exception ex) {
				AnalysisController.LOG.error("Could not load plugin: " + mPlugin.getClassname(), ex);
				continue;
			}
		}

		/* Now we have all plugins. We can start to assemble the wiring. */
		for (final MIPlugin mPlugin : mPlugins) {
			/* Check whether the ports exist and log this if necessary. */
			AnalysisController.checkPorts(mPlugin, pluginMap.get(mPlugin));
			final EList<MIRepositoryConnector> mPluginRPorts = mPlugin.getRepositories();
			for (final MIRepositoryConnector mPluginRPort : mPluginRPorts) {
				this.connect(pluginMap.get(mPlugin), mPluginRPort.getName(), repositoryMap.get(mPluginRPort.getRepository()));
			}
			final EList<MIOutputPort> mPluginOPorts = mPlugin.getOutputPorts();
			for (final MIOutputPort mPluginOPort : mPluginOPorts) {
				final String outputPortName = mPluginOPort.getName();
				final AbstractPlugin srcPlugin = pluginMap.get(mPlugin);

				/* Get all ports which should be subscribed to this port. */
				final EList<MIInputPort> mSubscribers = mPluginOPort.getSubscribers();
				for (final MIInputPort mSubscriber : mSubscribers) {
					/* Find the mapping and subscribe */
					final String inputPortName = mSubscriber.getName();
					final AbstractPlugin dstPlugin = pluginMap.get(mSubscriber.getParent());
					this.connect(srcPlugin, outputPortName, dstPlugin, inputPortName);
				}
			}
		}
	}

	/**
	 * This method checks the ports of the given model plugin against the ports of the actual plugin. If there are ports which are in the model instance, but not in
	 * the "real" plugin, a warning is logged. This method should be called during the creation of an <i>AnalysisController</i> via a configuration file to find
	 * invalid (outdated) ports.
	 * 
	 * @param mPlugin
	 *            The model instance of the plugin.
	 * @param plugin
	 *            The corresponding "real" plugin.
	 */
	private static void checkPorts(final MIPlugin mPlugin, final AbstractPlugin plugin) {
		/* Get all ports. */
		final EList<MIOutputPort> mOutputPorts = mPlugin.getOutputPorts();
		final EList<MIInputPort> mInputPorts = (mPlugin instanceof MIAnalysisPlugin) ? ((MIAnalysisPlugin) mPlugin).getInputPorts() : new BasicEList<MIInputPort>();
		final String[] outputPorts = plugin.getAllOutputPortNames();
		final String[] inputPorts = plugin.getAllInputPortNames();

		/* Sort the arrays for binary search. */
		Arrays.sort(outputPorts);
		Arrays.sort(inputPorts);

		/* Check whether the ports of the model plugin exist. */
		for (final MIOutputPort mOutputPort : mOutputPorts) {
			if (Arrays.binarySearch(outputPorts, mOutputPort.getName()) < 0) {
				AnalysisController.LOG.warn("The output port '" + mOutputPort.getName() + "' of '" + mPlugin.getName() + "' does not exist.");
			}
		}
		for (final MIInputPort mInputPort : mInputPorts) {
			if (Arrays.binarySearch(inputPorts, mInputPort.getName()) < 0) {
				AnalysisController.LOG.warn("The input port '" + mInputPort.getName() + "' of '" + mPlugin.getName() + "' does not exist.");
			}
		}
	}

	/**
	 * This method uses the given configuration object and checks the used keys against the actual existing keys within the given plugin. If there are keys in the
	 * configuration object which are not used in the plugin, a warning is logged, but nothing happens. This method should be called during the creation of the
	 * plugins via a given configuration file to find outdated properties.
	 * 
	 * @param plugin
	 *            The plugin to be used for the check.
	 * @param configuration
	 *            The configuration to be checked for correctness.
	 */
	private static void checkConfiguration(final AbstractPlugin plugin, final Configuration configuration) {
		/* Get the actual configuration of the plugin (and therefore the real existing keys) */
		final Configuration actualConfiguration = plugin.getCurrentConfiguration();
		/* Run through all used keys in the given configuration. */
		final Enumeration<Object> keyEnum = configuration.keys();
		while (keyEnum.hasMoreElements()) {
			final String key = (String) keyEnum.nextElement();
			if (!actualConfiguration.containsKey(key) && !(key.equals(AbstractPlugin.CONFIG_NAME))) {
				/* Found an invalid key. */
				AnalysisController.LOG.warn("Invalid property of '" + plugin.getName() + "' found: '" + key + "'.");
			}
		}
	}

	/**
	 * This method can be used to store the current configuration of this analysis controller in a specified file. The file can later be used to initialize the
	 * analysis controller.
	 * 
	 * @param file
	 *            The file in which the configuration will be stored.
	 * @return true iff the configuration has been saved successfully.
	 */
	public final boolean saveToFile(final File file) {
		return AnalysisController.saveToFile(file, this.getCurrentConfiguration());
	}

	/**
	 * This method should be used to connect two plugins. The plugins have to be registered withis this controller instance.
	 * * @param src
	 * The source plugin.
	 * 
	 * @param outputPortName
	 *            The output port of the source plugin.
	 * @param dst
	 *            The destination plugin.
	 * @param inputPortName
	 *            The input port of the destination port.
	 * @return true if and only if both given plugins are valid, registered within this controller, the output and input ports exist and if they are compatible.
	 *         Furthermore the destination plugin must not be a reader.
	 */
	public boolean connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst, final String inputPortName) {
		if (this.state != STATE.READY) {
			AnalysisController.LOG.error("Unable to connect readers and filters after starting analysis.");
			return false;
		}
		// TODO Log different errors.
		/* Make sure that the plugins are registered and use the method of AbstractPlugin (This should be the only allowed call to this method). */
		return (this.filters.contains(src) || this.readers.contains(src)) && (this.filters.contains(dst) || this.readers.contains(dst))
				&& AbstractPlugin.connect(src, outputPortName, dst, inputPortName);
	}

	/**
	 * Connects the given repository to this plugin via the given name.
	 * 
	 * @param name
	 *            The name of the port to connect the repository.
	 * @param repo
	 *            The repository which should be used.
	 * @return true if and only if the repository-port is valid, the repository itself is compatible and the port is not used yet. Also the analysis must not run
	 *         yet.
	 */
	public boolean connect(final AbstractPlugin plugin, final String name, final AbstractRepository repo) {
		if (this.state != STATE.READY) {
			AnalysisController.LOG.error("Unable to connect repositories after starting analysis.");
			return false;
		}
		// TODO Log different errors.
		/* Make sure that the plugins are registered and use the method of AbstractPlugin (This should be the only allowed call to this method). */
		return (this.filters.contains(plugin) || this.readers.contains(plugin)) && plugin.connect(name, repo);
	}

	/**
	 * This method delivers the current configuration of this instance as an instance of <code>MIProject</code>.
	 * 
	 * @return A filled meta model instance if everything went well, null otherwise.
	 */
	public final MIProject getCurrentConfiguration() {
		try {
			/* Create a factory to create all other model instances. */
			final MAnalysisMetaModelFactory factory = new MAnalysisMetaModelFactory();
			final MIProject mProject = factory.createProject();
			mProject.setName(this.projectName);
			final Map<AbstractPlugin, MIPlugin> pluginMap = new HashMap<AbstractPlugin, MIPlugin>();
			final Map<AbstractRepository, MIRepository> repositoryMap = new HashMap<AbstractRepository, MIRepository>();

			/* Store the libraries. */
			mProject.getDependencies().addAll(this.dependencies);

			/* Run through all repositories and create the model-counterparts. */
			final List<AbstractRepository> repos = new ArrayList<AbstractRepository>(this.repos);
			for (final AbstractRepository repo : repos) {
				final MIRepository mRepo = factory.createRepository();
				mRepo.setClassname(repo.getClass().getName());
				mProject.getRepositories().add(mRepo);
				repositoryMap.put(repo, mRepo);
			}
			/* Run through all plugins and create the model-counterparts. */
			final List<AbstractPlugin> plugins = new ArrayList<AbstractPlugin>(this.filters);
			for (final AbstractReaderPlugin reader : this.readers) {
				plugins.add(reader);
			}
			for (final AbstractPlugin plugin : plugins) {
				MIPlugin mPlugin;
				if (plugin instanceof AbstractReaderPlugin) {
					mPlugin = factory.createReader();
				} else {
					mPlugin = factory.createAnalysisPlugin();
				}
				/* Remember the mapping. */
				pluginMap.put(plugin, mPlugin);
				mPlugin.setClassname(plugin.getClass().getName());
				mPlugin.setName(plugin.getName());
				/* Extract the configuration. */
				Configuration configuration = plugin.getCurrentConfiguration();
				if (null == configuration) { // should not happen, but better safe than sorry
					configuration = new Configuration();
				}
				final EList<MIProperty> properties = mPlugin.getProperties();
				for (final Entry<Object, Object> configEntry : configuration.entrySet()) {
					final MIProperty property = factory.createProperty();
					property.setName(configEntry.getKey().toString());
					property.setValue(configEntry.getValue().toString());
					properties.add(property);
				}
				/* Extract the repositories. */
				final Map<String, AbstractRepository> currRepositories = plugin.getCurrentRepositories();
				final Set<Entry<String, AbstractRepository>> repoSet = currRepositories.entrySet();
				for (final Entry<String, AbstractRepository> repoEntry : repoSet) {
					/* Try to find the repository within our map. */
					final MIRepository mRepository = repositoryMap.get(repoEntry.getValue());
					/* If it doesn't exist, we have a problem.. */
					if (mRepository == null) {
						AnalysisController.LOG.error("Repository not contained in project. Maybe the repository has not been registered.");
						return null;
					}
					/* Now the connector. */
					final MIRepositoryConnector mRepositoryConn = factory.createRepositoryConnector();
					mRepositoryConn.setName(repoEntry.getKey());
					mRepositoryConn.setRepository(mRepository);

					mPlugin.getRepositories().add(mRepositoryConn);
				}
				/* Create the ports. */
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
					((MIAnalysisPlugin) mPlugin).getInputPorts().add(mInputPort);
				}

				mProject.getPlugins().add(mPlugin);
			}
			/* Now connect the plugins. */
			for (final IPlugin plugin : plugins) {
				final MIPlugin mOutputPlugin = pluginMap.get(plugin);
				/* Check all output ports of the original plugin. */
				for (final String outputPortName : plugin.getAllOutputPortNames()) {
					/* Get the corresponding port of the model counterpart and get also the plugins which are currently connected with the original plugin. */
					final EList<MIInputPort> subscribers = AnalysisController.findOutputPort(mOutputPlugin, outputPortName).getSubscribers();
					/* Run through all connected plugins. */
					for (final PluginInputPortReference subscriber : plugin.getConnectedPlugins(outputPortName)) {
						final IPlugin subscriberPlugin = subscriber.getPlugin();
						final MIPlugin mSubscriberPlugin = pluginMap.get(subscriberPlugin);
						// TODO: It seems like mSubscriberPlugin can sometimes be null. Why?
						/* Now connect them. */
						if (mSubscriberPlugin != null) {
							final MIInputPort mInputPort = AnalysisController.findInputPort((MIAnalysisPlugin) mSubscriberPlugin, subscriber.getInputPortName());
							subscribers.add(mInputPort);
						}
					}
				}
			}

			/* We are finished. Return the finished project. */
			return mProject;
		} catch (final Exception ex) { // TODO. why the catch? Could anything be thrown?
			AnalysisController.LOG.error("Unable to save configuration.", ex);
			return null;
		}

	}

	/**
	 * Starts an {@link AnalysisController} instance and returns after the configured reader finished reading and all analysis plug-ins terminated; or immediately,
	 * if an error occurs.
	 * 
	 * @return true on success; false if an error occurred
	 */
	@Override
	public final void run() {
		synchronized (this) {
			if (this.state != STATE.READY) {
				AnalysisController.LOG.error("AnalysisController may be executed only once.");
				return;
			}
			this.state = STATE.RUNNING;
		}
		// Make sure that a log reader exists.
		if (this.readers.size() == 0) {
			AnalysisController.LOG.error("No log reader registered.");
			this.terminate(true);
			return;
		}

		// Call execute() method of all plug-ins.
		for (final AbstractAnalysisPlugin filter : this.filters) {
			/* Make also sure that all repository ports of all plugins are connected. */
			if (!filter.areAllRepositoryPortsConnected()) {
				AnalysisController.LOG.error("Plugin '" + filter.getName() + "' has unconnected repositories.");
				return;
			}
			if (!filter.init()) {
				AnalysisController.LOG.error("A plug-in's execute message failed.");
				this.terminate(true);
				return;
			}
		}
		// Start reading
		final CountDownLatch readerLatch = new CountDownLatch(this.readers.size());
		for (final AbstractReaderPlugin reader : this.readers) {
			/* Make also sure that all repository ports of all plugins are connected. */
			if (!reader.areAllRepositoryPortsConnected()) {
				AnalysisController.LOG.error("Reader '" + reader.getName() + "' has unconnected repositories.");
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (!reader.read()) {
						AnalysisController.LOG.error("Calling read() on Reader returned false.");
						AnalysisController.this.terminate(true);
					}
					readerLatch.countDown();
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
		this.terminate();
		return;
	}

	protected final void awaitInitialization() {
		try {
			this.initializationLatch.await();
		} catch (final InterruptedException ex) {
			AnalysisController.LOG.error("Interrupted while waiting for initilaizion of analysis controller.", ex);
		}
	}

	/**
	 * Initiates a termination of the analysis.
	 */
	public final void terminate() {
		this.terminate(false);
	}

	public final void terminate(final boolean error) {
		synchronized (this) {
			if (this.state != STATE.RUNNING) {
				return;
			}
			if (error) {
				AnalysisController.LOG.info("Error during analysis. Terminating ...");
				this.state = STATE.FAILED;
			} else {
				AnalysisController.LOG.info("Terminating analysis.");
				this.state = STATE.TERMINATED;
			}
		}
		for (final AbstractReaderPlugin reader : this.readers) {
			reader.terminate(error);
		}
		for (final AbstractAnalysisPlugin filter : this.filters) {
			filter.terminate(error);
		}
	}

	/**
	 * Registers a log reader used as the source for monitoring records.
	 * 
	 * @param reader
	 */
	public final void registerReader(final AbstractReaderPlugin reader) {
		if (this.state != STATE.READY) {
			AnalysisController.LOG.error("Unable to reader filter after starting analysis.");
			return;
		}
		synchronized (this) {
			if (this.readers.contains(reader)) {
				AnalysisController.LOG.warn("Readers " + reader.getName() + " already registered.");
				return;
			}
			this.readers.add(reader);
		}
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered reader " + reader);
		}
	}

	/**
	 * Registers the passed plug-in <i>filter<i>.
	 * 
	 * All plugins which have been registered before calling the <i>run</i>-method, will be started once the analysis is started.
	 */
	public final void registerFilter(final AbstractAnalysisPlugin filter) {
		if (this.state != STATE.READY) {
			AnalysisController.LOG.error("Unable to register filter after starting analysis.");
			return;
		}
		synchronized (this) {
			if (this.filters.contains(filter)) {
				AnalysisController.LOG.warn("Filter " + filter.getName() + " already registered.");
				return;
			}
			this.filters.add(filter);
		}
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered plugin " + filter);
		}
	}

	/**
	 * Registers the passed repository <i>repository<i>.
	 */
	public final void registerRepository(final AbstractRepository repository) {
		if (this.state != STATE.READY) {
			AnalysisController.LOG.error("Unable to register respository after starting analysis.");
			return;
		}

		synchronized (this) {
			if (this.repos.contains(repository)) {
				// TODO -> getName()
				AnalysisController.LOG.warn("Repository " + repository.toString() + " already registered.");
				return;
			}
			this.repos.add(repository);
		}

		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered Repository " + repository);
		}
	}

	public final String getProjectName() {
		return this.projectName;
	}

	public final Collection<AbstractReaderPlugin> getReaders() {
		return Collections.unmodifiableCollection(this.readers);
	}

	public final Collection<AbstractAnalysisPlugin> getFilters() {
		return Collections.unmodifiableCollection(this.filters);
	}

	public final Collection<AbstractRepository> getRepositories() {
		return Collections.unmodifiableCollection(this.repos);
	}

	public final STATE getState() {
		return this.state;
	}

	/**
	 * This method can be used to load a meta model instance from a given file.
	 * 
	 * @param file
	 *            The file to be loaded.
	 * @return An instance of <code>MIProject</code> if everything went well, null otherwise.
	 */
	public static final MIProject loadFromFile(final File file) {
		/* Create a resource set to work with. */
		final ResourceSet resourceSet = new ResourceSetImpl();
		/* Initialize the package information */
		MAnalysisMetaModelPackage.init();
		/* Set OPTION_RECORD_UNKNOWN_FEATURE prior to calling getResource. */
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new EcoreResourceFactoryImpl() {
					@Override
					public Resource createResource(final URI uri) {
						final XMIResourceImpl resource = (XMIResourceImpl) super.createResource(uri);
						resource.getDefaultLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
						return resource;
					}
				});
		/* Try to load the ressource. */
		try {
			final XMIResource resource = (XMIResource) resourceSet.getResource(URI.createFileURI(file.toString()), true);
			final EList<EObject> content;
			resource.load(Collections.EMPTY_MAP);
			content = resource.getContents();
			if (!content.isEmpty()) {
				// The first (and only) element should be the project.
				return (MIProject) content.get(0);
			} else {
				return null;
			}
		} catch (final IOException ex) {
			AnalysisController.LOG.error("Could not open the given file.", ex);
			return null;
		} catch (final Exception ex) {
			/* Some exceptions like the XMIException can be thrown during loading although it cannot be seen. Catch this situation. */
			AnalysisController.LOG.error("The given file is not a valid kax-configuration file.", ex);
			return null;
		}
	}

	/**
	 * This method can be used to save the given instance of <code>MIProject</code> within a given file.
	 * 
	 * @param file
	 *            The file to be used for the storage.
	 * @param project
	 *            The project to be stored.
	 * @return true iff the storage was succesful.
	 */
	public static final boolean saveToFile(final File file, final MIProject project) {
		/* Create a resource and put the given project into it. */
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
		resource.getContents().add(project);

		/* Make sure that the controller uses utf8 instead of ascii. */
		final HashMap<String, String> options = new HashMap<String, String>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");

		/* Now try to save the resource. */
		try {
			resource.save(options);
		} catch (final IOException ex) {
			AnalysisController.LOG.error("Unable to save configuration file '" + file.getAbsolutePath() + "'.", ex);
			return false;
		}
		return true;
	}

	/**
	 * This method can be used to convert a given list of <code>MIProperty</code> to a configuration object.
	 * 
	 * @param mProperties
	 *            The properties to be converted.
	 * @return A filled configuration object.
	 */
	private static final Configuration modelPropertiesToConfiguration(final EList<MIProperty> mProperties) {
		final Configuration configuration = new Configuration();
		/* Run through the properties and convert every single of them. */
		for (final MIProperty mProperty : mProperties) {
			configuration.setProperty(mProperty.getName(), mProperty.getValue());
		}
		return configuration;
	}

	/**
	 * Searches for an input port within the given plugin with the given name.
	 * 
	 * @param mPlugin
	 *            The plugin which will be searched through.
	 * @param name
	 *            The name of the searched input port.
	 * @return The searched port or null, if it is not available.
	 */
	private static final MIInputPort findInputPort(final MIAnalysisPlugin mPlugin, final String name) {
		for (final MIInputPort port : mPlugin.getInputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	/**
	 * Searches for an output port within the given plugin with the given name.
	 * 
	 * @param mPlugin
	 *            The plugin which will be searched through.
	 * @param name
	 *            The name of the searched output port.
	 * @return The searched port or null, if it is not available.
	 */
	private static final MIOutputPort findOutputPort(final MIPlugin mPlugin, final String name) {
		for (final MIOutputPort port : mPlugin.getOutputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected static final <C> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration) {
		C createdClass = null; // NOPMD
		try {
			final Class<?> clazz = Class.forName(classname);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(Configuration.class).newInstance(configuration);
			} else {
				AnalysisController.LOG.error("Class '" + classname + "' has to implement '" + c.getSimpleName() + "'"); // NOCS (MultipleStringLiteralsCheck)
			}
		} catch (final ClassNotFoundException ex) {
			AnalysisController.LOG.error(c.getSimpleName() + ": Class '" + classname + "' not found", ex); // NOCS (MultipleStringLiteralsCheck)
		} catch (final NoSuchMethodException ex) {
			AnalysisController.LOG.error(c.getSimpleName() + ": Class '" + classname // NOCS (MultipleStringLiteralsCheck)
					+ "' has to implement a (public) constructor that accepts a single Configuration", ex);
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			AnalysisController.LOG.error(c.getSimpleName() + ": Failed to load class for name '" + classname + "'", ex); // NOCS (MultipleStringLiteralsCheck)
		}
		return createdClass;
	}
}
