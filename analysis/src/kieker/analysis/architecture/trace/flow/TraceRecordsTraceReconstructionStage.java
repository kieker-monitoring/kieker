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

import java.util.concurrent.TimeUnit;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.InputPort;

/**
 * Trace Reconstruction Filter (Event) to reconstruct event based (flow) traces
 * based on TraceEventRecords.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class TraceRecordsTraceReconstructionStage extends AbstractEventRecordTraceReconstructionStage {

	/** Input port receiving the trace records. */
	private final InputPort<TraceEventRecords> traceEventRecordsInputPort = this.createInputPort(TraceEventRecords.class);

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
	public TraceRecordsTraceReconstructionStage(final TimeUnit timeUnit, final boolean repairEventBasedTraces, final long maxTraceDuration,
			final long maxTraceTimeout) {
		super(timeUnit, repairEventBasedTraces, maxTraceDuration, maxTraceTimeout);
	}

	@Override
	protected void execute() throws Exception {
		super.execute();

		final TraceEventRecords traceEventRecords = this.traceEventRecordsInputPort.receive();
		if (traceEventRecords != null) {
			final TraceMetadata trace = traceEventRecords.getTraceMetadata();
			if (null != trace) {
				this.newFlowRecordEvent(trace);
			}
			for (final AbstractTraceEvent record : traceEventRecords.getTraceEvents()) {
				this.newFlowRecordEvent(record);
			}
		}
	}

	/**
	 * @return Return reconstruct traces from incoming traces port.
	 */
	public InputPort<TraceEventRecords> getTraceEventRecordsInputPort() {
		return this.traceEventRecordsInputPort;
	}
}
