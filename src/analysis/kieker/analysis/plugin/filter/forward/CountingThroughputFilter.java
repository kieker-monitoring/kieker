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

import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.ImmutableEntry;

/**
 * An instance of this class computes the throughput in terms of the number of events received per time unit.
 * 
 * Note that only one of the input ports should be used in a configuration!
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.6
 */
@Plugin(
		description = "A filter computing the throughput in terms of the number of events received per time unit",
		outputPorts = {
			@OutputPort(name = CountingThroughputFilter.OUTPUT_PORT_NAME_RELAYED_OBJECTS, eventTypes = { Object.class },
					description = "Provides each incoming object"),
			@OutputPort(name = CountingThroughputFilter.OUTPUT_PORT_NAME_THROUGHPUT, eventTypes = { Long.class },
					description = "Provides each incoming object")
		},
		configuration = {
			@Property(name = CountingThroughputFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
					defaultValue = CountingThroughputFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(name = CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVAL_SIZE,
					defaultValue = CountingThroughputFilter.CONFIG_PROPERTY_VALUE_INTERVAL_SIZE_ONE_MINUTE),
			@Property(name = CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP,
					defaultValue = "true")
		})
public final class CountingThroughputFilter extends AbstractFilterPlugin {

	/**
	 * The name of the input port receiving the records.
	 */
	public static final String INPUT_PORT_NAME_RECORDS = "inputRecords";
	/**
	 * The name of the input port receiving other objects.
	 */
	public static final String INPUT_PORT_NAME_OBJECTS = "inputObjects";

	/**
	 * The name of the output port delivering the received objects.
	 */
	public static final String OUTPUT_PORT_NAME_RELAYED_OBJECTS = "relayedEvents";
	/**
	 * The name of the output port delivering the received objects.
	 */
	public static final String OUTPUT_PORT_NAME_THROUGHPUT = "throughput";

	/** The name of the property determining the time unit. */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	/** The name of the property determining the interval size. */
	public static final String CONFIG_PROPERTY_NAME_INTERVAL_SIZE = "intervalSize";

	/**
	 * The default value of the time unit property (nanoseconds).
	 */
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()

	/**
	 * If the value is set to false, the intervals are computed based on time since 1970-1-1.
	 */
	public static final String CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP = "intervalsBasedOn1stTstamp";

	/**
	 * The configuration property value for {@link #CONFIG_PROPERTY_NAME_INTERVAL_SIZE}, leading to a bin size of 1 minute.
	 */
	public static final String CONFIG_PROPERTY_VALUE_INTERVAL_SIZE_ONE_MINUTE = "60000000000";

	private static final Log LOG = LogFactory.getLog(CountingThroughputFilter.class);

	private volatile long firstIntervalStart = -1;
	private final boolean intervalsBasedOn1stTstamp;
	private final TimeUnit timeunit;

	/**
	 * For a key <i>k</i>, the {@link Queue} stores the number of events observed in the time interval <i>(k-intervalSize,k(</i>, i.e.,
	 * the interval <b>excludes</b> the value <i>k</i>.
	 */
	private final Queue<Entry<Long, Long>> eventCountsPerInterval = new ConcurrentLinkedQueue<Entry<Long, Long>>();

	private final long intervalSize;

	private final AtomicLong currentCountForCurrentInterval = new AtomicLong(0);

	private volatile long firstTimestampInCurrentInterval = -1; // initialized with the first incoming event
	private volatile long lastTimestampInCurrentInterval = -1; // initialized with the first incoming event

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public CountingThroughputFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;

		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			LOG.warn(configTimeunitProperty + " is no valid TimeUnit! Using inherited value of " + this.timeunit.name() + " instead.");
			configTimeunit = this.timeunit;
		}

		this.intervalSize = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_INTERVAL_SIZE), configTimeunit);
		this.intervalsBasedOn1stTstamp = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INTERVAL_SIZE, Long.toString(this.intervalSize));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP, Boolean.toString(this.intervalsBasedOn1stTstamp));
		return configuration;
	}

	private void processEvent(final Object event, final long currentTime) {
		final long startOfTimestampsInterval = this.computeFirstTimestampInInterval(currentTime);
		final long endOfTimestampsInterval = this.computeLastTimestampInInterval(currentTime);

		synchronized (this) {
			// Check if we need to close the current interval.
			if (endOfTimestampsInterval > this.lastTimestampInCurrentInterval) {
				if (this.firstTimestampInCurrentInterval >= 0) { // don't do this for the first record (only used for initialization of variables)
					final long count = this.currentCountForCurrentInterval.get();
					super.deliver(OUTPUT_PORT_NAME_THROUGHPUT, count);
					this.eventCountsPerInterval.add(new ImmutableEntry<Long, Long>(this.lastTimestampInCurrentInterval + 1, count));

					long numIntervalsElapsed = 1; // refined below
					numIntervalsElapsed = (endOfTimestampsInterval - this.lastTimestampInCurrentInterval) / this.intervalSize;
					if (numIntervalsElapsed > 1) { // NOPMD (AvoidDeeplyNestedIfStmts)
						for (int i = 1; i < numIntervalsElapsed; i++) {
							super.deliver(OUTPUT_PORT_NAME_THROUGHPUT, 0L); // do we really want to send this?
							this.eventCountsPerInterval.add(new ImmutableEntry<Long, Long>((this.lastTimestampInCurrentInterval + (i * this.intervalSize)) + 1, 0L));
						}
					}

				}

				this.firstTimestampInCurrentInterval = startOfTimestampsInterval;
				this.lastTimestampInCurrentInterval = endOfTimestampsInterval;
				this.currentCountForCurrentInterval.set(0);
			}

			this.currentCountForCurrentInterval.incrementAndGet(); // only incremented in synchronized blocks
		}
		super.deliver(OUTPUT_PORT_NAME_RELAYED_OBJECTS, event);
	}

	/**
	 * This method represents the input port for incoming records.
	 * 
	 * @param record
	 *            The next record.
	 */
	// #841 What happens with unordered events (i.e., timestamps before firstTimestampInCurrentInterval)?
	@InputPort(name = INPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class },
			description = "Receives incoming monitoring records to be considered for the throughput computation and uses the record's logging timestamp")
	public final void inputRecord(final IMonitoringRecord record) {
		this.processEvent(record, record.getLoggingTimestamp());
	}

	/**
	 * This method represents the input port for incoming object.
	 * 
	 * @param object
	 *            The next object.
	 */
	@InputPort(name = INPUT_PORT_NAME_OBJECTS, eventTypes = { Object.class },
			description = "Receives incoming objects to be considered for the throughput computation and uses the current system time")
	public final void inputObjects(final Object object) {
		this.processEvent(object, this.currentTime());
	}

	/**
	 * Returns the current time in {@link TimeUnit#MILLISECONDS} since 1970.
	 * 
	 * @return The current time
	 */
	private long currentTime() {
		return this.timeunit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	// #840 is this correct? it probably makes more sense to provide a copy.
	public Collection<Entry<Long, Long>> getCountsPerInterval() {
		return Collections.unmodifiableCollection(this.eventCountsPerInterval);
	}

	/**
	 * Returns the first timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * 
	 * @return The timestamp in question.
	 */
	private long computeFirstTimestampInInterval(final long timestamp) {
		final long referenceTimePoint;

		if (this.firstIntervalStart == -1) {
			this.firstIntervalStart = timestamp;
		}

		if (this.intervalsBasedOn1stTstamp) {
			referenceTimePoint = this.firstIntervalStart;
		} else {
			referenceTimePoint = 0;
		}

		return referenceTimePoint + (((timestamp - referenceTimePoint) / this.intervalSize) * this.intervalSize);
	}

	/**
	 * Returns the last timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * @return The timestamp in question.
	 */
	private long computeLastTimestampInInterval(final long timestamp) {
		final long referenceTimePoint;
		if (this.intervalsBasedOn1stTstamp) {
			referenceTimePoint = this.firstIntervalStart;
		} else {
			referenceTimePoint = 0;
		}

		return referenceTimePoint + (((((timestamp - referenceTimePoint) / this.intervalSize) + 1) * this.intervalSize) - 1);
	}

	/**
	 * @return the intervalSize
	 */
	public long getIntervalSize() {
		return this.intervalSize;
	}

	/**
	 * @return the firstTimestampInCurrentInterval -1 if no record processed so far
	 */
	public long getFirstTimestampInCurrentInterval() {
		return this.firstTimestampInCurrentInterval;
	}

	/**
	 * @return the lastTimestampInCurrentInterval -1 if no record processed so far
	 */
	public long getLastTimestampInCurrentInterval() {
		return this.lastTimestampInCurrentInterval;
	}

	/**
	 * @return the currentCountForCurrentInterval
	 */
	public long getCurrentCountForCurrentInterval() {
		return this.currentCountForCurrentInterval.get();
	}
}
