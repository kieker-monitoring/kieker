package kieker.panalysis.base;



public interface IInputPort<S extends IStage, T> extends IPort<S, T> {

	/**
	 * @since 1.10
	 */
	enum State {
		OPEN, CLOSING
	}

	public abstract State getState();

	public abstract void setState(final State state);
}
