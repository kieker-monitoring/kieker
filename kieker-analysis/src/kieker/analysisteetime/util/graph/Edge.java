package kieker.analysisteetime.util.graph;

public interface Edge extends GraphElement {

	public Vertex getVertex(Direction direction) throws IllegalArgumentException;

}
