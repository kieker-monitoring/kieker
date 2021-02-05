/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage;

import org.junit.Assert;
import org.junit.Test;

import teetime.framework.test.StageTester;

public class DynamicEventDispatcherTest {

	@Test
	public void testBase() {
		try {
			this.testDispatcher(null, false, false, false);
		} catch (final Exception e) {

		}
	}

	@Test
	public void testRootMatcher() {
		final IEventMatcher<Object> rootMatcher = new ImplementsEventMatcher<>(Object.class, null);
		this.testDispatcher(rootMatcher, false, false, false);

		Assert.assertThat(rootMatcher.getOutputPort(), StageTester.produces(this.createValues()));
	}

	@Test
	public void testRootMatcherOutputOther() {
		final IEventMatcher<String> rootMatcher = new ImplementsEventMatcher<>(String.class, null);
		final DynamicEventDispatcher dispatcher = this.testDispatcher(rootMatcher, false, false, true);

		Assert.assertThat(rootMatcher.getOutputPort(), StageTester.produces("hello"));
		Assert.assertThat(dispatcher.getOutputOtherPort(), StageTester.produces(10, 20, 5.5, false));
	}

	@Test
	public void testRootMatcherReportUnknown() {
		final IEventMatcher<String> rootMatcher = new ImplementsEventMatcher<>(String.class, null);
		final DynamicEventDispatcher dispatcher = this.testDispatcher(rootMatcher, false, true, true);

		Assert.assertThat(rootMatcher.getOutputPort(), StageTester.produces("hello"));
		Assert.assertThat(dispatcher.getOutputOtherPort(), StageTester.produces(10, 20, 5.5, false));
	}

	@Test
	public void testRootMatcherCountEvents() {
		final IEventMatcher<String> rootMatcher = new ImplementsEventMatcher<>(String.class, null);
		final DynamicEventDispatcher dispatcher = this.testDispatcher(rootMatcher, true, true, true);

		Assert.assertThat(rootMatcher.getOutputPort(), StageTester.produces("hello"));
		Assert.assertThat(dispatcher.getOutputOtherPort(), StageTester.produces(10, 20, 5.5, false));
		Assert.assertEquals("Number of events do not match", dispatcher.getEventCount(), 5);
	}

	private DynamicEventDispatcher testDispatcher(final IEventMatcher<? extends Object> rootEventMatcher, final boolean countEvents, final boolean reportUnknown,
			final boolean outputOther) {
		final DynamicEventDispatcher dispatcher = new DynamicEventDispatcher(rootEventMatcher, countEvents, reportUnknown, outputOther);

		final Object[] elements = this.createValues();

		StageTester.test(dispatcher).and().send(elements).to(dispatcher.getInputPort()).start();

		return dispatcher;
	}

	private Object[] createValues() {
		final Object[] elements = new Object[5];

		elements[0] = "hello";
		elements[1] = 10;
		elements[2] = 20;
		elements[3] = 5.5;
		elements[4] = false;

		return elements;
	}

}
