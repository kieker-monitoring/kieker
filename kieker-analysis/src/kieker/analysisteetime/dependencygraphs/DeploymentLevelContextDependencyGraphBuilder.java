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

import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * Dependency graph builder for <strong>deployment context</strong> dependency graphs
 * at the <strong>deployment level</strong>.
 * Currently deployment context dependency graphs are only available at the deployment
 * level, but for clarity reasons it is explicitly mentioned in the class name.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
class DeploymentLevelContextDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	public DeploymentLevelContextDependencyGraphBuilder() {
		super();
	}

	@Override
	protected Vertex addVertex(final DeployedOperation deployedOperation) {
		final DeployedOperation operation = deployedOperation;
		final DeployedComponent component = operation.getComponent();
		final DeploymentContext context = component.getDeploymentContext();

		final int contextId = this.identifierRegistry.getIdentifier(context);

		final Vertex contextVertex = this.graph.addVertexIfAbsent(contextId);
		contextVertex.setPropertyIfAbsent("type", "<<execution container>>"); // TODO move to constant
		contextVertex.setPropertyIfAbsent("name", context.getName()); // TODO move to constant

		return contextVertex;
	}

}
