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
package kieker.analysis.behavior;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.UserSession;
import kieker.common.record.session.ISessionEvent;
import kieker.common.record.session.SessionEndEvent;
import kieker.common.record.session.SessionStartEvent;

import teetime.framework.test.StageTester;

public class EntryCallSequenceStageTest { // NOCS tests do not need constructors

	private static final String HOSTNAME = "test-host";
	private static final String SESSION_ID = "<session>";
	private static final String OP_1 = "op1()";
	private static final String OP_2 = "op2()";
	private static final String OP_3 = "op3()";
	private static final String COMPONENT_1 = "component1";

	@Before
	public void setUp() throws Exception {}

	@Test
	public void testEntryCallSequenceNoTimeout() {
		final EntryCallSequenceStage stage = new EntryCallSequenceStage(null);

		final ISessionEvent sessionStartEvent = new SessionStartEvent(0, EntryCallSequenceStageTest.HOSTNAME, EntryCallSequenceStageTest.SESSION_ID);
		final ISessionEvent sessionEndEvent = new SessionEndEvent(10, EntryCallSequenceStageTest.HOSTNAME, EntryCallSequenceStageTest.SESSION_ID);

		final List<EntryCallEvent> entryCalls = new ArrayList<>();
		entryCalls.add(new EntryCallEvent(1, 2, EntryCallSequenceStageTest.OP_1, EntryCallSequenceStageTest.COMPONENT_1, EntryCallSequenceStageTest.SESSION_ID,
				EntryCallSequenceStageTest.HOSTNAME, new String[0], new String[0], 0));
		entryCalls.add(new EntryCallEvent(1, 2, EntryCallSequenceStageTest.OP_2, EntryCallSequenceStageTest.COMPONENT_1, EntryCallSequenceStageTest.SESSION_ID,
				EntryCallSequenceStageTest.HOSTNAME, new String[0], new String[0], 0));
		entryCalls.add(new EntryCallEvent(1, 2, EntryCallSequenceStageTest.OP_3, EntryCallSequenceStageTest.COMPONENT_1, EntryCallSequenceStageTest.SESSION_ID,
				EntryCallSequenceStageTest.HOSTNAME, new String[0], new String[0], 0));

		StageTester.test(stage).and().send(sessionStartEvent).to(stage.getSessionEventInputPort()).and().send(entryCalls).to(stage.getEntryCallInputPort())
				.send(sessionEndEvent).to(stage.getSessionEventInputPort()).start();

		final UserSession session = new UserSession(EntryCallSequenceStageTest.HOSTNAME, EntryCallSequenceStageTest.SESSION_ID);
		entryCalls.forEach(call -> session.add(call));

		MatcherAssert.assertThat(stage.getUserSessionOutputPort(), StageTester.produces(session));
	}

}
