package kieker.panalysis.base;

abstract class AbstractPort<S extends IStage, T> implements IPort<T> {

	private IPipe<T, ?> associatedPipe;

	public void setAssociatedPipe(final IPipe<T, ?> associatedPipe) {
		this.associatedPipe = associatedPipe;
	}

	public IPipe<T, ?> getAssociatedPipe() {
		return this.associatedPipe;
	}
}
