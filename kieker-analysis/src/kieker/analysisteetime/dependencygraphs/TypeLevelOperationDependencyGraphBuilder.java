/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.dependencygraphs;

import kieker.analysisteetime.dependencygraphs.vertextypes.VertexType;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.OperationType;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * Dependency graph builder for <strong>operation</strong> dependency graphs
 * at the <strong>type level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TypeLevelOperationDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public TypeLevelOperationDependencyGraphBuilder(final ExecutionModel executionModel, final StatisticsModel statisticsModel) {
		super(executionModel, statisticsModel);
	}

	@Override
	protected Vertex addVertex(final DeployedOperation deployedOperation) {
		final OperationType operation = deployedOperation.getAssemblyOperation().getOperationType();
		final ComponentType component = operation.getComponentType();

		final int componentId = this.identifierRegistry.getIdentifier(component);
		final Vertex componentVertex = this.graph.addVertexIfAbsent(componentId);
		componentVertex.setPropertyIfAbsent(PropertyKeys.TYPE, VertexType.COMPONENT_TYPE);
		componentVertex.setPropertyIfAbsent(PropertyKeys.NAME, component.getName());
		componentVertex.setPropertyIfAbsent(PropertyKeys.PACKAGE_NAME, component.getPackage());

		final Graph componentSubgraph = componentVertex.addChildGraphIfAbsent();
		final int operationId = this.identifierRegistry.getIdentifier(operation);
		final Vertex operationVertex = componentSubgraph.addVertexIfAbsent(operationId);
		operationVertex.setPropertyIfAbsent(PropertyKeys.TYPE, VertexType.OPERATION_TYPE);
		operationVertex.setPropertyIfAbsent(PropertyKeys.NAME, operation.getName());
		operationVertex.setPropertyIfAbsent(PropertyKeys.RETURN_TYPE, operation.getReturnType());
		operationVertex.setPropertyIfAbsent(PropertyKeys.MODIFIERS, operation.getModifiers());
		operationVertex.setPropertyIfAbsent(PropertyKeys.PARAMETER_TYPES, operation.getParameterTypes());
		this.responseTimeDecorator.decorate(operationVertex, operation);

		return operationVertex;
	}

}
