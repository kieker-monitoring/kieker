package kieker.analysisteetime.util.graph.traversal;

import kieker.analysisteetime.util.graph.Vertex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public interface VertexVisitor {

	public void visitVertex(Vertex vertex);

}
