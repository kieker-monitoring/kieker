/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.delta.stages;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import teetime.framework.AbstractProducerStage;

import org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage;
import org.oceandsl.tools.restructuring.restructuremodel.TransformationModel;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class ResturctureModelReader extends AbstractProducerStage<TransformationModel> {

    private final Path inputPath;

    public ResturctureModelReader(final Path inputPath) {
        this.inputPath = inputPath;
    }

    @Override
    protected void execute() throws Exception {
        final Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> extensionToFactoryMap = registry.getExtensionToFactoryMap();
        extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();

        final EPackage.Registry packageRegistry = resourceSet.getPackageRegistry();
        packageRegistry.put(RestructuremodelPackage.eNS_URI, RestructuremodelPackage.eINSTANCE);

        this.logger.info("Loading model {}", this.inputPath.getFileName().toString());
        final Resource resource = resourceSet.getResource(URI.createFileURI(this.inputPath.toFile().getAbsolutePath()),
                true);
        for (final Diagnostic error : resource.getErrors()) {
            this.logger.error("Error loading '{}' of {}:{}  {}", this.inputPath.getFileName().toString(),
                    error.getLocation(), error.getLine(), error.getMessage());
        }
        for (final Diagnostic error : resource.getWarnings()) {
            this.logger.error("Warning loading '{}' of {}:{}  {}", this.inputPath.getFileName().toString(),
                    error.getLocation(), error.getLine(), error.getMessage());
        }

        final Iterator<EObject> iterator = resource.getAllContents();
        while (iterator.hasNext()) {
            iterator.next().eCrossReferences();
        }

        this.outputPort.send((TransformationModel) resource.getContents().get(0));
        this.workCompleted();
    }

}
