package de.chw.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author Christian Wulf
 * @see "Dynamic Circular WorkStealing Deque"
 * 
 */
// BETTER use a type parameter
public class CircularWorkStealingDeque {
	public static final Object EMPTY = new Object();
	public static final Object ABORT = new Object();

	private static final long LOG_INITIAL_SIZE = 10;

	private volatile long bottom = 0;
	// private volatile long top = 0;
	private final AtomicLong top = new AtomicLong();
	private volatile CircularArray activeArray = new CircularArray(LOG_INITIAL_SIZE);

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
		CircularArray a = this.activeArray;
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
	 */
	public Object popBottom() {
		long b = this.bottom;
		final CircularArray a = this.activeArray;
		b = b - 1;
		this.bottom = b;
		final long t = this.top.get();
		final long size = b - t;
		if (size < 0) {
			this.bottom = t;
			return EMPTY;
		}
		Object o = a.get(b);
		if (size > 0) {
			this.perhapsShrink(b, t);
			return o;
		}
		if (!this.casTop(t, t + 1)) {
			o = EMPTY;
		}
		this.bottom = t + 1;
		return o;
	}

	void perhapsShrink(final long b, long t) {
		final CircularArray a = this.activeArray;
		if ((b - t) < (a.size() / 4)) {
			final CircularArray aa = a.shrink(b, t);
			this.activeArray = aa;
			final long ss = aa.size();
			this.bottom = b + ss;
			t = this.top.get();
			if (!this.casTop(t, t + ss)) {
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
	 */
	public Object steal() {
		final long t = this.top.get();
		final CircularArray oldArr = this.activeArray;
		final long b = this.bottom;
		final CircularArray a = this.activeArray;
		final long size = b - t;
		if (size <= 0) {
			return EMPTY;
		}
		if ((size % a.size()) == 0) {
			if ((oldArr == a) && (t == this.top.get())) {
				return EMPTY;
			} else {
				return ABORT;
			}
		}
		final Object o = a.get(t);
		if (!this.casTop(t, t + 1)) {
			return ABORT;
		}
		return o;
	}
}
