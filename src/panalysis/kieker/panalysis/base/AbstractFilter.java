package kieker.panalysis.base;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractFilter<InputPort extends Enum<InputPort>, OutputPort extends Enum<OutputPort>> extends AbstractStage<InputPort> implements
		Sink<InputPort>,
		Source<OutputPort> {

	private final Map<InputPort, Pipe<?>> inputPortPipes;
	private final Map<OutputPort, Pipe<Object>> outputPortPipes;
	private final Map<InputPort, Pipe<?>> readOnlyInputPortPipes;
	private TaskBundle taskBundle;
	private final int numTasksThreshold = 100;

	public AbstractFilter(final Class<InputPort> inputEnumType, final Class<OutputPort> outputEnumType) {
		this.inputPortPipes = new EnumMap<InputPort, Pipe<?>>(inputEnumType);
		this.outputPortPipes = new EnumMap<OutputPort, Pipe<Object>>(outputEnumType);
		this.readOnlyInputPortPipes = Collections.unmodifiableMap(this.inputPortPipes);
	}

	public void setPipeForOutputPort(final OutputPort outputPort, final Pipe<Object> pipe) {
		this.outputPortPipes.put(outputPort, pipe);
	}

	// protected void put(final OutputPort port, final Object record) {
	// if (this.taskBundle == null) {
	// this.taskBundle = new TaskBundle(this, new LinkedList<Object>());
	// }
	//
	// this.taskBundle.getTasks().add(record);
	//
	// if (this.taskBundle.getTasks().size() == this.numTasksThreshold) {
	// this.put(port, this.taskBundle);
	// this.taskBundle = null;
	// }
	// }

	protected void put(final OutputPort port, final Object record) {
		final Pipe<Object> pipe = this.outputPortPipes.get(port);
		if (pipe == null)
		{
			return; // ignore unconnected port
		}
		pipe.put(record);
	}

	public void setPipeForInputPort(final InputPort inputPort, final Pipe<?> pipe) {
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

	protected boolean isEmpty(final InputPort inputPort) {
		final Pipe<?> pipe = this.inputPortPipes.get(inputPort);
		return pipe.isEmpty();
	}

	public Map<InputPort, Pipe<?>> getInputPortPipes() {
		return this.readOnlyInputPortPipes;
	}
}
