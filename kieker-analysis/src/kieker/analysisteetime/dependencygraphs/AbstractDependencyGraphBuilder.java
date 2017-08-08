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

import java.time.temporal.ChronoUnit;

import kieker.analysisteetime.dependencygraphs.vertextypes.VertexType;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.statistics.Properties;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.statistics.Units;
import kieker.analysisteetime.util.ComposedKey;
import kieker.analysisteetime.util.ObjectIdentifierRegistry;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.impl.GraphImpl;

/**
 * Abstract template class for dependency graph builders. To use this abstract builder,
 * simply extend it and implement the {@code addVertex(DeployedOperation deployedOperation)}
 * method.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */

public abstract class AbstractDependencyGraphBuilder implements DependencyGraphBuilder {

	private static final Object ENTRY_VERTEX_IDENTIFIER = "entry";

	protected final Graph graph;
	protected final ObjectIdentifierRegistry identifierRegistry;
	protected final ResponseTimeDecorator responseTimeDecorator;

	protected final ExecutionModel executionModel;
	protected final StatisticsModel statisticsModel;

	public AbstractDependencyGraphBuilder(final ExecutionModel executionModel, final StatisticsModel statisticsModel) {
		this.graph = new GraphImpl();
		this.identifierRegistry = new ObjectIdentifierRegistry();
		this.responseTimeDecorator = new ResponseTimeDecorator(statisticsModel, ChronoUnit.NANOS);
		this.executionModel = executionModel;
		this.statisticsModel = statisticsModel;
	}

	@Override
	public Graph build() {
		for (final AggregatedInvocation invocation : this.executionModel.getAggregatedInvocations().values()) {
			this.handleInvocation(invocation);
		}
		return this.graph;
	}

	private void handleInvocation(final AggregatedInvocation invocation) {
		final Vertex sourceVertex = invocation.getSource() != null ? this.addVertex(invocation.getSource()) : this.addVertexForEntry();
		final Vertex targetVertex = this.addVertex(invocation.getTarget());
		final long calls = this.statisticsModel.get(invocation).getStatistic(Units.RESPONSE_TIME).getProperty(Properties.COUNT);
		this.addEdge(sourceVertex, targetVertex, calls);
	}

	protected abstract Vertex addVertex(final DeployedOperation deployedOperation);

	protected Vertex addVertexForEntry() {
		final int id = this.identifierRegistry.getIdentifier(ENTRY_VERTEX_IDENTIFIER);
		final Vertex vertex = this.graph.addVertexIfAbsent(id);
		vertex.setPropertyIfAbsent(PropertyKeys.TYPE, VertexType.ENTRY);
		vertex.setProperty(PropertyKeys.NAME, ENTRY_VERTEX_IDENTIFIER);
		return vertex;
	}

	private Edge addEdge(final Vertex source, final Vertex target, final long calls) {
		final int edgeId = this.identifierRegistry.getIdentifier(ComposedKey.of(source, target));
		final Edge edge = source.addEdgeIfAbsent(edgeId, target);
		edge.setPropertyIfAbsent(PropertyKeys.CALLS, calls);
		return edge;
	}

}
