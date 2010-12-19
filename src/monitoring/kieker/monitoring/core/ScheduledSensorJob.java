package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.probe.util.ITriggeredSensor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class ScheduledSensorJob implements Runnable {
	private static final Log log = LogFactory
			.getLog(ScheduledSensorJob.class);

	private final MonitoringController monitoringController;
	private final ITriggeredSensor sensor;

	/**
	 * Constructs a new {@link ScheduledSensorJob} with the given parameters.
	 * 
	 * @param monitoringController
	 *            used to log the sensed data (represented as
	 *            {@link IMonitoringRecord}s) via
	 *            {@link MonitoringController#newMonitoringRecord(IMonitoringRecord)}
	 * @param sensor
	 *            sensor to be trigger via
	 *            {@link ITriggeredSensor#senseAndLog(MonitoringController)}
	 */
	public ScheduledSensorJob(
			final MonitoringController monitoringController,
			final ITriggeredSensor sensor) {
		this.monitoringController = monitoringController;
		this.sensor = sensor;
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException {
		try {
			this.sensor.senseAndLog(this.monitoringController);
		} catch (final Exception e) {
			final String errorMsg =
					"Exception occurred: "
							+ e.getMessage();
			ScheduledSensorJob.log.error(errorMsg, e);
			/* Re-throw exception */
			throw new RuntimeException(errorMsg, e);
		}
	}
}
