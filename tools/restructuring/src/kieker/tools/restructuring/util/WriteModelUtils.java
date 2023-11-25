/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.repository.ArchitectureModelManagementUtils;
import kieker.tools.restructuring.restructuremodel.TransformationModel;

/**
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public final class WriteModelUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArchitectureModelManagementUtils.class);

	private WriteModelUtils() {
		// ensure utils are not instantiated
	}

	public static void writeModelRepository(final Path outputDirectory, final String filename,
			final TransformationModel model) throws IOException {

		final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> extensionToFactoryMap = registry.getExtensionToFactoryMap();
		extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl());

		// store models
		final ResourceSet resourceSet = new ResourceSetImpl();
		// resourceSet.setResourceFactoryRegistry(registry);
		// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi",new
		// XMIResourceFactoryImpl());
		if (!Files.exists(outputDirectory)) {
			Files.createDirectory(outputDirectory);
		}

		WriteModelUtils.writeModel(resourceSet, outputDirectory, filename, model);

	}

	private static <T extends EObject> void writeModel(final ResourceSet resourceSet, final Path outputDirectory,
			final String filename, final T model) {
		WriteModelUtils.LOGGER.info("Saving model {}", filename);

		final File modelFile = outputDirectory.resolve(filename + ".xmi").toAbsolutePath().toFile();

		final Resource resource = resourceSet.createResource(URI.createFileURI(modelFile.getAbsolutePath()));
		resource.getContents().add(model);

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (final IOException e) {
			WriteModelUtils.LOGGER.error("Cannot write {} model to storage. Cause: {}", modelFile.getAbsoluteFile(), // NOPMD
					e.getLocalizedMessage());
		}
	}
}
