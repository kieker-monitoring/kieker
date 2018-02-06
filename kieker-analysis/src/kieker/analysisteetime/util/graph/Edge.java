package kieker.analysisteetime.util.graph;

/**
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public interface Edge extends GraphElement {

	public Vertex getVertex(Direction direction) throws IllegalArgumentException;

}
