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

package kieker.tools.traceAnalysis.plugins.flow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import kieker.common.record.flow.TraceEvent;
import kieker.tools.traceAnalysis.plugins.traceReconstruction.InvalidTraceException;
import kieker.tools.traceAnalysis.systemModel.AbstractTrace;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class EventRecordTrace extends AbstractTrace implements Iterable<TraceEvent> {

	private int minOrderIndex = -1;
	private int maxOrderIndex = -1;
	private long minTimestamp = -1;
	private long maxTimestamp = -1;

	/**
	 * {@link TraceEvent}s sorted/unified by {@link TraceEvent#getOrderIndex()}
	 */
	private final SortedSet<TraceEvent> events = new TreeSet<TraceEvent>(new Comparator<TraceEvent>() {

		@Override
		public int compare(final TraceEvent e1, final TraceEvent e2) {
			if ((e1 == e2) || (e1.getOrderIndex() == e2.getOrderIndex())) { // same order index
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

	/**
	 * Adds a {@link TraceEvent} to the trace.
	 * The given event must have the {@link #getTraceId()} of this trace and another event with {@link TraceEvent#getOrderIndex()} must not have been added before.
	 * 
	 * @param execution
	 * @throws InvalidTraceException
	 *             if one of the constraints is violated.
	 */
	public void add(final TraceEvent execution) throws InvalidTraceException {
		synchronized (this) {
			if (this.getTraceId() != execution.getTraceId()) {
				throw new InvalidTraceException("TraceId of new record (" + execution.getTraceId() + ") differs from Id of this trace (" + this.getTraceId() + ")");
			}
			if (this.events.add(execution) == false) {
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
		final Iterator<TraceEvent> it = this.events.iterator();
		while (it.hasNext()) {
			final TraceEvent e = it.next();
			strBuild.append("<");
			strBuild.append(e.toString());
			strBuild.append(">\n");
		}
		return strBuild.toString();
	}

	/**
	 * Returns the {@link TraceEvent}s ordered by {@link TraceEvent#getOrderIndex()}
	 */
	@Override
	public Iterator<TraceEvent> iterator() {
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
	 * Returns the list of {@link TraceEvent}s, ordered by {@link TraceEvent#getOrderIndex()}.
	 * 
	 * @return
	 */
	public List<TraceEvent> eventList() {
		return new ArrayList<TraceEvent>(this.events);
	}
}
