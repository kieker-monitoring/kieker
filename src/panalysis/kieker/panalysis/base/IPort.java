package kieker.panalysis.base;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 *            the type of elements this port accepts
 */
interface IPort<T> {

	public abstract IPipe<T, ?> getAssociatedPipe();

	public abstract void setAssociatedPipe(final IPipe<T, ?> associatedPipe);

}
