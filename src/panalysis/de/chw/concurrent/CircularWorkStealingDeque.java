package de.chw.concurrent;

//BETTER use a type parameter
public class CircularWorkStealingDeque {
	public final static Object Empty = new Object();
	public final static Object Abort = new Object();

	private final static long LogInitialSize = 10;
	private volatile long bottom = 0;
	private volatile long top = 0;
	private volatile CircularArray activeArray = new CircularArray(LogInitialSize);

	private boolean casTop(final long oldVal, final long newVal) {
		boolean preCond;
		synchronized (this) {
			preCond = (this.top == oldVal);
			if (preCond) {
				this.top = newVal;
			}
		}
		return preCond;
	}

	public void pushBottom(final Object o) {
		final long b = this.bottom;
		final long t = this.top;
		CircularArray a = this.activeArray;
		final long size = b - t;
		if (size > (a.size() - 1)) {
			a = a.grow(b, t);
			this.activeArray = a;
		}
		a.put(b, o);
		this.bottom = b + 1;
	}

	public Object popBottom() {
		long b = this.bottom;
		final CircularArray a = this.activeArray;
		b = b - 1;
		this.bottom = b;
		final long t = this.top;
		final long size = b - t;
		if (size < 0) {
			this.bottom = t;
			return Empty;
		}
		Object o = a.get(b);
		if (size > 0) {
			this.perhapsShrink(b, t);
			return o;
		}
		if (!this.casTop(t, t + 1)) {
			o = Empty;
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
			t = this.top;
			if (!this.casTop(t, t + ss))
			{
				this.bottom = b;
				// a.free();
			}
		}
	}

	public Object steal() {
		final long t = this.top;
		final CircularArray oldArr = this.activeArray;
		final long b = this.bottom;
		final CircularArray a = this.activeArray;
		final long size = b - t;
		if (size <= 0) {
			return Empty;
		}
		if ((size % a.size()) == 0) {
			if ((oldArr == a) && (t == this.top)) {
				return Empty;
			} else {
				return Abort;
			}
		}
		final Object o = a.get(t);
		if (!this.casTop(t, t + 1)) {
			return Abort;
		}
		return o;
	}
}
