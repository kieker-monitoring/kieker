/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace.flow;

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

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Trace Reconstruction Filter (Event) to reconstruct event based (flow) traces.
 *
 * @author Jan Waller
 * @author Reiner Jung -- teetime port
 *
 * @since 1.6
 */
public abstract class AbstractEventRecordTraceReconstructionStage extends AbstractStage {
	/** Output port delivering the valid traces. */
	private final OutputPort<TraceEventRecords> validTracesOutputPort = this.createOutputPort(TraceEventRecords.class);

	/** Output port delivering the invalid traces. */
	private final OutputPort<TraceEventRecords> invalidTracesOutputPort = this.createOutputPort(TraceEventRecords.class);

	/** Input port receiving the trace records. */
	private final InputPort<Long> timestampsInputPort;

	private final long maxTraceDuration;
	private final long maxTraceTimeout;
	private final boolean hasTimeout;
	private final boolean repairEventBasedTraces;
	private long maxEncounteredLoggingTimestamp = -1;

	private final Map<Long, TraceBuffer> traceId2trace;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timeUnit
	 *            property determining the time unit
	 * @param repairEventBasedTraces
	 *            repair traces where AfterEvents are missing
	 * @param maxTraceDuration
	 *            max trace duration
	 * @param maxTraceTimeout
	 *            max trace timeout, if set to Long.MAX_VALUE no timeout is used
	 */
	public AbstractEventRecordTraceReconstructionStage(final TimeUnit timeUnit, final boolean repairEventBasedTraces, final long maxTraceDuration,
			final long maxTraceTimeout) {
		super();

		this.repairEventBasedTraces = repairEventBasedTraces;
		this.maxTraceDuration = timeUnit.convert(maxTraceDuration, timeUnit);
		this.maxTraceTimeout = timeUnit.convert(maxTraceTimeout, timeUnit);
		this.hasTimeout = (this.maxTraceTimeout != Long.MAX_VALUE) || (this.maxTraceDuration != Long.MAX_VALUE);
		if (this.hasTimeout) {
			this.timestampsInputPort = this.createInputPort(Long.class);
		} else {
			this.timestampsInputPort = null;
		}
		this.traceId2trace = new ConcurrentHashMap<>();
	}

	@Override
	protected void execute() throws Exception {
		if (this.hasTimeout) {
			final Long timestamp = this.timestampsInputPort.receive();
			if (timestamp != null) {
				synchronized (this) {
					if (this.hasTimeout) {
						this.processTimeoutQueue(timestamp);
					}
				}
			}
		}
	}

	public OutputPort<TraceEventRecords> getInvalidTracesOutputPort() {
		return this.invalidTracesOutputPort;
	}

	public OutputPort<TraceEventRecords> getValidTracesOutputPort() {
		return this.validTracesOutputPort;
	}

	/**
	 * @return Return input port for a periodic time signal.
	 */
	public InputPort<Long> getTimestampsInputPort() {
		return this.timestampsInputPort;
	}

	/**
	 * This method is the input port for the new events for this filter.
	 *
	 * @param record
	 *            The new record to handle.
	 */
	protected void newFlowRecordEvent(final IFlowRecord record) {
		if (record instanceof TraceMetadata) {
			this.newTraceMetadataEvent((TraceMetadata) record);
		} else if (record instanceof AbstractTraceEvent) {
			this.newAbstractTraceEvent((AbstractTraceEvent) record);
		} else {
			this.logger.error("Received illegal IFlowRecord event of type {}", record.getClass());
			return; // invalid type which should not happen due to the specified eventTypes
		}
	}

	private void handleTrace(final TraceBuffer traceBuffer, final long traceId) {
		if (traceBuffer.isFinished()) {
			synchronized (this) { // has to be synchronized because of timeout cleanup
				this.traceId2trace.remove(traceId);
			}
			this.validTracesOutputPort.send(traceBuffer.toTraceEvents());
		}
	}

	private void handleTimeoutQueue(final long loggingTimestamp) {
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

	private void newTraceMetadataEvent(final TraceMetadata record) {
		final Long traceId = record.getTraceId();
		TraceBuffer traceBuffer = this.traceId2trace.get(traceId);
		if (traceBuffer == null) { // first record for this id!
			synchronized (this) {
				traceBuffer = this.traceId2trace.get(traceId);
				if (traceBuffer == null) { // NOCS (DCL)
					traceBuffer = new TraceBuffer();
					traceBuffer.setRepairEventBasedTracesEnabled(this.repairEventBasedTraces);
					this.traceId2trace.put(traceId, traceBuffer);
				}
			}
		}
		traceBuffer.setTrace(record);
		this.handleTrace(traceBuffer, traceId);
		this.handleTimeoutQueue(-1);
	}

	private void newAbstractTraceEvent(final AbstractTraceEvent event) {
		final Long traceId = event.getTraceId();
		TraceBuffer traceBuffer = this.traceId2trace.get(traceId);
		if (traceBuffer == null) { // first record for this id!
			synchronized (this) {
				traceBuffer = this.traceId2trace.get(traceId);
				if (traceBuffer == null) { // NOCS (DCL)
					traceBuffer = new TraceBuffer();
					traceBuffer.setRepairEventBasedTracesEnabled(this.repairEventBasedTraces);
					this.traceId2trace.put(traceId, traceBuffer);
				}
			}
		}
		traceBuffer.insertEvent(event);
		this.handleTrace(traceBuffer, traceId);
		this.handleTimeoutQueue(event.getTimestamp());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onTerminating() {
		synchronized (this) {
			final Collection<Long> sortedTraceIds = this.getSortedTraceIds(this.traceId2trace.keySet());

			for (final Long traceId : sortedTraceIds) {
				final TraceBuffer traceBuffer = this.traceId2trace.get(traceId);
				if (this.repairEventBasedTraces && !traceBuffer.getEventStack().isEmpty()) {
					traceBuffer.repairAllBeforeEventsLeftInStackAtTermination();
				}

				if (traceBuffer.isInvalid()) {
					this.invalidTracesOutputPort.send(traceBuffer.toTraceEvents());
				} else {
					this.validTracesOutputPort.send(traceBuffer.toTraceEvents());
				}
			}

			// this.traceId2trace.clear();
		}
		super.onTerminating();
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
					this.invalidTracesOutputPort.send(traceBuffer.toTraceEvents());
				} else {
					this.validTracesOutputPort.send(traceBuffer.toTraceEvents());
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
				if (this.beforeEventStack.isEmpty()) {
					LOGGER.error("AfterEvent on empty BeforeEvent queue. {} ", event.toString());
				} else {
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
