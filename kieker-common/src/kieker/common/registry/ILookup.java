/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.registry;

/**
 * A generic interface to query registered unique IDs of objects.
 *
 * @param <E>
 *            the type of the objects
 *
 * @author Jan Waller
 *
 * @since 1.8
 *
 * @deprecated since 1.15 remove or change in 1.16 uses deprecated interface
 */
@Deprecated
public interface ILookup<E> extends IRegistry<E> {

	/**
	 * Registers the object with the unique id.
	 *
	 * @param value
	 *            the object
	 * @param id
	 *            the unique id
	 * @return
	 * 		false on duplicate id
	 *
	 * @since 1.8
	 */
	public boolean set(final E value, final int id);
}
