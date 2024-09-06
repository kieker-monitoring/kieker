/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace;

import kieker.analysis.architecture.trace.flow.TraceEventRecords;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.AbstractConsumerStage;

/**
 * This sink counts and reports the number of incoming invalid
 * {@link TraceEventRecords}.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- ported to teetime
 *
 * @since 1.15
 */
public class InvalidEventRecordTraceCounter extends AbstractConsumerStage<TraceEventRecords> {

	private static final long TRACE_ID_IF_NONE = -1;

	private int numTracesProcessed;
	private int numTracesFailed;

	private long lastTraceIdError = -1;

	private final boolean logInvalidTraces;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param logInvalidTraces
	 *            if true invalid traces are logged in the error log
	 */
	public InvalidEventRecordTraceCounter(final boolean logInvalidTraces) {
		this.logInvalidTraces = logInvalidTraces;
	}

	@Override
	protected void execute(final TraceEventRecords invalidTrace) throws Exception {
		if (this.logInvalidTraces) {
			this.logger.error("Invalid trace: {}", invalidTrace);
		}

		final TraceMetadata traceMetadata = invalidTrace.getTraceMetadata();
		if (traceMetadata != null) {
			this.reportError(invalidTrace.getTraceMetadata().getTraceId());
		} else {
			final AbstractTraceEvent[] events = invalidTrace.getTraceEvents();
			if ((events != null) && (events.length > 0)) {
				this.reportError(events[0].getTraceId());
			} else {
				this.reportError(InvalidEventRecordTraceCounter.TRACE_ID_IF_NONE); // we can't do any better
			}
		}
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminating {}", this.getClass().getCanonicalName());
		super.onTerminating();
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
}
