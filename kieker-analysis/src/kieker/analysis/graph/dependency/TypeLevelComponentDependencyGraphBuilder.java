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

import kieker.analysis.graph.IVertex;
import kieker.analysis.graph.dependency.vertextypes.VertexType;
import kieker.analysis.statistics.StatisticsModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.OperationType;

/**
 * Dependency graph builder for <strong>component</strong> dependency graphs
 * at the <strong>type level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TypeLevelComponentDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public TypeLevelComponentDependencyGraphBuilder(final ExecutionModel executionModel, final StatisticsModel statisticsModel) {
		super(executionModel, statisticsModel);
	}

	@Override
	protected IVertex addVertex(final DeployedOperation deployedOperation) {
		final OperationType operation = deployedOperation.getAssemblyOperation().getOperationType();
		final ComponentType component = operation.getComponentType();

		final int componentId = this.identifierRegistry.getIdentifier(component);
		final IVertex componentVertex = this.graph.addVertexIfAbsent(componentId);
		componentVertex.setPropertyIfAbsent(PropertyKeys.TYPE, VertexType.COMPONENT_TYPE);
		componentVertex.setPropertyIfAbsent(PropertyKeys.NAME, component.getName());
		componentVertex.setPropertyIfAbsent(PropertyKeys.PACKAGE_NAME, component.getPackage());
		this.responseTimeDecorator.decorate(componentVertex, component);

		return componentVertex;
	}

}
