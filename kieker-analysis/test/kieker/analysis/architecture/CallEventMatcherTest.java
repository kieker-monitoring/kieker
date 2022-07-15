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
package kieker.analysis.architecture;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.architecture.recovery.events.CallEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;

public class CallEventMatcherTest {

	@Test
	public void test() {
		final OperationEvent caller = new OperationEvent("host", "component", "caller");
		final OperationEvent callee = new OperationEvent("host", "component", "callee");
		final OperationEvent callee2 = new OperationEvent("host", "component", "callee2");

		final CallEvent callEvent = new CallEvent(caller, callee, Duration.ZERO);

		final CallEventMatcher matcher = new CallEventMatcher();
		Assert.assertTrue(matcher.checkControlEvent(callee, callEvent));
		Assert.assertFalse(matcher.checkControlEvent(callee2, callEvent));
	}

}
