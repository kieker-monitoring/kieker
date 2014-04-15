package de.chw.util;

import java.util.Iterator;

public class CyclicIterator<T> implements Iterator<T> {

	private final Iterable<T> iterable;
	private Iterator<T> iterator;

	public CyclicIterator(final Iterable<T> iterable) {
		this.iterable = iterable;
		this.iterator = iterable.iterator();
	}

	public boolean hasNext() {
		return true;
	}

	public T next() {
		if (!this.iterator.hasNext()) {
			this.iterator = this.iterable.iterator();
		}
		return this.iterator.next();
	}

	public void remove() {
		this.iterator.remove();
	}

}
