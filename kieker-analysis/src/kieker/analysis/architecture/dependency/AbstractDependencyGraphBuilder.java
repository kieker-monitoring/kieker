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

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.graph.GraphFactory;
import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.INode;
import kieker.analysis.graph.dependency.vertextypes.VertexType;
import kieker.analysis.util.ObjectIdentifierRegistry;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.AggregatedInvocation;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.statistics.EPredefinedUnits;
import kieker.model.analysismodel.statistics.EPropertyType;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.util.ComposedKey;

/**
 * Abstract template class for dependency graph builders. To use this abstract builder,
 * simply extend it and implement the {@code addVertex(DeployedOperation deployedOperation)}
 * method.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */

public abstract class AbstractDependencyGraphBuilder implements IDependencyGraphBuilder {

	private static final Object ENTRY_VERTEX_IDENTIFIER = "entry";

	protected IGraph<INode, IEdge> graph;
	protected ObjectIdentifierRegistry identifierRegistry;
	protected ResponseTimeDecorator responseTimeDecorator;

	protected ExecutionModel executionModel;
	protected StatisticsModel statisticsModel;

	public AbstractDependencyGraphBuilder() {}

	@Override
	public IGraph<INode, IEdge> build(final ModelRepository repository) {
		// TODO this must be refactored and separated out in a separate function
		this.graph = GraphFactory.createGraph(repository.getName());

		this.executionModel = repository.getModel(ExecutionModel.class);
		this.statisticsModel = repository.getModel(StatisticsModel.class);
		this.identifierRegistry = new ObjectIdentifierRegistry();
		this.responseTimeDecorator = new ResponseTimeDecorator(this.statisticsModel, ChronoUnit.NANOS);
		for (final AggregatedInvocation invocation : this.executionModel.getAggregatedInvocations().values()) {
			this.handleInvocation(invocation);
		}
		return this.graph;
	}

	private void handleInvocation(final AggregatedInvocation invocation) {
		final INode sourceVertex = invocation.getSource() != null ? this.addVertex(invocation.getSource()) : this.addVertexForEntry(); // NOCS (declarative)
		final INode targetVertex = this.addVertex(invocation.getTarget());
		final long calls = (Long) this.statisticsModel.getStatistics().get(invocation).getStatistics().get(EPredefinedUnits.INVOCATION).getProperties()
				.get(EPropertyType.COUNT);
		this.addEdge(sourceVertex, targetVertex, calls);
	}

	protected abstract INode addVertex(final DeployedOperation deployedOperation);

	protected INode addVertexForEntry() {
		final String id = String.valueOf(this.identifierRegistry.getIdentifier(ENTRY_VERTEX_IDENTIFIER));
		final Optional<INode> nodeOptional = this.graph.getGraph().nodes().stream().filter(node -> id.equals(node.getId())).findFirst();
		final INode vertex;
		if (nodeOptional.isPresent()) {
			vertex = nodeOptional.get();
		} else {
			vertex = GraphFactory.createNode(id);
			this.graph.getGraph().addNode(vertex);
		}

		vertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ENTRY);
		vertex.setProperty(PropertyConstants.NAME, ENTRY_VERTEX_IDENTIFIER);
		return vertex;
	}

	protected IEdge addEdge(final INode source, final INode target, final long calls) {
		final String edgeId = String.valueOf(this.identifierRegistry.getIdentifier(ComposedKey.of(source, target)));
		final Optional<IEdge> edgeOptional = this.graph.getGraph().edges().stream().filter(edge -> edgeId.equals(edge.getId())).findFirst();

		final IEdge edge;
		if (edgeOptional.isPresent()) {
			edge = edgeOptional.get();
		} else {
			edge = GraphFactory.createEdge(edgeId);
			this.graph.getGraph().addEdge(source, target, edge);
		}

		edge.setPropertyIfAbsent(PropertyConstants.CALLS, calls);
		return edge;
	}

	protected IGraph<INode, IEdge> addChildGraphIfAbsent(final INode node) {
		if (!node.hasChildGraph()) {
			node.createChildGraph();
		}
		return node.getChildGraph();
	}

	protected INode addVertexIfAbsent(final IGraph<INode, IEdge> checkGraph, final String id) {
		final Optional<INode> nodeOptional = checkGraph.getGraph().nodes().stream().filter(node -> id.equals(node.getId())).findFirst();
		if (nodeOptional.isPresent()) {
			return nodeOptional.get();
		} else {
			final INode node = GraphFactory.createNode(id);
			checkGraph.getGraph().addNode(node);
			return node;
		}
	}

}
