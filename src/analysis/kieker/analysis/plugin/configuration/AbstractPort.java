/***************************************************************************
 * Copyright 2011 by
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

package kieker.analysis.plugin.configuration;

import java.util.Collection;

/**
 * This class represents a port within this system used for the pipe & filter
 * structure of the analysis plugins and readers.
 * 
 * @author Nils Christian Ehmke
 */
abstract class AbstractPort implements IPort {

	private final String description;
	protected final Collection<Class<?>> eventTypes;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param description
	 *            A human-readable string explaining what this port can
	 *            be used for. This string will probably be used later for a
	 *            GUI.
	 * 
	 * @param eventTypes
	 *            A list containing the classes which are transfered by this
	 *            port. If a component tries to use the port to send an object
	 *            which is not from a (sub)class within this list, the request
	 *            will be ignored.
	 */
	public AbstractPort(final String description, final Collection<Class<?>> eventTypes) {
		this.description = description;
		this.eventTypes = eventTypes;
	}

	/**
	 * Delivers a list containing the classes which are treated by this port.
	 * 
	 * @return A list of the event types.
	 */
	@Override
	public Collection<Class<?>> getEventTypes() {
		return eventTypes;
	}

	/**
	 * Delivers a human-readable string explaining what this port can be used
	 * for.
	 * 
	 * @returns The description of this instance.
	 */
	public final String getDescription() {
		return this.description;
	}
}
