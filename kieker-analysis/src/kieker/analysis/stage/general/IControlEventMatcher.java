/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.general;

/**
 * Matcher interface to control whether an event can pass or must be held.
 *
 * @param <C>
 *            control event type
 * @param <B>
 *            controlled event type
 *
 * @author Reiner Jung
 * @since 1.15
 *
 */
public interface IControlEventMatcher<C, B> {

	/**
	 * Check whether the given event must be handled by the controller
	 *
	 * @param baseEvent
	 *            the base event
	 * @return returns true when the base event must only be released with a proper control event
	 */
	boolean requiresControlEvent(B baseEvent);

	/**
	 * Check whether a base event matches a control event.
	 *
	 * @param controlEvent
	 *            the control event
	 * @param baseEvent
	 *            the base event to be checked
	 * @return returns true if the base event is accepted
	 */
	boolean checkControlEvent(C controlEvent, B baseEvent);

	/**
	 * True whether a control event must be kept to be reused for another base event.
	 *
	 * @param baseEvent
	 *            the previously checked base event
	 * @return true to keep control event
	 */
	boolean keepControlEvent(B baseEvent);
}
