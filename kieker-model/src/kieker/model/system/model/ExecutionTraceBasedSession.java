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

package kieker.model.system.model;

import java.util.Comparator;

import kieker.common.util.dataformat.LoggingTimestampConverter;
import kieker.model.system.model.util.TraceStartTimestampComparator;

/**
 * Specialized sub-class for sessions which are derived from execution traces
 * (see {@link ExecutionTrace}).
 *
 * @author Holger Knoche
 * @since 1.10
 *
 */
public class ExecutionTraceBasedSession extends AbstractSession<ExecutionTrace> {

	/**
	 * Creates a new execution trace-based session with the given session ID.
	 *
	 * @param sessionId
	 *            The session ID to use
	 */
	public ExecutionTraceBasedSession(final String sessionId) {
		super(sessionId);
	}

	@Override
	protected Comparator<? super ExecutionTrace> getOrderComparator() {
		return new TraceStartTimestampComparator();
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder(512);
		synchronized (this) {
			strBuild.append("SessionId ").append(this.getSessionId());
			strBuild.append(" (startTime=").append(this.getStartTimestamp());
			strBuild.append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.getStartTimestamp()));
			strBuild.append("); endTime=").append(this.getEndTimestamp());
			strBuild.append(" (")
					.append(LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.getEndTimestamp()));
			strBuild.append("):\n");
			for (final ExecutionTrace t : this.getStateContainedTraces()) {
				strBuild.append('{');
				strBuild.append(t.toString()).append("}\n");
			}
		}
		return strBuild.toString();
	}

}
