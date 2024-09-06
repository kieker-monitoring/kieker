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

package kieker.analysis.plugin.filter.forward;

import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.Display;
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
 * @author Jan Waller, Nils Christian Ehmke
 *
 * @since 1.4
 * @deprecated since 1.15.1 old plugin api
 */
@Deprecated
@Plugin(
		description = "A filter counting the elements flowing through this filter",
		outputPorts = {
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, eventTypes = Object.class,
					description = "Provides each incoming object"),
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_COUNT, eventTypes = Long.class,
					description = "Provides the current object count")
		})
public final class CountingFilter extends AbstractFilterPlugin {

	/**
	 * The name of the input port receiving the incoming events.
	 */
	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	/**
	 * The name of the output port passing the incoming events.
	 */
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	/**
	 * The name of the output port which delivers the current counter value.
	 */
	public static final String OUTPUT_PORT_NAME_COUNT = "currentEventCount";

	private final AtomicLong counter = new AtomicLong();

	private final PlainText plainText = new PlainText();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public CountingFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
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
	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = Object.class,
			description = "Receives incoming objects to be counted and forwarded")
	public final void inputEvent(final Object event) {
		final Long count = CountingFilter.this.counter.incrementAndGet();

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, event);
		super.deliver(OUTPUT_PORT_NAME_COUNT, count);
	}

	@Override
	public boolean init() {
		return super.init();
	}

	@Display(name = "Counter Display")
	public final PlainText plainTextDisplay() {
		return this.plainText;
	}

}
