package de.chw.concurrent;

/**
 * 
 * @author Christian Wulf
 * @see "Dynamic Circular WorkStealing Deque"
 * 
 */
// BETTER use a type parameter
public class CircularArray {

	private final long log_size;
	private final Object[] segment;

	public CircularArray(final long log_size) {
		this.log_size = log_size;
		this.segment = new Object[1 << this.log_size];
	}

	public long size() {
		return 1 << this.log_size;
	}

	public Object get(final long i) {
		return this.segment[(int) (i % this.size())]; // risk of overflow
	}

	public void put(final long i, final Object o) {
		this.segment[(int) (i % this.size())] = o; // risk of overflow
	}

	public CircularArray grow(final long b, final long t) {
		final CircularArray a = new CircularArray(this.log_size + 1);
		for (long i = t; i < b; i++) {
			a.put(i, this.get(i));
		}
		return a;
	}

	public CircularArray shrink(final long b, final long t) {
		final CircularArray a = new CircularArray(this.log_size - 1);
		for (long i = t; i < b; i++) {
			a.put(i, this.get(i));
		}
		return a;
	}
}
