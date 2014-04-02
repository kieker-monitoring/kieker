package kieker.panalysis;

import kieker.panalysis.base.Pipe;
import kieker.panalysis.base.Sink;
import kieker.panalysis.base.Source;
import kieker.panalysis.base.Stage;

public class MethodCallPipe<T, TargetInputPort extends Enum<TargetInputPort>> implements Pipe<T> {

	private final Stage<TargetInputPort> targetStage;
	private final TargetInputPort targetPort;
	private T record;

	public MethodCallPipe(final Stage<TargetInputPort> targetStage, final TargetInputPort targetPort) {
		this.targetStage = targetStage;
		this.targetPort = targetPort;
	}

	public void put(final T record) {
		this.record = record;
		this.targetStage.execute(this.targetPort);
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
		final Pipe<Object> pipe = new MethodCallPipe<Object, InputPort>(targetStage, targetPort);
		sourceStage.setPipeForOutputPort(sourcePort, pipe);
		targetStage.setPipeForInputPort(targetPort, pipe);
	}

}
