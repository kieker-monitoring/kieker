package kieker.panalysis.framework.core;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 *            the type of elements this port accepts
 */
interface IPort<S extends IStage, T> {

	IPipe<T> getAssociatedPipe();

	void setAssociatedPipe(final IPipe<T> associatedPipe);

	S getOwningStage();

	int getIndex();

}
