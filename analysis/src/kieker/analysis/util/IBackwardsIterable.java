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

package kieker.analysis.util;

import java.util.Iterator;

/**
 * Implementing this interface allows an object to get iterated backwards. The
 * object returned by {@code backwards()} can be used by a "foreach" statement.
 *
 * @param <T>
 *            the type of elements returned by the iterator
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public interface IBackwardsIterable<T> {

	/**
	 * Returns an Iterator that iterates the elements backwards.
	 *
	 * @return returns the iterator
	 *
	 * @since 1.14
	 */
	public Iterator<T> backwardsIterator();

	/**
	 * Returns an Iterable that iterates the elements backwards using the
	 * Iterator returned by {@code backwardsIterator()}.
	 *
	 * @return returns an iterable
	 *
	 * @since 1.14
	 */
	public default Iterable<T> backwards() {
		return new Iterable<>() {
			@Override
			public Iterator<T> iterator() {
				return IBackwardsIterable.this.backwardsIterator();
			}
		};
	}

}
