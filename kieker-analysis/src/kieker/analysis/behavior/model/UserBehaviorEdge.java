/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
import java.util.List;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.generic.graph.impl.EdgeImpl;

/**
 * The edge between two nodes of a BehaviorModel.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class UserBehaviorEdge extends EdgeImpl {

	private final List<EventGroup> eventGroups = new ArrayList<>();

	/**
	 * Create a new edge without an entry call event attached.
	 *
	 * @param id
	 *            edge id
	 */
	public UserBehaviorEdge(final String id) {
		super(id);
	}

	/**
	 * Create a new edge between two nodes.
	 *
	 * @param id
	 *            edge id
	 * @param event
	 *            the event which is represented by the edge
	 */
	public UserBehaviorEdge(final String id, final EntryCallEvent event) {
		super(id);
		this.addEvent(event);
	}

	/**
	 * Add a new event to the edge.
	 *
	 * @param event
	 *            the event which should be added to the edge
	 */
	public void addEvent(final EntryCallEvent event) {
		boolean success = false;
		for (final EventGroup eventGroup : this.eventGroups) {
			if (eventGroup.hasSameParameters(event)) {
				eventGroup.getEvents().add(event);
				success = true;
				break;
			}
		}
		if (!success) {
			final EventGroup newEventGroup = new EventGroup(event.getParameters());
			newEventGroup.getEvents().add(event);
			this.eventGroups.add(newEventGroup);
		}
	}

	public List<EventGroup> getEventGroups() {
		return this.eventGroups;
	}

}
