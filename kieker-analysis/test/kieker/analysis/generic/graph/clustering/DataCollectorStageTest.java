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

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;

import teetime.framework.test.StageTester;

public class DataCollectorStageTest { // NOCS tests do not need constructors

	@Test
	public void testEventsNoLimits() { // NOPMD has MatcherAssert
		final DataCollectorStage<Integer> stage = new DataCollectorStage<>();
		StageTester.test(stage).and().send(1, 2, 3, 4, 5).to(stage.getDataInputPort()).start();
		final List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		MatcherAssert.assertThat(stage.getOpticsOutputPort(), StageTester.produces(list));
	}

	@Test
	public void testEventsWithSizeLimit() {  // NOPMD has MatcherAssert
		final DataCollectorStage<Integer> stage = new DataCollectorStage<>(2);
		StageTester.test(stage).and().send(1, 2, 3, 4, 5).to(stage.getDataInputPort()).start();
		final List<Integer> list1 = new ArrayList<>();
		final List<Integer> list2 = new ArrayList<>();
		final List<Integer> list3 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		list2.add(3);
		list2.add(4);
		list3.add(5);
		MatcherAssert.assertThat(stage.getOpticsOutputPort(), StageTester.produces(list1, list2, list3));
	}

	// TODO currently stages with multiple input ports cannot be checked under specific conditions, as the
	// sequence of events to multiple input ports cannot be guaranteed causing race conditions during tests.
	@Ignore
	@Test
	public void testEventsTimeLimit() {  // NOPMD has MatcherAssert
		final DataCollectorStage<Integer> stage = new DataCollectorStage<>();
		StageTester.test(stage).and().send(0L).to(stage.getTimeTriggerInputPort())
			.and().send(1, 2, 3, 4, 5).to(stage.getDataInputPort()).start();
		final List<Integer> list1 = new ArrayList<>();
		final List<Integer> list2 = new ArrayList<>();
		list1.add(1);
		list2.add(2);
		list2.add(3);
		list2.add(4);
		list2.add(5);
		MatcherAssert.assertThat(stage.getOpticsOutputPort(), StageTester.produces(list1, list2));
	}

}
