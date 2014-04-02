package kieker.panalysis.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class StealAwareQueue<T> extends ConcurrentLinkedQueue<T> {
	private static final long serialVersionUID = 4584858495723208972L;

	public StealAwareQueue<T> pollMultiple(int numItemsToPoll) {
		final StealAwareQueue<T> items = new StealAwareQueue<T>();

		// BETTER use a queue that is more efficient when taking multiple items at once
		while (numItemsToPoll-- > 0) {
			final T item = this.poll();
			if (item == null) {
				break;
			}
			items.add(item);
		}

		return items;
	}
}
