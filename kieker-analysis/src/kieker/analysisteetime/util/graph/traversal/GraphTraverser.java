package kieker.analysisteetime.util.graph.traversal;

import java.util.ArrayList;
import java.util.List;

import kieker.analysisteetime.util.graph.Graph;

public abstract class GraphTraverser {

	protected List<VertexVisitor> vertexVisitors;
	protected List<EdgeVisitor> edgeVisitors;

	public GraphTraverser() {
		this.vertexVisitors = new ArrayList<>();
		this.edgeVisitors = new ArrayList<>();
	}

	public GraphTraverser(final VertexVisitor vertexVisitor, final EdgeVisitor edgeVisitor) {
		this();
		vertexVisitors.add(vertexVisitor);
		edgeVisitors.add(edgeVisitor);
	}

	public GraphTraverser(final List<VertexVisitor> vertexVisitors, final List<EdgeVisitor> edgeVisitors) {
		this.vertexVisitors = vertexVisitors;
		this.edgeVisitors = edgeVisitors;
	}

	public List<VertexVisitor> getVertexVisitors() {
		return vertexVisitors;
	}

	public void setVertexVisitors(final List<VertexVisitor> vertexVisitors) {
		this.vertexVisitors = vertexVisitors;
	}

	public void addVertexVisitor(final VertexVisitor vertexVisitor) {
		vertexVisitors.add(vertexVisitor);
	}

	public List<EdgeVisitor> getEdgeVisitors() {
		return edgeVisitors;
	}

	public void setEdgeVisitors(final List<EdgeVisitor> edgeVisitors) {
		this.edgeVisitors = edgeVisitors;
	}

	public void addEdgeVisitor(final EdgeVisitor edgeVisitor) {
		edgeVisitors.add(edgeVisitor);
	}

	public abstract void traverse(final Graph graph);

}
