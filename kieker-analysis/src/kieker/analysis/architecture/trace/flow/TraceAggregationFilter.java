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
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * This filter collects incoming traces for a specified amount of time. Any
 * traces representing the same series of events will be used to calculate
 * statistical informations like the average runtime of this kind of trace. Only
 * one specimen of these traces containing this information will be forwarded
 * from this filter.
 *
 * Statistical outliers regarding the runtime of the trace will be treated
 * special and therefore send out as they are and will not be mixed with others.
 *
 * @author Jan Waller, Florian Biss
 *
 * @since 1.9
 */
public class TraceAggregationFilter extends AbstractStage {

	/** Output port for the processed traces. */
	private final OutputPort<TraceEventRecords> tracesOutputPort = this.createOutputPort(TraceEventRecords.class);

	/** Input port receiving the trace records. */
	private final InputPort<TraceEventRecords> tracesInputPort = this.createInputPort();

	/** Clock input for timeout handling. */
	private final InputPort<Long> timestampInputPort = this.createInputPort(Long.class);

	private final TimeUnit timeunit;
	private final long maxCollectionDuration;

	private final Map<TraceEventRecords, TraceAggregationBuffer> trace2buffer;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timeunit
	 *            timeunit used in traces
	 * @param maxCollectionDuration
	 *            max duration of a trace
	 */
	public TraceAggregationFilter(final TimeUnit timeunit, final long maxCollectionDuration) {
		super();

		this.timeunit = timeunit;
		this.maxCollectionDuration = this.timeunit.convert(maxCollectionDuration, timeunit);
		this.trace2buffer = new TreeMap<>(new TraceComperator());
	}

	@Override
	protected void execute() throws Exception {
		final Long timestamp = this.timestampInputPort.receive();
		if (timestamp != null) {
			synchronized (this) {
				this.processTimeoutQueue(timestamp);
			}
		}
		final TraceEventRecords traceEventRecords = this.tracesInputPort.receive();
		if (traceEventRecords != null) {
			this.newEvent(traceEventRecords);
		}
	}

	/**
	 * This method is the input port for incoming traces.
	 *
	 * @param traceEventRecords
	 *            incoming TraceEventRecords
	 */
	private void newEvent(final TraceEventRecords traceEventRecords) {
		final long timestamp = this.timeunit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
		synchronized (this) {
			TraceAggregationBuffer traceBuffer = this.trace2buffer.get(traceEventRecords);
			if (traceBuffer == null) { // NOCS (DCL)
				traceBuffer = new TraceAggregationBuffer(timestamp, traceEventRecords);
				this.trace2buffer.put(traceEventRecords, traceBuffer);
			}
			traceBuffer.count();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTerminating() {
		synchronized (this) {
			for (final Entry<TraceEventRecords, TraceAggregationBuffer> entry : this.trace2buffer.entrySet()) {
				final TraceAggregationBuffer buffer = entry.getValue();
				final TraceEventRecords record = buffer.getTraceEventRecords();
				record.setCount(buffer.getCount());
				this.tracesOutputPort.send(record);
			}
			this.trace2buffer.clear();
		}
		super.onTerminating();
	}

	private void processTimeoutQueue(final long timestamp) {
		final long bufferTimeout = timestamp - this.maxCollectionDuration;
		for (final Iterator<Entry<TraceEventRecords, TraceAggregationBuffer>> iterator = this.trace2buffer.entrySet()
				.iterator(); iterator.hasNext();) {
			final TraceAggregationBuffer traceBuffer = iterator.next().getValue();
			if (traceBuffer.getBufferCreatedTimestamp() <= bufferTimeout) {
				final TraceEventRecords record = traceBuffer.getTraceEventRecords();
				record.setCount(traceBuffer.getCount());
				this.tracesOutputPort.send(record);
			}
			iterator.remove();
		}
	}

	/**
	 * Buffer for similar traces that are to be aggregated into a single trace.
	 *
	 * @author Jan Waller, Florian Biss
	 */
	private static final class TraceAggregationBuffer {
		private final long bufferCreatedTimestamp;
		private final TraceEventRecords aggregatedTrace;

		private int countOfAggregatedTraces;

		public TraceAggregationBuffer(final long bufferCreatedTimestamp, final TraceEventRecords trace) {
			this.bufferCreatedTimestamp = bufferCreatedTimestamp;
			this.aggregatedTrace = trace;
		}

		public void count() {
			this.countOfAggregatedTraces++;
		}

		public long getBufferCreatedTimestamp() {
			return this.bufferCreatedTimestamp;
		}

		public TraceEventRecords getTraceEventRecords() {
			return this.aggregatedTrace;
		}

		public int getCount() {
			return this.countOfAggregatedTraces;
		}
	}

	/**
	 * @author Jan Waller, Florian Fittkau, Florian Biss
	 */
	private static final class TraceComperator implements Comparator<TraceEventRecords>, Serializable {
		private static final long serialVersionUID = 8920766818232517L;

		/**
		 * Creates a new instance of this class.
		 */
		public TraceComperator() {
			// default empty constructor
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(final TraceEventRecords t1, final TraceEventRecords t2) {
			final AbstractTraceEvent[] recordsT1 = t1.getTraceEvents();
			final AbstractTraceEvent[] recordsT2 = t2.getTraceEvents();

			if (recordsT1.length != recordsT2.length) {
				return recordsT1.length - recordsT2.length;
			}

			final int cmpHostnames = t1.getTraceMetadata().getHostname().compareTo(t2.getTraceMetadata().getHostname());
			if (cmpHostnames != 0) {
				return cmpHostnames;
			}

			for (int i = 0; i < recordsT1.length; i++) {
				final AbstractTraceEvent recordT1 = recordsT1[i];
				final AbstractTraceEvent recordT2 = recordsT2[i];

				final int cmpClass = recordT1.getClass().getName().compareTo(recordT2.getClass().getName());
				if (cmpClass != 0) {
					return cmpClass;
				}
				if (recordT1 instanceof AbstractOperationEvent) {
					final int cmpSignature = ((AbstractOperationEvent) recordT1).getOperationSignature()
							.compareTo(((AbstractOperationEvent) recordT2).getOperationSignature());
					if (cmpSignature != 0) {
						return cmpSignature;
					}
				}
				if (recordT1 instanceof AfterOperationFailedEvent) {
					final int cmpError = ((AfterOperationFailedEvent) recordT1).getCause()
							.compareTo(((AfterOperationFailedEvent) recordT2).getCause());
					if (cmpError != 0) {
						return cmpClass;
					}
				}
			}
			// All records match.
			return 0;
		}
	}
}
