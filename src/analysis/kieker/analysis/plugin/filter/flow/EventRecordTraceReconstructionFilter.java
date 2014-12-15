/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
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
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
@Plugin(
		name = "Trace Reconstruction Filter (Event)",
		description = "Filter to reconstruct event based (flow) traces",
		outputPorts = {
			@OutputPort(
					name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
					description = "Outputs valid traces", eventTypes = { TraceEventRecords.class }),
			@OutputPort(
					name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID,
					description = "Outputs traces missing crucial records", eventTypes = { TraceEventRecords.class }) },
		configuration = {
			@Property(
					name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT,
					defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(
					name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION,
					defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TIME),
			@Property(
					name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT,
					defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TIME) })
public final class EventRecordTraceReconstructionFilter extends AbstractFilterPlugin {
	/**
	 * The name of the output port delivering the valid traces.
	 */
	public static final String OUTPUT_PORT_NAME_TRACE_VALID = "validTraces";
	/**
	 * The name of the output port delivering the invalid traces.
	 */
	public static final String OUTPUT_PORT_NAME_TRACE_INVALID = "invalidTraces";
	/**
	 * The name of the input port receiving the trace records.
	 */
	public static final String INPUT_PORT_NAME_TRACE_RECORDS = "traceRecords";
	/**
	 * The name of the input port receiving the trace records.
	 */
	public static final String INPUT_PORT_NAME_TRACEEVENT_RECORDS = "traceEventRecords";
	/**
	 * The name of the input port receiving the trace records.
	 */
	public static final String INPUT_PORT_NAME_TIME_EVENT = "timestamps";

	/**
	 * The name of the property determining the time unit.
	 */
	public static final String CONFIG_PROPERTY_NAME_TIMEUNIT = "timeunit";
	/**
	 * The name of the property determining the maximal trace duration.
	 */
	public static final String CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION = "maxTraceDuration";
	/**
	 * The name of the property determining the maximal trace timeout.
	 */
	public static final String CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT = "maxTraceTimeout";
	/**
	 * The default value of the properties for the maximal trace duration and timeout.
	 */
	public static final String CONFIG_PROPERTY_VALUE_MAX_TIME = "9223372036854775807"; // String.valueOf(Long.MAX_VALUE)
	/**
	 * The default value of the time unit property (nanoseconds).
	 */
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()

	private final TimeUnit timeunit;
	private final long maxTraceDuration;
	private final long maxTraceTimeout;
	private final boolean timeout;
	private long maxEncounteredLoggingTimestamp = -1;

	private final Map<Long, TraceBuffer> traceId2trace;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public EventRecordTraceReconstructionFilter(final Configuration configuration, final IProjectContext projectContext) {
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

		this.maxTraceDuration = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION), configTimeunit);
		this.maxTraceTimeout = this.timeunit.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT), configTimeunit);
		this.timeout = !((this.maxTraceTimeout == Long.MAX_VALUE) && (this.maxTraceDuration == Long.MAX_VALUE));
		this.traceId2trace = new ConcurrentHashMap<Long, TraceBuffer>();
	}

	/**
	 * This method is the input port for the timeout.
	 * 
	 * @param timestamp
	 *            The timestamp
	 */
	@InputPort(
			name = INPUT_PORT_NAME_TIME_EVENT,
			description = "Input port for a periodic time signal",
			eventTypes = { Long.class })
	public void newEvent(final Long timestamp) {
		synchronized (this) {
			if (this.timeout) {
				this.processTimeoutQueue(timestamp);
			}
		}
	}

	/**
	 * This method is the input port for the new events for this filter.
	 * 
	 * @param traceEventRecords
	 *            The new record to handle.
	 */
	@InputPort(
			name = INPUT_PORT_NAME_TRACEEVENT_RECORDS,
			description = "Reconstruct traces from incoming traces",
			eventTypes = { TraceEventRecords.class })
	public void newTraceEventRecord(final TraceEventRecords traceEventRecords) {
		final TraceMetadata trace = traceEventRecords.getTraceMetadata();
		if (null != trace) {
			this.newEvent(trace);
		}
		for (final AbstractTraceEvent record : traceEventRecords.getTraceEvents()) {
			this.newEvent(record);
		}
	}

	/**
	 * This method is the input port for the new events for this filter.
	 * 
	 * @param record
	 *            The new record to handle.
	 */
	@InputPort(
			name = INPUT_PORT_NAME_TRACE_RECORDS,
			description = "Reconstruct traces from incoming flow records",
			eventTypes = { TraceMetadata.class, AbstractTraceEvent.class })
	public void newEvent(final IFlowRecord record) {
		final Long traceId;
		TraceBuffer traceBuffer;
		final long loggingTimestamp;
		if (record instanceof TraceMetadata) {
			traceId = ((TraceMetadata) record).getTraceId();
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
			traceBuffer.setTrace((TraceMetadata) record);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
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

		private TraceMetadata trace;
		private final SortedSet<AbstractTraceEvent> events = new TreeSet<AbstractTraceEvent>(COMPARATOR);

		private boolean closeable;
		private boolean damaged;
		private int openEvents;
		private int maxOrderIndex = -1;

		private long minLoggingTimestamp = Long.MAX_VALUE;
		private long maxLoggingTimestamp = -1;

		private long traceId = -1;

		private final Deque<BeforeOperationEvent> eventStack = new LinkedList<BeforeOperationEvent>();
		private final Deque<AbstractTraceEvent> eventsAfterCheck = new LinkedList<AbstractTraceEvent>();

		/**
		 * Creates a new instance of this class.
		 */
		public TraceBuffer() {
			// default empty constructor
		}

		public void insertEvent(AbstractTraceEvent event) {
			this.checkEventIfAfterEventMissingAndRepair(event);
			while (!this.eventsAfterCheck.isEmpty()) {
				event = this.eventsAfterCheck.removeFirst();
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
						LOG.error("Duplicate entry for orderIndex " + orderIndex + " with traceId " + myTraceId);
						this.damaged = true;
					}
				}
			}
		}

		// Short description: All incoming BeforeEvents are added into a stack and it proceeds as before.
		// If a AfterEvent arrives, it is matched to the last added BeforeEvent.
		// If it matches, all is good and it proceeds normally.
		// Otherwise AfterEvents corresponding to the last added BeforeEvent are manually added.
		// Afterwards the BeforeEvent is released, and the matching process continues with the next BeforeEvent in the stack.

		// In general: I used a LinkedList as a stack (LIFO Use) to collect all BeforeEvents. In addition I used a LinkedList (FIFO Use) to add all events in a list.
		// I used a List for all incoming events for the case we add some AfterEvents manually. I did not know another solution to add all Events in the
		// inserEvent(..) method above.
		// Therefore I added that loop in the insertEvent(..) method above.
		public void checkEventIfAfterEventMissingAndRepair(final AbstractTraceEvent event) {
			int orderIndex = event.getOrderIndex();
			final int shortTimeGapBetweenBeforeAndAfter = 100000; // Used for some time gap between manually added AfterEvent and his BeforeEvent

			// Only true, if AfterEvents were already added manually -> OrderIndex of all following events are not correct, so must be changed
			if (orderIndex < this.maxOrderIndex) {
				orderIndex = this.maxOrderIndex + 1;
			}

			if (event instanceof BeforeOperationEvent) {
				this.eventStack.addLast((BeforeOperationEvent) event);
				this.eventsAfterCheck.add(event);
			} else if (event instanceof AfterOperationEvent) {
				while ((this.eventStack.getLast().getOperationSignature() != ((AfterOperationEvent) event).getOperationSignature()) &&
						(this.eventStack.getLast().getClassSignature() != ((AfterOperationEvent) event).getClassSignature())) {

					// adds new AfterOperationEvent according to his BeforeEvent (AfterEvent was missing)
					this.eventsAfterCheck.add(new AfterOperationEvent(this.eventStack.getLast().getTimestamp() + shortTimeGapBetweenBeforeAndAfter,
							event.getTraceId(), orderIndex,
							this.eventStack.getLast().getOperationSignature(), this.eventStack.getLast().getClassSignature()));
					this.eventStack.removeLast();
					orderIndex++;
				}
				this.eventStack.removeLast();
				// True, if current AfterOperationEvent matches to the BeforeOperationEvent in the stack at beginning -> event can be passed without changes
				if ((orderIndex - 1) == this.maxOrderIndex) {
					this.eventsAfterCheck.add(event);
				} else {
					// One or more AfterOperationEvent(s) had to be added manually
					// the actual event needs a new orderIndex, because of the new added AfterOperationEvent(s)
					this.eventsAfterCheck.add(new AfterOperationEvent(event.getTimestamp(), event.getTraceId(), orderIndex,
							((AfterOperationEvent) event).getOperationSignature(), ((AfterOperationEvent) event).getClassSignature()));
				}
			}
		}

		public void setTrace(final TraceMetadata trace) {
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
				return (this.trace == null) || this.damaged || (this.openEvents != 0) || (((this.maxOrderIndex + 1) != this.events.size()) || this.events.isEmpty());
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

			/**
			 * Creates a new instance of this class.
			 */
			public TraceEventComperator() {
				// default empty constructor
			}

			@Override
			public int compare(final AbstractTraceEvent o1, final AbstractTraceEvent o2) {
				return o1.getOrderIndex() - o2.getOrderIndex();
			}
		}
	}

}
