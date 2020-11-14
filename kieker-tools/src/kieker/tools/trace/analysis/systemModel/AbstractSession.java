/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sessions group traces which belong to the same high-level user interaction.
 *
 *
 * @author Holger Knoche
 * @since 1.10
 *
 * @param <T> The concrete type of trace this session is based on
 * @deprecated 1.15 moved to kieker-model
 */
@Deprecated
public abstract class AbstractSession<T extends AbstractTrace> {

	private final SortedSet<T> containedTraces; // protected visibility to avoid synthetic access
	private final String sessionId;

	private volatile long startTime = Long.MAX_VALUE; // protected visibility to avoid synthetic access
	private volatile long endTime = Long.MIN_VALUE; // protected visibility to avoid synthetic access

	private volatile ISessionState<T> state = new ModifiableState();

	/**
	 * Creates a new abstract session with the given session ID.
	 *
	 * @param sessionId The session ID for this session
	 */
	public AbstractSession(final String sessionId) {
		this.sessionId = sessionId;
		this.containedTraces = new TreeSet<>(this.getOrderComparator());
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
	 * @param trace The trace to add.
	 */
	public void addTrace(final T trace) {
		this.state.addTrace(trace);
	}

	/**
	 * Returns the traces contained in this session.
	 *
	 * @return See above
	 */
	public SortedSet<T> getStateContainedTraces() {
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

	protected long getStartTime() {
		return this.startTime;
	}

	protected long getEndTime() {
		return this.endTime;
	}

	protected void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	protected void setEndTime(final long endTime) {
		this.endTime = endTime;
	}

	protected void setState(final ISessionState<T> state) {
		this.state = state;
	}

	protected SortedSet<T> getContainedTraces() {
		return this.containedTraces;
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

	// Note: currently synthetic-accesses to variables are used. Do not use the
	// respective getter methods!
	private class ModifiableState implements ISessionState<T> {

		// avoid warnings creating objects of this type without an explicit (default)
		// constructor
		public ModifiableState() {
			super();
		}

		// Access to startTime and endTime emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public synchronized void addTrace(final T trace) { // NOPMD (AvoidSynchronizedAtMethodLevel)
			if (!AbstractSession.this.getContainedTraces().add(trace)) {
				return;
			}

			if (trace.getStartTimestamp() < AbstractSession.this.getStartTime()) {
				AbstractSession.this.setStartTime(trace.getStartTimestamp());
			}
			if (trace.getEndTimestamp() > AbstractSession.this.getEndTime()) {
				AbstractSession.this.setEndTime(trace.getEndTimestamp());
			}
		}

		// Access to containedTraces emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public synchronized SortedSet<T> getContainedTraces() { // NOPMD (AvoidSynchronizedAtMethodLevel)
			return Collections.unmodifiableSortedSet(AbstractSession.this.getContainedTraces());
		}

		// Access to startTime and containedTraces emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public synchronized long getStartTimestamp() { // NOPMD (AvoidSynchronizedAtMethodLevel)
			if (AbstractSession.this.getContainedTraces().isEmpty()) {
				return 0;
			} else {
				return AbstractSession.this.getStartTime();
			}
		}

		// Access to endTime and containedTraces emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public synchronized long getEndTimestamp() { // NOPMD (AvoidSynchronizedAtMethodLevel)
			if (AbstractSession.this.getContainedTraces().isEmpty()) {
				return 0;
			} else {
				return AbstractSession.this.getEndTime();
			}
		}

		// Access to state emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public synchronized void setCompleted() { // NOPMD (AvoidSynchronizedAtMethodLevel)
			AbstractSession.this.setState(new UnmodifiableState());
		}

	}

	private class UnmodifiableState implements ISessionState<T> {

		public UnmodifiableState() {
			// Empty default constructor
		}

		@Override
		public void addTrace(final T trace) {
			// Do nothing
		}

		// Access to containedTraces emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public SortedSet<T> getContainedTraces() {
			return Collections.unmodifiableSortedSet(AbstractSession.this.getContainedTraces());
		}

		// Access to startTime and containedTraces emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public long getStartTimestamp() {
			if (AbstractSession.this.getContainedTraces().isEmpty()) {
				return 0;
			} else {
				return AbstractSession.this.getStartTime();
			}
		}

		// Access to endTime and containedTraces emulated by synthetic accessor method
		// @SuppressWarnings("synthetic-access")
		@Override
		public long getEndTimestamp() {
			if (AbstractSession.this.getContainedTraces().isEmpty()) {
				return 0;
			} else {
				return AbstractSession.this.getEndTime();
			}
		}

		@Override
		public void setCompleted() {
			// Do nothing
		}

	}

}
