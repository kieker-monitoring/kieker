/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.systemModel;

/**
 * This is the abstract base for a trace (like a message trace e.g.).
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractTrace {

	/** This constant can be used as an ID to show that the trace has no ID. */
	public static final String NO_TRACE_ID = "N/A";

	private final TraceInformation traceInformation;

	/**
	 * Default constructor.
	 */
	protected AbstractTrace() {
		this(-1, NO_TRACE_ID);
	}

	public AbstractTrace(final long traceId) {
		this(traceId, NO_TRACE_ID);
	}

	public AbstractTrace(final long traceId, final String sessionId) {
		this.traceInformation = new TraceInformation(traceId, sessionId);
	}

	/**
	 * Returns information about this trace.
	 * 
	 * @return See above
	 */
	public TraceInformation getTraceInformation() {
		return this.traceInformation;
	}

	public long getTraceId() {
		return this.traceInformation.getTraceId();
	}

	/**
	 * Delivers the ID of the session.
	 * 
	 * @return The session ID.
	 */
	public String getSessionId() {
		return this.traceInformation.getSessionId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		// On purpose, we are not considering the sessionId here
		return (int) (this.getTraceId() ^ (this.getTraceId() >>> 32));
	}

	@Override
	public abstract boolean equals(Object obj);
}
