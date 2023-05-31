/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.registry.writer;

/**
 * A generic interface to assign unique IDs to objects.
 *
 * @param <E>
 *            the type of the objects
 *
 * @author Hannes Strubel
 *
 * @since 1.13
 */
public interface IWriterRegistry<E> { // NOCS //NOPMD

	/**
	 * Gets the ID of this registry.
	 *
	 * @return The registry's ID
	 *
	 * @since 1.13
	 */
	public long getId();

	/**
	 * @param value
	 *            a registered value
	 * @return unique id
	 *
	 * @since 1.13
	 */
	public int getId(E value);

	/**
	 * @param value
	 *            to be registered
	 *
	 * @since 1.13
	 */
	public void register(E value);
}
