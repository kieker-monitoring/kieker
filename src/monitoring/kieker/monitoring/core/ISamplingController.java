package kieker.monitoring.core;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public interface ISamplingController {
	/**
	 * Schedules the given {@link ISampler} with given initial delay, and
	 * period.
	 * 
	 * @param sigarLogger
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler,
			final long initialDelay, final long period, final TimeUnit timeUnit);

	/**
	 * Stops future executions of the given periodic {@link ScheduledSamplerJob}
	 * .
	 * 
	 * @param sampler
	 * @return true if the sensor is not registered
	 */
	public boolean removeScheduledSampler(final ScheduledSamplerJob sampler);
}
