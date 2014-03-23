package kieker.panalysis;

import java.util.concurrent.LinkedBlockingQueue;

import kieker.panalysis.base.Pipe;
import kieker.panalysis.base.Sink;
import kieker.panalysis.base.Source;

public class QueuePipe<T> extends LinkedBlockingQueue<T> implements Pipe<T> {

	private static final long serialVersionUID = 1L;

	public QueuePipe(final int capacity) {
		super(capacity);
	}

	@Override
	public void put(final T record) {
		try {
			super.put(record);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public T take() {
		try {
			return super.take();
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public T tryTake() {
		return super.poll();
	}

	static public <OutputPort extends Enum<OutputPort>, InputPort extends Enum<InputPort>> void connect(final Source<OutputPort> sourceStage,
			final OutputPort sourcePort, final Sink<InputPort> targetStage, final InputPort targetPort) {
		final Pipe<Object> pipe = new QueuePipe<Object>(Integer.MAX_VALUE);
		sourceStage.setPipeForOutputPort(sourcePort, pipe);
		targetStage.setPipeForInputPort(targetPort, pipe);
	}

}
