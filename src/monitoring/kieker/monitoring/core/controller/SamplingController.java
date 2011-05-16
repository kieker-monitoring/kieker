package kieker.monitoring.core.controller;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class SamplingController extends AbstractController implements
		ISamplingController {
	private final static Log log = LogFactory.getLog(SamplingController.class);

	/** Executes the {@link AbstractSigarSampler}s. */
	private final ScheduledThreadPoolExecutor periodicSensorsPoolExecutor;

	protected SamplingController(final Configuration configuration) {
		final int threadPoolSize =
				configuration
						.getIntProperty(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE);
		// FIXME: caller should check in advance?
		this.periodicSensorsPoolExecutor =
				new ScheduledThreadPoolExecutor(threadPoolSize,
						// Handler for failed sensor executions that simply logs
						// notifications.
						new RejectedExecutionHandler() {
							@Override
							public void rejectedExecution(final Runnable r,
									final ThreadPoolExecutor executor) {
								SamplingController.log
										.error("Exception caught by RejectedExecutionHandler for Runnable "
												+ r
												+ " and ThreadPoolExecutor "
												+ executor);
							}
						});
		this.periodicSensorsPoolExecutor
				.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		this.periodicSensorsPoolExecutor
				.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	@Override
	protected final void cleanup() {
		SamplingController.log.debug("Shutting down Sampling Controller");
		if (this.periodicSensorsPoolExecutor != null) {
			this.periodicSensorsPoolExecutor.shutdown();
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Sampling Controller: ");
		if (this.periodicSensorsPoolExecutor != null) {
			sb.append("Periodic Sensor available: Current Poolsize: '");
			sb.append(this.periodicSensorsPoolExecutor.getPoolSize());
			sb.append("'; Scheduled Tasks: '");
			sb.append(this.periodicSensorsPoolExecutor.getTaskCount());
			sb.append("'");
		} else {
			sb.append("No periodic Sensor available");
		}
		return sb.toString();
	}

	@Override
	public final synchronized ScheduledSamplerJob schedulePeriodicSampler(
			final ISampler sensor, final long initialDelay, final long period,
			final TimeUnit timeUnit) {
		if (this.periodicSensorsPoolExecutor.getCorePoolSize() < 1) {
			SamplingController.log
					.warn("Won't schedule periodic sensor since core pool size <1: "
							+ this.periodicSensorsPoolExecutor
									.getCorePoolSize());
			return null;
		}
		// TODO: should we check for NullPointer at getMonitoringController() ??
		final ScheduledSamplerJob job =
				new ScheduledSamplerJob(super.monitoringController, sensor);
		// we need to keep the future for later cancellation/removal
		final ScheduledFuture<?> future =
				this.periodicSensorsPoolExecutor.scheduleAtFixedRate(job,
						initialDelay, period, timeUnit);
		job.setFuture(future);
		return job;
	}

	@Override
	public final synchronized boolean removeScheduledSampler(
			final ScheduledSamplerJob sensorJob) {
		final ScheduledFuture<?> future = sensorJob.getFuture();
		if (future != null) {
			future.cancel(false); // do not interrupt when running
		} else {
			SamplingController.log
					.warn("ScheduledFuture of ScheduledSamplerJob null: "
							+ sensorJob);
		}
		final boolean success =
				this.periodicSensorsPoolExecutor.remove(sensorJob);
		this.periodicSensorsPoolExecutor.purge();
		return success;
	}
}
