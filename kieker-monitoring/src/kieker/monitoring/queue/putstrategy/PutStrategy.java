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

package kieker.monitoring.queue.putstrategy;

import java.util.Queue;

/**
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public interface PutStrategy { // NOCS //NOPMD (name without prefix "I" for reasons of readability)

	/**
	 *
	 * @param queue
	 *            queue
	 * @param element
	 *            element
	 *
	 * @param <E>
	 *            element type
	 *
	 * @throws InterruptedException
	 *             on interruption of the queue
	 *
	 * @since 1.13
	 */
	<E> void backoffOffer(Queue<E> queue, E element) throws InterruptedException;

	/**
	 * @since 1.13
	 */
	void signal();
}
