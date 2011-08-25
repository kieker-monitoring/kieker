package kieker.monitoring.core.controller;

import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public interface ISamplingController {

	/**
	 * Schedules the given {@link ISampler} with given initial delay, and period.
	 * 
	 * @param sampler
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public abstract ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay, final long period, final TimeUnit timeUnit);

	/**
	 * Stops future executions of the given periodic {@link ScheduledSamplerJob}.
	 * 
	 * @param sampler
	 * @return true if the sensor is not registered
	 */
	public abstract boolean removeScheduledSampler(final ScheduledSamplerJob sampler);

}
