/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Iterator;
import java.util.List;

/**
 * This class is a container for a whole trace of messages (represented as actual instances of {@link AbstractMessage}).
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class MessageTrace extends AbstractTrace {

	private final List<AbstractMessage> messages;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param traceId
	 *            The ID for this trace.
	 * @param seq
	 *            The list of messages this trace consists of.
	 */
	public MessageTrace(final long traceId, final List<AbstractMessage> seq) {
		this(traceId, AbstractTrace.NO_TRACE_ID, seq);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param traceId
	 *            The ID for this trace.
	 * @param sessionId
	 *            The ID of the current session.
	 * @param seq
	 *            The list of messages this trace consists of.
	 */
	public MessageTrace(final long traceId, final String sessionId, final List<AbstractMessage> seq) {
		super(traceId, sessionId);
		this.messages = seq;
	}

	/**
	 * Delivers the sequence stored in this message trace as a vector (a list).
	 * 
	 * @return A list containing the whole trace.
	 */
	public final List<AbstractMessage> getSequenceAsVector() {
		return this.messages;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder("Trace " + this.getTraceId() + ":\n");
		final Iterator<AbstractMessage> it = this.messages.iterator();
		while (it.hasNext()) {
			final AbstractMessage m = it.next();
			strBuild.append('<');
			strBuild.append(m.toString());
			strBuild.append(">\n");
		}
		return strBuild.toString();
	}

	// Explicit delegation to super method to make FindBugs happy
	@Override
	public int hashCode() { // NOPMD (forward hashcode)
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof MessageTrace)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final MessageTrace other = (MessageTrace) obj;

		// this usually includes checks for the trace ids
		return this.messages.equals(other.messages);
	}
}
