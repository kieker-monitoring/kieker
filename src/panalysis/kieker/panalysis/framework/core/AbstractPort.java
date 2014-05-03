package kieker.panalysis.framework.core;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
abstract class AbstractPort<S extends IStage, T> implements IPort<S, T> {

	private IPipe<T> associatedPipe;
	private S owningStage;
	private int index;

	public void setAssociatedPipe(final IPipe<T> associatedPipe) {
		this.associatedPipe = associatedPipe;
	}

	public IPipe<T> getAssociatedPipe() {
		return this.associatedPipe;
	}

	public S getOwningStage() {
		return this.owningStage;
	}

	public void setOwningStage(final S owningStage) {
		this.owningStage = owningStage;
	}

	public int getIndex() {
		return this.index;
	}

	void setIndex(final int index) {
		this.index = index;
	}
}
