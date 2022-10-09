/***************************************************************************
 * Copyright (c) 2012-2013 Eduardo R. D'Avila (https://github.com/erdavila)
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
package kieker.analysis.behavior.mtree.utils;

/**
 * A pair of objects of the same type.
 *
 * @param <T>
 *            The type of the objects.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public class Pair<T> {

	/**
	 * The first object.
	 */
	private T first;

	/**
	 * The second object.
	 */
	private T second;

	/**
	 * Creates a pair of {@code null} objects.
	 */
	public Pair() {}

	/**
	 * Creates a pair with the objects specified in the arguments.
	 *
	 * @param first
	 *            The first object.
	 * @param second
	 *            The second object.
	 */
	public Pair(final T first, final T second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Accesses an object by its index. The {@link #first} object has index
	 * {@code 0} and the {@link #second} object has index {@code 1}.
	 *
	 * @param index
	 *            The index of the object to be accessed.
	 * @return The {@link #first} object if {@code index} is {@code 0}; the
	 *         {@link #second} object if {@code index} is {@code 1}.
	 * @throws IllegalArgumentException
	 *             If {@code index} is neither {@code 0}
	 *             or {@code 1}.
	 */
	public T get(final int index) throws IllegalArgumentException {
		switch (index) {
		case 0:
			return this.first;
		case 1:
			return this.second;
		default:
			throw new IllegalArgumentException();
		}
	}

	public T getFirst() {
		return this.first;
	}

	public T getSecond() {
		return this.second;
	}

}
