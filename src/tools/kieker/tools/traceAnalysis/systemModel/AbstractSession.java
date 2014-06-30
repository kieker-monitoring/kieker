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
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sessions group traces which belong to the same high-level user interaction.
 * 
 * 
 * @author Holger Knoche
 * 
 * @param <T>
 *            The concrete type of trace this session is based on
 */
// TODO: #699: Is the implementation with ISessionState &quot;over-complicating&quot; things?
public abstract class AbstractSession<T extends AbstractTrace> {

	private final SortedSet<T> containedTraces;
	private final String sessionId;

	private volatile long startTime = Long.MAX_VALUE;
	private volatile long endTime = Long.MIN_VALUE;

	private volatile ISessionState<T> state = new ModifiableState();

	/**
	 * Creates a new abstract session with the given session ID.
	 * 
	 * @param sessionId
	 *            The session ID for this session
	 */
	public AbstractSession(final String sessionId) {
		this.sessionId = sessionId;
		this.containedTraces = new TreeSet<T>(this.getOrderComparator());
	}

	/**
	 * Returns this session's session ID.
	 * 
	 * @return See above
	 */
	public String getSessionId() {
		return this.sessionId;
	}

	/**
	 * Adds a trace to this session.
	 * 
	 * @param trace
	 *            The trace to add.
	 */
	public void addTrace(final T trace) {
		this.state.addTrace(trace);
	}

	/**
	 * Returns the traces contained in this session.
	 * 
	 * @return See above
	 */
	public SortedSet<T> getContainedTraces() {
		return this.state.getContainedTraces();
	}

	/**
	 * Returns this trace's start timestamp.
	 * 
	 * @return See above
	 */
	public long getStartTimestamp() {
		return this.state.getStartTimestamp();
	}

	/**
	 * Returns this trace's end timestamp.
	 * 
	 * @return See above
	 */
	public long getEndTimestamp() {
		return this.state.getEndTimestamp();
	}

	/**
	 * Marks this session as completed, i.e. prevents any future changes.
	 */
	public void setCompleted() {
		this.state.setCompleted();
	}

	protected abstract Comparator<? super T> getOrderComparator();

	private interface ISessionState<T extends AbstractTrace> {

		void addTrace(T trace);

		SortedSet<T> getContainedTraces();

		long getStartTimestamp();

		long getEndTimestamp();

		void setCompleted();

	}

	// TODO: Fix warnings for synthetic-accesses (note: do not use the respective getter methods!)
	private class ModifiableState implements ISessionState<T> {

		// avoid warnings creating objects of this type without an explicit (default) constructor
		public ModifiableState() {
			super();
		}

		public synchronized void addTrace(final T trace) {
			if (!AbstractSession.this.containedTraces.add(trace)) {
				return;
			}

			if (trace.getStartTimestamp() < AbstractSession.this.startTime) {
				AbstractSession.this.startTime = trace.getStartTimestamp();
			}
			if (trace.getEndTimestamp() > AbstractSession.this.endTime) {
				AbstractSession.this.endTime = trace.getEndTimestamp();
			}
		}

		public synchronized SortedSet<T> getContainedTraces() {
			return Collections.unmodifiableSortedSet(AbstractSession.this.containedTraces);
		}

		public synchronized long getStartTimestamp() {
			return (AbstractSession.this.containedTraces.isEmpty()) ? 0 : AbstractSession.this.startTime;
		}

		public synchronized long getEndTimestamp() {
			return (AbstractSession.this.containedTraces.isEmpty()) ? 0 : AbstractSession.this.endTime;
		}

		public synchronized void setCompleted() {
			AbstractSession.this.state = new UnmodifiableState();
		}

	}

	private class UnmodifiableState implements ISessionState<T> {

		public void addTrace(final T trace) {
			// Do nothing
		}

		public SortedSet<T> getContainedTraces() {
			return Collections.unmodifiableSortedSet(AbstractSession.this.containedTraces);
		}

		public long getStartTimestamp() {
			return (AbstractSession.this.containedTraces.isEmpty()) ? 0 : AbstractSession.this.startTime;
		}

		public long getEndTimestamp() {
			return (AbstractSession.this.containedTraces.isEmpty()) ? 0 : AbstractSession.this.endTime;
		}

		public void setCompleted() {
			// Do nothing
		}

	}

}
