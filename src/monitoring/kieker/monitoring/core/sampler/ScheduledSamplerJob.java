package kieker.monitoring.core.sampler;

import java.util.concurrent.ScheduledFuture;

import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn
 */
public class ScheduledSamplerJob implements Runnable {
	// private static final Log log =
	// LogFactory.getLog(ScheduledSamplerJob.class);

	private final IMonitoringController monitoringController;
	private final ISampler sampler;

	/**
	 * Constructs a new {@link ScheduledSamplerJob} with the given parameters.
	 * 
	 * @param monitoringController
	 *            used to log the sampled data (represented as
	 *            {@link kieker.common.record.IMonitoringRecord}s) via
	 *            {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}.
	 * @param sensor
	 *            sampler to be trigger via
	 *            {@link kieker.monitoring.core.sampler.ISampler#sample(IMonitoringController)}
	 */
	public ScheduledSamplerJob(final IMonitoringController monitoringController, final ISampler sensor) {
		this.monitoringController = monitoringController;
		this.sampler = sensor;
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException {
		try {
			if (!this.monitoringController.isMonitoringEnabled()) {
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
	public void setFuture(final ScheduledFuture<?> future) {
		this.future = future;
	}

	/**
	 * 
	 * @return the {@link ScheduledFuture} which allows to cancel future
	 *         executions of this {@link ScheduledSamplerJob}.
	 */
	public ScheduledFuture<?> getFuture() {
		return this.future;
	}
}
