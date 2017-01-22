package kieker.analysisteetime.util.graph;

public interface Vertex extends GraphElement {

	public Graph addChildGraph();

	public boolean hasChildGraph();

	public Graph getChildGraph();

	public void removeChildGraph();

	public int getDepth();

	public Iterable<Edge> getEdges(Direction direction);

	public Iterable<Vertex> getVertices(Direction direction);

	public Edge addEdge(Vertex inVertex);

	public Edge addEdge(Object id, Vertex inVertex);

}
