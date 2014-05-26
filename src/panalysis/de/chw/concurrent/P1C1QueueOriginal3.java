/*
 * Copyright 2012 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.chw.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <ul>
 * <li>Lock free, observing single writer principal.
 * <li>Replacing the long fields with AtomicLong and using lazySet instead of volatile assignment.
 * <li>Using the power of 2 mask, forcing the capacity to next power of 2.
 * <li>Adding head and tail cache fields. Avoiding redundant volatile reads.
 * <li>Padding head/tail AtomicLong fields. Avoiding false sharing.
 * <li>Padding head/tail cache fields. Avoiding false sharing.
 * </ul>
 */
public final class P1C1QueueOriginal3<E> implements Queue<E> {
	private final int capacity;
	private final int mask;
	private final E[] buffer;

	private final AtomicLong tail = new PaddedAtomicLong(0);
	private final AtomicLong head = new PaddedAtomicLong(0);

	public static class PaddedLong {
		public long value = 0, p1, p2, p3, p4, p5, p6;
	}

	private final PaddedLong tailCache = new PaddedLong();
	private final PaddedLong headCache = new PaddedLong();

	@SuppressWarnings("unchecked")
	public P1C1QueueOriginal3(final int capacity) {
		this.capacity = P1C1QueueOriginal3.findNextPositivePowerOfTwo(capacity);
		this.mask = this.capacity - 1;
		this.buffer = (E[]) new Object[this.capacity];
	}

	public static int findNextPositivePowerOfTwo(final int value) {
		return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
	}

	public boolean add(final E e) {
		if (this.offer(e)) {
			return true;
		}

		throw new IllegalStateException("Queue is full");
	}

	public boolean offer(final E e) {
		if (null == e) {
			throw new NullPointerException("Null is not a valid element");
		}

		final long currentTail = this.tail.get();
		final long wrapPoint = currentTail - this.capacity;
		if (this.headCache.value <= wrapPoint) {
			this.headCache.value = this.head.get();
			if (this.headCache.value <= wrapPoint) {
				return false;
			}
		}

		this.buffer[(int) currentTail & this.mask] = e;
		this.tail.lazySet(currentTail + 1);

		return true;
	}

	public E poll() {
		final long currentHead = this.head.get();
		if (currentHead >= this.tailCache.value) {
			this.tailCache.value = this.tail.get();
			if (currentHead >= this.tailCache.value) {
				return null;
			}
		}

		final int index = (int) currentHead & this.mask;
		final E e = this.buffer[index];
		this.buffer[index] = null;
		this.head.lazySet(currentHead + 1);

		return e;
	}

	public E remove() {
		final E e = this.poll();
		if (null == e) {
			throw new NoSuchElementException("Queue is empty");
		}

		return e;
	}

	public E element() {
		final E e = this.peek();
		if (null == e) {
			throw new NoSuchElementException("Queue is empty");
		}

		return e;
	}

	public E peek() {
		return this.buffer[(int) this.head.get() & this.mask];
	}

	public int size() {
		return (int) (this.tail.get() - this.head.get());
	}

	public boolean isEmpty() {
		return this.tail.get() == this.head.get();
	}

	public boolean contains(final Object o) {
		if (null == o) {
			return false;
		}

		for (long i = this.head.get(), limit = this.tail.get(); i < limit; i++) {
			final E e = this.buffer[(int) i & this.mask];
			if (o.equals(e)) {
				return true;
			}
		}

		return false;
	}

	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(final T[] a) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(final Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(final Collection<?> c) {
		for (final Object o : c) {
			if (!this.contains(o)) {
				return false;
			}
		}

		return true;
	}

	public boolean addAll(final Collection<? extends E> c) {
		for (final E e : c) {
			this.add(e);
		}

		return true;
	}

	public boolean removeAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(final Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		Object value;
		do {
			value = this.poll();
		} while (null != value);
	}
}
