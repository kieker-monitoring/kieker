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
package kieker.analysis.behavior.model.generation;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.behavior.TestHelper;
import kieker.analysis.behavior.UserSessionToModelConverter;
import kieker.analysis.behavior.data.PayloadAwareEntryCallEvent;
import kieker.analysis.behavior.data.UserSession;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.model.Edge;
import kieker.analysis.behavior.model.Node;

import teetime.framework.test.StageTester;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class UserSessionToModelGeneratorTest {

	private final UserSessionToModelConverter converter = new UserSessionToModelConverter();

	private UserSession session;

	@Before
	public void setup() {

		final String[] parameters1 = { "view A" };
		final String[] values1 = { "" };
		final PayloadAwareEntryCallEvent event1 = TestHelper.createEvent(1, "A", parameters1, values1);

		final String[] parameters2 = { "view B" };
		final String[] values2 = { "" };
		final PayloadAwareEntryCallEvent event2 = TestHelper.createEvent(2, "B", parameters2, values2);

		final String[] parameters3 = { "stay at B" };
		final String[] values3 = { "with value 1" };
		final PayloadAwareEntryCallEvent event3 = TestHelper.createEvent(3, "B", parameters3, values3);

		final String[] parameters4 = { "stay at B" };
		final String[] values4 = { "with value 2" };
		final PayloadAwareEntryCallEvent event4 = TestHelper.createEvent(4, "B", parameters4, values4);

		final String[] parameters5 = { "stay at B with other parameter name" };
		final String[] values5 = { "" };
		final PayloadAwareEntryCallEvent event5 = TestHelper.createEvent(5, "B", parameters5, values5);

		this.session = new UserSession("", "");

		this.session.add(event1);
		this.session.add(event2);
		this.session.add(event3);
		this.session.add(event4);
		this.session.add(event5);
	}

	@Test
	public void test() {
		final BehaviorModel model = this.startAndGetSolutions(this.session);

		// 3 nodes: A, B, Init
		MatcherAssert.assertThat(model.getNodes().size(), Matchers.is(3));

		// 3 edges (Init -> A, A -> B, B -> B)
		MatcherAssert.assertThat(model.getEdges().size(), Matchers.is(3));

		final Node nodeB = model.getNodes().get("B");

		// only 1 edge: (B -> B)
		MatcherAssert.assertThat(nodeB.getOutgoingEdges().size(), Matchers.is(1));

		// get edge to itself
		final Edge edgeBtoB = nodeB.getOutgoingEdges().get(nodeB);

		// two event groups
		MatcherAssert.assertThat(edgeBtoB.getEventGroups().size(), Matchers.is(2));

	}

	private BehaviorModel startAndGetSolutions(final UserSession input) {
		// prepare input
		final List<UserSession> inputList = new ArrayList<>();
		inputList.add(input);

		// get output
		final List<BehaviorModel> solutions = new ArrayList<>();
		StageTester.test(this.converter).and().send(input).to(this.converter.getInputPort()).and().receive(solutions)
				.from(this.converter.getOutputPort()).start();
		return solutions.get(0);
	}

}
