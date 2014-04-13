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
public class CircularWorkStealingDequeWithSentinel<T> {

	public static enum State {
		REGULAR, EMPTY, ABORT
	}

	public static class ReturnValue<T> {
		private final State state;
		private final T value;

		public ReturnValue(final State state, final T value) {
			this.state = state;
			this.value = value;
		}

		public T getValue() {
			return this.value;
		}

		public State getState() {
			return this.state;
		}
	}

	private static final long LOG_INITIAL_SIZE = 10;

	private volatile long bottom = 0;
	// private volatile long top = 0;
	private final AtomicLong top = new AtomicLong();
	private volatile CircularArray<T> activeArray = new CircularArray<T>(LOG_INITIAL_SIZE);

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

	public void pushBottom(final T o) {
		final long b = this.bottom;
		final long t = this.top.get();
		CircularArray<T> a = this.activeArray;
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
	 *         <li><code>empty()</code> if the deque contains no elements,
	 *         <li><i>the latest element</i> otherwise
	 *         </ul>
	 */
	public ReturnValue<T> popBottom() {
		long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		b = b - 1;
		this.bottom = b;
		final long t = this.top.get();
		final long size = b - t;
		if (size < 0) {
			this.bottom = t;
			return new ReturnValue<T>(State.EMPTY, null);
		}
		ReturnValue<T> o = new ReturnValue<T>(State.REGULAR, a.get(b));
		if (size > 0) {
			this.perhapsShrink(b, t);
			return o;
		}
		if (!this.casTop(t, t + 1)) {
			o = new ReturnValue<T>(State.EMPTY, null);
		}
		this.bottom = t + 1;
		return o;
	}

	void perhapsShrink(final long b, final long t) {
		long temp = t;
		final CircularArray<T> a = this.activeArray;
		if ((b - temp) < (a.size() / 4)) {
			final CircularArray<T> aa = a.shrink(b, temp);
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
	 *         <li><code>empty()</code> if the deque contains no elements,
	 *         <li><code>abort()</code> if the deque is currently being stolen by another thread,
	 *         <li><i>the oldest element</i> otherwise
	 *         </ul>
	 */
	public ReturnValue<T> steal() {
		final long t = this.top.get();
		final CircularArray<T> oldArr = this.activeArray;
		final long b = this.bottom;
		final CircularArray<T> a = this.activeArray;
		final long size = b - t;
		if (size <= 0) {
			return new ReturnValue<T>(State.EMPTY, null);
		}
		if ((size % a.size()) == 0) {
			if ((oldArr == a) && (t == this.top.get())) {
				return new ReturnValue<T>(State.EMPTY, null);
			} else {
				return new ReturnValue<T>(State.ABORT, null);
			}
		}
		final ReturnValue<T> o = new ReturnValue<T>(State.REGULAR, a.get(t));
		if (!this.casTop(t, t + 1)) {
			return new ReturnValue<T>(State.ABORT, null);
		}
		return o;
	}

	/**
	 * For debugging purposes
	 * 
	 * @return but does not remove the bottom element from this deque
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
