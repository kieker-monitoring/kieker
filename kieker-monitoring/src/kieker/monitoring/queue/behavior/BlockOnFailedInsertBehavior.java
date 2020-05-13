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

package kieker.monitoring.queue.behavior;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Christian Wulf
 *
 * @param <E>
 *            the type of the element which should be inserted into the queue.
 *
 * @since 1.13
 */
public class BlockOnFailedInsertBehavior<E> implements InsertBehavior<E> {

	/** The logger for this class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BlockOnFailedInsertBehavior.class);
	/** The blocking queue to which elements should be inserted. */
	private final BlockingQueue<E> queue;
	/** Current number of blocked inserts. */
	private int numBlocked;

	/**
	 * @param queue the blocking queue to use this insert behavior
	 */
	public BlockOnFailedInsertBehavior(final BlockingQueue<E> queue) {
		this.queue = queue;
	}

	@Override
	public boolean insert(final E element) {
		final boolean offered = this.queue.offer(element); // first, try the faster non-blocking insert
		if (offered) {
			return true;
		}

		// if the faster non-blocking insert has failed, try the slower blocking wait
		try {
			this.queue.put(element); // blocking wait
			this.numBlocked++;
			return true;
		} catch (final InterruptedException e) {
			// The interrupt status has been reset by the put method when throwing the exception.
			// We will not propagate the interrupt because the error is reported by returning false.
			LOGGER.warn("Interrupted when adding new monitoring record to queue.", e);
		}
		LOGGER.error("Failed to add new monitoring record to queue (maximum number of attempts reached).");
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder(50)
				.append(this.getClass())
				.append("\n\t\t")
				.append("numBlocked: ")
				.append(this.numBlocked);
		return builder.toString();
	}

}
