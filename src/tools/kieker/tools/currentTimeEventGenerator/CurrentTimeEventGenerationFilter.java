/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.currentTimeEventGenerator;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.TimestampRecord;

/**
 * Generates time events with a given resolution based on the timestamps of
 * incoming {@link kieker.common.record.IMonitoringRecord}s.
 * 
 * <ol>
 * <li>The first record received via {@link #inputTimestamp(Long)} immediately leads to a new {@link TimestampEvent} with the given timestamp.</li>
 * <li>The timestamp of the first record is stored as {@link #firstTimestamp} and future events are generated at {@link #firstTimestamp} + i *
 * {@link #timerResolution}.</li>
 * <li>Future {@link kieker.common.record.IMonitoringRecord} may lead to future {@link TimestampRecord} as follows:
 * <ol>
 * <li>A newly incoming {@link kieker.common.record.IMonitoringRecord} with logging timestamp {@literal tstamp} leads to the new timer events satisfying
 * {@link #firstTimestamp} + i * {@link #timerResolution} {@literal <} {@literal tstamp}.</li>
 * </ol>
 * </li>
 * </ol>
 * 
 * 
 * It is guaranteed that the generated timestamps are in ascending order.
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(outputPorts =
		@OutputPort(name = CurrentTimeEventGenerationFilter.OUTPUT_PORT_NAME_CURRENT_TIME, eventTypes = { TimestampRecord.class }, description = "Provides current time events"))
public class CurrentTimeEventGenerationFilter extends AbstractFilterPlugin {
	public static final String INPUT_PORT_NAME_NEW_TIMESTAMP = "inputNewTimestamp";
	public static final String INPUT_PORT_NAME_NEW_RECORD = "inputNewRecord";

	public static final String OUTPUT_PORT_NAME_CURRENT_TIME = "currentTimeOutputPort";

	public static final String CONFIG_PROPERTY_NAME_TIME_RESOLUTION = "timeResolution";

	private static final Log LOG = LogFactory.getLog(CurrentTimeEventGenerationFilter.class);

	/**
	 * Timestamp of the record that was received first. Notice, that this is not
	 * necessarily the lowest timestamp.
	 */
	private volatile long firstTimestamp = -1;

	/**
	 * Maximum timestamp received so far.
	 */
	private volatile long maxTimestamp = -1;

	/**
	 * The timestamp of the most recent timer event.
	 */
	private volatile long mostRecentEventFired = -1;

	private final long timerResolution;

	/**
	 * Creates an event generator which generates time events with the given
	 * resolution in nanoseconds via the output port {@link #OUTPUT_PORT_NAME_CURRENT_TIME}.
	 * 
	 * @param configuration
	 *            The configuration to be used for this plugin.
	 */
	public CurrentTimeEventGenerationFilter(final Configuration configuration) {
		super(configuration);
		this.timerResolution = configuration.getLongProperty(CONFIG_PROPERTY_NAME_TIME_RESOLUTION);
	}

	@InputPort(name = INPUT_PORT_NAME_NEW_RECORD, eventTypes = { IMonitoringRecord.class },
			description = "Receives a new timestamp and extracts the logging timestamp as a time event")
	public void inputRecord(final IMonitoringRecord record) {
		if (record != null) {
			this.inputTimestamp(record.getLoggingTimestamp());
		}
	}

	/**
	 * Evaluates the given timestamp internal current time which may lead to
	 * newly generated events via {@link #OUTPUT_PORT_NAME_CURRENT_TIME}.
	 */
	@InputPort(name = INPUT_PORT_NAME_NEW_TIMESTAMP, description = "Receives a new timestamp as a time event", eventTypes = { Long.class })
	public void inputTimestamp(final Long timestamp) {
		if (timestamp < 0) {
			LOG.warn("Received timestamp value < 0: " + timestamp);
			return;
		}

		if (this.firstTimestamp == -1) {
			/**
			 * Initial record
			 */
			this.maxTimestamp = timestamp;
			this.firstTimestamp = timestamp;
			super.deliver(OUTPUT_PORT_NAME_CURRENT_TIME, new TimestampRecord(timestamp));
			this.mostRecentEventFired = timestamp;
		} else if (timestamp > this.maxTimestamp) {
			this.maxTimestamp = timestamp;
			/**
			 * Fire timer event(s) if required.
			 */
			for (long nextTimerEventAt = this.mostRecentEventFired + this.timerResolution; timestamp >= nextTimerEventAt; nextTimerEventAt = this.mostRecentEventFired
					+ this.timerResolution) {
				super.deliver(OUTPUT_PORT_NAME_CURRENT_TIME, new TimestampRecord(nextTimerEventAt));
				this.mostRecentEventFired = nextTimerEventAt;
			}
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_TIME_RESOLUTION, Long.toString(1000L));

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_TIME_RESOLUTION, Long.toString(this.timerResolution));

		return configuration;
	}
}
