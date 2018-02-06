package kieker.analysisteetime.util.graph;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface Graph extends Element {

	public String getName();

	public void setName(String name);

	public Vertex addVertex(Object id);

	public Vertex addVertexIfAbsent(Object id);

	public Vertex getVertex(Object id);

	public void removeVertex(Vertex vertex);

	public Iterable<Vertex> getVertices();

	public Edge addEdge(Object id, Vertex outVertex, Vertex inVertex);

	public Edge addEdgeIfAbsent(Object id, Vertex outVertex, Vertex inVertex);

	public Edge getEdge(Object id);

	public void removeEdge(Edge edge);

	public Iterable<Edge> getEdges();

}
