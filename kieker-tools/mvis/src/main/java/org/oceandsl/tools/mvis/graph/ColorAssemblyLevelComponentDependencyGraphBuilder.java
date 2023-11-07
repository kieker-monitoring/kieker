/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.mvis.graph;

import java.util.Optional;

import kieker.analysis.architecture.dependency.PropertyConstants;
import kieker.analysis.architecture.dependency.VertexType;
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;

import org.oceandsl.analysis.graph.IGraphElementSelector;
import org.oceandsl.tools.mvis.FullyQualifiedNamesFactory;

/**
 * @author Reiner Jung
 * @since 1.0
 *
 */
public class ColorAssemblyLevelComponentDependencyGraphBuilder extends AbstractColorDependencyGraphBuilder {

    public ColorAssemblyLevelComponentDependencyGraphBuilder(final IGraphElementSelector selector) {
        super(selector);
    }

    @Override
    protected INode addVertex(final DeployedOperation deployedOperation) {
        final AssemblyOperation operation = deployedOperation.getAssemblyOperation();
        final AssemblyComponent component = operation.getComponent();

        final INode componentVertex = this.addVertexIfAbsent(component);
        componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_COMPONENT);
        componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getComponentType().getName());
        componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getComponentType().getPackage());
        componentVertex.setPropertyIfAbsent(ExtraConstantsUtils.FOREGROUND_COLOR,
                this.selectForegroundColor(component));
        componentVertex.setPropertyIfAbsent(ExtraConstantsUtils.BACKGROUND_COLOR,
                this.selectBackgroundColor(component));

        this.responseTimeDecorator.decorate(componentVertex, component);

        return componentVertex;
    }

    @Override
    protected INode addStorageVertex(final DeployedStorage deployedStorage) {
        final AssemblyStorage storage = deployedStorage.getAssemblyStorage();
        final AssemblyComponent component = storage.getComponent();

        final INode componentVertex = this.addVertexIfAbsent(component);
        componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_COMPONENT);
        componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getComponentType().getName());
        componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getComponentType().getPackage());
        componentVertex.setPropertyIfAbsent(ExtraConstantsUtils.FOREGROUND_COLOR,
                this.selectForegroundColor(component));
        componentVertex.setPropertyIfAbsent(ExtraConstantsUtils.BACKGROUND_COLOR,
                this.selectBackgroundColor(component));

        return componentVertex;
    }

    protected INode addVertexIfAbsent(final AssemblyComponent component) {
        final String name = FullyQualifiedNamesFactory.createFullyQualifiedName(component);
        final Optional<INode> nodeOptional = this.graph.getGraph().nodes().stream()
                .filter(node -> name.equals(node.getId())).findFirst();
        if (nodeOptional.isEmpty()) {
            final INode node = GraphFactory.createNode(name);
            this.graph.getGraph().addNode(node);
            return node;
        }
        return nodeOptional.get();
    }

}
