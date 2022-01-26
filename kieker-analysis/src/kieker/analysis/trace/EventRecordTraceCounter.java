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
package kieker.analysis.trace;

import kieker.analysis.stage.flow.TraceEventRecords;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;

/**
 * Counts and reports the number of incoming valid/invalid
 * {@link TraceEventRecords}.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- ported to teetime
 *
 * @since 1.7
 * @deprecated 1.15 a joint counter for valid and invalid traces makes no sense and requires separate threads to execute, superseded by
 *             (Invalid|Valid)EventRecordTraceCounter
 */
@Deprecated
public class EventRecordTraceCounter extends AbstractStage {

	private static final long TRACE_ID_IF_NONE = -1;

	private final InputPort<TraceEventRecords> validEventRecordTraceInputPort = this
			.createInputPort(TraceEventRecords.class);
	private final InputPort<TraceEventRecords> invalidEventRecordTraceInputPort = this
			.createInputPort(TraceEventRecords.class);

	private int numTracesProcessed;
	private int numTracesSucceeded;
	private int numTracesFailed;

	private long lastTraceIdSuccess = -1;
	private long lastTraceIdError = -1;

	private final boolean logInvalidTraces;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param logInvalidTraces
	 *            if true invalid traces are logged in the error log
	 */
	public EventRecordTraceCounter(final boolean logInvalidTraces) {
		this.logInvalidTraces = logInvalidTraces;
	}

	@Override
	protected void execute() throws Exception {
		final TraceEventRecords validTrace = this.validEventRecordTraceInputPort.receive();
		if (validTrace != null) {
			this.reportSuccess(validTrace.getTraceMetadata().getTraceId());
		}
		final TraceEventRecords invalidTrace = this.invalidEventRecordTraceInputPort.receive();
		if (invalidTrace != null) {
			if (this.logInvalidTraces) {
				this.logger.error("Invalid trace: {}", invalidTrace);
			}

			final TraceMetadata trace = invalidTrace.getTraceMetadata();
			if (trace != null) {
				this.reportError(invalidTrace.getTraceMetadata().getTraceId());
			} else {
				final AbstractTraceEvent[] events = invalidTrace.getTraceEvents();
				if ((events != null) && (events.length > 0)) {
					this.reportError(events[0].getTraceId());
				} else {
					this.reportError(EventRecordTraceCounter.TRACE_ID_IF_NONE); // we can't do any better
				}
			}
		}
	}

	/**
	 * This method can be used to report a trace which has been processed
	 * successfully.
	 *
	 * @param traceId
	 *            The ID of the processed trace.
	 */
	protected final void reportSuccess(final long traceId) {
		synchronized (this) {
			this.lastTraceIdSuccess = traceId;
			this.numTracesSucceeded++;
			this.numTracesProcessed++;
		}
	}

	/**
	 * This method can be used to report a trace which has <b>not</b> been processed
	 * successfully.
	 *
	 * @param traceId
	 *            The ID of the processed trace.
	 */
	protected final void reportError(final long traceId) {
		synchronized (this) {
			this.lastTraceIdError = traceId;
			this.numTracesFailed++;
			this.numTracesProcessed++;
		}
	}

	public InputPort<TraceEventRecords> getInvalidEventRecordTraceInputPort() {
		return this.invalidEventRecordTraceInputPort;
	}

	public InputPort<TraceEventRecords> getValidEventRecordTraceInputPort() {
		return this.validEventRecordTraceInputPort;
	}

	/**
	 * Delivers the number of traces which have been processed successfully.
	 *
	 * @return The number of traces.
	 */
	public final int getSuccessCount() {
		synchronized (this) {
			return this.numTracesSucceeded;
		}
	}

	/**
	 * Delivers the number of traces which have <b>not</b> been processed
	 * successfully.
	 *
	 * @return The number of traces.
	 */
	public final int getErrorCount() {
		synchronized (this) {
			return this.numTracesFailed;
		}
	}

	/**
	 * Delivers the total number of traces which have been processed.
	 *
	 * @return The number of traces.
	 */
	public final int getTotalCount() {
		synchronized (this) {
			return this.numTracesProcessed;
		}
	}

	/**
	 * Delivers the ID of the last trace which has <b>not</b> been processed
	 * successfully.
	 *
	 * @return The trace ID.
	 */
	public final long getLastTraceIdError() {
		synchronized (this) {
			return this.lastTraceIdError;
		}
	}

	/**
	 * Delivers the ID of the last trace which has been processed successfully.
	 *
	 * @return The trace ID.
	 */
	public final long getLastTraceIdSuccess() {
		synchronized (this) {
			return this.lastTraceIdSuccess;
		}
	}

	/**
	 * Returns a user-addressed status message to be logged by the calling tool.
	 * Extending classes may override this method but should call the then-inherited
	 * method first.
	 *
	 */
	public void printStatusMessage() {
		synchronized (this) {
			this.logger.debug("Trace processing summary: {} total; {} succeeded; {} failed.", this.numTracesProcessed,
					this.numTracesSucceeded, this.numTracesFailed);
		}
	}

}
