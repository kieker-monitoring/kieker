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

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
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
		final Vertex vertex = this.graph.addVertex(operationCall.hashCode()); // TODO hashCode

		// TODO Perform the mapping

		// String name = operationCall.getOperation();
		// String stackDepth = String.valueOf(operationCall.getStackDepth() + 1);
		// String component = operationCall.getComponent();
		// String container = operationCall.getContainer();
		//
		// Boolean shortNames = true; // TODO hard coded
		//
		// if (shortNames) {
		// name = NameConverter.toShortOperationName(name);
		// }
		//
		// String label = container + "::\\n" + "@" + stackDepth + ":" + component + "\\n" + name;

		// vertex.setProperty("label", label);

		return vertex;
	}

	private Edge addEdge(final OperationCall operationCall, final OperationCall parentOperationCall) {

		final Vertex thisVertex = this.graph.getVertex(operationCall.hashCode()); // TODO hashCode
		final Vertex parentVertex = this.graph.getVertex(operationCall.getParent().hashCode()); // TODO hashCode

		if ((thisVertex == null) || (parentVertex == null)) {
			return null; // TODO
		}

		final Edge edge = this.graph.addEdge(null, parentVertex, thisVertex);
		// TODO Perform the mapping
		// edge.setProperty("label", String.valueOf(operationCall.getOrderIndex() + 1) + '.');
		return edge;
	}

	private Vertex addRootVertex(final OperationCall rootOperationCall) {

		final Vertex realRootVertex = this.graph.getVertex(rootOperationCall.hashCode()); // TODO hashCode

		if (realRootVertex == null) {
			return null; // TODO
		}

		// TODO Perform the mapping
		final Vertex rootVertex = this.graph.addVertex("Entry");
		// rootVertex.setProperty("label", "'Entry'");
		final Edge edge = this.graph.addEdge(null, rootVertex, realRootVertex);
		// edge.setProperty("label", "1.");

		return rootVertex;
	}

}
