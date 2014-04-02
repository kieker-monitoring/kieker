package kieker.panalysis.base;

public interface Sink<InputPort extends Enum<InputPort>> extends Stage<InputPort> {

	void setPipeForInputPort(final InputPort inputPort, final Pipe<Object> pipe);
}
