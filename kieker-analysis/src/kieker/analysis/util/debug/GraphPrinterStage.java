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

package kieker.analysis.util.debug;

import java.io.PrintStream;

import com.google.common.graph.EndpointPair;

import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.INode;

import teetime.framework.AbstractConsumerStage;

/**
 * This stage prints {@link Graph}s to a given {@link PrintStream} or {@code System.out} if no one specified.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphPrinterStage extends AbstractConsumerStage<IGraph<INode, IEdge>> {

	private final PrintStream printStream;

	public GraphPrinterStage() {
		this(System.out);
	}

	public GraphPrinterStage(final PrintStream printStream) {
		super();
		this.printStream = printStream;
	}

	@Override
	protected void execute(final IGraph<INode, IEdge> graph) {
		this.printGraph(graph, "");
	}

	private void printGraph(final IGraph<INode, IEdge> graph, final String offset) {
		this.printStream.printf("%sGraph %s Vertices:\n", graph.getLabel(), offset);
		for (final INode node : graph.getGraph().nodes()) {
			this.printStream.printf("%s%s\n", offset, node.getId());
			if (node.getChildGraph() != null) {
				this.printGraph(node.getChildGraph(), "   " + offset);
			}
		}
		this.printStream.printf("%sEdges:\n", offset);
		for (final IEdge edge : graph.getGraph().edges()) {
			final EndpointPair<INode> nodePair = graph.getGraph().incidentNodes(edge);
			final String sourceLabel;
			final String targetLabel;
			if (graph.getGraph().isDirected()) {
				sourceLabel = nodePair.source().getId();
				targetLabel = nodePair.target().getId();
			} else {
				sourceLabel = nodePair.nodeU().getId();
				targetLabel = nodePair.nodeV().getId();
			}

			this.printStream.printf("%s -> %s\n", sourceLabel, targetLabel);
		}
	}

}
