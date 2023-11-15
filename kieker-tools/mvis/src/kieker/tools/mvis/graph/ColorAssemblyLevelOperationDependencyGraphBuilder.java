/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis.graph;

import java.util.Optional;

import kieker.analysis.architecture.dependency.PropertyConstants;
import kieker.analysis.architecture.dependency.VertexType;
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.IGraphElementSelector;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.tools.mvis.FullyQualifiedNamesFactory;

/**
 * Dependency graph builder for <strong>operation</strong> dependency graphs at the <strong>assembly
 * level</strong>.
 *
 * @author Reiner jung
 */
public class ColorAssemblyLevelOperationDependencyGraphBuilder extends AbstractColorDependencyGraphBuilder {

	public ColorAssemblyLevelOperationDependencyGraphBuilder(final IGraphElementSelector selector) {
		super(selector);
	}

	@Override
	protected INode addVertex(final DeployedOperation deployedOperation) {
		final AssemblyOperation operation = deployedOperation.getAssemblyOperation();

		final INode componentVertex = this.addVertexIfAbsent(this.graph, operation.getComponent());

		final IGraph<INode, IEdge> componentSubgraph = this.addChildGraphIfAbsent(componentVertex);
		final INode operationVertex = this.addVertexIfAbsent(componentSubgraph, operation);

		this.responseTimeDecorator.decorate(operationVertex, operation);

		return operationVertex;
	}

	@Override
	protected INode addStorageVertex(final DeployedStorage deployedStorage) {
		final AssemblyStorage storage = deployedStorage.getAssemblyStorage();

		final INode componentVertex = this.addVertexIfAbsent(this.graph, storage.getComponent());

		final IGraph<INode, IEdge> componentSubgraph = this.addChildGraphIfAbsent(componentVertex);
		final INode accessVertex = this.addVertexIfAbsent(componentSubgraph, storage);

		this.responseTimeDecorator.decorate(accessVertex, storage);

		return accessVertex;
	}

	private INode addVertexIfAbsent(final IGraph<INode, IEdge> localGraph, final AssemblyComponent component) {
		final String name = FullyQualifiedNamesFactory.createFullyQualifiedName(component);
		final Optional<INode> nodeOptional = localGraph.getGraph().nodes().stream()
				.filter(node -> name.equals(node.getId())).findFirst();
		if (nodeOptional.isEmpty()) {
			final INode node = this.createNode(name, component);
			localGraph.getGraph().addNode(node);
			return node;
		}
		return nodeOptional.get();
	}

	private INode addVertexIfAbsent(final IGraph<INode, IEdge> localGraph, final AssemblyOperation operation) {
		final String name = FullyQualifiedNamesFactory.createFullyQualifiedName(operation);
		final Optional<INode> nodeOptional = localGraph.getGraph().nodes().stream()
				.filter(node -> name.equals(node.getId())).findFirst();
		if (nodeOptional.isEmpty()) {
			final INode node = this.createNode(name, operation);
			localGraph.getGraph().addNode(node);
			return node;
		}
		return nodeOptional.get();
	}

	private INode addVertexIfAbsent(final IGraph<INode, IEdge> localGraph, final AssemblyStorage storage) {
		final String name = FullyQualifiedNamesFactory.createFullyQualifiedName(storage);
		final Optional<INode> nodeOptional = localGraph.findNode(name);
		if (nodeOptional.isEmpty()) {
			final INode node = this.createNode(name, storage);
			localGraph.getGraph().addNode(node);
			return node;
		}
		return nodeOptional.get();
	}

	private INode createNode(final String name, final AssemblyComponent component) {
		final INode componentVertex = GraphFactory.createNode(name);

		componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_COMPONENT);
		componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getComponentType().getName());
		componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getComponentType().getPackage());
		componentVertex.setPropertyIfAbsent(ExtraConstantsUtils.FOREGROUND_COLOR,
				this.selectForegroundColor(component));
		componentVertex.setPropertyIfAbsent(ExtraConstantsUtils.BACKGROUND_COLOR,
				this.selectBackgroundColor(component));

		return componentVertex;
	}

	private INode createNode(final String name, final AssemblyOperation operation) {
		final INode operationVertex = GraphFactory.createNode(name);

		operationVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_OPERATION);
		operationVertex.setPropertyIfAbsent(PropertyConstants.NAME, operation.getOperationType().getName());
		operationVertex.setPropertyIfAbsent(PropertyConstants.RETURN_TYPE,
				operation.getOperationType().getReturnType());
		operationVertex.setPropertyIfAbsent(PropertyConstants.MODIFIERS, operation.getOperationType().getModifiers());
		operationVertex.setPropertyIfAbsent(PropertyConstants.PARAMETER_TYPES,
				operation.getOperationType().getParameterTypes());
		operationVertex.setPropertyIfAbsent(ExtraConstantsUtils.FOREGROUND_COLOR,
				this.selectForegroundColor(operation));
		operationVertex.setPropertyIfAbsent(ExtraConstantsUtils.BACKGROUND_COLOR,
				this.selectBackgroundColor(operation));

		return operationVertex;
	}

	private INode createNode(final String name, final AssemblyStorage storage) {
		final INode storageVertex = GraphFactory.createNode(name);

		storageVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_STORAGE);
		storageVertex.setPropertyIfAbsent(PropertyConstants.NAME, storage.getStorageType().getName());
		storageVertex.setPropertyIfAbsent(PropertyConstants.RETURN_TYPE, storage.getStorageType().getType());
		storageVertex.setPropertyIfAbsent(ExtraConstantsUtils.FOREGROUND_COLOR, this.selectForegroundColor(storage));
		storageVertex.setPropertyIfAbsent(ExtraConstantsUtils.BACKGROUND_COLOR, this.selectBackgroundColor(storage));

		return storageVertex;
	}

}
