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
package kieker.analysis.architecture.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.exception.ConfigurationException;

/**
 * Create, load and store architecture and utility models.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class ArchitectureModelFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArchitectureModelFactory.class);

	private ArchitectureModelFactory() {
		// factory class
	}

	public static ModelRepository createEmptyModelRepository(final String repositoryName) {
		return new ModelRepository(repositoryName);
	}

	/**
	 * Create an empty model repository.
	 *
	 * @param repositoryName
	 *            name of the repository
	 * @param descriptors
	 *            list of model descriptors
	 * @return returns on success an model repository with a set of empty models
	 */
	public static ModelRepository createModelRepository(final String repositoryName, final ModelDescriptor... descriptors) {
		final ModelRepository repository = ArchitectureModelFactory.createEmptyModelRepository(repositoryName);
		for (final ModelDescriptor descriptor : descriptors) {
			repository.register(descriptor, descriptor.getFactory().create(descriptor.getRootClass()));
		}
		return repository;
	}

	/**
	 * Read a set of model files into a model repository.
	 *
	 * @param inputDirectory
	 *            the directory containing all model files
	 * @param descriptors
	 *            list of model descriptors defining which models to read
	 * @return returns on success a complete model repository
	 * @throws ConfigurationException
	 *             on errors
	 */
	public static ModelRepository readModelRepository(final Path inputDirectory, final ModelDescriptor... descriptors) throws ConfigurationException {
		final ModelRepository repository = new ModelRepository(inputDirectory.getFileName().toString());

		final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> extensionToFactoryMap = registry.getExtensionToFactoryMap();
		extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl());

		final ResourceSet resourceSet = new ResourceSetImpl();

		final EPackage.Registry packageRegistry = resourceSet.getPackageRegistry();
		for (final ModelDescriptor descriptor : descriptors) {
			final EPackage ePackage = descriptor.getFactory().getEPackage();
			packageRegistry.put(ePackage.getNsURI(), ePackage);
		}

		for (final ModelDescriptor descriptor : descriptors) {
			ArchitectureModelFactory.readModel(resourceSet, repository, descriptor, inputDirectory);
		}

		return repository;
	}

	private static <T extends EObject> void readModel(final ResourceSet resourceSet, final ModelRepository repository,
			final ModelDescriptor descriptor, final Path path) throws ConfigurationException {
		LOGGER.info("Loading model {}", descriptor.getFilename());
		final File modelFile = ArchitectureModelFactory.createReadModelFileHandle(path, descriptor.getFilename());
		if (modelFile.exists()) {
			final Resource resource = resourceSet.getResource(URI.createFileURI(modelFile.getAbsolutePath()), true);
			for (final Diagnostic error : resource.getErrors()) {
				LOGGER.error("Error loading '{}' of {}:{}  {}", descriptor.getFilename(),
						error.getLocation(), error.getLine(), error.getMessage());
			}
			for (final Diagnostic error : resource.getWarnings()) {
				LOGGER.error("Warning loading '{}' of {}:{}  {}", descriptor.getFilename(),
						error.getLocation(), error.getLine(), error.getMessage());
			}
			repository.register(descriptor, resource.getContents().get(0));
			final Iterator<EObject> iterator = resource.getAllContents();
			while (iterator.hasNext()) {
				iterator.next().eCrossReferences();
			}
		} else {
			LOGGER.error("Error reading model file {}. File does not exist.",
					modelFile.getAbsoluteFile());
			throw new ConfigurationException(
					String.format("Error reading model file %s. File does not exist.", modelFile.getAbsoluteFile()));
		}
	}

	private static File createReadModelFileHandle(final Path path, final String filename) {
		return new File(path.toString() + File.separator + filename);
	}

	/**
	 * Write a model repository to the file system. The output directory is created in case it does not exist.
	 *
	 * @param outputDirectory
	 *            output directory for all model files
	 * @param repository
	 *            the model repository to be stored
	 * @throws IOException
	 *             on write errors
	 */
	public static void writeModelRepository(final Path outputDirectory, final ModelRepository repository)
			throws IOException {

		final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> extensionToFactoryMap = registry.getExtensionToFactoryMap();
		extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl());

		// store models
		final ResourceSet resourceSet = new ResourceSetImpl();

		final EPackage.Registry packageRegistry = resourceSet.getPackageRegistry();
		for (final EClass rootClass : repository.getModels().keySet()) {
			final ModelDescriptor descriptor = repository.getModelDescriptor(rootClass);
			final EPackage ePackage = descriptor.getFactory().getEPackage();
			packageRegistry.put(ePackage.getNsURI(), ePackage);
		}

		if (!Files.exists(outputDirectory)) {
			Files.createDirectory(outputDirectory);
		}

		for (final EClass rootClass : repository.getModels().keySet()) {
			final ModelDescriptor descriptor = repository.getModelDescriptor(rootClass);
			ArchitectureModelFactory.writeModel(resourceSet, outputDirectory, descriptor.getFilename(), repository.getModel(rootClass));
		}
	}

	private static <T extends EObject> void writeModel(final ResourceSet resourceSet, final Path outputDirectory,
			final String filename, final T model) {
		LOGGER.info("Saving model {}", filename);

		final File modelFile = ArchitectureModelFactory.createWriteModelFileHandle(outputDirectory, filename);

		final Resource resource = resourceSet.createResource(URI.createFileURI(modelFile.getAbsolutePath()));
		resource.getContents().add(model);

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (final IOException e) {
			LOGGER.error("Cannot write {} model to storage. Cause: {}",
					modelFile.getAbsoluteFile(), e.getLocalizedMessage());
		}
	}

	private static File createWriteModelFileHandle(final Path path, final String filename) {
		return new File(path.toFile().getAbsolutePath() + File.separator + filename);
	}

}
