package kieker.analysisteetime.util.graph.traversal;

import java.util.List;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

public class FlatGraphTraverser extends GraphTraverser {

	public FlatGraphTraverser() {
		super();
	}

	public FlatGraphTraverser(final List<VertexVisitor> vertexVisitors, final List<EdgeVisitor> edgeVisitors) {
		super(vertexVisitors, edgeVisitors);
	}

	public FlatGraphTraverser(final VertexVisitor vertexVisitor, final EdgeVisitor edgeVisitor) {
		super(vertexVisitor, edgeVisitor);
	}

	@Override
	public void traverse(final Graph graph) {

		for (Vertex vertex : graph.getVertices()) {
			for (VertexVisitor visitor : vertexVisitors) {
				visitor.visitVertex(vertex);
			}
		}

		for (Edge edge : graph.getEdges()) {
			for (EdgeVisitor visitor : edgeVisitors) {
				visitor.visitEdge(edge);
			}
		}

	}

}
