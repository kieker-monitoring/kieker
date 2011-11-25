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

package kieker.analysis.plugin.port;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.exception.InvalidPortSubscriberException;

/**
 * @author Andre van Hoorn
 */
public final class OutputPort extends AbstractPort implements IOutputPort {

	private final Collection<AbstractInputPort> subscribers = new CopyOnWriteArrayList<AbstractInputPort>();

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
	public OutputPort(final String description, final Collection<Class<?>> eventTypes) {
		/* Call the inherited constructor to delegate the initializing. */
		super(description, eventTypes);
	}

	/**
	 * This method can be used to deliver a given event of any type to the
	 * subscribers of this port. Keep in mind that not registered classes are
	 * not treated.
	 * 
	 * @param event
	 *            The event to be delivered to the subscribers.
	 * 
	 * @return true iff the event has been treated by this instance.
	 */
	public final boolean deliver(final Object event) {
		/* Check whether the class of the given event is registered. */
		boolean isRegistered = false;
		if (this.eventTypes != null) {
			for (final Class<?> c : this.eventTypes) {
				if (c.isInstance(event)) {
					isRegistered = true;
					break;
				}
			}
			if (!isRegistered) {
				return false;
			}
		}
		/* Seems like it's okay. Deliver it to the subscribers. */
		for (final IInputPort l : this.subscribers) {
			l.newEvent(event);
		}
		return true;
	}

	@Override
	public final void subscribe(final IInputPort subscriber) throws InvalidPortSubscriberException {
		final AbstractInputPort asubscriber = (AbstractInputPort) subscriber;
		/* If this port uses null as event type, everything can be delivered. */
		if ((this.eventTypes == null) && (asubscriber.eventTypes != null)) {
			throw new InvalidPortSubscriberException();
		}
		/* If the port uses null, it can receive anything. */
		if (!(asubscriber.eventTypes == null)) {
			/*
			 * Otherwise the port has to be able to handle every possible
			 * output of this instance.
			 */
			for (final Class<?> eventType : this.eventTypes) {
				boolean accepted = false;
				for (final Class<?> inputEventTypes : asubscriber.eventTypes) {
					if (inputEventTypes.isAssignableFrom(eventType)) {
						accepted = true;
					}
				}
				if (!accepted) {
					throw new InvalidPortSubscriberException();
				}
			}
		}
		this.subscribers.add(asubscriber);
	}

	@Override
	public final void unsubscribe(final IInputPort subscriber) {
		this.subscribers.remove(subscriber);
	}

	/**
	 * Delivers a list containing all subscribers of this port.
	 * 
	 * @return All registered input ports.
	 */
	public final List<AbstractInputPort> getSubscribers() {
		return new CopyOnWriteArrayList<AbstractInputPort>(this.subscribers);
	}
}
