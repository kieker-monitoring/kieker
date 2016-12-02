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

import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class BlockingQueueAdapter implements BlockingQueue<IMonitoringRecord> {

	private final Queue<IMonitoringRecord> queue;

	public BlockingQueueAdapter(final Queue<IMonitoringRecord> queue) {
		this.queue = queue;
	}

	@Override
	public boolean add(final IMonitoringRecord e) {
		return this.queue.add(e);
	}

	@Override
	public boolean offer(final IMonitoringRecord e) {
		return this.queue.offer(e);
	}

	@Override
	public int size() {
		return this.queue.size();
	}

	@Override
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		return this.queue.contains(o);
	}

	@Override
	public IMonitoringRecord remove() {
		return this.queue.remove();
	}

	@Override
	public IMonitoringRecord poll() {
		return this.queue.poll();
	}

	@Override
	public IMonitoringRecord element() {
		return this.queue.element();
	}

	@Override
	public Iterator<IMonitoringRecord> iterator() {
		return this.queue.iterator();
	}

	@Override
	public IMonitoringRecord peek() {
		return this.queue.peek();
	}

	@Override
	public Object[] toArray() {
		return this.queue.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return this.queue.toArray(a);
	}

	@Override
	public boolean remove(final Object o) {
		return this.queue.remove(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return this.queue.containsAll(c);
	}

	@Override
	public boolean addAll(final Collection<? extends IMonitoringRecord> c) {
		return this.queue.addAll(c);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return this.queue.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return this.queue.retainAll(c);
	}

	@Override
	public void clear() {
		this.queue.clear();
	}

	@Override
	public boolean equals(final Object o) {
		return this.queue.equals(o);
	}

	@Override
	public int hashCode() {
		return this.queue.hashCode();
	}

	// private final ThreadLocal<Long> backoffTimeInMs = ThreadLocal.withInitial(new Supplier<Long>() {
	// @Override
	// public Long get() {
	// return 1L;
	// }
	// });

	@Override
	public void put(final IMonitoringRecord e) throws InterruptedException {
		while (!this.queue.offer(e)) {
			// final long millis = this.backoffTimeInMs.get();
			Thread.sleep(1);
			// this.backoffTimeInMs.set(millis * 2);
		}
		// this.backoffTimeInMs.set(1L);

		// while (!this.queue.offer(e)) {
		// final long millis = this.backoffTimeInMs.get();
		// Thread.sleep(millis);
		// this.backoffTimeInMs.set(millis * 2);
		// }
		// this.backoffTimeInMs.set(1L);

		// int numYields = 0;
		// while (!this.offer(e) && (numYields < 5)) { // yield at most 5 times
		// Thread.yield();
		// numYields++;
		// }
		//
		// int millis = 1;
		// while (!this.offer(e)) { // afterwards, sleep
		// Thread.sleep(millis);
		// millis *= 2;
		// }
		//
		// while (!this.offer(e)) { // afterwards, wait for the lock
		// lock.wait();
		// }
	}

	@Override
	public boolean offer(final IMonitoringRecord e, final long timeout, final TimeUnit unit) throws InterruptedException {
		return this.add(e);
	}

	@Override
	public IMonitoringRecord take() throws InterruptedException {
		IMonitoringRecord e;
		while ((e = this.poll()) == null) {
			Thread.sleep(1);
		}
		return e;
	}

	@Override
	public IMonitoringRecord poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		return this.remove();
	}

	@Override
	public int remainingCapacity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(final Collection<? super IMonitoringRecord> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(final Collection<? super IMonitoringRecord> c, final int maxElements) {
		throw new UnsupportedOperationException();
	}

}
