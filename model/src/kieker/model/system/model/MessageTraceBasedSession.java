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

package kieker.model.system.model;

import java.util.Comparator;

import kieker.model.system.model.util.TraceStartTimestampComparator;

/**
 * Specialized sub-class for sessions based on message traces (see
 * {@link MessageTrace}).
 *
 * @author Holger Knoche
 * @since 1.10
 *
 */
public class MessageTraceBasedSession extends AbstractSession<MessageTrace> {

	/**
	 * Creates a new message trace-based session with the given session ID.
	 *
	 * @param sessionId
	 *            The session ID to use
	 */
	public MessageTraceBasedSession(final String sessionId) {
		super(sessionId);
	}

	@Override
	protected Comparator<? super MessageTrace> getOrderComparator() {
		return new TraceStartTimestampComparator();
	}

}
