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
package kieker.analysis.behavior.clustering;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.BasicCostFunction;
import kieker.analysis.generic.graph.impl.EdgeImpl;
import kieker.analysis.generic.graph.impl.NodeImpl;

public class UserBehaviorCostFunctionTest {

	private static final double NODE_COST = 1.0;
	private static final double EDGE_COST = 2.0;
	private static final double EDGE_GROUP_COST = 3.0;
	private static final double PARAMETER_WEIGHT = 0.5;
	
	UserBehaviorCostFunction userBehaviorCostFunction;
	BasicCostFunction<INode, IEdge> basicCostFunction;
	
	@Before
	public void setUp() {
		userBehaviorCostFunction = new UserBehaviorCostFunction(NODE_COST, EDGE_COST, EDGE_GROUP_COST, new IParameterWeighting() {
			
			@Override
			public double getInsertCost(String[] parameterNames) {
				return PARAMETER_WEIGHT;
			}
			
			@Override
			public double getDuplicateCost(String[] parameterNames) {
				return 1;
			}
		});
		basicCostFunction = new BasicCostFunction<>(NODE_COST, EDGE_COST);
	}

	@Test
	public void testComputeEdgeInsertionCostUserBehaviorEdge() {	
		UserBehaviorEdge edge = new UserBehaviorEdge("simple");
		assertEquals("Wrong edge cost", EDGE_COST, userBehaviorCostFunction.computeEdgeInsertionCost(edge), 0.0);
	}

	@Test
	public void testEdgeAnnotationDistanceUserBehaviorEdgeUserBehaviorEdge() {
		UserBehaviorEdge edge1 = new UserBehaviorEdge("simple1");
		EntryCallEvent event1 = new EntryCallEvent(0, 0, "op", "class", "session1", "host", null, null, 0);
		edge1.addEvent(event1);
		UserBehaviorEdge edge2 = new UserBehaviorEdge("simple2");
		EntryCallEvent event2 = new EntryCallEvent(0, 0, "op2", "class", "session1", "host", null, null, 0);
		edge2.addEvent(event2);
		
		UserBehaviorEdge edge3 = new UserBehaviorEdge("simple3");
		String[] params = { "a" };
		String[] values = { "b" };
		EntryCallEvent event3 = new EntryCallEvent(0, 0, "op2", "class", "session1", "host", params,  values , 0);
		edge3.addEvent(event3);		
		assertEquals("Wrong annotation distance", EDGE_GROUP_COST*2+PARAMETER_WEIGHT*2, userBehaviorCostFunction.edgeAnnotationDistance(edge1, edge3), 0.0);
	}

	@Test
	public void testComputeNodeInsertionCost() {
		INode node = new NodeImpl("id");
		assertEquals("Wrong node cost", NODE_COST, basicCostFunction.computeNodeInsertionCost(node), 0.0);
	}

	@Test
	public void testComputeEdgeInsertionCostE() {
		IEdge edge = new EdgeImpl("id");
		assertEquals("Wrong edge cost", EDGE_COST, basicCostFunction.computeEdgeInsertionCost(edge), 0.0);
	}

	@Test
	public void testNodeAnnotationDistance() {
		NodeImpl node1 = new NodeImpl("id1");
		NodeImpl node2 = new NodeImpl("id2");
		assertEquals("Wrong node annotation distance", 0.0, basicCostFunction.nodeAnnotationDistance(node1, node2), 0.0);
	}

	@Test
	public void testEdgeAnnotationDistance() {
		IEdge edge1 = new EdgeImpl("simple1");
		IEdge edge2 = new EdgeImpl("simple2");

		assertEquals("Wrong annotation distance", 0.0, basicCostFunction.edgeAnnotationDistance(edge1, edge2), 0.0);
	}

}
