package kieker.monitoring.probe.sigar.sensors;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.ITriggeredSensor;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Sigar;

/**
 * Eases the implementation of {@link ITriggeredSensor}s which collect
 * system-level sensor data via the {@link Sigar} API and store this data as
 * {@link IMonitoringRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractTriggeredSigarSensor implements ITriggeredSensor {

	private static final Log log = LogFactory
			.getLog(AbstractTriggeredSigarSensor.class);

	/**
	 * TODO: Make sure that {@link Sigar} is thread-safe! Otherwise we need to
	 * force synchronization.
	 */
	protected final Sigar sigar;

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private AbstractTriggeredSigarSensor() {
		this(null);
	}

	/**
	 * Constructs a new {@link AbstractTriggeredSigarSensor} with given
	 * {@link Sigar} instance used to retrieve the sensor data.
	 * 
	 * @param sigar
	 */
	public AbstractTriggeredSigarSensor(final Sigar sigar) {
		this.sigar = sigar;
	}
}