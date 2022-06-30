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

import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.INode;
import kieker.analysis.graph.dependency.vertextypes.VertexType;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;

/**
 * Dependency graph builder for <strong>component</strong> dependency graphs
 * at the <strong>deployment level</strong>.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DeploymentLevelComponentDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	/**
	 * Create a new graph builder.
	 */
	public DeploymentLevelComponentDependencyGraphBuilder() {
		super();
	}

	@Override
	protected INode addVertex(final DeployedOperation deployedOperation) {
		final DeployedOperation operation = deployedOperation;
		final DeployedComponent component = operation.getComponent();
		final DeploymentContext context = component.getContext();

		final String contextId = String.valueOf(this.identifierRegistry.getIdentifier(context));
		final INode contextVertex = this.addVertexIfAbsent(this.graph, contextId);
		contextVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.DEPLOYMENT_CONTEXT);
		contextVertex.setPropertyIfAbsent(PropertyConstants.NAME, context.getName());

		final IGraph<INode, IEdge> contextSubgraph = this.addChildGraphIfAbsent(contextVertex);
		final String componentId = String.valueOf(this.identifierRegistry.getIdentifier(component));
		final INode componentVertex = this.addVertexIfAbsent(contextSubgraph, componentId);
		componentVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.DEPLOYED_COMPONENT);
		componentVertex.setPropertyIfAbsent(PropertyConstants.NAME, component.getAssemblyComponent().getComponentType().getName());
		componentVertex.setPropertyIfAbsent(PropertyConstants.PACKAGE_NAME, component.getAssemblyComponent().getComponentType().getPackage());
		this.responseTimeDecorator.decorate(componentVertex, component);

		return componentVertex;
	}

}
