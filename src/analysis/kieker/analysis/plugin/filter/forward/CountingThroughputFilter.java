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

import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.ImmutableEntry;

/**
 * An instance of this class computes the throughput in terms of the number of events received per time unit.
 * 
 * Note that only one of the input ports should be used in a configuration!
 * 
 * TODO: In future versions, this filter should provide an output port in addition to the existing relay port.
 * TODO: In future versions, the throughput computation could (additionally) be triggered by external timer events
 * TODO: Introduce bounding capacity (Circular Buffer)
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Plugin(
		description = "A filter computing the throughput in terms of the number of events received per time unit",
		outputPorts = {
			@OutputPort(name = CountingThroughputFilter.OUTPUT_PORT_NAME_RELAYED_OBJECTS, eventTypes = { Object.class }, description = "Provides each incoming object")
		},
		configuration = {
			@Property(name = CountingThroughputFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = CountingThroughputFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(name = CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVAL_SIZE, defaultValue = CountingThroughputFilter.CONFIG_PROPERTY_VALUE_INTERVAL_SIZE_ONE_MINUTE),
			@Property(name = CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP, defaultValue = "true")
		})
public final class CountingThroughputFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_RECORDS = "inputRecords";
	public static final String INPUT_PORT_NAME_OBJECTS = "inputObjects";

	public static final String OUTPUT_PORT_NAME_RELAYED_OBJECTS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	public static final String CONFIG_PROPERTY_NAME_INTERVAL_SIZE = "intervalSize";
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()

	/**
	 * If the value is set to false, the intervals are computed based on time since 1970-1-1
	 */
	public static final String CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP = "intervalsBasedOn1stTstamp";

	/**
	 * The configuration property value for {@link #CONFIG_PROPERTY_NAME_INTERVAL_SIZE}, leading to a bin size of 1 minute
	 */
	public static final String CONFIG_PROPERTY_VALUE_INTERVAL_SIZE_ONE_MINUTE = "60000000000";

	private volatile long firstIntervalStart = -1;
	private final boolean intervalsBasedOn1stTstamp;
	private final TimeUnit timeunit = TimeUnit.NANOSECONDS; // TODO: should be inferred from used records?!

	// TODO: Introduce bounded capacity
	/**
	 * For a key <i>k</i>, the {@link Queue} stores the number of events observed in the time interval <i>(k-intervalSizeNanos,k(</i>, i.e.,
	 * the interval <b>excludes</b> the value <i>k</i>.
	 */
	private final Queue<Entry<Long, Long>> eventCountsPerInterval = new ConcurrentLinkedQueue<Entry<Long, Long>>();

	// TODO: additional TreeMap for accumulated values?

	private final long intervalSize;

	private final AtomicLong currentCountForCurrentInterval = new AtomicLong(0);

	private volatile long firstTimestampInCurrentInterval = -1; // initialized with the first incoming event
	private volatile long lastTimestampInCurrentInterval = -1; // initialized with the first incoming event

	/**
	 * Constructs a {@link CountingThroughputFilter}.
	 */
	public CountingThroughputFilter(final Configuration configuration) {
		super(configuration);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT));
		} catch (final IllegalArgumentException ex) {
			configTimeunit = this.timeunit;
		}
		this.intervalSize = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_INTERVAL_SIZE), configTimeunit);
		this.intervalsBasedOn1stTstamp = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP);
	}

	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_INTERVAL_SIZE, Long.toString(this.intervalSize));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP, Boolean.toString(this.intervalsBasedOn1stTstamp));
		return configuration;
	}

	private void processEvent(final Object event, final long currentTimeNanos) {
		final long startOfTimestampsInterval = this.computeFirstTimestampInInterval(currentTimeNanos);
		final long endOfTimestampsInterval = this.computeLastTimestampInInterval(currentTimeNanos);

		synchronized (this) {
			/*
			 * Check if we need to close the current interval.
			 */
			if (endOfTimestampsInterval > this.lastTimestampInCurrentInterval) {
				if (this.firstTimestampInCurrentInterval >= 0) { // don't do this for the first record (only used for initialization of variables)
					this.eventCountsPerInterval.add(
							new ImmutableEntry<Long, Long>(
									this.lastTimestampInCurrentInterval + 1,
									this.currentCountForCurrentInterval.get())
							);

					long numIntervalsElapsed = 1; // refined below
					numIntervalsElapsed = (endOfTimestampsInterval - this.lastTimestampInCurrentInterval) / this.intervalSize;
					if (numIntervalsElapsed > 1) {
						for (int i = 1; i < (numIntervalsElapsed); i++) {
							this.eventCountsPerInterval.add(
									new ImmutableEntry<Long, Long>((this.lastTimestampInCurrentInterval + (i * this.intervalSize)) + 1, 0l)
									);
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

	// TODO: What happens with unordered events (i.e., timestamps before firstTimestampInCurrentInterval)?
	@InputPort(name = INPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Receives incoming monitoring records to be considered for the throughput computation and uses the record's logging timestamp")
	public final void inputRecord(final IMonitoringRecord record) {
		this.processEvent(record, record.getLoggingTimestamp());
	}

	@InputPort(name = INPUT_PORT_NAME_OBJECTS, eventTypes = { Object.class }, description = "Receives incoming objects to be considered for the throughput computation and uses the current system time")
	public final void inputObjects(final Object object) {
		this.processEvent(object, this.currentTime());
	}

	/**
	 * Returns the current time in since 1970.
	 * 
	 * TODO: Note that we only have a timer resolution of milliseconds here!
	 * 
	 * @return
	 */
	private long currentTime() {
		return this.timeunit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	// TODO: is this correct? it probably makes more sense to provide a copy.
	public Collection<Entry<Long, Long>> getCountsPerInterval() {
		return Collections.unmodifiableCollection(this.eventCountsPerInterval);
	}

	/**
	 * Returns the first timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * @return
	 */
	private long computeFirstTimestampInInterval(final long timestamp) {
		final long referenceTimePoint;

		if (this.firstIntervalStart == -1) {
			this.firstIntervalStart = timestamp;
		}

		if (this.intervalsBasedOn1stTstamp) {
			referenceTimePoint = this.firstIntervalStart;
		} else {
			referenceTimePoint = 0; // 1970-1-1 in nanos
		}

		return referenceTimePoint + (((timestamp - referenceTimePoint) / this.intervalSize) * this.intervalSize);
	}

	/**
	 * Returns the last timestamp included in the interval that corresponds to the given timestamp.
	 * 
	 * @param timestamp
	 * @return
	 */
	private long computeLastTimestampInInterval(final long timestamp) {
		final long referenceTimePoint;
		if (this.intervalsBasedOn1stTstamp) {
			referenceTimePoint = this.firstIntervalStart;
		} else {
			referenceTimePoint = 0; // 1970-1-1 in nanos
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
