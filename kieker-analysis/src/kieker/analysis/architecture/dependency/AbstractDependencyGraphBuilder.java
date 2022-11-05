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
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;

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

	private static final String ENTRY_VERTEX_IDENTIFIER = "entry";

	protected IGraph graph;
	protected ResponseTimeDecorator responseTimeDecorator;

	protected ExecutionModel executionModel;
	protected StatisticsModel statisticsModel;

	public AbstractDependencyGraphBuilder() {}

	@Override
	public IGraph build(final ModelRepository repository) {
		this.graph = GraphFactory.createGraph(repository.getName());

		this.executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);
		this.statisticsModel = repository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL);
		this.responseTimeDecorator = new ResponseTimeDecorator(this.statisticsModel, ChronoUnit.NANOS);
		for (final Invocation invocation : this.executionModel.getInvocations().values()) {
			this.handleInvocation(invocation);
		}
		return this.graph;
	}

	private void handleInvocation(final Invocation invocation) {
		final INode sourceVertex = invocation.getCaller() != null ? this.addVertex(invocation.getCaller()) : this.addVertexForEntry(); // NOCS (declarative)
		final INode targetVertex = this.addVertex(invocation.getCallee());
		final long calls = (Long) this.statisticsModel.getStatistics().get(invocation).getProperties().get(PropertyConstants.CALLS);
		this.addEdge(sourceVertex, targetVertex, calls);
	}

	protected abstract INode addVertex(final DeployedOperation deployedOperation);

	protected INode addVertexForEntry() {
		final Optional<INode> nodeOptional = this.graph.findNode(ENTRY_VERTEX_IDENTIFIER);
		final INode vertex;
		if (nodeOptional.isPresent()) {
			vertex = nodeOptional.get();
		} else {
			vertex = GraphFactory.createNode(ENTRY_VERTEX_IDENTIFIER);
			this.graph.getGraph().addNode(vertex);
		}

		vertex.setPropertyIfAbsent(PropertyConstants.TYPE, VertexType.ENTRY);
		vertex.setProperty(PropertyConstants.NAME, ENTRY_VERTEX_IDENTIFIER);
		return vertex;
	}

	protected IEdge addEdge(final INode source, final INode target, final long calls) {
		final String edgeId = String.format("%s-%s", source, target);
		final Optional<IEdge> edgeOptional = this.graph.findEdge(edgeId);

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

	protected IGraph addChildGraphIfAbsent(final INode node) {
		if (!node.hasChildGraph()) {
			node.createChildGraph();
		}
		return node.getChildGraph();
	}

	protected INode addVertexIfAbsent(final IGraph checkGraph, final String id) {
		final Optional<INode> nodeOptional = checkGraph.findNode(id);
		if (nodeOptional.isPresent()) {
			return nodeOptional.get();
		} else {
			final INode node = GraphFactory.createNode(id);
			checkGraph.getGraph().addNode(node);
			return node;
		}
	}

}
