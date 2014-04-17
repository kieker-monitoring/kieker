package kieker.panalysis.framework.concurrent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.panalysis.framework.core.AbstractPipe;

public class SingleProducerSingleConsumerPipe<T> extends AbstractPipe<T, SingleProducerSingleConsumerPipe<T>> {

	// BETTER use a cache-aware queue (see the corresponding paper)
	private final ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<T>();

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

	public void copyAllOtherPipes(final List<SingleProducerSingleConsumerPipe<T>> pipesOfGroup) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void putInternal(final T token) {
		this.queue.add(token);
	}

	@Override
	protected T tryTakeInternal() {
		return this.queue.poll();
	}

}
