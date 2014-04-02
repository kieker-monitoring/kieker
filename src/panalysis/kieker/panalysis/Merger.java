package kieker.panalysis;

import kieker.panalysis.base.Filter;

public class Merger extends Filter<Merger.INPUT_PORT, Merger.OUTPUT_PORT> {

	static public enum INPUT_PORT {
		INPUT0, INPUT1
	}

	static public enum OUTPUT_PORT {
		OBJECT
	}

	private final INPUT_PORT[] inputPorts;
	private int index = 0;

	public Merger(final long id) {
		super(id, INPUT_PORT.class, OUTPUT_PORT.class);
		this.inputPorts = INPUT_PORT.values();
	}

	@Override
	public INPUT_PORT chooseInputPort() {
		return this.getNextPortInRoundRobinOrder();
	}

	public void execute(final INPUT_PORT inputPort) {
		final Object object = this.take(inputPort);
		this.put(OUTPUT_PORT.OBJECT, object);
	}

	private INPUT_PORT getNextPortInRoundRobinOrder() {
		INPUT_PORT port;
		do {
			port = this.inputPorts[this.index];
			this.index = (this.index + 1) % this.inputPorts.length;
		} while (this.isEmpty(port));
		return port;
	}

}
