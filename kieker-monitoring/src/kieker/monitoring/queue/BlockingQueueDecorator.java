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

package kieker.monitoring.queue;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.monitoring.queue.putstrategy.PutStrategy;
import kieker.monitoring.queue.takestrategy.TakeStrategy;

/**
 * A wrapper to provide a given queue a strategy to put and to take possibly in blocking mode.
 *
 * @param <E>
 *            the type of the elements in the given queue
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class BlockingQueueDecorator<E> extends AbstractQueue<E> implements BlockingQueue<E> {
	// "add()" internally calls "q.offer(e)" NOT "this.offer(e)". Hence, we inherit from AbstractQueue to get a correct add()/remove() etc. behavior.

	private final Queue<E> q;
	private final PutStrategy putStrategy;
	private final TakeStrategy takeStrategy;

	public BlockingQueueDecorator(final Queue<E> q, final PutStrategy putStrategy, final TakeStrategy takeStrategy) {
		this.q = q;
		this.putStrategy = putStrategy;
		this.takeStrategy = takeStrategy;
	}

	@Override
	public void put(final E e) throws InterruptedException {
		this.putStrategy.backoffOffer(this, e); // internally calls "offer(e)"
	}

	@Override
	public boolean offer(final E e) {
		final boolean offered = this.q.offer(e);
		if (offered) {
			this.takeStrategy.signal();
		}
		return offered;
	}

	@Override
	public E take() throws InterruptedException {
		return this.takeStrategy.waitPoll(this); // internally calls "poll()"
	}

	@Override
	public E poll() {
		final E e = this.q.poll();
		if (e != null) {
			this.putStrategy.signal();
		}
		return e;
	}

	@Override
	public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int remainingCapacity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(final Collection<? super E> c) {
		int count = 0;
		E e = this.poll();
		while (e != null) {
			c.add(e);
			count++;
			e = this.poll();
		}
		return count;
	}

	@Override
	public int drainTo(final Collection<? super E> c, final int maxElements) {
		int count = 0;
		E e = this.poll();
		while ((e != null) && (count < maxElements)) {
			c.add(e);
			count++;
			e = this.poll();
		}
		return count;
	}

	@Override
	public E peek() {
		return this.q.peek();
	}

	@Override
	public Iterator<E> iterator() {
		return this.q.iterator();
	}

	@Override
	public int size() {
		return this.q.size();
	}

}
