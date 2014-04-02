package kieker.panalysis;

import kieker.panalysis.base.Filter;

public class Distributor extends Filter<Distributor.INPUT_PORT, Distributor.OUTPUT_PORT> {

	static public enum INPUT_PORT {
		OBJECT
	}

	static public enum OUTPUT_PORT {
		OUTPUT0, OUTPUT1
	}

	private final OUTPUT_PORT[] outputPorts;
	private int index = 0;

	public Distributor(final long id) {
		super(id, INPUT_PORT.class, OUTPUT_PORT.class);
		this.outputPorts = OUTPUT_PORT.values();
	}

	@Override
	public INPUT_PORT chooseInputPort() {
		return INPUT_PORT.OBJECT;
	}

	public void execute(final INPUT_PORT inputPort) {
		final Object object = this.take(inputPort);
		final OUTPUT_PORT port = this.getNextPortInRoundRobinOrder();
		this.put(port, object);
	}

	private OUTPUT_PORT getNextPortInRoundRobinOrder() {
		final OUTPUT_PORT port = this.outputPorts[this.index];
		this.index = (this.index + 1) % this.outputPorts.length;
		return port;
	}
}
