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

package kieker.monitoring.queue.behavior;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Christian Wulf"
 *
 * @since 1.13
 *
 * @param <E>
 *            the type of the element which should be inserted into the queue.
 */
public class CountOnFailedInsertBehavior<E> implements InsertBehavior<E> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountOnFailedInsertBehavior.class);

	private final BlockingQueue<E> queue;
	private final AtomicLong numFailedInserts = new AtomicLong();

	public CountOnFailedInsertBehavior(final BlockingQueue<E> queue) {
		this.queue = queue;
	}

	@Override
	public boolean insert(final E element) {
		final boolean offered = this.queue.offer(element);
		if (!offered) {
			final long tmpMissedRecords = this.numFailedInserts.incrementAndGet();
			if (LOGGER.isWarnEnabled() && ((tmpMissedRecords % 1024) == 1)) {
				// warn upon the first failed element and upon all 1024th one
				LOGGER.warn("Queue is full, dropping records. Number of already dropped records: " + tmpMissedRecords);
			}
		}
		return true;
	}

	public long getNumFailedInserts() {
		return this.numFailedInserts.get();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder()
				.append(this.getClass())
				.append("\n\t\t")
				.append("Number of failed inserts: ")
				.append(this.getNumFailedInserts());
		return builder.toString();
	}
}
