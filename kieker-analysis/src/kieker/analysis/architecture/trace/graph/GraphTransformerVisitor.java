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

import java.util.Optional;

import kieker.analysis.architecture.trace.traversal.IOperationCallVisitor;
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.util.FullyQualifiedNamesFactory;
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

	private INode addVertex(final OperationCall operationCall) {
		final INode vertex = GraphFactory.createNode(FullyQualifiedNamesFactory.createFullyQualifiedName(operationCall));

		final OperationType operationType = operationCall.getOperation().getAssemblyOperation().getOperationType();
		final ComponentType componentType = operationType.getComponentType();
		final DeploymentContext deploymentContext = operationCall.getOperation().getComponent().getContext();

		vertex.setProperty("name", operationType.getName());
		vertex.setProperty("returnType", operationType.getReturnType());
		vertex.setProperty("modifiers", operationType.getModifiers());
		vertex.setProperty("parameterTypes", operationType.getParameterTypes());
		vertex.setProperty("component", componentType.getName());
		vertex.setProperty("package", componentType.getPackage());
		vertex.setProperty("deploymentContext", deploymentContext.getName());
		vertex.setProperty("stackDepth", operationCall.getStackDepth());
		// ... maybe further parameters
		this.graph.getGraph().addNode(vertex);

		return vertex;
	}

	private IEdge addEdge(final OperationCall operationCall) {
		final String thisVertexId = FullyQualifiedNamesFactory.createFullyQualifiedName(operationCall);
		final Optional<INode> thisVertex = this.graph.findNode(thisVertexId);
		final String parentVertexId = FullyQualifiedNamesFactory.createFullyQualifiedName(operationCall.getParent());
		final Optional<INode> parentVertex = this.graph.findNode(parentVertexId);

		if (!thisVertex.isPresent()) {
			throw new IllegalStateException("Target vertex not found (operationCall:" + operationCall + ").");
		}
		if (!parentVertex.isPresent()) {
			throw new IllegalStateException("Source vertex not found (operationCall:" + operationCall.getParent() + ").");
		}

		final IEdge edge = GraphFactory.createEdge(null);
		edge.setProperty("orderIndex", operationCall.getOrderIndex() + 1);

		this.graph.getGraph().addEdge(parentVertex.get(), thisVertex.get(), edge);

		return edge;
	}

	private INode addRootVertex(final OperationCall rootOperationCall) {
		final String rootVertexId = FullyQualifiedNamesFactory.createFullyQualifiedName(rootOperationCall);
		final Optional<INode> rootVertex = this.graph.findNode(rootVertexId);
		if (!rootVertex.isPresent()) {
			throw new IllegalStateException("Root vertex not found (operationCall:" + rootOperationCall + ").");
		}

		final INode entryVertex = GraphFactory.createNode("'Entry'");
		entryVertex.setProperty("artificial", true);
		entryVertex.setProperty("name", "'Entry'");

		final IEdge edge = GraphFactory.createEdge(null);
		edge.setProperty("orderIndex", 1);
		this.graph.getGraph().addEdge(entryVertex, rootVertex.get(), edge);

		return entryVertex;
	}

}
