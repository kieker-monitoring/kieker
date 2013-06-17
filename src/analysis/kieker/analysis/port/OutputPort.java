package kieker.analysis.port;

import java.util.Collection;

public final class OutputPort {

	private final Class<?>[] eventTypes;
	private Collection<InputPort> subscribers;

	public OutputPort(final Class<?>[] eventTypes) {
		this.eventTypes = eventTypes;
	}

	public void connect(final InputPort inputPort) {
		// Check types etc.

		this.subscribers.add(inputPort);
	}

	public void deliver(final Object data) {
		for (final InputPort subscriber : this.subscribers) {
			subscriber.newData(data);
		}
	}

}
