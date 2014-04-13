package kieker.panalysis.base;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <S>
 * @param <T>
 *            the type of elements this port accepts
 */
interface IPort<S extends IStage, T> {

	public abstract <P extends IPipe<T, P>> P getAssociatedPipe();

	public abstract void setAssociatedPipe(final IPipe<T, ?> associatedPipe);

}
