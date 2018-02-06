package kieker.analysisteetime.util.graph.traversal;

import kieker.analysisteetime.util.graph.Edge;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface EdgeVisitor {

	public void visitEdge(Edge edge);

}
