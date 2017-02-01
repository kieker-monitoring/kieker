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

package kieker.monitoring.queue;

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
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class BlockingQueueDecorator<E> implements BlockingQueue<E> {

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
		this.putStrategy.backoffOffer(this.q, e); // internally calls "offer(e)"
	}

	@Override
	public E take() throws InterruptedException {
		return this.takeStrategy.waitPoll(this.q);
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
	public boolean add(final E e) {
		return this.q.add(e);
	}

	@Override
	public int size() {
		return this.q.size();
	}

	@Override
	public boolean isEmpty() {
		return this.q.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		return this.q.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.q.iterator();
	}

	@Override
	public E remove() {
		return this.q.remove();
	}

	@Override
	public Object[] toArray() {
		return this.q.toArray();
	}

	@Override
	public E element() {
		return this.q.element();
	}

	@Override
	public E peek() {
		return this.q.peek();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return this.q.toArray(a);
	}

	@Override
	public boolean remove(final Object o) {
		return this.q.remove(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return this.q.containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return this.q.addAll(c);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return this.q.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return this.q.retainAll(c);
	}

	@Override
	public void clear() {
		this.q.clear();
	}

	@Override
	public boolean equals(final Object o) {
		return this.q.equals(o);
	}

	@Override
	public int hashCode() {
		return this.q.hashCode();
	}

}
