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

package kieker.analysis.plugin.filter.forward;

import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.PlainText;
import kieker.analysis.display.XYPlot;
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
 * @author Jan Waller, Nils Christian Ehmke
 * 
 * @since 1.4
 */
@Plugin(
		description = "A filter counting the elements flowing through this filter",
		outputPorts = {
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, eventTypes = { Object.class }, description = "Provides each incoming object"),
			@OutputPort(name = CountingFilter.OUTPUT_PORT_NAME_COUNT, eventTypes = { Long.class }, description = "Provides the current object count")
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

	private volatile long timeStampOfInitialization;

	private final PlainText plainText = new PlainText();
	private final XYPlot xyPlot = new XYPlot(50);

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
	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = { Object.class }, description = "Receives incoming objects to be counted and forwarded")
	public final void inputEvent(final Object event) {
		final Long count = CountingFilter.this.counter.incrementAndGet();

		this.updateDisplays();

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, event);
		super.deliver(OUTPUT_PORT_NAME_COUNT, count);
	}

	private void updateDisplays() {
		// XY Plot
		final long timeStampDeltaInSeconds = (System.currentTimeMillis() - this.timeStampOfInitialization) / 1000;
		this.xyPlot.setEntry("", timeStampDeltaInSeconds, this.counter.get());

		// Plain text
		this.plainText.setText(Long.toString(this.counter.get()));

	}

	@Override
	public boolean init() {
		if (super.init()) {
			this.timeStampOfInitialization = System.currentTimeMillis();
			return true;
		} else {
			return false;
		}
	}

	@Display(name = "Counter Display")
	public final PlainText plainTextDisplay() {
		return this.plainText;
	}

	@Display(name = "XYPlot Counter Display")
	public final XYPlot xyPlotDisplay() {
		return this.xyPlot;
	}

}
