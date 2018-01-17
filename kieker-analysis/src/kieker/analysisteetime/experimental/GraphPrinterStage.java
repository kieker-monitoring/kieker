package kieker.analysisteetime.experimental;

import java.io.PrintStream;

import kieker.analysisteetime.util.graph.Direction;
import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

import teetime.framework.AbstractConsumerStage;

public class GraphPrinterStage extends AbstractConsumerStage<Graph> {

	private final PrintStream printStream;

	public GraphPrinterStage() {
		this(System.out);
	}

	public GraphPrinterStage(final PrintStream printStream) {
		this.printStream = printStream;
	}

	@Override
	protected void execute(final Graph graph) {
		for (final Vertex vertex : graph.getVertices()) {
			this.printStream.println("Vertices:");
			this.printStream.println(vertex.getId());
			this.printStream.println("    Vertices:");
			for (final Vertex vertex1 : vertex.getChildGraph().getVertices()) {
				this.printStream.println("    " + vertex1.getId());
				this.printStream.println("        Vertices:");
				for (final Vertex vertex2 : vertex1.getChildGraph().getVertices()) {
					this.printStream.println("        " + vertex2.getId());
				}
				this.printStream.println("        Edges:");
				for (final Edge edge2 : vertex1.getChildGraph().getEdges()) {
					this.printStream.println("        " + edge2.getVertex(Direction.OUT).getId() + "->" + edge2.getVertex(Direction.IN).getId());
				}
			}
			this.printStream.println("    Edges:");
			for (final Edge edge1 : vertex.getChildGraph().getEdges()) {
				this.printStream.println("    " + edge1.getVertex(Direction.OUT).getId() + "->" + edge1.getVertex(Direction.IN).getId());
			}
		}
		this.printStream.println("Edges:");
		for (final Edge edge : graph.getEdges()) {
			this.printStream.println(edge.getVertex(Direction.OUT).getId() + "->" + edge.getVertex(Direction.IN).getId());
		}
	}

}
