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

package kieker.monitoring.queue.behavior;

/**
 * @author Christian Wulf (chw)
 *
 * @param <E>
 *            the type of the element which should be inserted into the queue.
 * @since 1.13
 */
public abstract class InsertBehavior<E> {

	/**
	 * @since 1.13
	 * @return <code>true</code> if the element after this <code>element</code> can be inserted, otherwise <code>false</code>.
	 */
	public abstract boolean insert(E element);

}
