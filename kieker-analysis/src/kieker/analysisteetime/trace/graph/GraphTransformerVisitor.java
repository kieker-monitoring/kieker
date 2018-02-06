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

package kieker.analysisteetime.trace.graph;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.OperationType;
import kieker.analysisteetime.trace.traversal.OperationCallVisitor;
import kieker.analysisteetime.util.ObjectIdentifierRegistry;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class GraphTransformerVisitor implements OperationCallVisitor {

	private final Graph graph;
	private final ObjectIdentifierRegistry objectIdentifierRegistry = new ObjectIdentifierRegistry();

	public GraphTransformerVisitor(final Graph graph) {
		super();
		this.graph = graph;
	}

	@Override
	public void visit(final OperationCall operationCall) {
		this.addVertex(operationCall);

		if (operationCall.getParent() != null) {
			this.addEdge(operationCall);
		} else {
			this.addRootVertex(operationCall);
		}
	}

	private Vertex addVertex(final OperationCall operationCall) {
		final int vertexId = this.objectIdentifierRegistry.getIdentifier(operationCall);
		final Vertex vertex = this.graph.addVertex(vertexId);

		final OperationType operationType = operationCall.getOperation().getAssemblyOperation().getOperationType();
		final ComponentType componentType = operationType.getComponentType();
		final DeploymentContext deploymentContext = operationCall.getOperation().getComponent().getDeploymentContext();

		vertex.setProperty("name", operationType.getName());
		vertex.setProperty("returnType", operationType.getReturnType());
		vertex.setProperty("modifiers", operationType.getModifiers());
		vertex.setProperty("parameterTypes", operationType.getParameterTypes());
		vertex.setProperty("component", componentType.getName());
		vertex.setProperty("package", componentType.getPackage());
		vertex.setProperty("deploymentContext", deploymentContext.getName());
		vertex.setProperty("stackDepth", operationCall.getStackDepth());
		// ... maybe further parameters

		return vertex;
	}

	private Edge addEdge(final OperationCall operationCall) {

		final int thisVertexId = this.objectIdentifierRegistry.getIdentifier(operationCall);
		final Vertex thisVertex = this.graph.getVertex(thisVertexId);
		final int parentVertexId = this.objectIdentifierRegistry.getIdentifier(operationCall.getParent());
		final Vertex parentVertex = this.graph.getVertex(parentVertexId);

		if (thisVertex == null) {
			throw new IllegalStateException("Target vertex not found (operationCall:" + operationCall + ").");
		} else if (parentVertex == null) {
			throw new IllegalStateException("Source vertex not found (operationCall:" + operationCall.getParent() + ").");
		}

		final Edge edge = this.graph.addEdge(null, parentVertex, thisVertex);
		edge.setProperty("orderIndex", operationCall.getOrderIndex() + 1);

		return edge;
	}

	private Vertex addRootVertex(final OperationCall rootOperationCall) {
		final int rootVertexId = this.objectIdentifierRegistry.getIdentifier(rootOperationCall);
		final Vertex rootVertex = this.graph.getVertex(rootVertexId);

		if (rootVertex == null) {
			throw new IllegalStateException("Root vertex not found (operationCall:" + rootOperationCall + ").");
		}

		final Vertex entryVertex = this.graph.addVertex("'Entry'");
		entryVertex.setProperty("artificial", true);
		entryVertex.setProperty("name", "'Entry'");

		final Edge edge = this.graph.addEdge(null, entryVertex, rootVertex);
		edge.setProperty("orderIndex", 1);

		return entryVertex;
	}

}
