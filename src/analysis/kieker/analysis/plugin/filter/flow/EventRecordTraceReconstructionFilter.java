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

package kieker.analysis.plugin.filter.flow;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.Trace;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * @author Jan Waller
 */
@Plugin(
		name = "Trace Reconstruction Filter (Event)",
		description = "Filter to reconstruct event based (flow) traces",
		outputPorts = {
			@OutputPort(name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, description = "Outputs valid traces", eventTypes = { TraceEventRecords.class }),
			@OutputPort(name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID, description = "Outputs traces missing crucial records", eventTypes = { TraceEventRecords.class }) },
		configuration = {
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TIME),
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TIME) })
public final class EventRecordTraceReconstructionFilter extends AbstractFilterPlugin {
	public static final String OUTPUT_PORT_NAME_TRACE_VALID = "validTraces";
	public static final String OUTPUT_PORT_NAME_TRACE_INVALID = "invalidTraces";
	public static final String INPUT_PORT_NAME_TRACE_RECORDS = "traceRecords";

	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	public static final String CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION = "maxTraceDuration";
	public static final String CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT = "maxTraceTimeout";
	public static final String CONFIG_PROPERTY_VALUE_MAX_TIME = "9223372036854775807"; // String.valueOf(Long.MAX_VALUE)
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()

	// internally we will assume nanosecond precision
	// TODO: it would be better to use the actual precision of the records (we assume records use nanoseconds)
	// TODO: log meta-information on monitoring logs somewhere? e.g. used timesource
	private final TimeUnit timeunit = TimeUnit.NANOSECONDS;
	private final long maxTraceDuration;
	private final long maxTraceTimeout;
	private final boolean timeout;
	private long maxEncounteredLoggingTimestamp = -1;

	private final Map<Long, TraceBuffer> traceId2trace;

	public EventRecordTraceReconstructionFilter(final Configuration configuration) {
		super(configuration);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT));
		} catch (final IllegalArgumentException ex) {
			configTimeunit = this.timeunit;
		}

		this.maxTraceDuration = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION), configTimeunit);
		this.maxTraceTimeout = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT), configTimeunit);
		this.timeout = !((this.maxTraceTimeout == Long.MAX_VALUE) && (this.maxTraceDuration == Long.MAX_VALUE)); // NOPMD (bitwise conversion makes code unreadable)
		this.traceId2trace = new ConcurrentHashMap<Long, TraceBuffer>();
	}

	@InputPort(
			name = INPUT_PORT_NAME_TRACE_RECORDS,
			description = "Reconstruct traces from incoming flow records",
			eventTypes = { Trace.class, AbstractTraceEvent.class })
	public void newEvent(final IFlowRecord record) {
		final Long traceId;
		TraceBuffer traceBuffer;
		final long loggingTimestamp;
		if (record instanceof Trace) {
			traceId = ((Trace) record).getTraceId();
			traceBuffer = this.traceId2trace.get(traceId);
			if (traceBuffer == null) { // first record for this id!
				synchronized (this) {
					traceBuffer = this.traceId2trace.get(traceId);
					if (traceBuffer == null) { // NOCS (DCL)
						traceBuffer = new TraceBuffer();
						this.traceId2trace.put(traceId, traceBuffer);
					}
				}
			}
			traceBuffer.setTrace((Trace) record);
			loggingTimestamp = -1;
		} else if (record instanceof AbstractTraceEvent) {
			traceId = ((AbstractTraceEvent) record).getTraceId();
			traceBuffer = this.traceId2trace.get(traceId);
			if (traceBuffer == null) { // first record for this id!
				synchronized (this) {
					traceBuffer = this.traceId2trace.get(traceId);
					if (traceBuffer == null) { // NOCS (DCL)
						traceBuffer = new TraceBuffer();
						this.traceId2trace.put(traceId, traceBuffer);
					}
				}
			}
			traceBuffer.insertEvent((AbstractTraceEvent) record);
			loggingTimestamp = ((AbstractTraceEvent) record).getTimestamp();
		} else {
			return; // invalid type which should not happen due to the specified eventTypes
		}
		if (traceBuffer.isFinished()) {
			synchronized (this) { // has to be synchronized because of timeout cleanup
				this.traceId2trace.remove(traceId);
			}
			super.deliver(OUTPUT_PORT_NAME_TRACE_VALID, traceBuffer.toTraceEvents());
		}
		if (this.timeout) {
			synchronized (this) {
				// can we assume a rough order of logging timestamps? (yes, except with DB reader)
				if (loggingTimestamp > this.maxEncounteredLoggingTimestamp) {
					this.maxEncounteredLoggingTimestamp = loggingTimestamp;
				}
				this.processTimeoutQueue(this.maxEncounteredLoggingTimestamp);
			}
		}
	}

	@Override
	public void terminate(final boolean error) {
		super.terminate(error);
		synchronized (this) {
			for (final Entry<Long, TraceBuffer> entry : this.traceId2trace.entrySet()) {
				final TraceBuffer traceBuffer = entry.getValue();
				if (traceBuffer.isInvalid()) {
					super.deliver(OUTPUT_PORT_NAME_TRACE_INVALID, traceBuffer.toTraceEvents());
				} else {
					super.deliver(OUTPUT_PORT_NAME_TRACE_VALID, traceBuffer.toTraceEvents());
				}
			}
			this.traceId2trace.clear();
		}
	}

	// only called within synchronized! We assume timestamps >= 0
	private void processTimeoutQueue(final long timestamp) {
		final long duration = timestamp - this.maxTraceDuration;
		final long traceTimeout = timestamp - this.maxTraceTimeout;
		for (final Iterator<Entry<Long, TraceBuffer>> iterator = this.traceId2trace.entrySet().iterator(); iterator.hasNext();) {
			final TraceBuffer traceBuffer = iterator.next().getValue();
			if ((traceBuffer.getMaxLoggingTimestamp() <= traceTimeout) // long time no see
					|| (traceBuffer.getMinLoggingTimestamp() <= duration)) { // max duration is gone
				if (traceBuffer.isInvalid()) {
					super.deliver(OUTPUT_PORT_NAME_TRACE_INVALID, traceBuffer.toTraceEvents());
				} else {
					super.deliver(OUTPUT_PORT_NAME_TRACE_VALID, traceBuffer.toTraceEvents());
				}
				iterator.remove();
			}
		}
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_TIMEUNIT, this.timeunit.name());
		configuration.setProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, String.valueOf(this.maxTraceDuration));
		configuration.setProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, String.valueOf(this.maxTraceTimeout));
		return configuration;
	}

	/**
	 * The TraceBuffer is synchronized to prevent problems with concurrent access.
	 * 
	 * @author Jan Waller
	 */
	private static final class TraceBuffer {
		private static final Log LOG = LogFactory.getLog(TraceBuffer.class);
		private static final Comparator<AbstractTraceEvent> COMPARATOR = new TraceEventComperator();

		private Trace trace;
		private final SortedSet<AbstractTraceEvent> events = new TreeSet<AbstractTraceEvent>(COMPARATOR);

		private boolean closeable;
		private boolean damaged;
		private int openEvents;
		private int maxOrderIndex = -1;

		private long minLoggingTimestamp = Long.MAX_VALUE;
		private long maxLoggingTimestamp = -1;

		private long traceId = -1;

		public TraceBuffer() {
			// default empty constructor
		}

		public void insertEvent(final AbstractTraceEvent event) {
			final long myTraceId = event.getTraceId();
			synchronized (this) {
				if (this.traceId == -1) {
					this.traceId = myTraceId;
				} else if (this.traceId != myTraceId) {
					LOG.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId + " in event " + event.toString());
					this.damaged = true;
				}
				final long loggingTimestamp = event.getTimestamp();
				if (loggingTimestamp > this.maxLoggingTimestamp) {
					this.maxLoggingTimestamp = loggingTimestamp;
				}
				if (loggingTimestamp < this.minLoggingTimestamp) {
					this.minLoggingTimestamp = loggingTimestamp;
				}
				final int orderIndex = event.getOrderIndex();
				if (orderIndex > this.maxOrderIndex) {
					this.maxOrderIndex = orderIndex;
				}
				if (event instanceof BeforeOperationEvent) {
					if (orderIndex == 0) {
						this.closeable = true;
					}
					this.openEvents++;
				} else if (event instanceof AfterOperationEvent) {
					this.openEvents--;
				} else if (event instanceof AfterOperationFailedEvent) {
					this.openEvents--;
				}
				if (!this.events.add(event)) {
					LOG.error("Duplicate entry for orderIndex " + orderIndex + " with tarceId " + myTraceId);
					this.damaged = true;
				}
			}
		}

		public void setTrace(final Trace trace) {
			final long myTraceId = trace.getTraceId();
			synchronized (this) {
				if (this.traceId == -1) {
					this.traceId = myTraceId;
				} else if (this.traceId != myTraceId) {
					LOG.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId + " in trace " + trace.toString());
					this.damaged = true;
				}
				if (this.trace == null) {
					this.trace = trace;
				} else {
					LOG.error("Duplicate Trace entry for traceId " + myTraceId);
					this.damaged = true;
				}
			}
		}

		public boolean isFinished() {
			synchronized (this) {
				return this.closeable && !this.isInvalid();
			}
		}

		public boolean isInvalid() {
			synchronized (this) {
				return (this.trace == null) || this.damaged || (this.openEvents != 0) || ((this.maxOrderIndex + 1) != this.events.size());
			}
		}

		public TraceEventRecords toTraceEvents() {
			synchronized (this) {
				return new TraceEventRecords(this.trace, this.events.toArray(new AbstractTraceEvent[this.events.size()]));
			}
		}

		public long getMaxLoggingTimestamp() {
			synchronized (this) {
				return this.maxLoggingTimestamp;
			}
		}

		public long getMinLoggingTimestamp() {
			synchronized (this) {
				return this.minLoggingTimestamp;
			}
		}

		/**
		 * @author Jan Waller
		 */
		private static final class TraceEventComperator implements Comparator<AbstractTraceEvent>, Serializable {
			private static final long serialVersionUID = 8920737343446332517L;

			public TraceEventComperator() {
				// default empty constructor
			}

			public int compare(final AbstractTraceEvent o1, final AbstractTraceEvent o2) {
				return o1.getOrderIndex() - o2.getOrderIndex();
			}
		}
	}
}
