/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.dependency;

import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.IVertex;
import kieker.analysis.graph.dependency.vertextypes.VertexType;
import kieker.analysis.stage.model.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.deployment.DeployedOperation;

/**
 * Dependency graph builder for <strong>operation</strong> dependency graphs
 * at the <strong>assembly level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AssemblyLevelOperationDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public AssemblyLevelOperationDependencyGraphBuilder(final ModelRepository repository) {
		super(repository);
	}

	@Override
	protected IVertex addVertex(final DeployedOperation deployedOperation) {
		final AssemblyOperation operation = deployedOperation.getAssemblyOperation();
		final AssemblyComponent component = operation.getAssemblyComponent();

		final int componentId = this.identifierRegistry.getIdentifier(component);
		final IVertex componentVertex = this.graph.addVertexIfAbsent(componentId);
		componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_COMPONENT);
		componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getComponentType().getName());
		componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getComponentType().getPackage());

		final IGraph componentSubgraph = componentVertex.addChildGraphIfAbsent();
		final int operationId = this.identifierRegistry.getIdentifier(operation);
		final IVertex operationVertex = componentSubgraph.addVertexIfAbsent(operationId);
		operationVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_OPERATION);
		operationVertex.setPropertyIfAbsent(PropertyConstants.NAME, operation.getOperationType().getName());
		operationVertex.setPropertyIfAbsent(PropertyConstants.RETURN_TYPE, operation.getOperationType().getReturnType());
		operationVertex.setPropertyIfAbsent(PropertyConstants.MODIFIERS, operation.getOperationType().getModifiers());
		operationVertex.setPropertyIfAbsent(PropertyConstants.PARAMETER_TYPES, operation.getOperationType().getParameterTypes());
		this.responseTimeDecorator.decorate(operationVertex, operation);

		return operationVertex;
	}

}
