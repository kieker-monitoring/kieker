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

package kieker.analysis.plugin.filter.flow;

import java.util.Arrays;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class TraceEventRecords {
	private final TraceMetadata trace;
	private final AbstractTraceEvent[] traceEvents;

	private int count = 1;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param trace
	 *            The trace to be stored in this object.
	 * @param traceEvents
	 *            The trace events to be stored in this object.
	 */
	public TraceEventRecords(final TraceMetadata trace, final AbstractTraceEvent[] traceEvents) { // NOPMD (stored directly)
//		if (null == trace) {
//			throw new NullPointerException("trace is null");
//		}
		if (null == traceEvents) {
			throw new NullPointerException("traceEvents is null");
		}
		this.trace = trace;
		this.traceEvents = traceEvents;
	}

	/**
	 * Delivers the stored traces.
	 * 
	 * @return The traces currently stored in this object.
	 */
	public TraceMetadata getTraceMetadata() {
		return this.trace;
	}

	/**
	 * Delivers the stored trace events.
	 * 
	 * @return The trace events currently stored in this object.
	 */
	public AbstractTraceEvent[] getTraceEvents() {
		return this.traceEvents; // NOPMD (internal array exposed)
	}

	public int getCount() {
		synchronized (this) {
			return this.count;
		}
	}

	public void setCount(final int count) {
		synchronized (this) {
			this.count = count;
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(64);
		sb.append(super.toString()).
			append("\n\tTrace (count=").append(this.count).
			append("): ").
			append(this.trace);
		for (final AbstractTraceEvent traceEvent : this.traceEvents) {
			sb.append("\n\t").
				append(traceEvent.getClass().getSimpleName()).
				append(": ").
				append(traceEvent);
		}
		sb.append('\n');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.trace == null) ? 0 : this.trace.hashCode()); // NOCS (?:)
		result = (prime * result) + Arrays.hashCode(this.traceEvents);
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final TraceEventRecords other = (TraceEventRecords) obj;
		if (this.trace == null) {
			if (other.trace != null) {
				return false;
			}
		} else if (!this.trace.equals(other.trace)) {
			return false;
		}
		
		return Arrays.equals(this.traceEvents, other.traceEvents);
	}
}
