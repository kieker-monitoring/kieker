package kieker.panalysis.base;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractSink<InputPort extends Enum<InputPort>> extends AbstractStage implements Sink<InputPort> {

	private final Map<InputPort, Pipe<?>> inputPortPipes;

	public AbstractSink(final long id, final Class<InputPort> inputEnumType) {
		super(id);
		this.inputPortPipes = new EnumMap<InputPort, Pipe<?>>(inputEnumType);
	}

	public void setPipeForInputPort(final InputPort inputPort, final Pipe<Object> pipe) {
		this.inputPortPipes.put(inputPort, pipe);
	}

	protected Object take(final InputPort inputPort) {
		final Pipe<?> pipe = this.inputPortPipes.get(inputPort);
		return pipe.take();
	}

	protected Object tryTake(final InputPort inputPort) {
		final Pipe<?> pipe = this.inputPortPipes.get(inputPort);
		return pipe.tryTake();
	}
}
