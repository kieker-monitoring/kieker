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
import kieker.analysis.architecture.trace.traversal.TraceTraverser;
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.trace.Trace;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceToGraphTransformer {

	// BETTER integrate GraphTransformerVisitor in this?

	private final TraceTraverser traceTraverser = new TraceTraverser();

	public TraceToGraphTransformer() {
		// Create a transformer
	}

	public IGraph<INode, IEdge> transform(final Trace trace) {
		final IGraph<INode, IEdge> graph = GraphFactory.createGraph("trace-" + trace.getTraceID());
		final IOperationCallVisitor visitor = new GraphTransformerVisitor(graph);
		this.traceTraverser.traverse(trace, visitor);
		return graph;
	}

}
