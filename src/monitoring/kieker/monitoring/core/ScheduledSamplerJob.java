package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class ScheduledSamplerJob implements Runnable {
	private static final Log log = LogFactory
			.getLog(ScheduledSamplerJob.class);

	private final MonitoringController monitoringController;
	private final ISampler sampler;

	/**
	 * Constructs a new {@link ScheduledSamplerJob} with the given parameters.
	 * 
	 * @param monitoringController
	 *            used to log the sampled data (represented as
	 *            {@link IMonitoringRecord}s) via
	 *            {@link MonitoringController#newMonitoringRecord(IMonitoringRecord)}
	 * @param sampler
	 *            sampler to be trigger via
	 *            {@link ISampler#sample(MonitoringController)}
	 */
	public ScheduledSamplerJob(
			final MonitoringController monitoringController,
			final ISampler sensor) {
		this.monitoringController = monitoringController;
		this.sampler = sensor;
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException {
		try {
			this.sampler.sample(this.monitoringController);
		} catch (final Exception e) {
			final String errorMsg =
					"Exception occurred: "
							+ e.getMessage();
			ScheduledSamplerJob.log.error(errorMsg, e);
			/* Re-throw exception */
			throw new RuntimeException(errorMsg, e);
		}
	}
}
