package kieker.panalysis.base;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractSource<OutputPort extends Enum<OutputPort>> extends AbstractStage implements Source<OutputPort> {

	private final Map<OutputPort, Pipe<Object>> outputPortPipes;

	public AbstractSource(final long id, final Class<OutputPort> enumType) {
		super(id);
		this.outputPortPipes = new EnumMap<OutputPort, Pipe<Object>>(enumType);
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

}
