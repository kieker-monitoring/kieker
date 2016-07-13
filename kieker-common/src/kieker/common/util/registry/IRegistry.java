/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.registry;

/**
 * A generic interface to assign unique IDs to objects.
 *
 * @param <E>
 *            the type of the objects
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public interface IRegistry<E> {

	/**
	 * Gets a unique id for an object.
	 *
	 * @param value
	 *            the object
	 * @return
	 * 		the unique id
	 *
	 * @since 1.5
	 */
	public int get(E value);

	/**
	 * Gets the object associated with the unique id.
	 *
	 * @param i
	 *            the unique id
	 * @return
	 * 		the associated object
	 *
	 * @since 1.5
	 */
	public E get(int i);

	/**
	 * Return an array with all registered objects.
	 *
	 * @return
	 * 		array of registered objects
	 *
	 * @since 1.5
	 */
	public E[] getAll();

	/**
	 * Returns the number of registered objects.
	 *
	 * @return
	 * 		number of registered objects
	 *
	 * @since 1.5
	 */
	public int getSize();

	/**
	 * Enables logging of newly registered objects.
	 *
	 * @param registryRecordReceiver
	 *            the IRegistryRecordReceiver logged to
	 *
	 * @since 1.5
	 */
	public void setRecordReceiver(final IRegistryRecordReceiver registryRecordReceiver);
}
