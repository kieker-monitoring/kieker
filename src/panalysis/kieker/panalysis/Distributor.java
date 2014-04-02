package kieker.panalysis;

import kieker.panalysis.base.AbstractFilter;

public class Distributor extends AbstractFilter<Distributor.INPUT_PORT, Distributor.OUTPUT_PORT> {

	static public enum INPUT_PORT {
		OBJECT
	}

	static public enum OUTPUT_PORT {
		OUTPUT0, OUTPUT1
	}

	private final OUTPUT_PORT[] outputPorts;
	private int index = 0;

	public Distributor() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
		this.outputPorts = OUTPUT_PORT.values();
	}

	public void execute() {
		final Object object = this.take(INPUT_PORT.OBJECT);
		final OUTPUT_PORT port = this.getNextPortInRoundRobinOrder();
		this.put(port, object);
	}

	private OUTPUT_PORT getNextPortInRoundRobinOrder() {
		final OUTPUT_PORT port = this.outputPorts[this.index];
		this.index = (this.index + 1) % this.outputPorts.length;
		return port;
	}
}
