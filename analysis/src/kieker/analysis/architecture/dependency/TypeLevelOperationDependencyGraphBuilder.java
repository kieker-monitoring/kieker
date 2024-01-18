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

package kieker.analysis.architecture.dependency;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.util.FullyQualifiedNamesFactory;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;

/**
 * Dependency graph builder for <strong>operation</strong> dependency graphs
 * at the <strong>type level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TypeLevelOperationDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public TypeLevelOperationDependencyGraphBuilder() {
		super();
	}

	@Override
	protected INode addVertex(final DeployedOperation deployedOperation) {
		final OperationType operation = deployedOperation.getAssemblyOperation().getOperationType();
		final ComponentType component = operation.getComponentType();

		final INode componentVertex = this.addVertexIfAbsent(this.graph, FullyQualifiedNamesFactory.createFullyQualifiedName(component));
		componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.COMPONENT_TYPE);
		componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getName());
		componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getPackage());

		final IGraph<INode, IEdge> componentSubgraph = this.addChildGraphIfAbsent(componentVertex);
		final INode operationVertex = this.addVertexIfAbsent(componentSubgraph, FullyQualifiedNamesFactory.createFullyQualifiedName(operation));
		operationVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.OPERATION_TYPE);
		operationVertex.setPropertyIfAbsent(PropertyConstants.NAME, operation.getName());
		operationVertex.setPropertyIfAbsent(PropertyConstants.RETURN_TYPE, operation.getReturnType());
		operationVertex.setPropertyIfAbsent(PropertyConstants.MODIFIERS, operation.getModifiers());
		operationVertex.setPropertyIfAbsent(PropertyConstants.PARAMETER_TYPES, operation.getParameterTypes());
		this.responseTimeDecorator.decorate(operationVertex, operation);

		return operationVertex;
	}
}
