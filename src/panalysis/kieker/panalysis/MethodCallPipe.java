package kieker.panalysis;

import kieker.panalysis.base.Pipe;
import kieker.panalysis.base.Sink;
import kieker.panalysis.base.Source;
import kieker.panalysis.base.Stage;

public class MethodCallPipe<T> implements Pipe<T> {

	private final Stage targetStage;
	private T record;

	public MethodCallPipe(final Stage targetStage) {
		this.targetStage = targetStage;
	}

	public void put(final T record) {
		this.record = record;
		this.targetStage.execute();
	}

	public T take() {
		return this.record;
	}

	public T tryTake() {
		return this.record;
	}

	static public <OutputPort extends Enum<OutputPort>, InputPort extends Enum<InputPort>> void connect(final Source<OutputPort> sourceStage,
			final OutputPort sourcePort, final Sink<InputPort> targetStage, final InputPort targetPort) {
		final Pipe<Object> pipe = new MethodCallPipe<Object>(targetStage);
		sourceStage.setPipeForOutputPort(sourcePort, pipe);
		targetStage.setPipeForInputPort(targetPort, pipe);
	}

}
