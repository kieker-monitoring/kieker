/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import kieker.common.record.flow.trace.Trace;

/**
 * @author Jan Waller
 */
public final class TraceEventRecords {
	private final Trace trace;
	private final AbstractTraceEvent[] traceEvents;

	public TraceEventRecords(final Trace trace, final AbstractTraceEvent[] traceEvents) { // NOPMD (stored directly)
		this.trace = trace;
		this.traceEvents = traceEvents;
	}

	public Trace getTrace() {
		return this.trace;
	}

	public AbstractTraceEvent[] getTraceEvents() {
		return this.traceEvents; // NOPMD (internal array exposed)
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append("\n\tTrace: ");
		sb.append(this.trace);
		for (final AbstractTraceEvent traceEvent : this.traceEvents) {
			sb.append("\n\t");
			sb.append(traceEvent.getClass().getSimpleName());
			sb.append(": ");
			sb.append(traceEvent);
		}
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
	public boolean equals(final Object obj) {
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
		if (!Arrays.equals(this.traceEvents, other.traceEvents)) {
			return false;
		}
		return true;
	}
}
