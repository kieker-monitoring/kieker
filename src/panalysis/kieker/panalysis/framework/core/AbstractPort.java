package kieker.panalysis.framework.core;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
abstract class AbstractPort<S extends IStage, T> implements IPort<S, T> {

	private IPipe<? super T> associatedPipe;
	private S owningStage;
	private int index;

	public <A extends T> void setAssociatedPipe(final IPipe<? super T> associatedPipe) {
		this.associatedPipe = associatedPipe;
	}

	public IPipe<? super T> getAssociatedPipe() {
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
