package kieker.panalysis;

import java.util.List;

import kieker.panalysis.base.Pipe;
import kieker.panalysis.base.Sink;
import kieker.panalysis.base.Source;
import kieker.panalysis.base.Stage;

public class MethodCallPipe<T> implements Pipe<T> {

	private final Stage<?> targetStage;
	private T record;

	public MethodCallPipe(final Stage<?> targetStage) {
		this.targetStage = targetStage;
	}

	public void put(final T record) {
		this.record = record;
		this.targetStage.execute();
	}

	public T take() {
		final T temp = this.record;
		this.record = null;
		return temp;
	}

	public T tryTake() {
		return this.take();
	}

	public boolean isEmpty() {
		return this.record == null;
	}

	static public <OutputPort extends Enum<OutputPort>, InputPort extends Enum<InputPort>> void connect(final Source<OutputPort> sourceStage,
			final OutputPort sourcePort, final Sink<InputPort> targetStage, final InputPort targetPort) {
		final Pipe<Object> pipe = new MethodCallPipe<Object>(targetStage);
		sourceStage.setPipeForOutputPort(sourcePort, pipe);
		targetStage.setPipeForInputPort(targetPort, pipe);
	}

	public List<T> tryTakeMultiple(final int numItemsToTake) {
		throw new IllegalStateException("Taking more than one element is not possible. You tried to take " + numItemsToTake + " items.");
	}

	public void putMultiple(final List<T> items) {
		throw new IllegalStateException("Putting more than one element is not possible. You tried to put " + items.size() + " items.");
	}

}
