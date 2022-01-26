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

package kieker.analysis.debug;

import java.io.PrintStream;

import kieker.analysis.graph.Direction;
import kieker.analysis.graph.IEdge;
import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.IVertex;

import teetime.framework.AbstractConsumerStage;

/**
 * This stage prints {@link IGraph}s to a given {@link PrintStream} or {@code System.out} if no one specified.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphPrinterStage extends AbstractConsumerStage<IGraph> {

	private final PrintStream printStream;

	public GraphPrinterStage() {
		this(System.out);
	}

	public GraphPrinterStage(final PrintStream printStream) {
		super();
		this.printStream = printStream;
	}

	@Override
	protected void execute(final IGraph graph) {
		for (final IVertex vertex : graph.getVertices()) {
			this.printStream.println("Vertices:");
			this.printStream.println(vertex.getId());
			this.printStream.println("    Vertices:");
			for (final IVertex vertex1 : vertex.getChildGraph().getVertices()) {
				this.printStream.println("    " + vertex1.getId());
				this.printStream.println("        Vertices:");
				for (final IVertex vertex2 : vertex1.getChildGraph().getVertices()) {
					this.printStream.println("        " + vertex2.getId());
				}
				this.printStream.println("        Edges:");
				for (final IEdge edge2 : vertex1.getChildGraph().getEdges()) {
					this.printStream.println("        " + edge2.getVertex(Direction.OUT).getId() + "->" + edge2.getVertex(Direction.IN).getId());
				}
			}
			this.printStream.println("    Edges:");
			for (final IEdge edge1 : vertex.getChildGraph().getEdges()) {
				this.printStream.println("    " + edge1.getVertex(Direction.OUT).getId() + "->" + edge1.getVertex(Direction.IN).getId());
			}
		}
		this.printStream.println("Edges:");
		for (final IEdge edge : graph.getEdges()) {
			this.printStream.println(edge.getVertex(Direction.OUT).getId() + "->" + edge.getVertex(Direction.IN).getId());
		}
	}

}
