package kieker.panalysis.base;

import java.util.Map;

public interface Sink<InputPort extends Enum<InputPort>> extends Stage<InputPort> {

	void setPipeForInputPort(final InputPort inputPort, final Pipe<?> pipe);

	public Map<InputPort, Pipe<?>> getInputPortPipes();
}
