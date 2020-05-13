/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.ConstructionEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorFailedObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.BeforeConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationFailedObjectEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectEvent;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
@Plugin(name = "Trace Reconstruction Filter (Event)", description = "Filter to reconstruct event based (flow) traces", outputPorts = {
	@OutputPort(name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, description = "Outputs valid traces", eventTypes = {
		TraceEventRecords.class }),
	@OutputPort(name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID, description = "Outputs traces missing crucial records", eventTypes = {
		TraceEventRecords.class }) }, configuration = {
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT),
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TIME),
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, defaultValue = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TIME),
			@Property(name = EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES, defaultValue = "false") })
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
	 * The default value of the properties for the maximal trace duration and
	 * timeout.
	 */
	public static final String CONFIG_PROPERTY_VALUE_MAX_TIME = "9223372036854775807"; // String.valueOf(Long.MAX_VALUE)
	/**
	 * The default value of the time unit property (nanoseconds).
	 */
	public static final String CONFIG_PROPERTY_VALUE_TIMEUNIT = "NANOSECONDS"; // TimeUnit.NANOSECONDS.name()
	/**
	 * This is the name of the property determining whether to repair BeforeEvents
	 * with missing AfterEvents (e.g. because of software crash) or not.
	 */
	public static final String CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES = "repairEventBasedTraces";

	private final TimeUnit timeunit;
	private final long maxTraceDuration;
	private final long maxTraceTimeout;
	private final boolean hasTimeout;
	private final boolean repairEventBasedTracesEnabled;
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
	public EventRecordTraceReconstructionFilter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.timeunit = super.recordsTimeUnitFromProjectContext;

		final String configTimeunitProperty = configuration.getStringProperty(CONFIG_PROPERTY_NAME_TIMEUNIT);
		TimeUnit configTimeunit;
		try {
			configTimeunit = TimeUnit.valueOf(configTimeunitProperty);
		} catch (final IllegalArgumentException ex) {
			this.logger.warn("{} is no valid TimeUnit! Using inherited value of {} instead.", configTimeunitProperty,
					this.timeunit.name());
			configTimeunit = this.timeunit;
		}

		this.repairEventBasedTracesEnabled = configuration
				.getBooleanProperty(CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES);
		this.maxTraceDuration = this.timeunit
				.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION), configTimeunit);
		this.maxTraceTimeout = this.timeunit
				.convert(configuration.getLongProperty(CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT), configTimeunit);
		this.hasTimeout = (this.maxTraceTimeout != Long.MAX_VALUE) || (this.maxTraceDuration != Long.MAX_VALUE);
		this.traceId2trace = new ConcurrentHashMap<>();
	}

	/**
	 * This method is the input port for the timeout.
	 *
	 * @param timestamp
	 *            The timestamp
	 */
	@InputPort(name = INPUT_PORT_NAME_TIME_EVENT, description = "Input port for a periodic time signal", eventTypes = {
		Long.class })
	public void newEvent(final Long timestamp) {
		synchronized (this) {
			if (this.hasTimeout) {
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
	@InputPort(name = INPUT_PORT_NAME_TRACEEVENT_RECORDS, description = "Reconstruct traces from incoming traces", eventTypes = {
		TraceEventRecords.class })
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
	@InputPort(name = INPUT_PORT_NAME_TRACE_RECORDS, description = "Reconstruct traces from incoming flow records", eventTypes = {
		TraceMetadata.class, AbstractTraceEvent.class })
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
						traceBuffer.setRepairEventBasedTracesEnabled(this.repairEventBasedTracesEnabled);
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
						traceBuffer.setRepairEventBasedTracesEnabled(this.repairEventBasedTracesEnabled);
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
		if (this.hasTimeout) {
			synchronized (this) {
				// can we assume a rough order of logging timestamps? (yes, except with DB
				// reader)
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
			final Collection<Long> sortedTraceIds = this.getSortedTraceIds(this.traceId2trace.keySet());

			for (final Long traceId : sortedTraceIds) {
				final TraceBuffer traceBuffer = this.traceId2trace.get(traceId);
				if (this.repairEventBasedTracesEnabled && !traceBuffer.getEventStack().isEmpty()) {
					traceBuffer.repairAllBeforeEventsLeftInStackAtTermination();
				}

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

		final Collection<Long> sortedTraceIds = this.getSortedTraceIds(this.traceId2trace.keySet());

		for (final Long traceId : sortedTraceIds) {
			final TraceBuffer traceBuffer = this.traceId2trace.get(traceId);
			if ((traceBuffer.getMaxLoggingTimestamp() <= traceTimeout) // long time no see
					|| (traceBuffer.getMinLoggingTimestamp() <= duration)) { // max duration is gone

				if (traceBuffer.isInvalid()) {
					super.deliver(OUTPUT_PORT_NAME_TRACE_INVALID, traceBuffer.toTraceEvents());
				} else {
					super.deliver(OUTPUT_PORT_NAME_TRACE_VALID, traceBuffer.toTraceEvents());
				}

				this.traceId2trace.remove(traceId);
			}
		}
	}

	/**
	 * HACK: We sort the trace ids to get a deterministic result when plotting the
	 * traces to the dot format.
	 * <p>
	 * In future, we should better sort according to the start timestamp of each
	 * trace.
	 */
	private Collection<Long> getSortedTraceIds(final Set<Long> keys) {
		final List<Long> copiedKeys = new ArrayList<>(keys);
		Collections.sort(copiedKeys);

		return copiedKeys;
		// return keys;
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
		configuration.setProperty(CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES,
				Boolean.toString(this.repairEventBasedTracesEnabled));
		return configuration;
	}

	/**
	 * The TraceBuffer is synchronized to prevent problems with concurrent access.
	 *
	 * @author Jan Waller
	 */
	private static final class TraceBuffer {
		private static final Logger LOGGER = LoggerFactory.getLogger(TraceBuffer.class);
		private static final Comparator<AbstractTraceEvent> COMPARATOR = new TraceEventComperator();

		private TraceMetadata trace;
		private final SortedSet<AbstractTraceEvent> events = new TreeSet<>(COMPARATOR);

		private boolean closeable;
		private boolean damaged;
		private int openEvents;
		private int maxOrderIndex = -1;

		private long minLoggingTimestamp = Long.MAX_VALUE;
		private long maxLoggingTimestamp = -1;

		private long traceId = -1;

		private boolean beforeEventStackEmptyAtTermination;
		private boolean repairEventBasedTracesEnabled;

		private final Deque<BeforeOperationEvent> beforeEventStack = new LinkedList<>();
		private final Deque<AbstractTraceEvent> eventQueue = new LinkedList<>();

		/**
		 * Creates a new instance of this class.
		 */
		public TraceBuffer() {
			// default empty constructor
		}

		public void insertEvent(final AbstractTraceEvent event) {
			final long myTraceId = event.getTraceId();
			if (this.repairEventBasedTracesEnabled) {
				if ((event instanceof CallOperationEvent) || (event instanceof ConstructionEvent)
						|| this.beforeEventStackEmptyAtTermination) {
					this.eventQueue.add(event);
				} else {
					this.checkIfAfterEventsMissingThenRepair(event);
				}
			} else {
				this.eventQueue.add(event);
			}
			synchronized (this) {
				while (!this.eventQueue.isEmpty()) {
					final AbstractTraceEvent receivedEvent = this.eventQueue.removeFirst();
					if (this.traceId == -1) {
						this.traceId = myTraceId;
					} else if (this.traceId != myTraceId) {
						LOGGER.error("Invalid traceId! Expected: {} but found: {} in event {}", this.traceId, myTraceId,
								event.toString());
						this.damaged = true;
					}
					final long loggingTimestamp = receivedEvent.getTimestamp();
					if (loggingTimestamp > this.maxLoggingTimestamp) {
						this.maxLoggingTimestamp = loggingTimestamp;
					}
					if (loggingTimestamp < this.minLoggingTimestamp) {
						this.minLoggingTimestamp = loggingTimestamp;
					}
					final int orderIndex = receivedEvent.getOrderIndex();
					if (orderIndex > this.maxOrderIndex) {
						this.maxOrderIndex = orderIndex;
					}
					if (receivedEvent instanceof BeforeOperationEvent) {
						if (orderIndex == 0) {
							this.closeable = true;
						}
						this.openEvents++;
					} else if (receivedEvent instanceof AfterOperationEvent) {
						this.openEvents--;
					} else if (receivedEvent instanceof AfterOperationFailedEvent) {
						this.openEvents--;
					}
					if (!this.events.add(receivedEvent)) {
						LOGGER.error("Duplicate entry for orderIndex {} with traceId {}", orderIndex, myTraceId);
						this.damaged = true;
					}
				}
			}
		}

		public void checkIfAfterEventsMissingThenRepair(final AbstractTraceEvent event) {
			int orderIndex = event.getOrderIndex();
			final boolean alreadyRepairedSomeEvents = orderIndex <= this.maxOrderIndex;

			if (alreadyRepairedSomeEvents) {
				orderIndex = this.maxOrderIndex + 1;
			}

			if (event instanceof BeforeOperationEvent) {
				this.beforeEventStack.addLast((BeforeOperationEvent) event);
				this.eventQueue.add(event);
			} else if (event instanceof AfterOperationEvent) {
				while ((!this.beforeEventStack.getLast().getOperationSignature()
						.equals(((AfterOperationEvent) event).getOperationSignature()))
						&& (!(this.beforeEventStack.getLast().getClassSignature())
								.equals(((AfterOperationEvent) event).getClassSignature()))) {
					final BeforeOperationEvent beforeEvent = this.beforeEventStack.getLast();
					final String opSignature = beforeEvent.getOperationSignature();
					final String classSignature = beforeEvent.getClassSignature();
					final long timestamp = event.getTimestamp();
					final long traceID = event.getTraceId();

					if (beforeEvent instanceof BeforeConstructorObjectEvent) {
						this.eventQueue.add(new AfterConstructorObjectEvent(timestamp, traceID, orderIndex, opSignature,
								classSignature,
								((BeforeConstructorObjectEvent) this.beforeEventStack.getLast()).getObjectId()));
					} else if (beforeEvent instanceof BeforeConstructorEvent) {
						this.eventQueue.add(
								new AfterConstructorEvent(timestamp, traceID, orderIndex, opSignature, classSignature));
					} else if (beforeEvent instanceof BeforeOperationObjectEvent) {
						this.eventQueue.add(new AfterOperationObjectEvent(timestamp, traceID, orderIndex, opSignature,
								classSignature,
								((BeforeOperationObjectEvent) this.beforeEventStack.getLast()).getObjectId()));
					} else {
						this.eventQueue.add(
								new AfterOperationEvent(timestamp, traceID, orderIndex, opSignature, classSignature));
					}
					this.beforeEventStack.removeLast();
					orderIndex++;
				}

				this.beforeEventStack.removeLast();
				// true as long as no events repaired, event passes without orderIndex
				// adjustment
				if (!alreadyRepairedSomeEvents && ((orderIndex - 1) == this.maxOrderIndex)) {
					this.eventQueue.add(event);
				} else {
					final String opSignature = ((AfterOperationEvent) event).getOperationSignature();
					final String classSignature = ((AfterOperationEvent) event).getClassSignature();
					final long timestamp = event.getTimestamp();
					final long traceID = event.getTraceId();

					if (event instanceof AfterConstructorFailedObjectEvent) {
						this.eventQueue.add(new AfterConstructorFailedObjectEvent(timestamp, traceID, orderIndex,
								opSignature, classSignature, ((AfterConstructorFailedObjectEvent) event).getCause(),
								((AfterConstructorFailedObjectEvent) event).getObjectId()));
					} else if (event instanceof AfterConstructorObjectEvent) {
						this.eventQueue.add(new AfterConstructorObjectEvent(timestamp, traceID, orderIndex, opSignature,
								classSignature, ((AfterConstructorObjectEvent) event).getObjectId()));
					} else if (event instanceof AfterConstructorFailedEvent) {
						this.eventQueue.add(new AfterConstructorFailedEvent(timestamp, traceID, orderIndex, opSignature,
								classSignature, ((AfterConstructorFailedEvent) event).getCause()));
					} else if (event instanceof AfterConstructorEvent) {
						this.eventQueue.add(
								new AfterConstructorEvent(timestamp, traceID, orderIndex, opSignature, classSignature));
					} else if (event instanceof AfterOperationFailedObjectEvent) {
						this.eventQueue.add(new AfterOperationFailedObjectEvent(timestamp, traceID, orderIndex,
								opSignature, classSignature, ((AfterOperationFailedObjectEvent) event).getCause(),
								((AfterOperationFailedObjectEvent) event).getObjectId()));
					} else if (event instanceof AfterOperationObjectEvent) {
						this.eventQueue.add(new AfterOperationObjectEvent(timestamp, traceID, orderIndex, opSignature,
								classSignature, ((AfterOperationObjectEvent) event).getObjectId()));
					} else if (event instanceof AfterOperationFailedEvent) {
						this.eventQueue.add(new AfterOperationFailedEvent(timestamp, traceID, orderIndex, opSignature,
								classSignature, ((AfterOperationFailedEvent) event).getCause()));
					} else {
						this.eventQueue.add(
								new AfterOperationEvent(timestamp, traceID, orderIndex, opSignature, classSignature));
					}
				}
			}
		}

		public void repairAllBeforeEventsLeftInStackAtTermination() {
			this.beforeEventStackEmptyAtTermination = true;
			while (!this.beforeEventStack.isEmpty()) {
				final BeforeOperationEvent beforeEvent = this.beforeEventStack.getLast();
				final String opSignature = beforeEvent.getOperationSignature();
				final String classSignature = beforeEvent.getClassSignature();
				final long timestamp = beforeEvent.getTimestamp();
				final long traceID = beforeEvent.getTraceId();
				final int orderIndex = this.maxOrderIndex + 1;

				if (beforeEvent instanceof BeforeConstructorObjectEvent) {
					this.insertEvent(
							new AfterConstructorObjectEvent(timestamp, traceID, orderIndex, opSignature, classSignature,
									((BeforeConstructorObjectEvent) this.beforeEventStack.getLast()).getObjectId()));
				} else if (beforeEvent instanceof BeforeConstructorEvent) {
					this.insertEvent(
							new AfterConstructorEvent(timestamp, traceID, orderIndex, opSignature, classSignature));
				} else if (beforeEvent instanceof BeforeOperationObjectEvent) {
					this.insertEvent(
							new AfterOperationObjectEvent(timestamp, traceID, orderIndex, opSignature, classSignature,
									((BeforeOperationObjectEvent) this.beforeEventStack.getLast()).getObjectId()));
				} else {
					this.insertEvent(
							new AfterOperationEvent(timestamp, traceID, orderIndex, opSignature, classSignature));
				}
				this.beforeEventStack.removeLast();
			}
		}

		public void setTrace(final TraceMetadata trace) {
			final long myTraceId = trace.getTraceId();
			synchronized (this) {
				if (this.traceId == -1) {
					this.traceId = myTraceId;
				} else if (this.traceId != myTraceId) {
					LOGGER.error("Invalid traceId! Expected: {} but found: {} in trace {}", this.traceId, myTraceId,
							trace.toString());
					this.damaged = true;
				}
				if (this.trace == null) {
					this.trace = trace;
				} else {
					LOGGER.error("Duplicate Trace entry for traceId {}", myTraceId);
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
				return (this.trace == null) || this.damaged || (this.openEvents != 0)
						|| (((this.maxOrderIndex + 1) != this.events.size()) || this.events.isEmpty());
			}
		}

		public TraceEventRecords toTraceEvents() {
			synchronized (this) {
				return new TraceEventRecords(this.trace,
						this.events.toArray(new AbstractTraceEvent[this.events.size()]));
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

		public void setRepairEventBasedTracesEnabled(final boolean isEnabled) {
			this.repairEventBasedTracesEnabled = isEnabled;
		}

		public Deque<BeforeOperationEvent> getEventStack() {
			return this.beforeEventStack;
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
