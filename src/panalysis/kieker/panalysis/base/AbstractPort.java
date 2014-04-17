package kieker.panalysis.base;

abstract class AbstractPort<S extends IStage, T> implements IPort<S, T> {

	private IPipe<T, ?> associatedPipe;
	private S owningStage;

	public void setAssociatedPipe(final IPipe<T, ?> associatedPipe) {
		this.associatedPipe = associatedPipe;
	}

	public IPipe<T, ?> getAssociatedPipe() {
		return this.associatedPipe;
	}

	public S getOwningStage() {
		return this.owningStage;
	}

	public void setOwningStage(final S owningStage) {
		this.owningStage = owningStage;
	}
}
