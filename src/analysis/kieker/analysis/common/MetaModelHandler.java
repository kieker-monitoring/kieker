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

package kieker.analysis.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelPackage;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.common.configuration.Configuration;

/**
 * This is a helper class for the {@link kieker.analysis.AnalysisController}, which manages the handling of the meta model instances.
 * 
 * @author Andre van Hoorn, Nils Christian Ehmke, Jan Waller
 * 
 * @since 1.8
 */
public class MetaModelHandler {

	public static final void saveProjectToFile(final File file, final MIProject project) throws IOException {
		// Create a resource and put the given project into it
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		final Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
		resource.getContents().add(project);

		// Make sure that the controller uses utf8 instead of ascii.
		final Map<String, String> options = new HashMap<String, String>(); // NOPMD (no concurrent access)
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");

		// Now try to save the resource
		resource.save(options);
	}

	public static final MIProject loadProjectFromFile(final File file) throws IOException {
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
	public static final Configuration modelPropertiesToConfiguration(final EList<MIProperty> mProperties) {
		final Configuration configuration = new Configuration();
		// Run through the properties and convert every single of them
		for (final MIProperty mProperty : mProperties) {
			configuration.setProperty(mProperty.getName(), mProperty.getValue());
		}
		return configuration;
	}

	/**
	 * This method checks the ports of the given model plugin against the ports of the actual plugin. If there are ports which are in the model instance, but not in
	 * the "real" plugin, an exception is thrown.
	 * 
	 * This method should be called during the creation of an <i>AnalysisController</i> via a configuration file to find invalid (outdated) ports.
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
		final Set<String> outputPorts = new HashSet<String>();
		for (final String outputPort : plugin.getAllOutputPortNames()) {
			outputPorts.add(outputPort);
		}
		final Set<String> inputPorts = new HashSet<String>();
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
		final EList<MIInputPort> mInputPorts = (mPlugin instanceof MIFilter) ? ((MIFilter) mPlugin).getInputPorts() : new BasicEList<MIInputPort>(); // NOCS
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
		final List<MIProperty> properties = new ArrayList<MIProperty>(configuration.size());
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
	public static final MIInputPort findInputPort(final MIFilter mPlugin, final String name) {
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
	public static final MIOutputPort findOutputPort(final MIPlugin mPlugin, final String name) {
		for (final MIOutputPort port : mPlugin.getOutputPorts()) {
			if (port.getName().equals(name)) {
				return port;
			}
		}
		return null;
	}
}
