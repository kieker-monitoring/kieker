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

package kieker.analysis.architecture.trace.graph;

import kieker.analysis.architecture.trace.traversal.IOperationCallVisitor;
import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.IVertex;
import kieker.analysis.util.ObjectIdentifierRegistry;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphTransformerVisitor implements IOperationCallVisitor {

	private final IGraph graph;
	private final ObjectIdentifierRegistry objectIdentifierRegistry = new ObjectIdentifierRegistry();

	public GraphTransformerVisitor(final IGraph graph) {
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

	private IVertex addVertex(final OperationCall operationCall) {
		final int vertexId = this.objectIdentifierRegistry.getIdentifier(operationCall);
		final IVertex vertex = this.graph.addVertex(vertexId);

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

	private IEdge addEdge(final OperationCall operationCall) {
		final int thisVertexId = this.objectIdentifierRegistry.getIdentifier(operationCall);
		final IVertex thisVertex = this.graph.getVertex(thisVertexId);
		final int parentVertexId = this.objectIdentifierRegistry.getIdentifier(operationCall.getParent());
		final IVertex parentVertex = this.graph.getVertex(parentVertexId);

		if (thisVertex == null) {
			throw new IllegalStateException("Target vertex not found (operationCall:" + operationCall + ").");
		} else if (parentVertex == null) {
			throw new IllegalStateException(
					"Source vertex not found (operationCall:" + operationCall.getParent() + ").");
		}

		final IEdge edge = this.graph.addEdge(null, parentVertex, thisVertex);
		edge.setProperty("orderIndex", operationCall.getOrderIndex() + 1);

		return edge;
	}

	private IVertex addRootVertex(final OperationCall rootOperationCall) {
		final int rootVertexId = this.objectIdentifierRegistry.getIdentifier(rootOperationCall);
		final IVertex rootVertex = this.graph.getVertex(rootVertexId);

		if (rootVertex == null) {
			throw new IllegalStateException("Root vertex not found (operationCall:" + rootOperationCall + ").");
		}

		final IVertex entryVertex = this.graph.addVertex("'Entry'");
		entryVertex.setProperty("artificial", true);
		entryVertex.setProperty("name", "'Entry'");

		final IEdge edge = this.graph.addEdge(null, entryVertex, rootVertex);
		edge.setProperty("orderIndex", 1);

		return entryVertex;
	}

}
