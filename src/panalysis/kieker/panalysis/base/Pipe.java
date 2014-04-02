package kieker.panalysis.base;

public interface Pipe<T> {

	void put(T record);

	T take();

	/**
	 * 
	 * @return and removes the next record if the pipe is not empty, otherwise <code>null</code>
	 */
	T tryTake();

	/**
	 * 
	 * @return <code>true</code> if the pipe contains no element, otherwise <code>false</code>.<br>
	 *         <i>This method is used to find the next non-empty port of a stage with multiple ports.<i>
	 */
	boolean isEmpty();

}
