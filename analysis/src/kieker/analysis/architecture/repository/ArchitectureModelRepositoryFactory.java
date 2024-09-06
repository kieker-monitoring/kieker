/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.io.BufferedWriter;
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
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypePackage;

/**
 * Create, load and store architecture and utility models.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class ArchitectureModelRepositoryFactory {

	/** Standard type model file name. */
	public static final String TYPE_MODEL_NAME = "type-model.xmi";

	/** Standard assembly model file name. */
	public static final String ASSEMBLY_MODEL_NAME = "assembly-model.xmi";

	/** Standard deployment model file name. */
	public static final String DEPLOYMENT_MODEL_NAME = "deployment-model.xmi";

	/** Standard execution model file name. */
	public static final String EXECUTION_MODEL_NAME = "execution-model.xmi";

	/** Standard statistics model file name. */
	public static final String STATISTICS_MODEL_NAME = "statistics-model.xmi";

	/** Standard source model file name. */
	public static final String SOURCE_MODEL_NAME = "source-model.xmi";

	/** Model descriptor for the type model. */
	public static final ModelDescriptor TYPE_MODEL_DESCRIPTOR = new ModelDescriptor(
			ArchitectureModelRepositoryFactory.TYPE_MODEL_NAME, TypePackage.Literals.TYPE_MODEL, TypeFactory.eINSTANCE);
	/** Model descriptor for the assembly model. */
	public static final ModelDescriptor ASSEMBLY_MODEL_DESCRIPTOR = new ModelDescriptor(
			ArchitectureModelRepositoryFactory.ASSEMBLY_MODEL_NAME, AssemblyPackage.Literals.ASSEMBLY_MODEL,
			AssemblyFactory.eINSTANCE);
	/** Model descriptor for the deployment model. */
	public static final ModelDescriptor DEPLOYMENT_MODEL_DESCRIPTOR = new ModelDescriptor(
			ArchitectureModelRepositoryFactory.DEPLOYMENT_MODEL_NAME, DeploymentPackage.Literals.DEPLOYMENT_MODEL,
			DeploymentFactory.eINSTANCE);
	/** Model descriptor for the execution model. */
	public static final ModelDescriptor EXECUTION_MODEL_DESCRIPTOR = new ModelDescriptor(
			ArchitectureModelRepositoryFactory.EXECUTION_MODEL_NAME, ExecutionPackage.Literals.EXECUTION_MODEL,
			ExecutionFactory.eINSTANCE);
	/** Model descriptor for the statistics model. (optional) */
	public static final ModelDescriptor STATISTICS_MODEL_DESCRIPTOR = new ModelDescriptor(
			ArchitectureModelRepositoryFactory.STATISTICS_MODEL_NAME, StatisticsPackage.Literals.STATISTICS_MODEL,
			StatisticsFactory.eINSTANCE, false);
	/** Model descriptor for the source model. (optional) */
	public static final ModelDescriptor SOURCE_MODEL_DESCRIPTOR = new ModelDescriptor(
			ArchitectureModelRepositoryFactory.SOURCE_MODEL_NAME, SourcePackage.Literals.SOURCE_MODEL,
			SourceFactory.eINSTANCE, false);

	private static final Logger LOGGER = LoggerFactory.getLogger(ArchitectureModelRepositoryFactory.class);

	private ArchitectureModelRepositoryFactory() {
		// factory class
	}

	/**
	 * Create an empty repository for architecture models.
	 *
	 * @param repositoryName
	 *            name of the repository
	 * @return a new instance of an empty repository
	 */
	public static ModelRepository createEmptyModelRepository(final String repositoryName) {
		return new ModelRepository(repositoryName);
	}

	/**
	 * Create a model repository with a set of empty models.
	 *
	 * @param repositoryName
	 *            name of the repository
	 * @param descriptors
	 *            list of model descriptors
	 * @return returns on success an model repository with a set of empty models
	 */
	public static ModelRepository createModelRepository(final String repositoryName, final ModelDescriptor... descriptors) {
		final ModelRepository repository = ArchitectureModelRepositoryFactory.createEmptyModelRepository(repositoryName);
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
		final ModelRepository repository = ArchitectureModelRepositoryFactory.createEmptyModelRepository(inputDirectory.getFileName().toString());

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
			ArchitectureModelRepositoryFactory.readModel(resourceSet, repository, descriptor, inputDirectory);
		}

		return repository;
	}

	private static <T extends EObject> void readModel(final ResourceSet resourceSet, final ModelRepository repository,
			final ModelDescriptor modelDescriptor, final Path path) throws ConfigurationException {
		ArchitectureModelRepositoryFactory.LOGGER.debug("Loading model {}", modelDescriptor.getFilename());
		final File modelFile = ArchitectureModelRepositoryFactory.createReadModelFileHandle(path, modelDescriptor.getFilename());
		if (modelFile.exists()) {
			final Resource resource = resourceSet.getResource(URI.createFileURI(modelFile.getAbsolutePath()), true);
			for (final Diagnostic error : resource.getErrors()) {
				ArchitectureModelRepositoryFactory.LOGGER.error("Error loading '{}' of {}:{}  {}", modelDescriptor.getFilename(),
						error.getLocation(), error.getLine(), error.getMessage());
			}
			for (final Diagnostic error : resource.getWarnings()) {
				ArchitectureModelRepositoryFactory.LOGGER.error("Warning loading '{}' of {}:{}  {}", modelDescriptor.getFilename(),
						error.getLocation(), error.getLine(), error.getMessage());
			}
			repository.register(modelDescriptor, resource.getContents().get(0));
			final Iterator<EObject> iterator = resource.getAllContents();
			while (iterator.hasNext()) {
				iterator.next().eCrossReferences();
			}
		} else if (modelDescriptor.isRequired()) {
			ArchitectureModelRepositoryFactory.LOGGER.error("Error reading model file {}. File does not exist.",
					modelFile.getAbsoluteFile());
			throw new ConfigurationException(
					String.format("Error reading model file %s. File does not exist.", modelFile.getAbsoluteFile()));
		} else {
			ArchitectureModelRepositoryFactory.LOGGER.warn("Optional model file {} not present.", modelFile.getAbsoluteFile());
			repository.register(modelDescriptor, modelDescriptor.getFactory().create(modelDescriptor.getRootClass()));
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
		resourceSet.setResourceFactoryRegistry(registry);

		final EPackage.Registry packageRegistry = resourceSet.getPackageRegistry();
		for (final EClass rootClass : repository.getModels().keySet()) {
			final ModelDescriptor descriptor = repository.getModelDescriptor(rootClass);
			final EPackage ePackage = descriptor.getFactory().getEPackage();
			packageRegistry.put(ePackage.getNsURI(), ePackage);
		}

		if (!Files.exists(outputDirectory)) {
			Files.createDirectory(outputDirectory);
		}

		ArchitectureModelRepositoryFactory.writeEclipseProject(outputDirectory, repository.getName());

		for (final EClass rootClass : repository.getModels().keySet()) {
			final ModelDescriptor descriptor = repository.getModelDescriptor(rootClass);
			ArchitectureModelRepositoryFactory.writeModel(resourceSet, outputDirectory, descriptor.getFilename(), repository.getModel(rootClass));
		}
	}

	private static void writeEclipseProject(final Path outputDirectory, final String name) throws IOException {
		final Path projectPath = outputDirectory.resolve(".project");
		try (BufferedWriter writer = Files.newBufferedWriter(projectPath)) {
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.write("<projectDescription>\n");
			writer.write(String.format("    <name>%s</name>\n", name));
			writer.write("    <comment></comment>\n");
			writer.write("    <projects>\n");
			writer.write("    </projects>\n");
			writer.write("    <buildSpec>\n");
			writer.write("    </buildSpec>\n");
			writer.write("    <natures>\n");
			writer.write("    </natures>\n");
			writer.write("</projectDescription>\n");
			writer.close();
		}
	}

	private static <T extends EObject> void writeModel(final ResourceSet resourceSet, final Path outputDirectory,
			final String filename, final T model) {
		ArchitectureModelRepositoryFactory.LOGGER.debug("Saving model {}", filename);

		final File modelFile = ArchitectureModelRepositoryFactory.createWriteModelFileHandle(outputDirectory, filename);

		final Resource resource = resourceSet.createResource(URI.createFileURI(modelFile.getAbsolutePath()));
		resource.getContents().add(model);

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (final IOException e) {
			ArchitectureModelRepositoryFactory.LOGGER.error("Cannot write {} model to storage. Cause: {}",
					modelFile.getAbsoluteFile(), e.getLocalizedMessage());
		}
	}

	private static File createWriteModelFileHandle(final Path path, final String filename) {
		return new File(path.toFile().getAbsolutePath() + File.separator + filename);
	}

}
