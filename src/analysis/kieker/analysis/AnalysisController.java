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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.Pair;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.analysis.reader.IMonitoringReader;
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

	private IMonitoringReader logReader;
	private final Collection<AbstractAnalysisPlugin> plugins = new CopyOnWriteArrayList<AbstractAnalysisPlugin>();
	/** Will be count down after the analysis is set-up. */
	private final CountDownLatch initializationLatch = new CountDownLatch(1);

	/**
	 * Constructs an {@link AnalysisController} instance.
	 */
	public AnalysisController() {
		// do nothing
		// TODO: is this needed? Else we could move loadFromModelProject to constructor and make logReader final
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses the given instance of @link{Project} to construct the analysis.
	 * 
	 * @param project
	 *            The project instance for the analysis.
	 * @throws Exception
	 */
	public AnalysisController(final MIProject project) throws Exception {
		this.loadFromModelProject(project);
	}

	/**
	 * This constructors loads the model from the given file as a configuration and creates an instance of this class. The file should therefore be an instance of
	 * the analysis meta model.
	 * 
	 * @param file
	 *            The configuration file for the analysis.
	 * 
	 * @return A completely initialized instance of {@link AnalysisController}.
	 * 
	 * @throws Exception
	 *             If something went wrong.
	 */
	public AnalysisController(final File file) throws Exception {
		/* Try to load everything. */
		final EList<EObject> content = AnalysisController.openModelFile(file);
		if ((content != null) && !content.isEmpty()) {
			// The first (and only) element should be the project. Use it to configure this instance.
			final MIProject project = (MIProject) content.get(0);
			try {
				this.loadFromModelProject(project);
			} catch (final Exception ex) {
				AnalysisController.LOG.error("Could not load the configuration from the given file: " + file.getName(), ex);
				// TODO: Throw something more specific. Best would be either log or throw, thus throw a new specific exception with ex as cause instead of logging
				// TODO: perhaps it would be better, if we always return a correct AnalysisController, thus perhaps one with an empty Project.
				throw ex;
			}
		} else {
			// TODO: handle else!
		}
	}

	private final Configuration modelPropertiesToConfiguration(final EList<MIProperty> mProperties) {
		final Configuration configuration = new Configuration(null);
		/* Run through the properties and convert every single of them. */
		for (final MIProperty mProperty : mProperties) {
			configuration.setProperty(mProperty.getName(), mProperty.getValue());
		}
		return configuration;
	}

	private final void loadFromModelProject(final MIProject mproject) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		/* Get all repositories. */
		// final EList<IRepository> mRepositories = mproject.getRepositories();
		// TODO Create the repositories and use them, once this is possible.

		/*
		 * We run through the project and collect all plugins. As we create an actual object for every plugin within the model, we have to remember the mapping
		 * between the plugins within the model and the actual objects we create.
		 */
		final EList<MIPlugin> mPlugins = mproject.getPlugins();
		final Map<MIPlugin, AbstractPlugin> pluginMap = new HashMap<MIPlugin, AbstractPlugin>();

		/* Now run through all plugins. */
		for (final MIPlugin mPlugin : mPlugins) {
			/* Extract the necessary informations to create the plugin. */
			final Configuration configuration = this.modelPropertiesToConfiguration(mPlugin.getProperties());

			/* Create the plugin and put it into our map. */
			final Constructor<?> pluginConstructor = Class.forName(mPlugin.getClassname()).getConstructor(Configuration.class);
			final AbstractPlugin plugin = (AbstractPlugin) pluginConstructor.newInstance(configuration.getPropertiesStartingWith(mPlugin.getClassname()));
			pluginMap.put(mPlugin, plugin);

			/* Set the other properties of the plugin. */
			plugin.setName(mPlugin.getName());

			/* Add the plugin to our controller instance. */
			if (plugin instanceof AbstractReaderPlugin) {
				this.setReader((AbstractReaderPlugin) plugin);
			} else {
				this.registerPlugin((AbstractAnalysisPlugin) plugin);
			}
		}

		/* Now we have all plugins. We can start to assemble the wiring. */
		for (final MIPlugin mPlugin : mPlugins) {
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
		} catch (final IOException e) {
			AnalysisController.LOG.error("Could not open the given file.");
			return null;
		}
	}

	/**
	 * This method can be used to store the current configuration of this analysis controller in a specified file. The file can later be used to initialize the
	 * analysis controller.
	 * 
	 * @param file
	 *            The file in which the configuration will be stored.
	 * @param projectName
	 *            The name which is used for the new project.
	 * @return true iff the configuration has been saved successfully.
	 */
	public final boolean saveToFile(final File file, final String projectName) {
		final MIProject project = this.getCurrentConfiguration(projectName);
		final boolean success = this.saveProject(file, project);

		return success;

	}

	private boolean saveProject(final File file, final MIProject project) {
		/* Create a resource and put the given project into it. */
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
		resource.getContents().add(project);

		/* Now try to save the resource. */
		try {
			resource.save(null);
		} catch (final IOException e) {
			AnalysisController.LOG.error(String.format("Unable to save configuration file '%s'.", file.getAbsolutePath()));
			return false;
		}

		return true;
	}

	private MIProject getCurrentConfiguration(final String projectName) {
		try {
			/* Create a factory to create all other model instances. */
			final MAnalysisMetaModelFactory factory = new MAnalysisMetaModelFactory();
			final MIProject project = factory.createProject();
			final Map<AbstractPlugin, MIPlugin> pluginMap = new HashMap<AbstractPlugin, MIPlugin>();

			project.setName(projectName);

			/* Run through all plugins and create he model-counterparts. */
			final List<AbstractPlugin> plugins = new ArrayList<AbstractPlugin>(this.plugins);
			plugins.add((AbstractPlugin) this.logReader);

			for (final AbstractPlugin plugin : plugins) {
				final MIPlugin mPlugin = (plugin instanceof AbstractReaderPlugin) ? factory.createReader() : factory.createAnalysisPlugin();

				/* Remember the mapping. */
				pluginMap.put(plugin, mPlugin);

				mPlugin.setClassname(plugin.getClass().getName());
				mPlugin.setName(plugin.getName());

				/* Extract the configuration. */
				final Configuration configuration = plugin.getCurrentConfiguration();
				final Set<Entry<Object, Object>> configSet = configuration.entrySet();
				for (final Entry<Object, Object> configEntry : configSet) {
					final MIProperty property = factory.createProperty();
					property.setName(configEntry.getKey().toString());
					property.setValue(configEntry.getValue().toString());
					mPlugin.getProperties().add(property);
				}

				/* Create the ports. */
				final String outs[] = plugin.getAllOutputPortNames();
				for (final String out : outs) {
					final MIOutputPort mOutputPort = factory.createOutputPort();
					mOutputPort.setName(out);
					mPlugin.getOutputPorts().add(mOutputPort);
				}

				final String ins[] = plugin.getAllInputPortNames();
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
				final String outputPortNames[] = plugin.getAllOutputPortNames();

				/* Check all output ports of the original plugin. */
				for (final String outputPortName : outputPortNames) {
					/* Get the corresponding port of the model counterpart and get also the plugins which are currently connected with the original plugin. */
					final MIOutputPort mOutputPort = AnalysisController.findOutputPort(mOutputPlugin, outputPortName);
					final List<Pair<AbstractPlugin, String>> subscribers = plugin.getConnectedPlugins(outputPortName);

					/* Run through all connected plugins. */
					for (final Pair<AbstractPlugin, String> subscriber : subscribers) {
						final AbstractPlugin subscriberPlugin = subscriber.getFst();
						final MIPlugin mSubscriberPlugin = pluginMap.get(subscriberPlugin);
						// TODO: It seems like mSubscriberPlugin can sometimes be null. Why?
						/* Now connect them. */
						if (mSubscriberPlugin != null) {
							final MIInputPort mInputPort = AnalysisController.findInputPort((MIAnalysisPlugin) mSubscriberPlugin, subscriber.getSnd());

							mOutputPort.getSubscribers().add(mInputPort);
						}
					}
				}
			}

			/* We are finished. Return the finished project. */
			return project;
		} catch (final Exception ex) {
			AnalysisController.LOG.error("Unable to save configuration.");
			return null;
		}

	}

	static private MIInputPort findInputPort(final MIAnalysisPlugin mPlugin, final String name) {
		final Iterator<MIInputPort> iter = mPlugin.getInputPorts().iterator();
		while (iter.hasNext()) {
			final MIInputPort port = iter.next();
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	static private MIOutputPort findOutputPort(final MIPlugin mPlugin, final String name) {
		final Iterator<MIOutputPort> iter = mPlugin.getOutputPorts().iterator();
		while (iter.hasNext()) {
			final MIOutputPort port = iter.next();
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
			/**
			 * Call execute() method of all plug-ins.
			 */
			for (final AbstractAnalysisPlugin c : this.plugins) {
				if (!c.execute()) {
					AnalysisController.LOG.error("A plug-in's execute message failed");
					success = false;
				}
			}

			/**
			 * Make sure that log reader is not null
			 */
			if (this.logReader == null) {
				AnalysisController.LOG.error("No log reader registered.");
				success = false;
			}

			/**
			 * Start reading
			 */
			if (success) {
				// notify threads waiting for the initialization to be done
				this.initializationLatch.countDown();
				if (!this.logReader.read()) {
					AnalysisController.LOG.error("Calling execute() on logReader returned false");
					success = false;
				}
			}
		} catch (final Exception exc) { // NOCS // NOPMD
			AnalysisController.LOG.error("Error occurred: " + exc.getMessage());
			success = false;
		} finally {
			// to make sure that all waiting threads are released
			this.initializationLatch.countDown();
			try {
				for (final AbstractAnalysisPlugin c : this.plugins) {
					c.terminate(!success); // normal termination (w/o error)
				}
			} catch (final Exception e) { // NOCS // NOPMD
				AnalysisController.LOG.error("Error during termination: " + e.getMessage(), e);
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
		this.logReader.terminate();
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
	 * Sets the log reader used as the source for monitoring records.
	 * 
	 * @param reader
	 */
	public void setReader(final IMonitoringReader reader) { // TODO: is this really still needed? Should be done with Project...
		this.logReader = reader;
		AnalysisController.LOG.debug("Registered reader " + reader);
	}

	/**
	 * Registers the passed plug-in <i>c<i>. All plugins which have been
	 * registered before calling the <i>run</i>-method, will be started once
	 * the analysis is started.
	 */
	public void registerPlugin(final AbstractAnalysisPlugin plugin) { // TODO: is this really still needed? Should be done with Project...
		this.plugins.add(plugin);
		if (AnalysisController.LOG.isDebugEnabled()) {
			AnalysisController.LOG.debug("Registered plugin " + plugin);
		}
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
		} catch (final ClassNotFoundException e) {
			AnalysisController.LOG.error(c.getSimpleName() + ": Class '" + classname + "' not found", e); // NOCS (MultipleStringLiteralsCheck)
		} catch (final NoSuchMethodException e) {
			AnalysisController.LOG.error(c.getSimpleName() + ": Class '" + classname // NOCS (MultipleStringLiteralsCheck)
					+ "' has to implement a (public) constructor that accepts a single Configuration", e);
		} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			AnalysisController.LOG.error(c.getSimpleName() + ": Failed to load class for name '" + classname + "'", e); // NOCS (MultipleStringLiteralsCheck)
		}
		return createdClass;
	}
}
