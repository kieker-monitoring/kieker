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

	public void execute() {
		final INPUT_PORT port = this.inputPorts[this.index];
		final Object object = this.take(port);
		this.put(OUTPUT_PORT.OBJECT, object);
		this.index = (this.index + 1) % this.inputPorts.length;
	}

}
