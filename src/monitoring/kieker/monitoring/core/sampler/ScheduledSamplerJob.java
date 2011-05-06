package kieker.monitoring.core.sampler;

import java.util.concurrent.ScheduledFuture;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn
 */
public class ScheduledSamplerJob implements Runnable {
	// private static final Log log =
	// LogFactory.getLog(ScheduledSamplerJob.class);

	private final IMonitoringController monitoringController;
	private final ISampler sampler;

	private volatile boolean isCancelled = false;

	/**
	 * Constructs a new {@link ScheduledSamplerJob} with the given parameters.
	 * 
	 * @param monitoringController
	 *            used to log the sampled data (represented as
	 *            {@link IMonitoringRecord}s) via
	 *            {@link IMonitoringController#newMonitoringRecord(IMonitoringRecord)}
	 * @param sampler
	 *            sampler to be trigger via
	 *            {@link ISampler#sample(IMonitoringController)}
	 */
	public ScheduledSamplerJob(
			final IMonitoringController monitoringController,
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
			if (this.isCancelled
					|| !this.monitoringController.isMonitoringEnabled()) {
				return;
			}
			this.sampler.sample(this.monitoringController);
		} catch (final Exception ex) {
			/* Re-throw exception because run must throw RuntimeException */
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	private volatile ScheduledFuture<?> future = null;
	
	/**
	 * 
	 * @param future
	 */
	public void setFuture (final ScheduledFuture<?> future) {
		this.future = future;
	}
	
	/**
	 * 
	 * @return
	 */
	public ScheduledFuture<?> getFuture () {
		return this.future;
	}
}
