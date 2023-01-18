package kieker.analysis.generic.graph.clustering;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.impl.EdgeImpl;
import kieker.analysis.generic.graph.impl.NodeImpl;

public class BasicCostFunctionTest {

	private static final double NODE_COST = 1.0;
	private static final double EDGE_COST = 1.5;

	private final BasicCostFunction<INode, IEdge> costFunction = new BasicCostFunction<>(BasicCostFunctionTest.NODE_COST, BasicCostFunctionTest.EDGE_COST);

	@Before
	public void setUp() throws Exception {}

	@Test
	public void testComputeNodeInsertionCost() {
		final INode node = new NodeImpl("test");
		Assert.assertEquals(BasicCostFunctionTest.NODE_COST, this.costFunction.computeNodeInsertionCost(node), 0.0);
	}

	@Test
	public void testComputeEdgeInsertionCost() {
		final IEdge edge = new EdgeImpl("test");
		Assert.assertEquals(BasicCostFunctionTest.EDGE_COST, this.costFunction.computeEdgeInsertionCost(edge), 0.0);
	}

	@Test
	public void testNodeAnnotationDistance() {
		final INode node1 = new NodeImpl("node1");
		final INode node2 = new NodeImpl("node2");
		Assert.assertEquals(0.0, this.costFunction.nodeAnnotationDistance(node1, node2), 0.0);
	}

	@Test
	public void testEdgeAnnotationDistance() {
		final IEdge edge1 = new EdgeImpl("edge1");
		final IEdge edge2 = new EdgeImpl("edge2");
		Assert.assertEquals(0.0, this.costFunction.edgeAnnotationDistance(edge1, edge2), 0.0);
	}

}
