/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.EventGroup;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.BasicCostFunction;
import kieker.analysis.generic.graph.impl.NodeImpl;

public class UserBehaviorCostFunctionTest {

	protected static final double PARAMETER_DUPLICATION_COST = 0.1;
	protected static final double PARAMETER_INSERTION_COST = 0.15;

	private static final double NODE_COST = 1.0;
	private static final double EDGE_COST = 1.5;
	private static final double EVENT_GROUP_COST = 1.3;

	private final BasicCostFunction<INode, UserBehaviorEdge> costFunction;

	public UserBehaviorCostFunctionTest() {
		this.costFunction = new UserBehaviorCostFunction(UserBehaviorCostFunctionTest.NODE_COST,
				UserBehaviorCostFunctionTest.EDGE_COST, UserBehaviorCostFunctionTest.EVENT_GROUP_COST, new IParameterWeighting() {

					@Override
					public double getInsertCost(final String[] parameterNames) {
						return UserBehaviorCostFunctionTest.PARAMETER_INSERTION_COST;
					}

					@Override
					public double getDuplicateCost(final String[] parameterNames) {
						return UserBehaviorCostFunctionTest.PARAMETER_DUPLICATION_COST;
					}

				});
	}

	@Test
	public void testComputeNodeInsertionCost() {
		final INode node = new NodeImpl("node");
		Assert.assertEquals(UserBehaviorCostFunctionTest.NODE_COST, this.costFunction.computeNodeInsertionCost(node), 0.0);
	}

	@Test
	public void testComputeEdgeInsertionCost() {
		final UserBehaviorEdge edge = new UserBehaviorEdge("test");
		Assert.assertEquals(UserBehaviorCostFunctionTest.EDGE_COST, this.costFunction.computeEdgeInsertionCost(edge), 0.0);
	}

	@Test
	public void testNodeAnnotationDistance() {
		final INode node1 = new NodeImpl("node1");
		final INode node2 = new NodeImpl("node2");
		Assert.assertEquals(0.0, this.costFunction.nodeAnnotationDistance(node1, node2), 0.0);
	}

	@Test
	public void testEdgeAnnotationDistance() {
		final UserBehaviorEdge edge1 = new UserBehaviorEdge("edge1");
		final UserBehaviorEdge edge2 = new UserBehaviorEdge("edge2");
		Assert.assertEquals(0.0, this.costFunction.edgeAnnotationDistance(edge1, edge2), 0.0);
	}

	@Test
	public void testEdgeAnnotationDistanceWithSameEdgeButDifferentEvents() {
		final UserBehaviorEdge edge1 = new UserBehaviorEdge("simple1");
		final EntryCallEvent event1 = new EntryCallEvent(0, 0, "op", "class", "session1", "host", null, null, 0);
		edge1.addEvent(event1);
		final UserBehaviorEdge edge2 = new UserBehaviorEdge("simple1");
		final EntryCallEvent event2 = new EntryCallEvent(0, 0, "op2", "class", "session1", "host", null, null, 0);
		edge2.addEvent(event2);

		final UserBehaviorEdge edge3 = new UserBehaviorEdge("simple3");
		final String[] params = { "a" };
		final String[] values = { "b" };
		final EntryCallEvent event3 = new EntryCallEvent(0, 0, "op2", "class", "session1", "host", params, values, 0);
		edge3.addEvent(event3);
		Assert.assertEquals("Wrong annotation distance",
				(UserBehaviorCostFunctionTest.EVENT_GROUP_COST * 2) + (UserBehaviorCostFunctionTest.PARAMETER_INSERTION_COST * 2),
				this.costFunction.edgeAnnotationDistance(edge1, edge3), 0.0);
	}

	@Test
	public void testEdgeAnnotationDistanceWithSameEdgeButDifferentEventsGroup() {
		final UserBehaviorEdge edge1 = new UserBehaviorEdge("edge1");
		final UserBehaviorEdge edge2 = new UserBehaviorEdge("edge1");

		final EventGroup eventGroup1 = new EventGroup(new String[] { "p1", "p2" });
		edge1.getEventGroups().add(eventGroup1);

		final EventGroup eventGroup2 = new EventGroup(new String[] { "p1", "p3" });
		edge2.getEventGroups().add(eventGroup2);

		Assert.assertEquals(UserBehaviorCostFunctionTest.EVENT_GROUP_COST * 2, this.costFunction.edgeAnnotationDistance(edge1, edge2), 0.0);
	}

}
