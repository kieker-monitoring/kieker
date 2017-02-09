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

import java.util.Collection;

import org.apache.commons.lang3.tuple.Pair;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.util.ObjectIdentifierRegistry;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.impl.GraphImpl;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DependencyGraphCreator {

	private final ExecutionModel executionModel;

	public DependencyGraphCreator(final ExecutionModel executionModel) {
		this.executionModel = executionModel;
	}

	// Operation Dependecy Graph -> Deployment Level
	public Graph create() {

		final ObjectIdentifierRegistry identifierRegistry = new ObjectIdentifierRegistry();

		final Graph graph = new GraphImpl();
		graph.setName("dep-graph"); // TODO

		final Collection<AggregatedInvocation> aggregatedInvocations = this.executionModel.getAggregatedInvocations().values();
		for (final AggregatedInvocation invocation : aggregatedInvocations) {

			final DeployedOperation sourceOperation = invocation.getSource();
			final DeployedOperation targetOperation = invocation.getTarget();

			final int sourceOperationId = identifierRegistry.getIdentifier(sourceOperation);
			final int targetOperationId = identifierRegistry.getIdentifier(targetOperation);

			final DeployedComponent sourceComponent = sourceOperation.getComponent();
			final DeployedComponent targetComponent = targetOperation.getComponent();

			final int sourceComponentId = identifierRegistry.getIdentifier(sourceComponent);
			final int targetComponentId = identifierRegistry.getIdentifier(targetComponent);

			final DeploymentContext sourceDeploymentContext = sourceComponent.getDeploymentContext();
			final DeploymentContext targetDeploymentContext = targetComponent.getDeploymentContext();

			final int sourceDeploymentContextId = identifierRegistry.getIdentifier(sourceDeploymentContext);
			final int targetDeploymentContextId = identifierRegistry.getIdentifier(targetDeploymentContext);

			final Vertex sourceDeploymentContextVertex = graph.addVertexIfAbsent(sourceDeploymentContextId);
			final Vertex targetDeploymentContextVertex = graph.addVertexIfAbsent(targetDeploymentContextId);

			final Graph sourceDeploymentContextSubgraph = sourceDeploymentContextVertex.addChildGraphIfAbsent();
			final Graph targetDeploymentContextSubgraph = targetDeploymentContextVertex.addChildGraphIfAbsent();

			final Vertex sourceComponentVertex = sourceDeploymentContextSubgraph.addVertexIfAbsent(sourceComponentId);
			final Vertex targetComponentVertex = targetDeploymentContextSubgraph.addVertexIfAbsent(targetComponentId);

			final Graph sourceComponentSubgraph = sourceComponentVertex.addChildGraphIfAbsent();
			final Graph targetComponentSubgraph = targetComponentVertex.addChildGraphIfAbsent();

			final Vertex sourceOperationVertex = sourceComponentSubgraph.addVertexIfAbsent(sourceOperationId);
			final Vertex targetOperationVertex = targetComponentSubgraph.addVertexIfAbsent(targetOperationId);

			final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceOperationVertex, targetOperationVertex));
			final Edge edge = sourceOperationVertex.addEdgeIfAbsent(edgeId, targetOperationVertex);
		}

		return graph;
	}

	// Component Dependecy Graph -> Deployment Level
	public Graph create2() {

		final ObjectIdentifierRegistry identifierRegistry = new ObjectIdentifierRegistry();

		final Graph graph = new GraphImpl();
		graph.setName("dep-graph"); // TODO

		final Collection<AggregatedInvocation> aggregatedInvocations = this.executionModel.getAggregatedInvocations().values();
		for (final AggregatedInvocation invocation : aggregatedInvocations) {

			final DeployedOperation sourceOperation = invocation.getSource();
			final DeployedOperation targetOperation = invocation.getTarget();

			// final int sourceOperationId = identifierRegistry.getIdentifier(sourceOperation);
			// final int targetOperationId = identifierRegistry.getIdentifier(targetOperation);

			final DeployedComponent sourceComponent = sourceOperation.getComponent();
			final DeployedComponent targetComponent = targetOperation.getComponent();

			final int sourceComponentId = identifierRegistry.getIdentifier(sourceComponent);
			final int targetComponentId = identifierRegistry.getIdentifier(targetComponent);

			final DeploymentContext sourceDeploymentContext = sourceComponent.getDeploymentContext();
			final DeploymentContext targetDeploymentContext = targetComponent.getDeploymentContext();

			final int sourceDeploymentContextId = identifierRegistry.getIdentifier(sourceDeploymentContext);
			final int targetDeploymentContextId = identifierRegistry.getIdentifier(targetDeploymentContext);

			final Vertex sourceDeploymentContextVertex = graph.addVertexIfAbsent(sourceDeploymentContextId);
			final Vertex targetDeploymentContextVertex = graph.addVertexIfAbsent(targetDeploymentContextId);

			final Graph sourceDeploymentContextSubgraph = sourceDeploymentContextVertex.addChildGraphIfAbsent();
			final Graph targetDeploymentContextSubgraph = targetDeploymentContextVertex.addChildGraphIfAbsent();

			final Vertex sourceComponentVertex = sourceDeploymentContextSubgraph.addVertexIfAbsent(sourceComponentId);
			final Vertex targetComponentVertex = targetDeploymentContextSubgraph.addVertexIfAbsent(targetComponentId);

			final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceComponentVertex, targetComponentVertex));
			final Edge edge = sourceComponentVertex.addEdgeIfAbsent(edgeId, targetComponentVertex);

			// final Graph sourceComponentSubgraph = sourceComponentVertex.addChildGraphIfAbsent();
			// final Graph targetComponentSubgraph = targetComponentVertex.addChildGraphIfAbsent();

			// final Vertex sourceOperationVertex = sourceComponentSubgraph.addVertexIfAbsent(sourceOperationId);
			// final Vertex targetOperationVertex = targetComponentSubgraph.addVertexIfAbsent(targetOperationId);

			// final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceOperationVertex, targetOperationVertex));
			// final Edge edge = sourceOperationVertex.addEdgeIfAbsent(edgeId, targetOperationVertex);
		}

		return graph;
	}

	// Deployment Context Dependecy Graph -> Deployment Level
	public Graph create3() {

		final ObjectIdentifierRegistry identifierRegistry = new ObjectIdentifierRegistry();

		final Graph graph = new GraphImpl();
		graph.setName("dep-graph"); // TODO

		final Collection<AggregatedInvocation> aggregatedInvocations = this.executionModel.getAggregatedInvocations().values();
		for (final AggregatedInvocation invocation : aggregatedInvocations) {

			final DeployedOperation sourceOperation = invocation.getSource();
			final DeployedOperation targetOperation = invocation.getTarget();

			// final int sourceOperationId = identifierRegistry.getIdentifier(sourceOperation);
			// final int targetOperationId = identifierRegistry.getIdentifier(targetOperation);

			final DeployedComponent sourceComponent = sourceOperation.getComponent();
			final DeployedComponent targetComponent = targetOperation.getComponent();

			// final int sourceComponentId = identifierRegistry.getIdentifier(sourceComponent);
			// final int targetComponentId = identifierRegistry.getIdentifier(targetComponent);

			final DeploymentContext sourceDeploymentContext = sourceComponent.getDeploymentContext();
			final DeploymentContext targetDeploymentContext = targetComponent.getDeploymentContext();

			final int sourceDeploymentContextId = identifierRegistry.getIdentifier(sourceDeploymentContext);
			final int targetDeploymentContextId = identifierRegistry.getIdentifier(targetDeploymentContext);

			final Vertex sourceDeploymentContextVertex = graph.addVertexIfAbsent(sourceDeploymentContextId);
			final Vertex targetDeploymentContextVertex = graph.addVertexIfAbsent(targetDeploymentContextId);

			final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceDeploymentContextVertex, targetDeploymentContextVertex));
			final Edge edge = sourceDeploymentContextVertex.addEdgeIfAbsent(edgeId, targetDeploymentContextVertex);

			// final Graph sourceDeploymentContextSubgraph = sourceDeploymentContextVertex.addChildGraphIfAbsent();
			// final Graph targetDeploymentContextSubgraph = targetDeploymentContextVertex.addChildGraphIfAbsent();

			// final Vertex sourceComponentVertex = sourceDeploymentContextSubgraph.addVertexIfAbsent(sourceComponentId);
			// final Vertex targetComponentVertex = targetDeploymentContextSubgraph.addVertexIfAbsent(targetComponentId);

			// final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceComponentVertex, targetComponentVertex));
			// final Edge edge = sourceComponentVertex.addEdgeIfAbsent(edgeId, targetComponentVertex);

			// final Graph sourceComponentSubgraph = sourceComponentVertex.addChildGraphIfAbsent();
			// final Graph targetComponentSubgraph = targetComponentVertex.addChildGraphIfAbsent();

			// final Vertex sourceOperationVertex = sourceComponentSubgraph.addVertexIfAbsent(sourceOperationId);
			// final Vertex targetOperationVertex = targetComponentSubgraph.addVertexIfAbsent(targetOperationId);

			// final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceOperationVertex, targetOperationVertex));
			// final Edge edge = sourceOperationVertex.addEdgeIfAbsent(edgeId, targetOperationVertex);
		}

		return graph;
	}

	// Operation Dependecy Graph -> Assembly Level
	public Graph create4() {

		final ObjectIdentifierRegistry identifierRegistry = new ObjectIdentifierRegistry();

		final Graph graph = new GraphImpl();
		graph.setName("dep-graph"); // TODO

		final Collection<AggregatedInvocation> aggregatedInvocations = this.executionModel.getAggregatedInvocations().values();
		for (final AggregatedInvocation invocation : aggregatedInvocations) {

			final AssemblyOperation sourceOperation = invocation.getSource().getAssemblyOperation();
			final AssemblyOperation targetOperation = invocation.getTarget().getAssemblyOperation();

			final int sourceOperationId = identifierRegistry.getIdentifier(sourceOperation);
			final int targetOperationId = identifierRegistry.getIdentifier(targetOperation);

			final AssemblyComponent sourceComponent = sourceOperation.getAssemblyComponent();
			final AssemblyComponent targetComponent = targetOperation.getAssemblyComponent();

			final int sourceComponentId = identifierRegistry.getIdentifier(sourceComponent);
			final int targetComponentId = identifierRegistry.getIdentifier(targetComponent);

			// final DeploymentContext sourceDeploymentContext = sourceComponent.getDeploymentContext();
			// final DeploymentContext targetDeploymentContext = targetComponent.getDeploymentContext();

			// final int sourceDeploymentContextId = identifierRegistry.getIdentifier(sourceDeploymentContext);
			// final int targetDeploymentContextId = identifierRegistry.getIdentifier(targetDeploymentContext);

			// final Vertex sourceDeploymentContextVertex = graph.addVertexIfAbsent(sourceDeploymentContextId);
			// final Vertex targetDeploymentContextVertex = graph.addVertexIfAbsent(targetDeploymentContextId);

			// final Graph sourceDeploymentContextSubgraph = sourceDeploymentContextVertex.addChildGraphIfAbsent();
			// final Graph targetDeploymentContextSubgraph = targetDeploymentContextVertex.addChildGraphIfAbsent();

			final Vertex sourceComponentVertex = graph.addVertexIfAbsent(sourceComponentId);
			final Vertex targetComponentVertex = graph.addVertexIfAbsent(targetComponentId);

			final Graph sourceComponentSubgraph = sourceComponentVertex.addChildGraphIfAbsent();
			final Graph targetComponentSubgraph = targetComponentVertex.addChildGraphIfAbsent();

			final Vertex sourceOperationVertex = sourceComponentSubgraph.addVertexIfAbsent(sourceOperationId);
			final Vertex targetOperationVertex = targetComponentSubgraph.addVertexIfAbsent(targetOperationId);

			final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceOperationVertex, targetOperationVertex));
			final Edge edge = sourceOperationVertex.addEdgeIfAbsent(edgeId, targetOperationVertex);
		}

		return graph;
	}

	// Component Dependecy Graph -> Assembly Level
	public Graph create5() {

		final ObjectIdentifierRegistry identifierRegistry = new ObjectIdentifierRegistry();

		final Graph graph = new GraphImpl();
		graph.setName("dep-graph"); // TODO

		final Collection<AggregatedInvocation> aggregatedInvocations = this.executionModel.getAggregatedInvocations().values();
		for (final AggregatedInvocation invocation : aggregatedInvocations) {

			final AssemblyOperation sourceOperation = invocation.getSource().getAssemblyOperation();
			final AssemblyOperation targetOperation = invocation.getTarget().getAssemblyOperation();

			// final int sourceOperationId = identifierRegistry.getIdentifier(sourceOperation);
			// final int targetOperationId = identifierRegistry.getIdentifier(targetOperation);

			final AssemblyComponent sourceComponent = sourceOperation.getAssemblyComponent();
			final AssemblyComponent targetComponent = targetOperation.getAssemblyComponent();

			final int sourceComponentId = identifierRegistry.getIdentifier(sourceComponent);
			final int targetComponentId = identifierRegistry.getIdentifier(targetComponent);

			// final DeploymentContext sourceDeploymentContext = sourceComponent.getDeploymentContext();
			// final DeploymentContext targetDeploymentContext = targetComponent.getDeploymentContext();

			// final int sourceDeploymentContextId = identifierRegistry.getIdentifier(sourceDeploymentContext);
			// final int targetDeploymentContextId = identifierRegistry.getIdentifier(targetDeploymentContext);

			// final Vertex sourceDeploymentContextVertex = graph.addVertexIfAbsent(sourceDeploymentContextId);
			// final Vertex targetDeploymentContextVertex = graph.addVertexIfAbsent(targetDeploymentContextId);

			// final Graph sourceDeploymentContextSubgraph = sourceDeploymentContextVertex.addChildGraphIfAbsent();
			// final Graph targetDeploymentContextSubgraph = targetDeploymentContextVertex.addChildGraphIfAbsent();

			final Vertex sourceComponentVertex = graph.addVertexIfAbsent(sourceComponentId);
			final Vertex targetComponentVertex = graph.addVertexIfAbsent(targetComponentId);

			final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceComponentVertex, targetComponentVertex));
			final Edge edge = sourceComponentVertex.addEdgeIfAbsent(edgeId, targetComponentVertex);

			// final Graph sourceComponentSubgraph = sourceComponentVertex.addChildGraphIfAbsent();
			// final Graph targetComponentSubgraph = targetComponentVertex.addChildGraphIfAbsent();

			// final Vertex sourceOperationVertex = sourceComponentSubgraph.addVertexIfAbsent(sourceOperationId);
			// final Vertex targetOperationVertex = targetComponentSubgraph.addVertexIfAbsent(targetOperationId);

			// final int edgeId = identifierRegistry.getIdentifier(Pair.of(sourceOperationVertex, targetOperationVertex));
			// final Edge edge = sourceOperationVertex.addEdgeIfAbsent(edgeId, targetOperationVertex);
		}

		return graph;
	}

}
