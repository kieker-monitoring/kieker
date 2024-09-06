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

import teetime.framework.AbstractConsumerStage;

/**
 * This sink counts and reports the number of incoming valid
 * {@link TraceEventRecords}.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- ported to teetime
 *
 * @since 1.15
 */
public class ValidEventRecordTraceCounter extends AbstractConsumerStage<TraceEventRecords> {

	private int numTracesProcessed;
	private int numTracesSucceeded;

	private long lastTraceIdSuccess = -1;

	/**
	 * Creates a new instance of this class using the given parameters.
	 */
	public ValidEventRecordTraceCounter() {}

	@Override
	protected void execute(final TraceEventRecords trace) throws Exception {
		synchronized (this) {
			this.lastTraceIdSuccess = trace.getTraceMetadata().getTraceId();
			this.numTracesSucceeded++;
			this.numTracesProcessed++;
		}
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminating {}", this.getClass().getCanonicalName());
		super.onTerminating();
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
	 * Delivers the ID of the last trace which has been processed successfully.
	 *
	 * @return The trace ID.
	 */
	public final long getLastTraceIdSuccess() {
		synchronized (this) {
			return this.lastTraceIdSuccess;
		}
	}
}
