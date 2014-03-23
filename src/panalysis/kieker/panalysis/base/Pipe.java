package kieker.panalysis.base;

public interface Pipe<T> {

	void put(T record);

	T take();

	/**
	 * 
	 * @return and removes the next record if the pipe is not empty, otherwise <code>null</code>
	 */
	T tryTake();

}
