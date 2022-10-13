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
import java.util.Arrays;
import java.util.List;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.analysis.behavior.data.EntryCallEvent;

/**
 * An EventGroup contains all events of an edge, which have the same parameters.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class EventGroup {

	// the parameters of the events of the EventGroup can be an attribute, as all events have the
	// same parameters
	private final String[] parameters;
	private final List<EntryCallEvent> events;

	public EventGroup(final String[] parameters) {
		this.parameters = parameters;
		this.events = new ArrayList<>();
	}

	public String[] getParameters() {
		return this.parameters;
	}

	public List<EntryCallEvent> getEvents() {
		return this.events;
	}

	public boolean hasSameParameters(final EntryCallEvent event) {
		return Arrays.equals(event.getParameters(), this.parameters);
	}

	public boolean hasSameParameters(final EventGroup eventGroup) {
		return Arrays.equals(eventGroup.getParameters(), this.parameters);
	}

}
