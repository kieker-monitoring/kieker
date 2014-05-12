/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package de.chw.concurrent;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author Christian Wulf
 * 
 * @see "Dynamic Circular WorkStealing Deque"
 * 
 * @since 1.10
 */
public class CircularWorkStealingDeque<T> {

	public static class DequeIsEmptyException extends DequePopException {
		private static final long serialVersionUID = -6685406255103741724L;
	}

	public static final DequeIsEmptyException DEQUE_IS_EMPTY_EXCEPTION = new DequeIsEmptyException();

	public static class OperationAbortedException extends DequePopException {
		private static final long serialVersionUID = 2983001853326344073L;
	}

	public static final OperationAbortedException OPERATION_ABORTED_EXCEPTION = new OperationAbortedException();

	private static final long LOG_INITIAL_SIZE = 10;

	private volatile long bottom = 0;
	private final AtomicLong top = new AtomicLong();
	private volatile CircularArray<T> activeArray = new CircularArray<T>(LOG_INITIAL_SIZE);

	private boolean casTop(final long oldVal, final long newVal) {
		return this.top.compareAndSet(oldVal, newVal);
	}

	/**
	 * 
	 * @param o
	 *            a non-<code>null</code> element
	 */
	public void pushBottom(final T o) {
		final long b = this.bottom;
		final long t = this.top.get();
		CircularArray<T> a = this.activeArray;
		final int numElementsToPush = 1;
		final long currentSize = b - t;
		final long newSize = currentSize + numElementsToPush;
		if (newSize > a.getCapacity()) {
			a = a.grow(b, t);
			this.activeArray = a;
		}
		a.put(b, o);
		this.bottom = b + numElementsToPush;
	}

	/**
	 * 
	 * @param elements
	 *            a non-<code>null</code> list
	 */
	public void pushBottomMultiple(final List<T> elements) {
		final long b = this.bottom;
		final long t = this.top.get();
		CircularArray<T> a = this.activeArray;
		final int numElementsToPush = elements.size();
		final long currentSize = b - t;
		final long newSize = currentSize + numElementsToPush;
		if (newSize > a.getCapacity()) {
			a = a.grow(b, t);
			this.activeArray = a;
		}

		for (final T elem : elements) {
			a.put(b, elem);
		}

		this.bottom = b + numElementsToPush;
	}

	/**
	 * Returns and removes the latest element from this deque.
	 * 
	 * @return
	 *         <ul>
	 *         <li><code>null</code> if the deque contains no elements,
	 *         <li><i>the latest element</i> otherwise
	 *         </ul>
	 */
	public T popBottom() {
		long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		b = b - 1;
		this.bottom = b;
		final long t = this.top.get();
		final long size = b - t;
		if (size < 0) {
			this.bottom = t;
			return this.empty();
		}
		T o = this.regular(a.get(b));
		if (size > 0) {
			this.perhapsShrink(b, t);
			return o;
		}
		if (!this.casTop(t, t + 1)) {
			o = this.empty();
		}
		this.bottom = t + 1;
		return o;
	}

	/**
	 * Returns and removes the latest element from this deque.
	 * 
	 * @return <i>the latest element</i>, otherwise it throws a <code>DequeIsEmptyException</code>
	 * 
	 * @throws DequeIsEmptyException
	 */
	public T popBottomEx() {
		long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		b = b - 1;
		this.bottom = b;
		final long t = this.top.get();
		final long size = b - t;
		if (size < 0) {
			this.bottom = t;
			return this.emptyEx();
		}
		T o = this.regular(a.get(b));
		if (size > 0) {
			this.perhapsShrink(b, t);
			return o;
		}
		if (!this.casTop(t, t + 1)) {
			o = this.emptyEx();
		}
		this.bottom = t + 1;
		return o;
	}

	private void perhapsShrink(final long b, final long t) {
		long temp = t;
		final CircularArray<T> a = this.activeArray;
		if ((b - temp) < (a.getCapacity() / 4)) {
			final CircularArray<T> aa = a.shrink(b, temp);
			this.activeArray = aa;
			final long ss = aa.getCapacity();
			this.bottom = b + ss;
			temp = this.top.get();
			if (!this.casTop(temp, temp + ss)) {
				this.bottom = b;
				// a.free();
			}
		}
	}

	/**
	 * Tries to steal (return & remove) the oldest element from this deque.
	 * 
	 * @return
	 *         <ul>
	 *         <li><code>null</code> if the deque contains no elements,
	 *         <li>(and also) <code>null</code> if the deque is currently being stolen by another thread,
	 *         <li><i>the oldest element</i> otherwise
	 *         </ul>
	 */
	public T steal() {
		final long t = this.top.get();
		final CircularArray<T> oldArr = this.activeArray;
		final long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		final long size = b - t;
		if (size <= 0) {
			return this.empty();
		}
		if ((size % a.getCapacity()) == 0) {
			if ((oldArr == a) && (t == this.top.get())) {
				return this.empty();
			} else {
				return this.abort();
			}
		}
		final T o = this.regular(a.get(t));
		if (!this.casTop(t, t + 1)) {
			return this.abort();
		}
		return o;
	}

	/**
	 * Tries to steal (return & remove) the oldest element from this deque.
	 * 
	 * @return <i>the oldest element</i>, otherwise it throws a <code>DequeIsEmptyException</code> or a <code>OperationAbortedException</code>
	 * 
	 * @throws DequeIsEmptyException
	 * @throws OperationAbortedException
	 */
	public T stealEx() {
		final long t = this.top.get();
		final CircularArray<T> oldArr = this.activeArray;
		final long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		final long size = b - t;
		if (size <= 0) {
			return this.emptyEx();
		}
		if ((size % a.getCapacity()) == 0) {
			if ((oldArr == a) && (t == this.top.get())) {
				return this.emptyEx();
			} else {
				return this.abortEx();
			}
		}
		final T o = this.regular(a.get(t));
		if (!this.casTop(t, t + 1)) {
			return this.abortEx();
		}
		return o;
	}

	private T empty() {
		return null;
	}

	private T emptyEx() {
		throw DEQUE_IS_EMPTY_EXCEPTION;
	}

	private T abort() {
		return null;
	}

	private T abortEx() {
		throw OPERATION_ABORTED_EXCEPTION;
	}

	private T regular(final T value) {
		return value;
	}

	/**
	 * Returns but does not remove the latest element from this deque.<br>
	 * <i>For debugging purposes</i>
	 * 
	 * @return <ul>
	 *         <li><code>null</code> if the deque contains no elements,
	 *         <li><i>the latest element</i> otherwise
	 *         </ul>
	 */
	public T readBottom() {
		final long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		final T o = a.get(b);
		return o;
	}

	// bottom: 4093
	// bottom: 66429
	// bottom: 29993
	// bottom: 29992
	//
	//
	// bottom: 4093
	// bottom: 66429
	// bottom: 30008
	// bottom: 30007

	public boolean isEmpty() {
		final long t = this.top.get();
		final long b = this.bottom;
		return t >= b;
	}

	/**
	 * For debugging purposes
	 * 
	 * @return the number of elements this deque contains
	 */
	public long size(final Object sourceStage) {
		final long t = this.top.get();
		final long b = this.bottom;
		final long size = b - t;
		System.out.println("sourceStage=" + sourceStage + ", " + "bottom: " + this.bottom);
		return size;
	}

}
