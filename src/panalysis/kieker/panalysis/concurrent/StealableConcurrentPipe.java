package kieker.panalysis.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kieker.panalysis.base.Pipe;

public class StealableConcurrentPipe<T> implements Pipe<T> {

	private final List<StealableConcurrentPipe<T>> otherPipes;
	/** FIXME must be thread-safe */
	private final Queue<T> items = new LinkedList<T>();

	private int numItemsToSteal;

	public StealableConcurrentPipe(final List<StealableConcurrentPipe<T>> otherPipes) {
		this.otherPipes = otherPipes;
	}

	public T take() {
		final T item = this.tryTake();
		if (item == null) {
			this.steal();
		}
		return this.tryTake();
	}

	public void put(final T record) {
		this.items.add(record);
	}

	public T tryTake() {
		return this.items.poll();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public List<T> tryTakeMultiple(int numItemsToTake) {
		final List<T> tookItems = new LinkedList<T>();
		while (numItemsToTake-- > 0) {
			final T item = this.tryTake();
			if (item == null) {
				break;
			}
			tookItems.add(item);
		}
		return tookItems;
	}

	public void putMultiple(final List<T> newItems) {
		this.items.addAll(newItems);
	}

	private void steal() {
		for (final StealableConcurrentPipe<T> otherPipe : this.otherPipes) {
			final List<T> stolenItems = otherPipe.tryTakeMultiple(this.numItemsToSteal);
			if (stolenItems != null) {
				this.items.addAll(stolenItems);
				return;
			}
		}
	}

}
