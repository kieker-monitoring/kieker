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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin;
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
import kieker.analysis.plugin.PluginInputPortReference;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

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
 * TODOs: - In the context of realizing a event driven architecture for the model synthesis layer, it makes sense to refactor the KiekerRecordConsumers to
 * KiekerRecordFilters. Consumers are only about how data goes in - but we also have now a concept what should happen if the data goes out: its again a publisher, to
 * which other filters or plugins can subscribe to.
 * 
 * @author Andre van Hoorn, Matthias Rohr
 */
public final class AnalysisController {
	private static final Log LOG = LogFactory.getLog(AnalysisController.class);

	private final String projectName;
	// private IMonitoringReader logReader;
	private final Collection<AbstractReaderPlugin> readers = new CopyOnWriteArrayList<AbstractReaderPlugin>();
	private final Collection<AbstractAnalysisPlugin> filters = new CopyOnWriteArrayList<AbstractAnalysisPlugin>();
	private final Collection<AbstractRepository> repos = new CopyOnWriteArrayList<AbstractRepository>();

	/** Will be count down after the analysis is set-up. */
	private final CountDownLatch initializationLatch = new CountDownLatch(1);

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
	 * @throws Exception
	 */
	public AnalysisController(final MIProject project) throws NullPointerException {
		if (project != null) {
			this.loadFromModelProject(project);
			this.projectName = project.getName();
		} else {
			throw new NullPointerException("Failed to load project.");
		}
	}

	/**
	 * This method can be used to load a meta model instance from a given file.
	 * 
	 * @param file
	 *            The file to be loaded.
	 * @return An instance of <code>MIProject</code> if everything went well, null otherwise.
	 */
	public static final MIProject loadFromFile(final File file) {
		final EList<EObject> content = AnalysisController.openModelFile(file);
		if ((content != null) && !content.isEmpty()) {
			// The first (and only) element should be the project.
			return (MIProject) content.get(0);
		}
		return null;
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
	 * This method can be used to load the configuration from a given meta model instance.
	 * 
	 * @param mproject
	 *            The instance to be used for configuration.
	 */
	private final void loadFromModelProject(final MIProject mproject) {
		/* Create the repositories. */
		final Map<MIRepository, AbstractRepository> repositoryMap = new HashMap<MIRepository, AbstractRepository>();
		for (final MIRepository mRepository : mproject.getRepositories()) {
			/* Extract the necessary informations to create the repository. */
			final Configuration configuration = AnalysisController.modelPropertiesToConfiguration(mRepository.getProperties());
			try {
				final AbstractRepository repository = AnalysisController.createAndInitialize(AbstractRepository.class, mRepository.getClassname(), configuration);
				// final Constructor<?> repositoryConstructor = Class.forName(mRepository.getClassname()).getConstructor(Configuration.class);
				// final AbstractRepository repository = (AbstractRepository) repositoryConstructor.newInstance(configuration.getPropertiesStartingWith(mRepository
				// .getClassname()));
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
			configuration.setProperty(pluginClassname + ".name", mPlugin.getName());
			/* Create the plugin and put it into our map. */
			try {
				// final Constructor<?> pluginConstructor = Class.forName(pluginClassname).getConstructor(Configuration.class);
				// final AbstractPlugin plugin = (AbstractPlugin) pluginConstructor.newInstance(configuration.getPropertiesStartingWith(pluginClassname));
				final AbstractPlugin plugin = AnalysisController.createAndInitialize(AbstractPlugin.class, pluginClassname, configuration);
				pluginMap.put(mPlugin, plugin);
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
			final EList<MIRepositoryConnector> mPluginRPorts = mPlugin.getRepositories();
			for (final MIRepositoryConnector mPluginRPort : mPluginRPorts) {
				pluginMap.get(mPlugin).connect(mPluginRPort.getName(), repositoryMap.get(mPluginRPort.getRepository()));
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
					AbstractPlugin.connect(srcPlugin, outputPortName, dstPlugin, inputPortName);
				}
			}
		}
	}

	/**
	 * Opens a given file which should contain an instance of the analysis meta
	 * model and delivers a list with the whole content.
	 * 
	 * @param file
	 *            The file to be opened.
	 * @return A list with the content of the file or null if the loading did
	 *         fail
	 */
	private final static EList<EObject> openModelFile(final File file) {
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
		final XMIResource resource = (XMIResource) resourceSet.getResource(URI.createFileURI(file.toString()), true);
		try {
			resource.load(Collections.EMPTY_MAP);
			return resource.getContents();
		} catch (final IOException ex) {
			AnalysisController.LOG.error("Could not open the given file.", ex);
			return null;
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
		return AnalysisController.saveProject(file, this.getCurrentConfiguration());
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
	private static boolean saveProject(final File file, final MIProject project) {
		/* Create a resource and put the given project into it. */
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
		resource.getContents().add(project);
		/* Now try to save the resource. */
		try {
			resource.save(null);
		} catch (final IOException ex) {
			AnalysisController.LOG.error("Unable to save configuration file '" + file.getAbsolutePath() + "'.", ex);
			return false;
		}
		return true;
	}

	/**
	 * This method delivers the current configuration of this instance as an instance of <code>MIProject</code>.
	 * 
	 * @return A filled meta model instance if everything went well, null otherwise.
	 */
	public MIProject getCurrentConfiguration() {
		try {
			/* Create a factory to create all other model instances. */
			final MAnalysisMetaModelFactory factory = new MAnalysisMetaModelFactory();
			final MIProject project = factory.createProject();
			project.setName(this.projectName);

			final Map<AbstractPlugin, MIPlugin> pluginMap = new HashMap<AbstractPlugin, MIPlugin>();
			final Map<AbstractRepository, MIRepository> repositoryMap = new HashMap<AbstractRepository, MIRepository>();

			/* Run through all repositories and create the model-counterparts. */
			final List<AbstractRepository> repos = new ArrayList<AbstractRepository>(this.repos);
			for (final AbstractRepository repo : repos) {
				final MIRepository mRepo = factory.createRepository();
				mRepo.setClassname(repo.getClass().getName());
				project.getRepositories().add(mRepo);
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
				final Set<Entry<Object, Object>> configSet = configuration.entrySet();
				for (final Entry<Object, Object> configEntry : configSet) {
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

				project.getPlugins().add(mPlugin);
			}

			/* Now connect the plugins. */
			for (final AbstractPlugin plugin : plugins) {
				final MIPlugin mOutputPlugin = pluginMap.get(plugin);
				/* Check all output ports of the original plugin. */
				for (final String outputPortName : plugin.getAllOutputPortNames()) {
					/* Get the corresponding port of the model counterpart and get also the plugins which are currently connected with the original plugin. */
					final EList<MIInputPort> subscribers = AnalysisController.findOutputPort(mOutputPlugin, outputPortName).getSubscribers();
					/* Run through all connected plugins. */
					for (final PluginInputPortReference subscriber : plugin.getConnectedPlugins(outputPortName)) {
						final AbstractPlugin subscriberPlugin = subscriber.getPlugin();
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
			return project;
		} catch (final Exception ex) {
			AnalysisController.LOG.error("Unable to save configuration.", ex);
			return null;
		}

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
	private static MIInputPort findInputPort(final MIAnalysisPlugin mPlugin, final String name) {
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
	private static MIOutputPort findOutputPort(final MIPlugin mPlugin, final String name) {
		for (final MIOutputPort port : mPlugin.getOutputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	/**
	 * Starts an {@link AnalysisController} instance and returns after the configured reader finished reading and all analysis plug-ins terminated; or immediately,
	 * if an error occurs.
	 * 
	 * @return true on success; false if an error occurred
	 */
	public boolean run() {
		boolean success = true;
		try {
			// Make sure that a log reader exists.
			if (this.readers.size() == 0) {
				AnalysisController.LOG.error("No log reader registered.");
				success = false;
			}
			// Call execute() method of all plug-ins.
			for (final AbstractAnalysisPlugin filter : this.filters) {
				if (!filter.execute()) {
					AnalysisController.LOG.error("A plug-in's execute message failed.");
					success = false;
				}
			}
			// Start reading
			if (success) {
				// notify threads waiting for the initialization to be done
				this.initializationLatch.countDown();
				for (final AbstractReaderPlugin reader : this.readers) {
					// TODO: Better would be asynchronously spawning a thread per reader.
					if (!reader.read()) {
						AnalysisController.LOG.error("Calling read() on Reader returned false.");
						success = false;
					}
				}
			}
		} catch (final Exception ex) { // NOCS // NOPMD
			AnalysisController.LOG.error("Error occurred", ex);
			success = false;
		} finally {
			// to make sure that all waiting threads are released
			this.initializationLatch.countDown();
			try {
				for (final AbstractAnalysisPlugin filter : this.filters) {
					filter.terminate(!success); // normal termination (w/o error)
				}
			} catch (final Exception ex) { // NOCS // NOPMD
				AnalysisController.LOG.error("Error during termination", ex);
			}
		}
		return success;
	}

	/**
	 * Initiates a termination of the analysis.
	 */
	public void terminate() {
		/*
		 * terminate the reader. After the reader has terminated, the run method() will terminate all plugins
		 */
		AnalysisController.LOG.info("Explicit termination of the analysis. Terminating the reader ...");
		for (final AbstractReaderPlugin reader : this.readers) {
			reader.terminate();
		}
	}

	/**
	 * Awaits until the controller finishes its initialization.
	 * 
	 * @return the initializationLatch
	 */
	protected final boolean awaitInitialization() {
		try {
			this.initializationLatch.await();
		} catch (final InterruptedException ex) {
			AnalysisController.LOG.error("Interrupted while waiting for AnalysisController to be initialized.", ex);
		}
		return true;
	}

	/**
	 * Registers a log reader used as the source for monitoring records.
	 * 
	 * @param reader
	 */
	public void registerReader(final AbstractReaderPlugin reader) {
		this.readers.add(reader);
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered reader " + reader);
		}
	}

	/**
	 * Registers the passed plug-in <i>filter<i>.
	 * 
	 * All plugins which have been registered before calling the <i>run</i>-method, will be started once the analysis is started.
	 */
	public void registerFilter(final AbstractAnalysisPlugin filter) {
		this.filters.add(filter);
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered plugin " + filter);
		}
	}

	/**
	 * Registers the passed repository <i>repository<i>.
	 */
	public void registerRepository(final AbstractRepository repository) {
		this.repos.add(repository);
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered Repository " + repository);
		}
	}

	public String getProjectName() {
		return this.projectName;
	}

	public Collection<AbstractReaderPlugin> getReaders() {
		return this.readers;
	}

	public Collection<AbstractAnalysisPlugin> getFilters() {
		return this.filters;
	}

	public Collection<AbstractRepository> getRepositories() {
		return this.repos;
	}

	@SuppressWarnings("unchecked")
	protected static final <C> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration) {
		C createdClass = null; // NOPMD
		try {
			final Class<?> clazz = Class.forName(classname);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(Configuration.class).newInstance(configuration.getPropertiesStartingWith(classname));
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
