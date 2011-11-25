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

import kieker.analysis.exception.InvalidPortSubscriberException;

/**
 * 
 * @author Andre van Hoorn
 */
public interface IOutputPort extends IPort {

	/**
	 * This method registers a given input port as a subscriber. Everything
	 * which is sent to this port is send directly to the subscribers.
	 * 
	 * @param subscriber
	 *            The port to be registered. The port should be able to handle
	 *            all possible outputs of this port.
	 * @throws InvalidPortSubscriberException
	 *             If the given port cannot handle all outputs of this port.
	 */
	public void subscribe(IInputPort subscriber) throws InvalidPortSubscriberException;

	/**
	 * This method unregisters a given input port.
	 * 
	 * @param subscriber
	 *            The subscriber to be removed. If the subscriber is not
	 *            registered, nothing happens.
	 */
	public void unsubscribe(IInputPort subscriber);
}
