/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.analysis.generic.graph.clustering;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.impl.EdgeImpl;
import kieker.analysis.generic.graph.impl.NodeImpl;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class BasicCostFunctionTest { // NOCS tests do not need constructors

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
