package kieker.analysisteetime.util.graph;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface Vertex extends GraphElement {

	public Graph addChildGraph();

	public Graph addChildGraphIfAbsent();

	public boolean hasChildGraph();

	public Graph getChildGraph();

	public void removeChildGraph();

	public int getDepth();

	public Iterable<Edge> getEdges(Direction direction);

	public Iterable<Vertex> getVertices(Direction direction);

	public Edge addEdge(Vertex inVertex);

	public Edge addEdge(Object id, Vertex inVertex);

	public Edge addEdgeIfAbsent(Object id, Vertex inVertex);

}
