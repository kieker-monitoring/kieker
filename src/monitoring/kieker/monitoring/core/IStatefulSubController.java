package kieker.monitoring.core;

interface IStatefulSubController {

	/**
	 * Internal API to terminate Monitoring
	 * 
	 * @return true on success
	 */
	abstract public boolean terminateMonitoring();

	/**
	 * Internal API to retrieve the current state's information as a
	 * <code>String</code>.
	 * 
	 * @param sb
	 *            the <code>StringBuilder</code> to append the result to.
	 *            Call by reference.
	 */
	abstract public void getState(StringBuilder sb);

}
