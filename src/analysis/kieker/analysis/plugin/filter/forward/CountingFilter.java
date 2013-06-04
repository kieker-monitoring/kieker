/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.Image;
import kieker.analysis.display.MeterGauge;
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
 * @author Jan Waller
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
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, event);
		super.deliver(OUTPUT_PORT_NAME_COUNT, count);
	}

	/**
	 * A simple test display, which prints the current counter on the given Kieker image to the given text object.
	 * 
	 * @param plainText
	 *            The text object to be filled with the current counter value.
	 */
	@Display(name = "Counter Display")
	public final void countDisplay(final PlainText plainText) {
		plainText.setText(Long.toString(this.counter.get()));
	}

	/**
	 * A simple test display, which prints the current counter on the given Kieker image.
	 * 
	 * @param image
	 *            The image object to be filled with the current counter value.
	 */
	@Display(name = "Visual Counter Display")
	public final void countDisplay(final Image image) {
		final String value = Long.toString(this.counter.get());
		final int width = image.getImage().getWidth();
		final int height = image.getImage().getHeight();
		final Graphics2D g = image.getGraphics();

		g.setFont(g.getFont().deriveFont(20.0f));

		g.setColor(Color.white);
		g.fillRect(0, 0, width - 1, height - 1);
		g.setColor(Color.gray);
		g.drawRect(0, 0, width - 2, height - 2);

		final Rectangle2D bounds = g.getFontMetrics().getStringBounds(value, g);

		g.drawString(value, (int) (width - bounds.getWidth()) / 2, (int) (height - bounds.getHeight()) / 2);
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

	/**
	 * A simple test display, which puts the current counter into the given plot.
	 * 
	 * @param plot
	 *            The plot object to be filled with the current counter value.
	 */
	@Display(name = "XYPlot Counter Display")
	public final void countDisplay(final XYPlot plot) {
		final long timeStampDeltaInMS = System.currentTimeMillis() - this.timeStampOfInitialization;

		plot.setEntry(timeStampDeltaInMS, this.counter.get());
	}

	/**
	 * A simple test display, which uses a meter gauge to show the current value.
	 * 
	 * @param meterGauge
	 *            The meter gauge object to be filled with the current counter value.
	 */
	@Display(name = "Meter Gauge Counter Display")
	public final void countDisplay(final MeterGauge meterGauge) {
		meterGauge.setIntervals(Arrays.asList((Number) 10, 20, 40, 100));
		meterGauge.setValue(this.counter);
	}
}
