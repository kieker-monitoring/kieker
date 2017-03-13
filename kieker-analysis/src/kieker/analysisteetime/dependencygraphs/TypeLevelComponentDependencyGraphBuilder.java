/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.OperationType;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * Dependency graph builder for <strong>component</strong> dependency graphs
 * at the <strong>type level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class TypeLevelComponentDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public TypeLevelComponentDependencyGraphBuilder() {
		super();
	}

	@Override
	protected Vertex addVertex(final DeployedOperation deployedOperation) {
		final OperationType operation = deployedOperation.getAssemblyOperation().getOperationType();
		final ComponentType component = operation.getComponentType();

		final int componentId = this.identifierRegistry.getIdentifier(component);
		final Vertex componentVertex = this.graph.addVertexIfAbsent(componentId);
		componentVertex.setPropertyIfAbsent("type", VertexType.COMPONENT_TYPE); // TODO move to constant
		componentVertex.setPropertyIfAbsent("name", component.getName()); // TODO move to constant

		return componentVertex;
	}

}
