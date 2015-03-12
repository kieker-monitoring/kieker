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

package kieker.common.util.registry.newversion;

/**
 * @param <T>
 *            the type of elements
 *
 * @author Christian Wulf
 *
 * @since 1.11
 */
public interface ILookup<T> {

	/**
	 * @since 1.11
	 */
	void add(int uniqueId, T element);

	/**
	 * @return the <code>element</code> that is associated with the passed <code>uniqueId</code>
	 *
	 * @since 1.11
	 */
	T get(int uniqueId);

	// int getSize();
}
