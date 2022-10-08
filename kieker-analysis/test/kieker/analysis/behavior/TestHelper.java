/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior;

import java.util.List;

import kieker.analysis.behavior.clustering.OpticsData;
import kieker.analysis.behavior.data.PayloadAwareEntryCallEvent;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.model.Edge;
import kieker.analysis.behavior.model.Node;
import kieker.analysis.behavior.mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 */
public final class TestHelper {

	private TestHelper() {};

	public static PayloadAwareEntryCallEvent createEvent(final int time, final String name, final String[] parameters,
			final String[] values) {
		return new PayloadAwareEntryCallEvent(time, time + 1, name, name, "session", "host", parameters, values, 0);

	}

	public static BehaviorModel createBehaviorModelA() {

		final BehaviorModel model = new BehaviorModel();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");

		final String[] parameters = {};
		final String[] values = {};

		final Edge edge1 = TestHelper.addEdge(model, "Init", "A");

		edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

		final Edge edge2 = TestHelper.addEdge(model, "A", "B");
		edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

		final Edge edge3 = TestHelper.addEdge(model, "B", "C");
		edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

		return model;

	}

	// only difference to A: different values
	public static BehaviorModel createBehaviorModelB() {

		final BehaviorModel model = new BehaviorModel();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");

		final String[] parameters = {};
		final String[] values = { "different value" };

		final Edge edge1 = TestHelper.addEdge(model, "Init", "A");

		edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

		final Edge edge2 = TestHelper.addEdge(model, "A", "B");
		edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

		final Edge edge3 = TestHelper.addEdge(model, "B", "C");
		edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

		return model;
	}

	// only difference to A: different parameterNames
	public static BehaviorModel createBehaviorModelC() {

		final BehaviorModel model = new BehaviorModel();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");

		final String[] parameters = { "different parameters" };
		final String[] values = {};

		final Edge edge1 = TestHelper.addEdge(model, "Init", "A");

		edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

		final Edge edge2 = TestHelper.addEdge(model, "A", "B");
		edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

		final Edge edge3 = TestHelper.addEdge(model, "B", "C");
		edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

		return model;
	}

	// only difference to A: additional edge
	public static BehaviorModel createBehaviorModelD() {

		final BehaviorModel model = new BehaviorModel();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");

		final String[] parameters = {};
		final String[] values = {};

		final Edge edge1 = TestHelper.addEdge(model, "Init", "A");

		edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

		final Edge edge2 = TestHelper.addEdge(model, "A", "B");
		edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

		final Edge edge3 = TestHelper.addEdge(model, "B", "C");
		edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

		final Edge edge4 = TestHelper.addEdge(model, "C", "B");
		edge4.addEvent(TestHelper.createEvent(3, "B", parameters, values));

		return model;

	}

	// only difference to A: additional node
	public static BehaviorModel createBehaviorModelE() {

		final BehaviorModel model = new BehaviorModel();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");
		TestHelper.addNode(model, "D"); // unconnected, but sould still increase distance

		final String[] parameters = {};
		final String[] values = {};

		final Edge edge1 = TestHelper.addEdge(model, "Init", "A");

		edge1.addEvent(TestHelper.createEvent(0, "A", parameters, values));

		final Edge edge2 = TestHelper.addEdge(model, "A", "B");
		edge2.addEvent(TestHelper.createEvent(1, "A", parameters, values));

		final Edge edge3 = TestHelper.addEdge(model, "B", "C");
		edge3.addEvent(TestHelper.createEvent(2, "C", parameters, values));

		return model;
	}

	public static void addNode(final BehaviorModel model, final String nodeName) {
		final Node node = new Node(nodeName);
		model.getNodes().put(nodeName, node);
	}

	public static Edge addEdge(final BehaviorModel model, final String sourceName,
			final String targetName) {
		final Node source = model.getNodes().get(sourceName);
		final Node target = model.getNodes().get(targetName);

		final Edge edge = new Edge(source, target);
		source.getOutgoingEdges().put(target, edge);
		source.getIngoingEdges().put(source, edge);
		model.getEdges().add(edge);
		return edge;
	}

	public static MTree<OpticsData> generateMTree(final List<OpticsData> models) {

		// final GraphEditDistance ged = new GraphEditDistance();
		final MTree<OpticsData> mtree = new MTree<>(20, 40, OpticsData.getDistanceFunction(), null);

		for (final OpticsData model : models) {
			mtree.add(model);
		}

		return mtree;
	}

}
