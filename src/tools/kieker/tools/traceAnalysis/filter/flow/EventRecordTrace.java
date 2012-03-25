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

package kieker.tools.traceAnalysis.filter.flow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.AbstractTrace;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class EventRecordTrace extends AbstractTrace implements Iterable<AbstractTraceEvent> {

	private int minOrderIndex = -1;
	private int maxOrderIndex = -1;
	private long minTimestamp = -1;
	private long maxTimestamp = -1;

	/**
	 * {@link AbstractTraceEvent}s sorted/unified by {@link AbstractTraceEvent#getOrderIndex()}
	 */
	private final SortedSet<AbstractTraceEvent> events = new TreeSet<AbstractTraceEvent>(new Comparator<AbstractTraceEvent>() {

		public int compare(final AbstractTraceEvent e1, final AbstractTraceEvent e2) {
			if ((e1 == e2) || (e1.getOrderIndex() == e2.getOrderIndex())) { // same order index // FIXME: use equal?
				return 0;
			} else if (e1.getOrderIndex() < e2.getOrderIndex()) {
				return -1;
			} else {
				// e1.getOrderIndex() > e2.getOrderIndex()) {
				return 1;
			}
		}
	});

	/**
	 * 
	 * @param traceId
	 * 
	 */
	public EventRecordTrace(final long traceId) {
		super(traceId);
	}

	public EventRecordTrace(final long traceId, final String sessionId) {
		super(traceId, sessionId);
	}

	/**
	 * Adds a {@link AbstractTraceEvent} to the trace.
	 * The given event must have the {@link #getTraceId()} of this trace and another event with {@link AbstractTraceEvent#getOrderIndex()} must not have been added
	 * before.
	 * 
	 * @param execution
	 * @throws InvalidTraceException
	 *             if one of the constraints is violated.
	 */
	public void add(final AbstractTraceEvent execution) throws InvalidTraceException {
		synchronized (this) {
			if (this.getTraceId() != execution.getTraceId()) {
				throw new InvalidTraceException("TraceId of new record (" + execution.getTraceId() + ") differs from Id of this trace (" + this.getTraceId() + ")");
			}
			if (!this.events.add(execution)) {
				throw new InvalidTraceException("Order index exists already" + execution.getOrderIndex());
			}
			if ((this.minTimestamp < 0) || (execution.getTimestamp() < this.minTimestamp)) {
				this.minTimestamp = execution.getTimestamp();
			}
			if ((this.maxTimestamp < 0) || (execution.getTimestamp() > this.maxTimestamp)) {
				this.maxTimestamp = execution.getTimestamp();
			}
			if ((this.minOrderIndex < 0) || (execution.getOrderIndex() < this.minOrderIndex)) {
				this.minOrderIndex = execution.getOrderIndex();
			}
			if ((this.maxOrderIndex < 0) || (execution.getOrderIndex() > this.maxOrderIndex)) {
				this.maxOrderIndex = execution.getOrderIndex();
			}
		}
	}

	// Explicit delegation to super method to make FindBugs happy

	@Override
	public int hashCode() { // NOPMD
		// TODO either this or equals might not be correct! both should consider traceId
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof EventRecordTrace)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final EventRecordTrace other = (EventRecordTrace) obj;

		return this.events.equals(other.events);
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder("Trace " + this.getTraceId() + ":\n");
		final Iterator<AbstractTraceEvent> it = this.events.iterator();
		while (it.hasNext()) {
			final AbstractTraceEvent e = it.next();
			strBuild.append('(');
			strBuild.append(e.getClass().getSimpleName());
			strBuild.append("):<");
			strBuild.append(e.toString());
			strBuild.append(">\n");
		}
		return strBuild.toString();
	}

	/**
	 * Returns the {@link AbstractTraceEvent}s ordered by {@link AbstractTraceEvent#getOrderIndex()}
	 */

	public Iterator<AbstractTraceEvent> iterator() {
		return this.events.iterator();
	}

	/**
	 * @return the minOrderIndex
	 */
	public int getMinOrderIndex() {
		return this.minOrderIndex;
	}

	/**
	 * @return the maxOrderIndex
	 */
	public int getMaxOrderIndex() {
		return this.maxOrderIndex;
	}

	/**
	 * @return the minTimestamp
	 */
	public long getMinTimestamp() {
		return this.minTimestamp;
	}

	/**
	 * @return the maxTimestamp
	 */
	public long getMaxTimestamp() {
		return this.maxTimestamp;
	}

	/**
	 * Returns the list of {@link AbstractTraceEvent}s, ordered by {@link AbstractTraceEvent#getOrderIndex()}.
	 * 
	 * @return
	 */
	public List<AbstractTraceEvent> eventList() {
		return new ArrayList<AbstractTraceEvent>(this.events);
	}
}
