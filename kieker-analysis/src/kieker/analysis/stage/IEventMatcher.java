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
package kieker.analysis.stage;

import teetime.framework.OutputPort;

/**
 * Interface for event matchers used in the dynamic event dispatcher.
 *
 * @author Reiner Jung
 *
 * @param <T>
 *            event type
 * @since 1.15
 */
public interface IEventMatcher<T> {

	/**
	 * Register an event matcher.
	 *
	 * @param outputPort
	 *            an output port for the event matcher
	 */
	void setOutputPort(final OutputPort<T> outputPort);

	/**
	 * Check whether the event should be send to a specific output port.
	 *
	 * @param event
	 *            event to be evaluated
	 * @param <R>
	 *            a sub type of <T>
	 * @return true if the even must be send to the corresponding output port
	 */
	<R extends Object> boolean matchEvent(R event);

	/**
	 * Return the next matcher.
	 *
	 * @return return the matcher
	 */
	IEventMatcher<? extends Object> getNextMatcher();

	/**
	 * Provide the corresponding output port.
	 *
	 * @return the output port of this matcher
	 */
	OutputPort<T> getOutputPort();

	/**
	 * Set a new following matcher.
	 *
	 * @param leaveEventMatcher
	 *            one matcher or a chain of matchers
	 */
	void setNextMatcher(IEventMatcher<? extends Object> leaveEventMatcher);

}
