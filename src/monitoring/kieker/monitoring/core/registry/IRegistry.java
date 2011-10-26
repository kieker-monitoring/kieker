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

package kieker.monitoring.core.registry;

/**
 * A generic interface to assign unique IDs to Objects of type E.
 * 
 * @author Jan Waller
 */
public interface IRegistry<E> {

	/**
	 * Gets a unique id for an object.
	 * 
	 * @param value
	 *            the object
	 * @return
	 *         the unique id
	 */
	public int get(E value);

	/**
	 * Gets the object associated with the unique id.
	 * 
	 * @param i
	 *            the unique id
	 * @return
	 *         the associated object
	 */
	public E get(int i);
}
