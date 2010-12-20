package kieker.monitoring.core;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public interface IPeriodicSensingController {
	/**
	 * Schedules the given {@link ITriggeredSensor} to be scheduled
	 * with the given initial delay, and period.
	 * 
	 * @param sigarLogger
	 * @param initialDelay
	 * @param period
	 * @param timeUnit
	 * @return
	 */
	public ScheduledSensorJob schedulePeriodicSensor(final ITriggeredSensor sensor,
			final long initialDelay, final long period, final TimeUnit timeUnit);

	/**
	 * Stops future executions of the given periodic sensor.
	 * 
	 * @param sensor
	 * @return true if the sensor is not registered
	 */
	public boolean removePeriodicSensor(final ScheduledSensorJob sensor);
}
