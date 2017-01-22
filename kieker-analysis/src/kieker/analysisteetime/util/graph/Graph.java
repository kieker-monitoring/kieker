package kieker.analysisteetime.util.graph;

public interface Graph extends Element {

	public String getName();

	public void setName(String name);

	public Vertex addVertex(Object id);

	public Vertex getVertex(Object id);

	public void removeVertex(Vertex vertex);

	public Iterable<Vertex> getVertices();

	public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex);

	public Edge getEdge(Object id);

	public void removeEdge(Edge edge);

	public Iterable<Edge> getEdges();

}
