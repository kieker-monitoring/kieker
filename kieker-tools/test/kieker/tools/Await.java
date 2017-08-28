/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools;

import java.io.IOException;
import java.lang.Thread.State;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class Await {

	private Await() {
		// utility class
	}

	/**
	 * @param timeoutInMs
	 *            a non-positive value means "do not wait at all"
	 */
	public static void awaitThreadState(final Thread thread, final State state, final int timeoutInMs) throws InterruptedException {
		final long interPauseTimeInMs = 10;

		int waitingTimeInMs = 1;
		while (thread.getState() != state) {
			if (waitingTimeInMs > timeoutInMs) {
				throw new AssertionError(
						"The given thread does not change its state to " + state + " within " + timeoutInMs + " ms. Current state: " + thread.getState());
			}
			Thread.sleep(interPauseTimeInMs);
			waitingTimeInMs += interPauseTimeInMs;
		}
	}

	/**
	 * @param timeoutInMs
	 *            a non-positive value means "do not wait at all"
	 */
	public static void awaitTcpPortIsReachable(final String hostname, final int port, final int timeoutInMs) throws InterruptedException {
		final long timeoutInNs = TimeUnit.MILLISECONDS.toNanos(timeoutInMs);
		final long start = System.nanoTime();

		while (!Await.isTcpPortReachable(hostname, port)) {
			final long currentWaitingTimeInNs = System.nanoTime() - start;
			if (currentWaitingTimeInNs > timeoutInNs) {
				throw new AssertionError(String.format("No server has listened on %s:%d within %d ms.", hostname, port, timeoutInMs));
			}
			Thread.sleep(50);
		}
	}

	private static boolean isTcpPortReachable(final String hostname, final int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(hostname, port));
			return true;
		} catch (final IOException e) { // especially, a java.net.ConnectException
			return false;
		}
	}
}
