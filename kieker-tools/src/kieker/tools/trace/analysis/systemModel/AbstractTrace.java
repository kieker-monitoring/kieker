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

package kieker.tools.trace.analysis.systemModel;

/**
 * This is the abstract base for a trace (like a message trace e.g.).
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 * @deprecated 1.15 moved to kieker-model
 */
@Deprecated
public abstract class AbstractTrace {

	/**
	 * Default value for the session ID.
	 */
	public static final String DEFAULT_SESSION_ID = "N/A";

	/** This constant can be used as an ID to show that the trace has no ID. */
	public static final String NO_TRACE_ID = "N/A";

	private final TraceInformation traceInformation;

	/**
	 * Default constructor.
	 */
	protected AbstractTrace() {
		this(-1, AbstractTrace.DEFAULT_SESSION_ID);
	}

	/**
	 * Creates a new abstract trace with the given trace ID and a default session
	 * ID.
	 *
	 * @param traceId The trace ID to use for the new trace
	 */
	public AbstractTrace(final long traceId) {
		this(traceId, AbstractTrace.DEFAULT_SESSION_ID);
	}

	/**
	 * Creates a new abstract trace with the given parameters.
	 *
	 * @param traceId   The trace ID to use for the new trace
	 * @param sessionId The session ID to use for the new trace
	 */
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

	/**
	 * Returns this trace's trace ID.
	 *
	 * @return See above
	 */
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
	@Override // NOCS requests implementation of equals and hashCode in pairs
	public int hashCode() { // NOCS requests implementation of equals and hashCode in pairs
		// On purpose, we are not considering the sessionId here
		return (int) (this.getTraceId() ^ (this.getTraceId() >>> 32));
	}

	@Override
	public abstract boolean equals(Object obj);

	/**
	 * Returns this trace's start timestamp.
	 *
	 * @return See above
	 */
	public abstract long getStartTimestamp();

	/**
	 * Returns this trace's end timestamp.
	 *
	 * @return See above
	 */
	public abstract long getEndTimestamp();
}
