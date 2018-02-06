package kieker.analysisteetime.util.graph.traversal;

import java.util.List;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DeepGraphTraverser extends AbstractGraphTraverser {

	public DeepGraphTraverser() {
		super();
	}

	public DeepGraphTraverser(final List<VertexVisitor> vertexVisitors, final List<EdgeVisitor> edgeVisitors) {
		super(vertexVisitors, edgeVisitors);
	}

	public DeepGraphTraverser(final VertexVisitor vertexVisitor, final EdgeVisitor edgeVisitor) {
		super(vertexVisitor, edgeVisitor);
	}

	@Override
	public void traverse(final Graph graph) {
		for (final Vertex vertex : graph.getVertices()) {
			for (final VertexVisitor visitor : this.vertexVisitors) {
				visitor.visitVertex(vertex);
			}
			if (vertex.hasChildGraph()) {
				this.traverse(vertex.getChildGraph());
			}
		}

		for (final Edge edge : graph.getEdges()) {
			for (final EdgeVisitor visitor : this.edgeVisitors) {
				visitor.visitEdge(edge);
			}
		}
	}

}
