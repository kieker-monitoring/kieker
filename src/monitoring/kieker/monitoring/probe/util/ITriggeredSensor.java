package kieker.monitoring.probe.util;

import kieker.monitoring.core.MonitoringController;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public interface ITriggeredSensor {

	/**
	 * Triggers a sensor to perform a measurement and pass the data to the given
	 * {@link MonitoringController}.
	 * 
	 * @throws Exception
	 *             thrown to indicate an error.
	 */
	public void senseAndLog(final MonitoringController monitoringCtrl)
			throws Exception;
}
