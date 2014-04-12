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

package de.chw.concurrent.alternative;

import java.util.concurrent.atomic.AtomicLong;

import de.chw.concurrent.CircularArray;

/**
 * 
 * @author Christian Wulf
 * 
 * @see "Dynamic Circular WorkStealing Deque"
 * 
 * @since 1.10
 */
public class UntypedExceptionalCircularWorkStealingDeque {
	public static class DequeIsEmptyException extends Exception {
		private static final long serialVersionUID = -6685406255103741724L;
	}

	public static final DequeIsEmptyException DEQUE_IS_EMPTY_EXCEPTION = new DequeIsEmptyException();

	public static class OperationAbortedException extends Exception {
		private static final long serialVersionUID = 2983001853326344073L;
	}

	public static final OperationAbortedException OPERATION_ABORTED_EXCEPTION = new OperationAbortedException();

	private static final long LOG_INITIAL_SIZE = 10;

	private volatile long bottom = 0;
	// private volatile long top = 0;
	private final AtomicLong top = new AtomicLong();
	private volatile CircularArray<Object> activeArray = new CircularArray<Object>(LOG_INITIAL_SIZE);

	private boolean casTop(final long oldVal, final long newVal) {
		// boolean preCond;
		// synchronized (this) {
		// preCond = (this.top == oldVal);
		// if (preCond) {
		// this.top = newVal;
		// }
		// }
		// return preCond;
		return this.top.compareAndSet(oldVal, newVal);
	}

	public void pushBottom(final Object o) {
		final long b = this.bottom;
		final long t = this.top.get();
		CircularArray<Object> a = this.activeArray;
		final long size = b - t;
		if (size > (a.size() - 1)) {
			a = a.grow(b, t);
			this.activeArray = a;
		}
		a.put(b, o);
		this.bottom = b + 1;
	}

	/**
	 * 
	 * @return
	 *         <ul>
	 *         <li><code>EMPTY</code> if the deque contains no elements,
	 *         <li><i>the latest element</i> otherwise
	 *         </ul>
	 * @throws DequeIsEmptyException
	 */
	public Object popBottom() throws DequeIsEmptyException {
		long b = this.bottom;
		final CircularArray<Object> a = this.activeArray;
		b = b - 1;
		this.bottom = b;
		final long t = this.top.get();
		final long size = b - t;
		if (size < 0) {
			this.bottom = t;
			throw DEQUE_IS_EMPTY_EXCEPTION;
		}
		final Object o = a.get(b);
		if (size > 0) {
			this.perhapsShrink(b, t);
			return o;
		}
		final boolean success = this.casTop(t, t + 1);
		this.bottom = t + 1;
		if (!success) {
			throw DEQUE_IS_EMPTY_EXCEPTION;
		}
		return o;
	}

	void perhapsShrink(final long b, final long t) {
		long temp = t;
		final CircularArray<Object> a = this.activeArray;
		if ((b - temp) < (a.size() / 4)) {
			final CircularArray<Object> aa = a.shrink(b, temp);
			this.activeArray = aa;
			final long ss = aa.size();
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
	 *         <li><code>EMPTY</code> if the deque contains no elements,
	 *         <li><code>ABORT</code> if the deque is currently being stolen by another thread,
	 *         <li><i>the oldest element</i> otherwise
	 *         </ul>
	 * @throws OperationAbortedException
	 * @throws DequeIsEmptyException
	 */
	public Object steal() throws OperationAbortedException, DequeIsEmptyException {
		final long t = this.top.get();
		final CircularArray<Object> oldArr = this.activeArray;
		final long b = this.bottom;
		final CircularArray<Object> a = this.activeArray;
		final long size = b - t;
		if (size <= 0) {
			throw DEQUE_IS_EMPTY_EXCEPTION;
		}
		if ((size % a.size()) == 0) {
			if ((oldArr == a) && (t == this.top.get())) {
				throw DEQUE_IS_EMPTY_EXCEPTION;
			} else {
				throw OPERATION_ABORTED_EXCEPTION;
			}
		}
		final Object o = a.get(t);
		if (!this.casTop(t, t + 1)) {
			throw OPERATION_ABORTED_EXCEPTION;
		}
		return o;
	}

	/**
	 * For debugging purposes
	 * 
	 * @return but does not remove the bottom element from this deque
	 */
	public Object readBottom() {
		final long b = this.bottom;
		final CircularArray<Object> a = this.activeArray;
		final Object o = a.get(b);
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
