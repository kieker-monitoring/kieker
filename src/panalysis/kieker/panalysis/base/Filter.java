package kieker.panalysis.base;

import java.util.EnumMap;
import java.util.Map;

public abstract class Filter<InputPort extends Enum<InputPort>, OutputPort extends Enum<OutputPort>> extends AbstractStage implements Sink<InputPort>,
		Source<OutputPort> {

	private final Map<InputPort, Pipe<?>> inputPortPipes;
	private final Map<OutputPort, Pipe<Object>> outputPortPipes;

	public Filter(final long id, final Class<InputPort> inputEnumType, final Class<OutputPort> outputEnumType) {
		super(id);
		this.inputPortPipes = new EnumMap<InputPort, Pipe<?>>(inputEnumType);
		this.outputPortPipes = new EnumMap<OutputPort, Pipe<Object>>(outputEnumType);
	}

	public void setPipeForOutputPort(final OutputPort outputPort, final Pipe<Object> pipe) {
		this.outputPortPipes.put(outputPort, pipe);
	}

	protected void put(final OutputPort port, final Object record) {
		final Pipe<Object> pipe = this.outputPortPipes.get(port);
		if (pipe == null)
		{
			return; // ignore unconnected port
		}
		pipe.put(record);
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
