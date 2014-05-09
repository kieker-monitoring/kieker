package kieker.panalysis.framework.concurrent;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import de.chw.concurrent.CircularWorkStealingDeque;

import kieker.panalysis.framework.core.AbstractPipe;

public class SingleProducerSingleConsumerPipe<T> extends AbstractPipe<T> {

	// BETTER use a cache-aware queue (see the corresponding paper)
	private final BlockingQueue<T> queue = new LinkedBlockingDeque<T>();

	public void putMultiple(final List<T> elements) {
		this.queue.addAll(elements);
	}

	public T read() {
		return this.queue.peek();
	}

	public List<?> tryTakeMultiple(final int numElementsToTake) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void putInternal(final T token) {
		this.queue.add(token);
	}

	@Override
	protected T tryTakeInternal() {
		try {
			return this.queue.take();
		} catch (final InterruptedException e) {
			return null;
		}
	}

	public T take() {
		final T token = this.tryTake();
		if (token == null) {
			throw CircularWorkStealingDeque.DEQUE_IS_EMPTY_EXCEPTION;
		}
		return token;
	}
}
