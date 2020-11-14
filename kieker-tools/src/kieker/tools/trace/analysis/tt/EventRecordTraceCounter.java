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
package kieker.tools.trace.analysis.tt;

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
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
 */
public class EventRecordTraceCounter extends AbstractStage {

	private final InputPort<TraceEventRecords> validEventRecordTracePort = this
			.createInputPort(TraceEventRecords.class);
	private final InputPort<TraceEventRecords> invalidEventRecordTracePort = this
			.createInputPort(TraceEventRecords.class);

	private int numTracesProcessed;
	private int numTracesSucceeded;
	private int numTracesFailed;

	private long lastTraceIdSuccess = -1;
	private long lastTraceIdError = -1;

	/**
	 * This is the name of the configuration determining whether to log invalid
	 * traces or not.
	 */
	public static final String CONFIG_PROPERTY_NAME_LOG_INVALID = "logInvalidTraces";

	private static final long TRACE_ID_IF_NONE = -1;

	private final boolean logInvalidTraces;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration  The configuration for this component.
	 * @param projectContext The project context for this component.
	 */
	public EventRecordTraceCounter(final boolean logInvalidTraces) {
		this.logInvalidTraces = logInvalidTraces;
	}

	@Override
	protected void execute() throws Exception {
		final TraceEventRecords validTrace = this.validEventRecordTracePort.receive();
		if (validTrace != null) {
			this.reportSuccess(validTrace.getTraceMetadata().getTraceId());
		}
		final TraceEventRecords invalidTrace = this.invalidEventRecordTracePort.receive();
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
	 * @param traceId The ID of the processed trace.
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
	 * @param traceId The ID of the processed trace.
	 */
	protected final void reportError(final long traceId) {
		synchronized (this) {
			this.lastTraceIdError = traceId;
			this.numTracesFailed++;
			this.numTracesProcessed++;
		}
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
