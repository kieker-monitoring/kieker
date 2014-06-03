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
package kieker.panalysis.stage.kieker.traceReconstruction;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import de.chw.concurrent.ConcurrentHashMapWithDefault;

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.analysis.plugin.filter.flow.reconstruction.TraceBuffer;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class TraceReconstructionFilter extends AbstractFilter<TraceReconstructionFilter> {

	public final IInputPort<TraceReconstructionFilter, Long> timestampInputPort = this.createInputPort();
	public final IInputPort<TraceReconstructionFilter, IFlowRecord> recordInputPort = this.createInputPort();

	public final IOutputPort<TraceReconstructionFilter, TraceEventRecords> traceInvalidOutputPort = this.createOutputPort();
	public final IOutputPort<TraceReconstructionFilter, TraceEventRecords> traceValidOutputPort = this.createOutputPort();

	private TimeUnit timeunit;
	private long maxTraceDuration;
	private long maxTraceTimeout;
	private boolean timeout;
	private long maxEncounteredLoggingTimestamp = -1;

	private Map<Long, TraceBuffer> traceId2trace = new ConcurrentHashMapWithDefault<Long, TraceBuffer>(new TraceBuffer());

	@Override
	protected boolean execute(final Context<TraceReconstructionFilter> context) {
		final Long timestamp = context.tryTake(this.timestampInputPort);
		if (timestamp != null) {
			if (this.timeout) {
				this.processTimeoutQueue(timestamp, context);
			}
			return true;
		}

		final IFlowRecord record = context.tryTake(this.recordInputPort);
		if (record != null) {
			final Long traceId = this.reconstructTrace(record);
			if (traceId != null) {
				this.putIfFinished(traceId, context);
				this.processTimestamp(record, context);
			}
			return true;
		}

		return false;
	}

	private void processTimestamp(final IFlowRecord record, final Context<TraceReconstructionFilter> context) {
		if (this.timeout) {
			synchronized (this) {
				final long loggingTimestamp = this.getTimestamp(record);
				// can we assume a rough order of logging timestamps? (yes, except with DB reader)
				if (loggingTimestamp > this.maxEncounteredLoggingTimestamp) {
					this.maxEncounteredLoggingTimestamp = loggingTimestamp;
				}
				this.processTimeoutQueue(this.maxEncounteredLoggingTimestamp, context);
			}
		}
	}

	private long getTimestamp(final IFlowRecord record) {
		if (record instanceof AbstractTraceEvent) {
			return ((AbstractTraceEvent) record).getTimestamp();
		}
		return -1;
	}

	private void putIfFinished(final Long traceId, final Context<TraceReconstructionFilter> context) {
		final TraceBuffer traceBuffer = this.traceId2trace.get(traceId);
		if (traceBuffer.isFinished()) {
			synchronized (this) { // has to be synchronized because of timeout cleanup
				this.traceId2trace.remove(traceId);
			}
			this.put(traceBuffer, context);
		}
	}

	private Long reconstructTrace(final IFlowRecord record) {
		Long traceId = null;
		if (record instanceof TraceMetadata) {
			traceId = ((TraceMetadata) record).getTraceId();
			final TraceBuffer traceBuffer = this.traceId2trace.get(traceId);

			traceBuffer.setTrace((TraceMetadata) record);
		} else if (record instanceof AbstractTraceEvent) {
			traceId = ((AbstractTraceEvent) record).getTraceId();
			final TraceBuffer traceBuffer = this.traceId2trace.get(traceId);

			traceBuffer.insertEvent((AbstractTraceEvent) record);
		}

		return traceId;
	}

	@Override
	public void onPipelineStarts() throws Exception {
		this.timeout = !((this.maxTraceTimeout == Long.MAX_VALUE) && (this.maxTraceDuration == Long.MAX_VALUE));
		super.onPipelineStarts();
	}

	private void processTimeoutQueue(final long timestamp, final Context<TraceReconstructionFilter> context) {
		final long duration = timestamp - this.maxTraceDuration;
		final long traceTimeout = timestamp - this.maxTraceTimeout;

		for (final Iterator<Entry<Long, TraceBuffer>> iterator = this.traceId2trace.entrySet().iterator(); iterator.hasNext();) {
			final TraceBuffer traceBuffer = iterator.next().getValue();
			if ((traceBuffer.getMaxLoggingTimestamp() <= traceTimeout) // long time no see
					|| (traceBuffer.getMinLoggingTimestamp() <= duration)) { // max duration is gone
				this.put(traceBuffer, context);
				iterator.remove();
			}
		}
	}

	private void put(final TraceBuffer traceBuffer, final Context<TraceReconstructionFilter> context) {
		final IOutputPort<TraceReconstructionFilter, TraceEventRecords> outputPort =
				(traceBuffer.isInvalid()) ? this.traceInvalidOutputPort : this.traceValidOutputPort;
		context.put(outputPort, traceBuffer.toTraceEvents());
	}

	public TimeUnit getTimeunit() {
		return this.timeunit;
	}

	public void setTimeunit(final TimeUnit timeunit) {
		this.timeunit = timeunit;
	}

	public long getMaxTraceDuration() {
		return this.maxTraceDuration;
	}

	public void setMaxTraceDuration(final long maxTraceDuration) {
		this.maxTraceDuration = maxTraceDuration;
	}

	public long getMaxTraceTimeout() {
		return this.maxTraceTimeout;
	}

	public void setMaxTraceTimeout(final long maxTraceTimeout) {
		this.maxTraceTimeout = maxTraceTimeout;
	}

	public long getMaxEncounteredLoggingTimestamp() {
		return this.maxEncounteredLoggingTimestamp;
	}

	public void setMaxEncounteredLoggingTimestamp(final long maxEncounteredLoggingTimestamp) {
		this.maxEncounteredLoggingTimestamp = maxEncounteredLoggingTimestamp;
	}

	public Map<Long, TraceBuffer> getTraceId2trace() {
		return this.traceId2trace;
	}

	public void setTraceId2trace(final Map<Long, TraceBuffer> traceId2trace) {
		this.traceId2trace = traceId2trace;
	}

}
