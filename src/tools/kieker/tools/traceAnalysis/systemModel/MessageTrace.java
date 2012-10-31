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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andre van Hoorn
 */
public class MessageTrace extends AbstractTrace {

	private final List<AbstractMessage> messages;

	private final long startTimestamp;
	private final long endTimestamp;

	/**
	 * Creates a new message trace from the given data.
	 * 
	 * @param traceId
	 *            The trace ID for this message trace
	 * @param seq
	 *            The messages contained in this message trace
	 */
	public MessageTrace(final long traceId, final List<AbstractMessage> seq) {
		this(traceId, AbstractTrace.DEFAULT_SESSION_ID, seq);
	}

	/**
	 * Creates a new message trace from the given data.
	 * 
	 * @param traceId
	 *            The trace ID for this message trace
	 * @param sessionId
	 *            The session ID for this message trace
	 * @param seq
	 *            The messages contained in this message trace
	 */
	public MessageTrace(final long traceId, final String sessionId, final List<AbstractMessage> seq) {
		super(traceId, sessionId);
		this.messages = seq;

		// Calculate start and end timestamp
		long minTimestamp = Long.MAX_VALUE;
		long maxTimestamp = Long.MIN_VALUE;
		for (final AbstractMessage message : seq) {
			if (message.getTimestamp() < minTimestamp) {
				minTimestamp = message.getTimestamp();
			}
			if (message.getTimestamp() > maxTimestamp) {
				maxTimestamp = message.getTimestamp();
			}
		}

		this.startTimestamp = minTimestamp;
		this.endTimestamp = maxTimestamp;
	}

	/**
	 * Returns the message sequence contained in this trace as an (unmodifiable) list.
	 * 
	 * @return See above
	 */
	public final List<AbstractMessage> getSequenceAsVector() {
		return Collections.unmodifiableList(this.messages);
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
		// TODO either this or equals might not be correct! both should consider traceId
		return super.hashCode();
	}

	@Override
	public long getStartTimestamp() {
		return this.startTimestamp;
	}

	@Override
	public long getEndTimestamp() {
		return this.endTimestamp;
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

		return this.messages.equals(other.messages);
	}
}
