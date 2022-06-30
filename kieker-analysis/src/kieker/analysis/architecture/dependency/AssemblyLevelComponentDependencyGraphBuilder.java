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

import kieker.analysis.graph.INode;
import kieker.analysis.graph.dependency.vertextypes.VertexType;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.deployment.DeployedOperation;

/**
 * Dependency graph builder for <strong>component</strong> dependency graphs
 * at the <strong>assembly level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AssemblyLevelComponentDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public AssemblyLevelComponentDependencyGraphBuilder() {
		super();
	}

	@Override
	protected INode addVertex(final DeployedOperation deployedOperation) {
		final AssemblyOperation operation = deployedOperation.getAssemblyOperation();
		final AssemblyComponent component = operation.getComponent();

		final String componentId = String.valueOf(this.identifierRegistry.getIdentifier(component));
		final INode componentVertex = this.addVertexIfAbsent(this.graph, componentId);
		componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ASSEMBLY_COMPONENT);
		componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getComponentType().getName());
		componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getComponentType().getPackage());
		this.responseTimeDecorator.decorate(componentVertex, component);

		return componentVertex;
	}

}
