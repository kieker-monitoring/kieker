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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableNetwork;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserBehaviorEdge;
import kieker.analysis.behavior.model.UserSession;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;

import teetime.framework.test.StageTester;

/**
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class UserSessionToBehaviorModelTransformationTest { // NOCS constructor

	private final UserSessionToBehaviorModelTransformation converter = new UserSessionToBehaviorModelTransformation();

	private UserSession session;

	@Before
	public void setup() {

		final String[] parameters1 = { "view A" };
		final String[] values1 = { "" };
		final EntryCallEvent event1 = TestHelper.createEvent(1, "A", parameters1, values1);

		final String[] parameters2 = { "view B" };
		final String[] values2 = { "" };
		final EntryCallEvent event2 = TestHelper.createEvent(2, "B", parameters2, values2);

		final String[] parameters3 = { "stay at B" };
		final String[] values3 = { "with value 1" };
		final EntryCallEvent event3 = TestHelper.createEvent(3, "B", parameters3, values3);

		final String[] parameters4 = { "stay at B" };
		final String[] values4 = { "with value 2" };
		final EntryCallEvent event4 = TestHelper.createEvent(4, "B", parameters4, values4);

		final String[] parameters5 = { "stay at B with other parameter name" };
		final String[] values5 = { "" };
		final EntryCallEvent event5 = TestHelper.createEvent(5, "B", parameters5, values5);

		this.session = new UserSession("", "");

		this.session.add(event1);
		this.session.add(event2);
		this.session.add(event3);
		this.session.add(event4);
		this.session.add(event5);
	}

	@Test
	public void test() {
		final IGraph<INode, UserBehaviorEdge> model = this.startAndGetSolutions(this.session);

		// 3 nodes: A, B, Init
		MatcherAssert.assertThat(model.getGraph().nodes().size(), Matchers.is(3));

		// 3 edges (Init -> A, A -> B, B -> B)
		MatcherAssert.assertThat(model.getGraph().edges().size(), Matchers.is(3));

		final INode nodeB = TestHelper.findNode(model.getGraph(), "B");

		// only 1 edge: (B -> B)
		MatcherAssert.assertThat(this.getSelfEdges(model.getGraph(), nodeB).count(), Matchers.is(1L));

		// two event groups
		MatcherAssert.assertThat(this.getSelfEdges(model.getGraph(), nodeB).findFirst().get().getEventGroups().size(), Matchers.is(2));
	}

	private Stream<UserBehaviorEdge> getSelfEdges(final MutableNetwork<INode, UserBehaviorEdge> model, final INode node) {
		return model.incidentEdges(node).stream().filter(edge -> {
			final EndpointPair<INode> endpoint = model.incidentNodes(edge);
			return endpoint.source() == endpoint.target();
		});
	}

	private IGraph<INode, UserBehaviorEdge> startAndGetSolutions(final UserSession input) {
		// prepare input
		final List<UserSession> inputList = new ArrayList<>();
		inputList.add(input);

		// get output
		final List<IGraph<INode, UserBehaviorEdge>> solutions = new ArrayList<>();
		StageTester.test(this.converter).and().send(input).to(this.converter.getInputPort()).and().receive(solutions)
				.from(this.converter.getOutputPort()).start();
		return solutions.get(0);
	}

}
