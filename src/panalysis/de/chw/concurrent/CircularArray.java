package de.chw.concurrent;

/**
 * 
 * @author Christian Wulf
 * @see "Dynamic Circular WorkStealing Deque"
 * 
 */
// BETTER use a type parameter
public class CircularArray {

	private final long logSize;
	private final Object[] segment;

	/**
	 * 
	 * @param log_size
	 *            The initial size of this array in log2, i.e., the number of bits to use
	 */
	public CircularArray(final long log_size) {
		this.logSize = log_size;
		this.segment = new Object[1 << this.logSize];
	}

	public long size() {
		return 1 << this.logSize;
	}

	public Object get(final long i) {
		return this.segment[(int) (i % this.size())]; // risk of overflow
	}

	public void put(final long i, final Object o) {
		this.segment[(int) (i % this.size())] = o; // risk of overflow
	}

	public CircularArray grow(final long b, final long t) {
		final CircularArray a = new CircularArray(this.logSize + 1);
		for (long i = t; i < b; i++) {
			a.put(i, this.get(i));
		}
		return a;
	}

	public CircularArray shrink(final long b, final long t) {
		final CircularArray a = new CircularArray(this.logSize - 1);
		for (long i = t; i < b; i++) {
			a.put(i, this.get(i));
		}
		return a;
	}
}
