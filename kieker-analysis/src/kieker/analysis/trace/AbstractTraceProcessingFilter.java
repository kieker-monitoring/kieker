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
package kieker.analysis.trace;

import kieker.model.repository.SystemModelRepository;

/**
 * This is an abstract base for filters processing traces.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- ported to TeeTime
 *
 * @param <T>
 *            an AbstractTrace type
 *
 * @since 1.1
 */
public abstract class AbstractTraceProcessingFilter<T> extends AbstractTraceAnalysisFilter<T> {

	private int numTracesProcessed;
	private int numTracesSucceeded;
	private int numTracesFailed;

	private long lastTraceIdSuccess = -1;
	private long lastTraceIdError = -1;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param systemModelRepository
	 *            the model repository to be used
	 */
	public AbstractTraceProcessingFilter(final SystemModelRepository systemModelRepository) {
		super(systemModelRepository);
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
			this.printDebugLogMessage(new String[] { "Trace processing summary: " + this.numTracesProcessed + " total; "
					+ this.numTracesSucceeded + " succeeded; " + this.numTracesFailed + " failed.", });
		}
	}
}
