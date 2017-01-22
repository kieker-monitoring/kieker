package kieker.analysisteetime.util.graph.export;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.traversal.EdgeVisitor;
import kieker.analysisteetime.util.graph.traversal.FlatGraphTraverser;
import kieker.analysisteetime.util.graph.traversal.GraphTraverser;
import kieker.analysisteetime.util.graph.traversal.VertexVisitor;

public abstract class AbstractTransformer<O> implements VertexVisitor, EdgeVisitor {

	private final GraphTraverser graphTraverser = new FlatGraphTraverser(this, this);

	protected Graph graph;

	protected AbstractTransformer(final Graph graph) {
		this.graph = graph;
	}

	public final O transform() {

		beforeTransformation();

		graphTraverser.traverse(graph);

		afterTransformation();

		return getTransformation();
	}

	protected abstract void beforeTransformation();

	protected abstract void afterTransformation();

	protected abstract void transformVertex(Vertex vertex);

	protected abstract void transformEdge(Edge edge);

	protected abstract O getTransformation();

	@Override
	public void visitVertex(final Vertex vertex) {
		transformVertex(vertex);
	}

	@Override
	public void visitEdge(final Edge edge) {
		transformEdge(edge);
	}

}
