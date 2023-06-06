/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.stage.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.IPlugin;
import kieker.analysis.plugin.IPlugin.PluginInputPortReference;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

/**
 * This is a helper class for the {@link kieker.analysis.AnalysisController}, which manages the handling
 * of the meta model instances.
 *
 * @author Andre van Hoorn, Nils Christian Ehmke, Jan Waller
 *
 * @since 1.8
 * @deprecated since 1.15 can be removed with the old TraceAnalysis
 */
@Deprecated
public final class MetaModelHandler {

	private MetaModelHandler() {
		// No code necessary
	}

	/**
	 * Saves the given meta model project to the given file.
	 *
	 * @param file
	 *            The file in which the project will be stored.
	 * @param project
	 *            The meta model project.
	 *
	 * @throws IOException
	 *             If something went wrong during the saving.
	 */
	public static void saveProjectToFile(final File file, final MIProject project) throws IOException {
		// Create a resource and put the given project into it
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
		resource.getContents().add(project);

		// Make sure that the controller uses utf8 instead of ascii.
		final Map<String, String> options = new HashMap<>(); // NOPMD (no concurrent access)
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");

		// Now try to save the resource
		resource.save(options);
	}

	/**
	 * Loads a meta model project instance from the given file.
	 *
	 * @param file
	 *            The file to load the model from.
	 * @return The meta model instance.
	 *
	 * @throws IOException
	 *             If something went wrong during the loading.
	 */
	public static MIProject loadProjectFromFile(final File file) throws IOException {
		// Create a resource set to work with.
		final ResourceSet resourceSet = new ResourceSetImpl();

		// Initialize the package - otherwise this method cannot read the element
		MAnalysisMetaModelPackage.init();

		// Set OPTION_RECORD_UNKNOWN_FEATURE prior to calling getResource.
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl() {

			@Override
			public Resource createResource(final URI uri) {
				final XMIResourceImpl resource = (XMIResourceImpl) super.createResource(uri);
				resource.getDefaultLoadOptions().put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
				return resource;
			}
		});
		// Try to load the resource
		final XMIResource resource = (XMIResource) resourceSet.getResource(URI.createFileURI(file.toString()), true);
		final EList<EObject> content;
		resource.load(Collections.EMPTY_MAP);
		content = resource.getContents();
		if (!content.isEmpty()) {
			// The first (and only) element should be the project.
			return (MIProject) content.get(0);
		} else {
			throw new IOException("No object found in file '" + file.getAbsolutePath() + "'.");
		}
	}

	/**
	 * This method can be used to convert a given list of <code>MIProperty</code> to a configuration object.
	 *
	 * @param mProperties
	 *            The properties to be converted.
	 * @return A filled configuration object.
	 */
	public static Configuration modelPropertiesToConfiguration(final EList<MIProperty> mProperties) {
		final Configuration configuration = new Configuration();
		// Run through the properties and convert every single of them
		for (final MIProperty mProperty : mProperties) {
			configuration.setProperty(mProperty.getName(), mProperty.getValue());
		}
		return configuration;
	}

	/**
	 * This method checks the ports of the given model plugin against the ports of the actual plugin. If there are
	 * ports which are in the model instance, but not in the "real" plugin, an exception is thrown.
	 *
	 * This method should be called during the creation of an <i>AnalysisController</i> via a configuration file
	 * to find invalid (outdated) ports.
	 *
	 * @param mPlugin
	 *            The model instance of the plugin.
	 * @param plugin
	 *            The corresponding "real" plugin.
	 * @throws AnalysisConfigurationException
	 *             If an invalid port has been detected.
	 */
	public static void checkPorts(final MIPlugin mPlugin, final AbstractPlugin plugin) throws AnalysisConfigurationException {
		// Get all ports.
		final EList<MIOutputPort> mOutputPorts = mPlugin.getOutputPorts();
		final Set<String> outputPorts = new HashSet<>();
		for (final String outputPort : plugin.getAllOutputPortNames()) {
			outputPorts.add(outputPort);
		}
		final Set<String> inputPorts = new HashSet<>();
		for (final String inputPort : plugin.getAllInputPortNames()) {
			inputPorts.add(inputPort);
		}
		// Check whether the ports of the model plugin exist.
		for (final MIOutputPort mOutputPort : mOutputPorts) {
			if (!outputPorts.contains(mOutputPort.getName())) {
				throw new AnalysisConfigurationException("The output port '" + mOutputPort.getName() + "' of '" + mPlugin.getName() + "' (" + mPlugin.getClassname()
						+ ") does not exist.");
			}
		}
		final EList<MIInputPort> mInputPorts = (mPlugin instanceof MIFilter) ? ((MIFilter) mPlugin).getInputPorts() : new BasicEList<>(); // NOCS
		for (final MIInputPort mInputPort : mInputPorts) {
			if (!inputPorts.contains(mInputPort.getName())) {
				throw new AnalysisConfigurationException("The input port '" + mInputPort.getName() + "' of '" + mPlugin.getName() + "' (" + mPlugin.getClassname()
						+ ") does not exist.");
			}
		}
	}

	/**
	 * Converts the given configuration into a list of {@link MIProperty}s using the given factory.
	 *
	 * @param configuration
	 *            The configuration to be converted.
	 * @param factory
	 *            The factory to be used to create the model instances.
	 * @return A list of model instances.
	 */
	public static List<MIProperty> convertProperties(final Configuration configuration, final MAnalysisMetaModelFactory factory) {
		if (null == configuration) { // should not happen, but better be safe than sorry
			return Collections.emptyList();
		}
		final List<MIProperty> properties = new ArrayList<>(configuration.size());
		for (final Enumeration<?> e = configuration.propertyNames(); e.hasMoreElements();) {
			final String key = (String) e.nextElement();
			final MIProperty property = factory.createProperty();
			property.setName(key);
			property.setValue(configuration.getStringProperty(key));
			properties.add(property);
		}
		return properties;
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
	public static MIInputPort findInputPort(final MIFilter mPlugin, final String name) {
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
	public static MIOutputPort findOutputPort(final MIPlugin mPlugin, final String name) {
		for (final MIOutputPort port : mPlugin.getOutputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}

	/**
	 * This method can be used to convert the current analysis configuration (which is represented by
	 * Java objects) into a meta model.
	 *
	 * @param readers
	 *            The readers within the analysis.
	 * @param filters
	 *            The filters within the analysis.
	 * @param repositories
	 *            The repositories within the analysis.
	 * @param dependencies
	 *            The dependencies of the analysis.
	 * @param projectName
	 *            The name of the project.
	 * @param globalConfiguration
	 *            The global project configuration.
	 *
	 * @return A meta model instance, representing the given analysis.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the given analysis components are somehow invalid connected.
	 */
	public static MIProject javaToMetaModel(final Collection<AbstractReaderPlugin> readers, final Collection<AbstractFilterPlugin> filters,
			final Collection<AbstractRepository> repositories, final Collection<MIDependency> dependencies, final String projectName,
			final Configuration globalConfiguration) throws AnalysisConfigurationException {
		try {
			// Create a factory to create all other model instances.
			final MAnalysisMetaModelFactory factory = new MAnalysisMetaModelFactory();

			// We start with the project instance
			final MIProject mProject = factory.createProject();
			mProject.setName(projectName);
			mProject.getDependencies().addAll(dependencies);

			final Map<AbstractPlugin, MIPlugin> pluginMap = new HashMap<>(); // NOPMD (no concurrent access)
			final Map<AbstractRepository, MIRepository> repositoryMap = new HashMap<>(); // NOPMD (no concurrent access)

			// Run through all repositories and create the model counterparts.
			for (final AbstractRepository repository : repositories) {
				final MIRepository mRepository = MetaModelHandler.createRepository(repository, factory);
				mProject.getRepositories().add(mRepository);
				// Remember the mapping.
				repositoryMap.put(repository, mRepository);
			}

			// Run through all plugins and create the model counterparts.
			final List<AbstractPlugin> plugins = new ArrayList<>(readers);
			plugins.addAll(filters);
			for (final AbstractPlugin plugin : plugins) {
				final MIPlugin mPlugin;
				if (plugin instanceof AbstractReaderPlugin) {
					mPlugin = factory.createReader();
				} else {
					mPlugin = factory.createFilter();
				}
				mPlugin.setId(EcoreUtil.generateUUID());

				mPlugin.setClassname(plugin.getClass().getName());
				mPlugin.setName(plugin.getName());
				mPlugin.getProperties().addAll(MetaModelHandler.convertProperties(plugin.getCurrentConfiguration(), factory));

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
					final MIRepositoryConnector mRepositoryConn = factory.createRepositoryConnector();
					mRepositoryConn.setId(EcoreUtil.generateUUID());
					mRepositoryConn.setName(repoEntry.getKey());
					mRepositoryConn.setRepository(mRepository);
					mPlugin.getRepositories().add(mRepositoryConn);
				}
				// Create the ports.
				final String[] outs = plugin.getAllOutputPortNames();
				for (final String out : outs) {
					final MIOutputPort mOutputPort = factory.createOutputPort();
					mOutputPort.setId(EcoreUtil.generateUUID());
					mOutputPort.setName(out);
					mPlugin.getOutputPorts().add(mOutputPort);
				}
				final String[] ins = plugin.getAllInputPortNames();
				for (final String in : ins) {
					final MIInputPort mInputPort = factory.createInputPort();
					mInputPort.setId(EcoreUtil.generateUUID());
					mInputPort.setName(in);
					((MIFilter) mPlugin).getInputPorts().add(mInputPort);
				}
				mProject.getPlugins().add(mPlugin);

				// Remember the mapping.
				pluginMap.put(plugin, mPlugin);
			}

			// Now connect the plugins.
			for (final IPlugin plugin : plugins) {
				final MIPlugin mOutputPlugin = pluginMap.get(plugin);
				// Check all output ports of the original plugin.
				for (final String outputPortName : plugin.getAllOutputPortNames()) {
					// Get the corresponding port of the model counterpart and get also the plugins which are currently connected with the original plugin.
					final EList<MIInputPort> subscribers = MetaModelHandler.findOutputPort(mOutputPlugin, outputPortName).getSubscribers();
					// Run through all connected plugins.
					for (final PluginInputPortReference subscriber : plugin.getConnectedPlugins(outputPortName)) {
						final IPlugin subscriberPlugin = subscriber.getPlugin();
						final MIPlugin mSubscriberPlugin = pluginMap.get(subscriberPlugin);
						// If it doesn't exist, we have a problem...
						if (mSubscriberPlugin == null) {
							throw new AnalysisConfigurationException("Plugin '" + subscriberPlugin.getName() + "' (" + subscriberPlugin.getPluginName()
									+ ") not contained in project. Maybe the plugin has not been registered.");
						}
						final MIInputPort mInputPort = MetaModelHandler.findInputPort((MIFilter) mSubscriberPlugin, subscriber.getInputPortName());
						subscribers.add(mInputPort);
					}
				}
			}

			// Now put our global configuration into the model instance
			final Set<Entry<Object, Object>> properties = globalConfiguration.entrySet();
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
	}

	private static MIRepository createRepository(final AbstractRepository repository, final MAnalysisMetaModelFactory factory) {
		final MIRepository mRepository = factory.createRepository();
		mRepository.setId(EcoreUtil.generateUUID());
		mRepository.setClassname(repository.getClass().getName());
		mRepository.getProperties().addAll(MetaModelHandler.convertProperties(repository.getCurrentConfiguration(), factory));

		return mRepository;
	}

	/**
	 * This method can be used to convert a given analysis meta model instance to the actual java instances.
	 *
	 * @param mProject
	 *            The meta model project.
	 * @param ac
	 *            The analysis controller which will be the parent of the new analysis.
	 * @param pluginConnections
	 *            The connections between the plugins (this object will be filled by the method).
	 * @param repositoryConnections
	 *            The connections between filters and repositories (this object will be filled by the method).
	 * @param dependencies
	 *            The dependencies of the analysis (this object will be filled by the method).
	 * @param classLoader
	 *            The class loader which will be used to create the analysis components.
	 * @param globalConfiguration
	 *            The global project configuration (this object will be filled by the method).
	 * @param repositoryMap
	 *            The mapping between the created repositories and the meta model instances (this object will
	 *            be filled by the method).
	 * @param pluginMap
	 *            The mapping between the created plugins and the meta model instances (this object will be filled
	 *            by the method).
	 *
	 * @throws AnalysisConfigurationException
	 *             If the given meta model instance is somehow invalid configured.
	 */
	public static void metaModelToJava(final MIProject mProject, final AnalysisController ac, final Collection<PluginConnection> pluginConnections,
			final Collection<RepositoryConnection> repositoryConnections, final Collection<MIDependency> dependencies, final ClassLoader classLoader,
			final Configuration globalConfiguration, final Map<MIRepository, AbstractRepository> repositoryMap, final Map<MIPlugin, AbstractPlugin> pluginMap)
			throws AnalysisConfigurationException {
		// Remember the libraries (But create them via a factory to avoid that the dependencies are removed during the saving.
		final MAnalysisMetaModelFactory factory = new MAnalysisMetaModelFactory();
		for (final MIDependency mDepdendency : mProject.getDependencies()) {
			final MIDependency mDepdendencyCopy = factory.createDependency();
			mDepdendencyCopy.setFilePath(mDepdendency.getFilePath());
			dependencies.add(mDepdendencyCopy);
		}
		// Create the repositories.
		for (final MIRepository mRepository : mProject.getRepositories()) {
			// Extract the necessary informations to create the repository.
			final Configuration configuration = MetaModelHandler.modelPropertiesToConfiguration(mRepository.getProperties());
			final AbstractRepository repository = MetaModelHandler.createAndInitialize(AbstractRepository.class, mRepository.getClassname(), configuration, ac,
					classLoader); // throws AnalysisConfigurationException on errors
			repositoryMap.put(mRepository, repository);
		}
		// We run through the project and collect all plugins. As we create an actual object for every plugin within the model, we have to remember the mapping
		// between the plugins within the model and the actual objects we create.
		final EList<MIPlugin> mPlugins = mProject.getPlugins();
		// Now run through all plugins.
		for (final MIPlugin mPlugin : mPlugins) {
			// Extract the necessary informations to create the plugin.
			final Configuration configuration = MetaModelHandler.modelPropertiesToConfiguration(mPlugin.getProperties());
			final String pluginClassname = mPlugin.getClassname();
			configuration.setProperty(AbstractAnalysisComponent.CONFIG_NAME, mPlugin.getName());
			// Create the plugin and put it into our map. */
			final AbstractPlugin plugin = MetaModelHandler.createAndInitialize(AbstractPlugin.class, pluginClassname, configuration, ac, classLoader);
			pluginMap.put(mPlugin, plugin);
			// Check the used configuration against the actual available configuration keys.
			MetaModelHandler.checkConfiguration(plugin, configuration);
		}
		// Now we have all plugins. We can start to assemble the wiring.
		for (final MIPlugin mPlugin : mPlugins) {
			// Check whether the ports exist and log this if necessary.
			MetaModelHandler.checkPorts(mPlugin, pluginMap.get(mPlugin));
			final EList<MIRepositoryConnector> mPluginRPorts = mPlugin.getRepositories();
			for (final MIRepositoryConnector mPluginRPort : mPluginRPorts) {
				repositoryConnections.add(new RepositoryConnection(pluginMap.get(mPlugin), repositoryMap.get(mPluginRPort.getRepository()), mPluginRPort.getName()));
			}
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
					pluginConnections.add(new PluginConnection(srcPlugin, dstPlugin, outputPortName, inputPortName));
				}
			}
		}

		// Now load our global configuration from the model instance
		for (final MIProperty mProperty : mProject.getProperties()) {
			globalConfiguration.setProperty(mProperty.getName(), mProperty.getValue());
		}
	}

	/**
	 * Creates and initializes the given class with the given configuration via reflection.
	 *
	 * @param c
	 *            The base class of the class to be created ({@link AbstractRepository}, {@link AbstractPlugin}).
	 * @param classname
	 *            The name of the class to be created.
	 * @param configuration
	 *            The configuration to be used to initialize the class.
	 * @param classLoader
	 *            The classloader which will be used to initialize the class.
	 *
	 * @param <C>
	 *            The type of the class.
	 *
	 * @return A fully initialized class.
	 * @throws AnalysisConfigurationException
	 *             If the class could not be found or the class doesn't implement the correct classloader.
	 */
	@SuppressWarnings("unchecked")
	private static <C extends AbstractAnalysisComponent> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration,
			final IProjectContext projectContext, final ClassLoader classLoader) throws AnalysisConfigurationException {
		try {
			final Class<?> clazz = Class.forName(classname, true, classLoader);
			if (c.isAssignableFrom(clazz)) {
				return (C) clazz.getConstructor(Configuration.class, IProjectContext.class).newInstance(configuration, projectContext);
			} else {
				throw new AnalysisConfigurationException("Class '" + classname + "' has to implement or extend '" + c.getSimpleName() + "'");
			}
		} catch (final ClassNotFoundException ex) {
			throw new AnalysisConfigurationException(c.getSimpleName() + ": Class '" + classname + "' not found", ex);
		} catch (final NoSuchMethodException ex) {
			throw new AnalysisConfigurationException(c.getSimpleName() + ": Class '" + classname
					+ "' has to implement a (public) constructor that accepts a single Configuration", ex);
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			throw new AnalysisConfigurationException(c.getSimpleName() + ": Failed to load class for name '" + classname + "'", ex);
		}
	}

	/**
	 * This method uses the given configuration object and checks the used keys against the actual existing keys
	 * within the given plugin. If there are keys in the configuration object which are not used in the plugin,
	 * an exception is thrown.
	 *
	 * This method should be called during the creation of the plugins via a given configuration file to find
	 * outdated properties.
	 *
	 * @param plugin
	 *            The plugin to be used for the check.
	 * @param configuration
	 *            The configuration to be checked for correctness.
	 * @throws AnalysisConfigurationException
	 *             If an invalid property has been detected.
	 */
	private static void checkConfiguration(final AbstractPlugin plugin, final Configuration configuration) throws AnalysisConfigurationException {
		final Set<String> possibleKeys = new HashSet<>();
		// Run through all used keys in the actual configuration. (all possible keys)
		for (final Enumeration<?> e = plugin.getCurrentConfiguration().propertyNames(); e.hasMoreElements();) {
			possibleKeys.add((String) e.nextElement());
		}

		// Add the keys from the annotation which are not already included
		for (final Property property : MetaModelHandler.extractAnnotatedProperties(plugin)) {
			possibleKeys.add(property.name());
		}

		// Run through all used keys in the given configuration.
		for (final Enumeration<?> e = configuration.propertyNames(); e.hasMoreElements();) {
			final String key = (String) e.nextElement();
			if (!possibleKeys.contains(key) && !(key.equals(AbstractAnalysisComponent.CONFIG_NAME))) {
				// Found an invalid key.
				throw new AnalysisConfigurationException("Invalid property of '" + plugin.getName() + "' (" + plugin.getPluginName() + ") found: '" + key + "'.");
			}
		}
	}

	private static Property[] extractAnnotatedProperties(final AbstractPlugin plugin) {
		final Plugin pluginAnnotation = plugin.getClass().getAnnotation(Plugin.class);
		if (pluginAnnotation != null) {
			return pluginAnnotation.configuration();
		}
		return new Property[0];
	}

	/**
	 * @author Nils Christian Ehmke
	 *
	 * @since 1.8
	 */
	public static class RepositoryConnection {

		private final AbstractPlugin source;
		private final AbstractRepository repository;
		private final String outputName;

		/**
		 * Creates a new connection between a repository and a filter.
		 *
		 * @param source
		 *            The filter.
		 * @param repository
		 *            The repository.
		 * @param outputName
		 *            The name of the repository port.
		 */
		public RepositoryConnection(final AbstractPlugin source, final AbstractRepository repository, final String outputName) {
			this.source = source;
			this.repository = repository;
			this.outputName = outputName;
		}

		public AbstractPlugin getSource() {
			return this.source;
		}

		public AbstractRepository getRepository() {
			return this.repository;
		}

		public String getOutputName() {
			return this.outputName;
		}

	}

	/**
	 * @author Nils Christian Ehmke
	 *
	 * @since 1.8
	 */
	public static class PluginConnection {
		private final AbstractPlugin source;
		private final AbstractPlugin destination;
		private final String outputName;
		private final String inputName;

		/**
		 * Creates a new connection between two filters.
		 *
		 * @param source
		 *            The source filter.
		 * @param destination
		 *            The destination filter.
		 * @param outputName
		 *            The name of the output port.
		 * @param inputName
		 *            The name of the input port.
		 */
		public PluginConnection(final AbstractPlugin source, final AbstractPlugin destination, final String outputName, final String inputName) {
			this.source = source;
			this.destination = destination;
			this.outputName = outputName;
			this.inputName = inputName;
		}

		public AbstractPlugin getSource() {
			return this.source;
		}

		public AbstractPlugin getDestination() {
			return this.destination;
		}

		public String getOutputName() {
			return this.outputName;
		}

		public String getInputName() {
			return this.inputName;
		}

	}
}
