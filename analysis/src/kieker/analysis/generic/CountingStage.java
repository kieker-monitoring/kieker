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

package kieker.analysis.generic;

import java.util.concurrent.atomic.AtomicLong;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * An instance of this class receives any objects, increments an intern thread-safe counter without printing any message and delivers the
 * unchanged objects to the output. The value of the counter can be retrieved by connected to the respective output port using a
 * corresponding method.
 *
 * @param <T>
 *            record type
 *
 * @author Jan Waller, Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.4
 */
public final class CountingStage<T> extends AbstractConsumerStage<T> {

	private final OutputPort<T> relayedEventsOutputPort = this.createOutputPort();
	private final OutputPort<Long> countOutputPort = this.createOutputPort();

	private final AtomicLong counter = new AtomicLong();
	private final boolean echo;
	private final int modulo;
	private final String label;

	/**
	 * Creates a new instance of this class and sets the time stamp of initialization.
	 *
	 * @param echo
	 *            if true, print out number of counted record to console log (INFO)
	 * @param modulo
	 *            print out count info to log only every n-event
	 */
	public CountingStage(final boolean echo, final int modulo) {
		this(echo, modulo, "");
	}

	/**
	 * Creates a new instance of this class and sets the time stamp of initialization.
	 *
	 * @param echo
	 *            if true, print out number of counted record to console log (INFO)
	 * @param modulo
	 *            print out count info to log only every n-event
	 * @param label
	 *            label added as prefix to the number in the log
	 */
	public CountingStage(final boolean echo, final int modulo, final String label) {
		this.echo = echo;
		this.modulo = modulo;
		this.label = label;
	}

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
	protected void execute(final T event) {
		final Long count = this.counter.incrementAndGet();
		if (this.echo && ((count % this.modulo) == 0)) {
			this.logger.info("Processed {} events: {}", this.label, count);
		}

		this.relayedEventsOutputPort.send(event);
		this.countOutputPort.send(count);
	}

	public OutputPort<Long> getCountOutputPort() {
		return this.countOutputPort;
	}

	public OutputPort<T> getRelayedEventsOutputPort() {
		return this.relayedEventsOutputPort;
	}

}
