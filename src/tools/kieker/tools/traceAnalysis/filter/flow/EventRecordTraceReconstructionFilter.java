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

package kieker.tools.traceAnalysis.filter.flow;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
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
		name = "Trace Reconstruction Filter",
		description = "Filter to reconstruct event based (flow) traces",
		outputPorts = {
			@OutputPort(name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID,
					description = "Outputs valid traces",
					eventTypes = { TraceEventRecords.class }),
			@OutputPort(name = EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID,
					description = "Outputs traces missing crucial records",
					eventTypes = { TraceEventRecords.class }) })
public final class EventRecordTraceReconstructionFilter extends AbstractFilterPlugin {
	public static final String OUTPUT_PORT_NAME_TRACE_VALID = "validTraces";
	public static final String OUTPUT_PORT_NAME_TRACE_INVALID = "invalidTraces";
	public static final String INPUT_PORT_NAME_TRACE_RECORDS = "traceRecords";

	private final Map<Long, TraceBuffer> traceId2trace;

	public EventRecordTraceReconstructionFilter(final Configuration configuration) {
		super(configuration);
		this.traceId2trace = new ConcurrentHashMap<Long, TraceBuffer>();
	}

	@InputPort(
			name = INPUT_PORT_NAME_TRACE_RECORDS,
			description = "Reconstruct traces from incoming flow records",
			eventTypes = { Trace.class, AbstractTraceEvent.class })
	public void newEvent(final IFlowRecord record) {
		final Long traceId;
		TraceBuffer traceBuffer;
		if (record instanceof Trace) {
			final Trace trace = (Trace) record;
			traceId = trace.getTraceId();
			traceBuffer = this.traceId2trace.get(traceId);
			if (traceBuffer == null) { // first record for this id!
				traceBuffer = new TraceBuffer();
				this.traceId2trace.put(traceId, traceBuffer);
			}
			traceBuffer.setTrace(trace);
		} else if (record instanceof AbstractTraceEvent) {
			final AbstractTraceEvent traceEvent = (AbstractTraceEvent) record;
			traceId = traceEvent.getTraceId();
			traceBuffer = this.traceId2trace.get(traceId);
			if (traceBuffer == null) { // first record for this id!
				traceBuffer = new TraceBuffer();
				this.traceId2trace.put(traceId, traceBuffer);
			}
			traceBuffer.insertEvent(traceEvent);
		} else {
			return; // invalid type which should not happen due to the specified eventTypes
		}
		if (traceBuffer.isFinished()) {
			this.traceId2trace.remove(traceId);
			super.deliver(OUTPUT_PORT_NAME_TRACE_VALID, traceBuffer.toTraceEvents());
		}
	}

	@Override
	public void terminate(final boolean error) {
		super.terminate(error);
		// when we arrive here, all records should be processed or damaged
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

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	private static final class TraceBuffer {
		private static final Log LOG = LogFactory.getLog(TraceBuffer.class);
		private static final Comparator<AbstractTraceEvent> COMPARATOR = new TraceEventComperator();

		private Trace trace = null;
		private final SortedSet<AbstractTraceEvent> events = new TreeSet<AbstractTraceEvent>(COMPARATOR);

		private boolean closeable = false;
		private boolean damaged = false;
		private int openEvents = 0;
		private int maxOrderIndex = -1;

		private long traceId = -1;

		public void insertEvent(final AbstractTraceEvent event) {
			final long myTraceId = event.getTraceId();
			if (this.traceId == -1) {
				this.traceId = myTraceId;
			} else if (this.traceId != myTraceId) {
				LOG.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId + " in event " + event.toString());
				this.damaged = true;
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

		public void setTrace(final Trace trace) {
			final long myTraceId = trace.getTraceId();
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

		public boolean isFinished() {
			return (this.closeable && !this.isInvalid());
		}

		public boolean isInvalid() {
			return ((this.trace == null) || this.damaged || (this.openEvents != 0) || ((this.maxOrderIndex + 1) != this.events.size()));
		}

		public TraceEventRecords toTraceEvents() {
			return new TraceEventRecords(this.trace, this.events.toArray(new AbstractTraceEvent[this.events.size()]));
		}

		private static final class TraceEventComperator implements Comparator<AbstractTraceEvent>, Serializable {
			private static final long serialVersionUID = 8920737343446332517L;

			public int compare(final AbstractTraceEvent o1, final AbstractTraceEvent o2) {
				return o1.getOrderIndex() - o2.getOrderIndex();
			}
		}
	}
}
