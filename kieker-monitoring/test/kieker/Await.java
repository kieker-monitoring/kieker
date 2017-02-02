/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker;

import java.lang.Thread.State;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class Await {

	private Await() {
		// utility class
	}

	public static void awaitThreadState(final Thread thread, final State state, final int timeoutInMs) throws InterruptedException {
		final long interPauseTimeInMs = 10;

		int waitingTimeInMs = 0;
		while (thread.getState() != state) {
			if (waitingTimeInMs > timeoutInMs) {
				throw new AssertionError(
						"The given thread does not change its state to " + state + " within " + timeoutInMs + " ms. Current state: " + thread.getState());
			}
			Thread.sleep(interPauseTimeInMs);
			waitingTimeInMs += interPauseTimeInMs;
		}
	}
}
