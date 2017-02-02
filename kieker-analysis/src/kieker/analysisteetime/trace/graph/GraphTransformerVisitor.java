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
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class GraphTransformerVisitor extends OperationCallVisitor {

	private final Graph graph;

	public GraphTransformerVisitor(final Graph graph) {
		super();
		this.graph = graph;
	}

	@Override
	public void visit(final OperationCall operationCall) {
		this.addVertex(operationCall);

		if (operationCall.getParent() != null) {
			this.addEdge(operationCall, operationCall.getParent());
		} else {
			this.addRootVertex(operationCall);
		}
	}

	private Vertex addVertex(final OperationCall operationCall) {
		// FIXME Using the hashCode() method here is a bad idea,
		// since it can lead to collisions. We need an absolute unique identifier
		// which could be the object itself or an identifier field.
		final Vertex vertex = this.graph.addVertex(operationCall.hashCode());

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
		// ... maybe further parameters

		return vertex;
	}

	private Edge addEdge(final OperationCall operationCall, final OperationCall parentOperationCall) {

		final Vertex thisVertex = this.graph.getVertex(operationCall.hashCode()); // TODO hashCode
		final Vertex parentVertex = this.graph.getVertex(operationCall.getParent().hashCode()); // TODO hashCode

		if (thisVertex == null) {
			throw new IllegalStateException("Target vertex not found (operationCall:" + operationCall + ").");
		} else if (parentVertex == null) {
			throw new IllegalStateException("Source vertex not found (operationCall:" + operationCall.getParent() + ").");
		}

		final Edge edge = this.graph.addEdge(null, parentVertex, thisVertex);
		edge.setProperty("orderIndex", operationCall.getOrderIndex());

		return edge;
	}

	private Vertex addRootVertex(final OperationCall rootOperationCall) {
		final Vertex realRootVertex = this.graph.getVertex(rootOperationCall.hashCode()); // TODO hashCode

		if (realRootVertex == null) {
			throw new IllegalStateException("Root vertex not found (operationCall:" + rootOperationCall + ").");
		}

		final Vertex rootVertex = this.graph.addVertex("'Entry'");
		rootVertex.setProperty("artificial", true);
		rootVertex.setProperty("name", "'Entry'");

		final Edge edge = this.graph.addEdge(null, rootVertex, realRootVertex);
		edge.setProperty("orderIndex", 1);

		return rootVertex;
	}

}
