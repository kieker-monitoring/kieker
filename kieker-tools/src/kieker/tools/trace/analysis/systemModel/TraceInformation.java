/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
 * This class stores trace meta-information, such as the trace ID. This kept separate from the actual trace
 * information to allow references to traces without the need to keep the trace itself.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
public class TraceInformation {

	private final long traceId;
	private final String sessionId;

	/**
	 * Creates a new trace information object using the given data.
	 *
	 * @param traceId
	 *            The trace ID to use
	 * @param sessionId
	 *            The session ID to use
	 */
	public TraceInformation(final long traceId, final String sessionId) {
		this.traceId = traceId;
		this.sessionId = sessionId;
	}

	/**
	 * Returns the trace ID.
	 *
	 * @return See above
	 */
	public long getTraceId() {
		return this.traceId;
	}

	/**
	 * Returns the trace's session ID, if any.
	 *
	 * @return See above. Note that this ID may be {@code null}.
	 */
	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public int hashCode() {
		return (int) this.getTraceId();
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof TraceInformation)) {
			return false;
		}

		// The equality currently relies only on the trace ID to facilitate trace coloring.
		final TraceInformation otherTraceInformation = (TraceInformation) other;
		return this.getTraceId() == otherTraceInformation.getTraceId();
	}

}
