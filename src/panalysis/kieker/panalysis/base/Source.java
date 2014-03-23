package kieker.panalysis.base;

public interface Source<OutputPort extends Enum<OutputPort>> {

	void setPipeForOutputPort(final OutputPort outputPort, final Pipe<Object> pipe);
}
