/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.tcp;

import java.util.concurrent.TimeUnit;

/**
 * Countdown class for connection timeouts.
 * 
 * @author Christian Wulf
 *
 * @since 1.14
 */
class TimeoutCountdown {

	private long currentTimeoutInNs;

	/**
	 * @param initialTimeoutInNs	
	 */
	public TimeoutCountdown(final long initialTimeoutInNs) {
		this.currentTimeoutInNs = initialTimeoutInNs;
	}

	/**
	 * Subtracts the given amount from the timeout.
	 * 
	 * @param amountInNs
	 *            The amount to subtract in nanoseconds
	 */
	public void countdownNs(final long amountInNs) {
		this.currentTimeoutInNs -= amountInNs;
	}

	/**
	 * @return the remaining time of the timeout in milliseconds.
	 */
	public int getRemainingTimeoutInMs() {
		return (int) TimeUnit.NANOSECONDS.toMillis(this.getRemainingTimeoutInNs());
	}

	/**
	 * @return the remaining time of the timeout in nanoseconds.
	 */
	public long getRemainingTimeoutInNs() {
		return this.currentTimeoutInNs;
	}

}
