package kieker.monitoring.core;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public interface ISampler {

	/**
	 * Triggers this {@link ISampler} to perform a measurement and to pass the data
	 * to the given {@link MonitoringController}.
	 * 
	 * @throws Exception
	 *             thrown to indicate an error.
	 */
	public void sample(final MonitoringController monitoringCtrl)
			throws Exception;
}
