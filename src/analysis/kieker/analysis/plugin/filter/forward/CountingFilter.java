/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter.forward;

import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.display.PlainText;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * An instance of this class receives any objects, increments an intern tread-safe counter without printing any message and delivers the
 * unchanged objects to the output. The value of the counter can be retrieved by connected to the respective output port using a
 * corresponding method.
 * 
 * @author Jan Waller
 */
@Plugin(
		description = "A filter counting the elements flowing through this filter",
		outputPorts = {
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, eventTypes = { Object.class }, description = "Provides each incoming object"),
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_COUNT, eventTypes = { Long.class }, description = "Provides the current object count")
		})
public final class CountingFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	public static final String OUTPUT_PORT_NAME_COUNT = "currentEventCount";

	private final AtomicLong counter = new AtomicLong();

	/**
	 * Constructs a {@link CountingFilter}.
	 */
	public CountingFilter(final Configuration configuration) {
		super(configuration);
	}

	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Returns the number of objects received until now.
	 * 
	 * @return
	 */
	public final long getMessageCount() {
		return this.counter.get();
	}

	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = { Object.class }, description = "Receives incoming objects to be counted and forwarded")
	public final void inputEvent(final Object event) {
		final Long count = CountingFilter.this.counter.incrementAndGet();
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, event);
		super.deliver(OUTPUT_PORT_NAME_COUNT, count);
	}

	/**
	 * This method is being used to display the currently stored value within this counter.
	 * 
	 * @return The current text within an instance of {@link PlainText}.
	 */
	@Display(name = "Counter Display")
	public final void countDisplay(final PlainText plainText) {
		plainText.setText(Long.toString(this.counter.get()));
	}
}
