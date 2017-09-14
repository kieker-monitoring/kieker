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

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author "Christian Wulf"
 *
 * @since 1.13
 *
 * @param <E>
 *            the type of the element which should be inserted into the queue.
 */
public class TerminateOnFailedInsertBehavior<E> implements InsertBehavior<E> {

	private static final Log LOG = LogFactory.getLog(TerminateOnFailedInsertBehavior.class);

	private final BlockingQueue<E> queue;

	public TerminateOnFailedInsertBehavior(final BlockingQueue<E> queue) {
		this.queue = queue;
	}

	@Override
	public boolean insert(final E element) {
		final boolean offered = this.queue.offer(element);
		if (!offered) {
			LOG.error(
					"Failed to add new monitoring record to queue. Queue is full. Either increase 'QueueSize' or change 'QueueFullBehavior' for the configured writer."); // NOCS
			return false;
		} else {
			return true;
		}
	}

}
