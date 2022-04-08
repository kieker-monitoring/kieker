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

package kieker.analysis.graph.dependency;

import kieker.analysis.graph.IVertex;
import kieker.analysis.graph.dependency.vertextypes.VertexType;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;

/**
 * Dependency graph builder for <strong>deployment context</strong> dependency graphs
 * at the <strong>deployment level</strong>.
 * Currently deployment context dependency graphs are only available at the deployment
 * level, but for clarity reasons it is explicitly mentioned in the class name.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DeploymentLevelContextDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	/**
	 * Create new graph builder.
	 *
	 * @param repository
	 *            model repository
	 */
	public DeploymentLevelContextDependencyGraphBuilder() {
		super();
	}

	@Override
	protected IVertex addVertex(final DeployedOperation deployedOperation) {
		final DeployedOperation operation = deployedOperation;
		final DeployedComponent component = operation.getComponent();
		final DeploymentContext context = component.getDeploymentContext();

		final int contextId = this.identifierRegistry.getIdentifier(context);
		final IVertex contextVertex = this.graph.addVertexIfAbsent(contextId);
		contextVertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.DEPLOYMENT_CONTEXT);
		contextVertex.setPropertyIfAbsent(PropertyConstants.NAME, context.getName());
		this.responseTimeDecorator.decorate(contextVertex, context);

		return contextVertex;
	}

}
