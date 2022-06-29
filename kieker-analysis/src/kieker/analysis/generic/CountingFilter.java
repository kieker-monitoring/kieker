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

package kieker.analysis.generic;

import java.util.concurrent.atomic.AtomicLong;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * An instance of this class receives any objects, increments an intern thread-safe counter without printing any message and delivers the
 * unchanged objects to the output. The value of the counter can be retrieved by connected to the respective output port using a
 * corresponding method.
 *
 * @author Jan Waller, Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.4
 */
public final class CountingFilter extends AbstractConsumerStage<Object> {

	private final OutputPort<Object> relayedEventsOutputPort = this.createOutputPort();
	private final OutputPort<Long> countOutputPort = this.createOutputPort();

	private final AtomicLong counter = new AtomicLong();

	/**
	 * Creates a new instance of this class and sets the time stamp of initialization.
	 */
	public CountingFilter() {}

	/**
	 * Returns the number of objects received until now.
	 *
	 * @return The current counter value.
	 */
	public final long getMessageCount() {
		return this.counter.get();
	}

	/**
	 * This method represents the input port of this filter.
	 *
	 * @param event
	 *            The next event.
	 */
	@Override
	protected void execute(final Object event) {
		final Long count = this.counter.incrementAndGet();

		this.relayedEventsOutputPort.send(event);
		this.countOutputPort.send(count);
	}

}
