package kieker.panalysis.base;

class Port {

	enum State {
		OPEN, CLOSING
	}

	private final IPipe pipe;
	private State state;

	public Port(final IPipe pipe) {
		this.pipe = pipe;
	}

	public IPipe getPipe() {
		return this.pipe;
	}

	public State getState() {
		return this.state;
	}

	public void setState(final State state) {
		this.state = state;
	}
}
