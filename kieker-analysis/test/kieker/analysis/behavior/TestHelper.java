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

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.clustering.BasicCostFunction;
import kieker.analysis.generic.graph.clustering.OpticsData;
import kieker.analysis.generic.graph.clustering.OpticsData.OPTICSDataGED;
import kieker.analysis.generic.graph.impl.EdgeImpl;
import kieker.analysis.generic.graph.impl.NodeImpl;
import kieker.analysis.generic.graph.mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 */
public final class TestHelper {

	private TestHelper() {};

	public static EntryCallEvent createEvent(final int time, final String name, final String[] parameters,
			final String[] values) {
		return new EntryCallEvent(time, time + 1, name, name, "session", "host", parameters, values, 0);

	}

	public static MutableNetwork<INode, IEdge> createBehaviorModelA() {

		final MutableNetwork<INode, IEdge> model = NetworkBuilder.directed().allowsSelfLoops(true).build();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");

		TestHelper.addEdge(model, "Init", "A");
		TestHelper.addEdge(model, "A", "B");
		TestHelper.addEdge(model, "B", "C");

		return model;

	}

	// only difference to A: additional edge
	public static MutableNetwork<INode, IEdge> createBehaviorModelD() {

		final MutableNetwork<INode, IEdge> model = NetworkBuilder.directed().allowsSelfLoops(true).build();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");

		TestHelper.addEdge(model, "Init", "A");
		TestHelper.addEdge(model, "A", "B");
		TestHelper.addEdge(model, "B", "C");
		TestHelper.addEdge(model, "C", "B");

		return model;

	}

	// only difference to A: additional node
	public static MutableNetwork<INode, IEdge> createBehaviorModelE() {

		final MutableNetwork<INode, IEdge> model = NetworkBuilder.directed().allowsSelfLoops(true).build();

		TestHelper.addNode(model, "Init");
		TestHelper.addNode(model, "A");
		TestHelper.addNode(model, "B");
		TestHelper.addNode(model, "C");
		TestHelper.addNode(model, "D"); // unconnected, but should still increase distance

		TestHelper.addEdge(model, "Init", "A");
		TestHelper.addEdge(model, "A", "B");
		TestHelper.addEdge(model, "B", "C");

		return model;
	}

	public static void addNode(final MutableNetwork<INode, IEdge> model, final String nodeName) {
		final INode node = new NodeImpl(nodeName);
		model.addNode(node);
	}

	public static IEdge addEdge(final MutableNetwork<INode, IEdge> model, final String sourceName,
			final String targetName) {
		final INode source = TestHelper.findNode(model, sourceName);
		final INode target = TestHelper.findNode(model, targetName);

		final IEdge edge = new EdgeImpl("");

		model.addEdge(source, target, edge);

		return edge;
	}

	public static MTree<OpticsData<INode, IEdge>> generateMTree(final List<OpticsData<INode, IEdge>> models) throws InternalErrorException {

		// final GraphEditDistance ged = new GraphEditDistance();
		final BasicCostFunction<INode, IEdge> costFunction = new BasicCostFunction<>(1, 1);
		final MTree<OpticsData<INode, IEdge>> mtree = new MTree<>(20, 40, new OPTICSDataGED<>(costFunction), null);

		for (final OpticsData<INode, IEdge> model : models) {
			mtree.add(model);
		}

		return mtree;
	}

	public static <E extends IEdge> INode findNode(final MutableNetwork<INode, E> model, final String signature) {
		return model.nodes().stream().filter(node -> node.getId().equals(signature)).findFirst().get();
	}
}
