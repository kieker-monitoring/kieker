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

	IPipe<? super T> getAssociatedPipe();

	<A extends T> void setAssociatedPipe(final IPipe<? super T> associatedPipe);

	S getOwningStage();

	int getIndex();

}
