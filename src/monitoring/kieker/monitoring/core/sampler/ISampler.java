package kieker.monitoring.core.sampler;

import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn
 */
public interface ISampler {

	/**
	 * Triggers this {@link ISampler} to perform a measurement and to pass the data
	 * to the given {@link IMonitoringController}.
	 *
	 * @throws Exception thrown to indicate an error.
	 */
	public void sample(final IMonitoringController monitoringController) throws Exception;
}
