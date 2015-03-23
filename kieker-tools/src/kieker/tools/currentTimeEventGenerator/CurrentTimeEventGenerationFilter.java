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

package kieker.tools.currentTimeEventGenerator;

import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.TimestampRecord;

/**
 * Generates time events with a given resolution based on the timestamps of
 * incoming {@link kieker.common.record.IMonitoringRecord}s.
 * 
 * <ol>
 * <li>The first record received via {@link #inputTimestamp(Long)} immediately leads to a new {@link TimestampRecord} with the given timestamp.</li>
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
 * It is guaranteed that the generated timestamps are in ascending order.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
@Plugin(description = "Generates time events with a given resolution based on the timestamps of incoming IMonitoringRecords",
		outputPorts = {
			@OutputPort(name = CurrentTimeEventGenerationFilter.OUTPUT_PORT_NAME_CURRENT_TIME_RECORD, eventTypes = { TimestampRecord.class },
					description = "Provides current time events"),
			@OutputPort(name = CurrentTimeEventGenerationFilter.OUTPUT_PORT_NAME_CURRENT_TIME_VALUE, eventTypes = { Long.class },
					description = "Provides current time values")
		},
		configuration = {
			@Property(name = CurrentTimeEventGenerationFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
					defaultValue = CurrentTimeEventGenerationFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(name = CurrentTimeEventGenerationFilter.CONFIG_PROPERTY_NAME_TIME_RESOLUTION, defaultValue = "1000")
		})
public class CurrentTimeEventGenerationFilter extends AbstractFilterPlugin {

	/** This is the name of the input port receiving new timestamps. */
	public static final String INPUT_PORT_NAME_NEW_TIMESTAMP = "inputNewTimestamp";
	/** This is the name of the input port receiving new records. */
	public static final String INPUT_PORT_NAME_NEW_RECORD = "inputNewRecord";

	/** This is the name of the output port delivering the timestamp records. */
	public static final String OUTPUT_PORT_NAME_CURRENT_TIME_RECORD = "currentTimeRecord";
	/** This is the name of the output port delivering the timestamps. */
	public static final String OUTPUT_PORT_NAME_CURRENT_TIME_VALUE = "currentTimeValue";

	/** This is the name of the property to determine the time resolution. */
	public static final String CONFIG_PROPERTY_NAME_TIME_RESOLUTION = "timeResolution";

	/**
	 * Property name for the configuration of the timeunit.
	 */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";

	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()

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

	private final TimeUnit timeunit;

	/**
	 * Creates an event generator which generates time events with the given resolution in timeunits via the output port
	 * {@link #OUTPUT_PORT_NAME_CURRENT_TIME_RECORD}.
	 * 
	 * @param configuration
	 *            The configuration to be used for this plugin.
	 * @param projectContext
	 *            The project context to be used for this plugin.
	 */
	public CurrentTimeEventGenerationFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			this.log.warn(configTimeunitProperty + " is no valid TimeUnit! Using inherited value of " + this.timeunit.name() + " instead.");
			configTimeunit = this.timeunit;
		}

		this.timerResolution = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_TIME_RESOLUTION), configTimeunit);
	}

	/**
	 * This method represents the input port for new records.
	 * 
	 * @param record
	 *            The next record.
	 */
	@InputPort(name = INPUT_PORT_NAME_NEW_RECORD, eventTypes = { IMonitoringRecord.class },
			description = "Receives a new record and extracts the logging timestamp as a time event")
	public void inputRecord(final IMonitoringRecord record) {
		this.inputTimestamp(record.getLoggingTimestamp());
	}

	/**
	 * Evaluates the given timestamp internal current time which may lead to
	 * newly generated events via {@link #OUTPUT_PORT_NAME_CURRENT_TIME_RECORD}.
	 * 
	 * @param timestamp
	 *            The next timestamp.
	 */
	@InputPort(name = INPUT_PORT_NAME_NEW_TIMESTAMP, description = "Receives a new timestamp as a time event", eventTypes = { Long.class })
	public void inputTimestamp(final Long timestamp) {
		if (timestamp < 0) {
			this.log.warn("Received timestamp value < 0: " + timestamp);
			return;
		}

		if (this.firstTimestamp == -1) {
			// Initial record
			this.maxTimestamp = timestamp;
			this.firstTimestamp = timestamp;
			super.deliver(OUTPUT_PORT_NAME_CURRENT_TIME_RECORD, new TimestampRecord(timestamp));
			super.deliver(OUTPUT_PORT_NAME_CURRENT_TIME_VALUE, timestamp);
			this.mostRecentEventFired = timestamp;
		} else if (timestamp > this.maxTimestamp) {
			this.maxTimestamp = timestamp;
			// Fire timer event(s) if required.
			for (long nextTimerEventAt = this.mostRecentEventFired + this.timerResolution; timestamp >= nextTimerEventAt; nextTimerEventAt =
					this.mostRecentEventFired + this.timerResolution) {
				super.deliver(OUTPUT_PORT_NAME_CURRENT_TIME_RECORD, new TimestampRecord(nextTimerEventAt));
				super.deliver(OUTPUT_PORT_NAME_CURRENT_TIME_VALUE, nextTimerEventAt);
				this.mostRecentEventFired = nextTimerEventAt;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIME_RESOLUTION, Long.toString(this.timerResolution));
		return configuration;
	}
}
