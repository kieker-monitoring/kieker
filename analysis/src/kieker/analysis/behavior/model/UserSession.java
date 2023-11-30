/***************************************************************************
 * Copyright (C) 2015 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.common.record.session.ISessionEvent;

/**
 * Represents a user session.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Christoph Dornieden
 *
 * @since 2.0.0
 */
public final class UserSession {

	/**
	 * Simple comparator for comparing the entry times.
	 */
	public static final Comparator<EntryCallEvent> SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME = new Comparator<>() {

		@Override
		public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
			if (o1.getEntryTime() > o2.getEntryTime()) {
				return 1;
			} else if (o1.getEntryTime() < o2.getEntryTime()) {
				return -1;
			}
			return 0;
		}
	};

	/**
	 * Simple comparator for comparing the exit times.
	 */
	public static final Comparator<EntryCallEvent> SORT_ENTRY_CALL_EVENTS_BY_EXIT_TIME = new Comparator<>() {

		@Override
		public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
			if (o1.getExitTime() > o2.getExitTime()) {
				return 1;
			} else if (o1.getExitTime() < o2.getExitTime()) {
				return -1;
			}
			return 0;
		}
	};

	/**
	 * internal storage of entry call events. Those represent the chronological order of user
	 * behavior
	 */
	private final List<EntryCallEvent> events = new ArrayList<>();
	/** host of session. */
	private final String host;
	/** session id. */
	private final String sessionId;

	/**
	 * Simple constructor. Create a user session.
	 *
	 * @param host
	 *            host name
	 * @param sessionId
	 *            unique session id
	 */
	public UserSession(final String host, final String sessionId) {
		this.host = host;
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
		return this.host + "," + this.sessionId;
	}

	/**
	 * Sort the internal events by the given {@link Comparator}. This class has a default one
	 * {@link #SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME}
	 *
	 * @param cmp
	 *            comparator to sort user sessions
	 */
	public void sortEventsBy(final Comparator<EntryCallEvent> cmp) {
		Collections.sort(this.events, cmp);
	}

	/**
	 * Add the given event to this user session.
	 *
	 * @param event
	 *            event to be added
	 */
	public void add(final EntryCallEvent event) {
		this.events.add(event);
	}

	/**
	 * Add the given event to this user session and sort the internal list by the entry time if true
	 * is set for sortByEntrytime.
	 *
	 * @param event
	 *            event to be added
	 * @param sortByEntryTime
	 *            true will trigger sort of the internal list
	 */
	public void add(final EntryCallEvent event, final boolean sortByEntryTime) {
		this.add(event);
		if (sortByEntryTime) {
			this.sortEventsBy(UserSession.SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME);
		}
	}

	/**
	 * Get the iterator of the internal event list.
	 *
	 * @return iterator
	 */
	public Iterator<EntryCallEvent> iterator() {
		return this.events.iterator();
	}

	/**
	 * Return the size of the events for this user session.
	 *
	 *
	 * @return size
	 */
	public int size() {
		return this.events.size();
	}

	/**
	 * Parse the id which would be constructed by the {@link UserSession} class if it contained that
	 * event.
	 *
	 * @param event
	 *            event
	 * @return unique id
	 */
	public static String createUserSessionId(final EntryCallEvent event) {
		return event.getHostname() + "," + event.getSessionId();
	}

	/**
	 * Parse the id which would be constructed by the {@link UserSession} class if it contained that
	 * event.
	 *
	 * @param event
	 *            event
	 * @return unique id
	 */
	public static String createUserSessionId(final ISessionEvent event) {
		return event.getHostname() + "," + event.getSessionId();
	}

	/**
	 * Get the exit time of this entire session.
	 *
	 * @return 0 if no events available at all and > 0 else.
	 */
	public long getExitTime() {
		long exitTime = 0;
		if (this.events.size() > 0) {
			this.sortEventsBy(UserSession.SORT_ENTRY_CALL_EVENTS_BY_EXIT_TIME);
			exitTime = this.events.get(this.events.size() - 1).getExitTime();
		}
		return exitTime;
	}

	/**
	 * Get the entry time of this entire session.
	 *
	 * @return 0 if no events available at all and > 0 else.
	 */
	public long getEntryTime() {
		long entryTime = 0;
		if (this.events.size() > 0) {
			this.sortEventsBy(UserSession.SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME);
			entryTime = this.events.get(this.events.size() - 1).getEntryTime();
		}
		return entryTime;
	}

	public List<EntryCallEvent> getEvents() {
		return this.events;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getHost() {
		return this.host;
	}

	@Override
	public int hashCode() {
		int eventHash = this.events.hashCode();
		for (final EntryCallEvent event : this.events) {
			eventHash += event.hashCode();
		}
		return super.hashCode() + this.host.hashCode() + this.sessionId.hashCode() + (int) this.getEntryTime() + (int) this.getExitTime() + eventHash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this.getClass().equals(obj.getClass())) {
			if (this == obj) {
				return true;
			} else {
				final UserSession otherSession = (UserSession) obj;
				return this.compareString(this.host, otherSession.host)
						&& this.compareString(this.sessionId, otherSession.sessionId)
						&& this.compareEvents(this.events, otherSession.events);
			}
		} else {
			return false;
		}
	}

	private boolean compareEvents(final List<EntryCallEvent> left, final List<EntryCallEvent> right) {
		if (left.size() == right.size()) {
			for (int i = 0; i < left.size(); i++) {
				if (!left.get(i).equals(right.get(i))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean compareString(final String left, final String right) {
		if ((left == null) && (right == null)) {
			return true;
		} else if (left != null) {
			return left.equals(right);
		} else {
			return false;
		}
	}

}
