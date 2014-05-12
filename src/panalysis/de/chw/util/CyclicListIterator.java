package de.chw.util;

import java.util.Iterator;
import java.util.List;

/**
 * This iterator infinitely iterates over a list and allows the list to be modified without throwing a <code>ConcurrentMOdificationException</code>.
 * 
 * @author Christian Wulf
 * 
 * @param <T>
 */
public class CyclicListIterator<T> implements Iterator<T> {

	private final List<T> list;
	// private Iterator<T> iterator;

	private int currentIndex = 0;

	public CyclicListIterator(final List<T> list) {
		this.list = list;
		// this.iterator = this.list.iterator();
	}

	public boolean hasNext() {
		return true;
	}

	public T next() {
		// if (!this.iterator.hasNext()) {
		// this.iterator = this.list.iterator();
		// }
		// return this.iterator.next();

		// the size of the list could have been changed due to
		// <li>an index overflow (then restart from index 0), or
		// <li>an add() or a remove(), so update the index
		this.currentIndex = this.getCurrentIndex();
		final T element = this.list.get(this.currentIndex);
		this.currentIndex++;
		return element;
	}

	public void remove() {
		// this.iterator.remove();
		this.currentIndex = this.getCurrentIndex();
		this.list.remove(this.currentIndex);
	}

	private int getCurrentIndex() {
		return this.currentIndex % this.list.size();
	}

}
