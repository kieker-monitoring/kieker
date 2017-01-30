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

import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author "Christian Wulf"
 * @param <E>
 *
 * @since 1.13
 */
public class BlockOnFailedInsertBehavior<E> implements InsertBehavior<E> {

	private static final Log LOG = LogFactory.getLog(BlockOnFailedInsertBehavior.class);

	private final BlockingQueue<E> queue;
	private final int maxRetries; // chw: why retries on interrupted?

	private int numBlocked;

	// private final List<Long> blockDurations;

	/**
	 * maxRetries = 10
	 */
	public BlockOnFailedInsertBehavior(final BlockingQueue<E> queue) {
		this(queue, 10);
	}

	protected BlockOnFailedInsertBehavior(final BlockingQueue<E> queue, final int maxRetries) {
		this.queue = queue;
		this.maxRetries = maxRetries;
		// this.blockDurations = new ArrayList<Long>(1024 * 1024);
	}

	@Override
	public boolean insert(final E element) {
		final boolean offered = this.queue.offer(element);
		if (offered) {
			return true;
		}

		// final long start = System.nanoTime();
		// for (int i = 0; i < this.maxRetries; i++) { // drop out if more than maxRetries interrupted
		try {
			this.queue.put(element);
			// final long end = System.nanoTime();
			// final long duration = end - start;
			// this.blockDurations.add(duration);
			this.numBlocked++;
			return true;
		} catch (final InterruptedException ignore) {
			// The interrupt status has been reset by the put method when throwing the exception.
			// We will not propagate the interrupt because the error is reported by returning false.
			// LOG.warn("Interrupted when adding new monitoring record to queue. Try: " + i);
			LOG.warn("Interrupted when adding new monitoring record to queue.");
		}
		// }
		LOG.error("Failed to add new monitoring record to queue (maximum number of attempts reached).");
		return false;
	}

	@Override
	public String toString() {
		// return "#: " + this.blockDurations.size() + ", " + this.blockDurations.toString();
		return "numBlocked: " + this.numBlocked;
	}

}
