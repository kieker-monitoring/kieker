package kieker.monitoring.probe.sigar;

import kieker.monitoring.probe.sigar.sensors.AbstractTriggeredSigarSensor;
import kieker.monitoring.probe.sigar.sensors.CPUsCombinedPercSensor;
import kieker.monitoring.probe.sigar.sensors.CPUsDetailedPercSensor;
import kieker.monitoring.probe.sigar.sensors.MemSwapUsageSensor;

import org.hyperic.sigar.Sigar;

/**
 * Defines the list of methods to be provided by a factory for {@link Sigar}
 * -based {@link AbstractTriggeredSigarSensor}s.
 * 
 * @author Andre van Hoorn
 * 
 */
public interface ISigarTriggeredSensorFactory {

	/**
	 * Creates an instance of {@link MemSwapUsageSensor}.
	 * 
	 * @return
	 */
	public MemSwapUsageSensor createSensorMemSwapUsage();

	/**
	 * Creates an instance of {@link CPUsDetailedPercSensor}.
	 * 
	 * @return
	 */
	public CPUsDetailedPercSensor createSensorCPUsDetailedPerc();

	/**
	 * Creates an instance of {@link CPUsCombinedPercSensor}.
	 * 
	 * @return
	 */
	public CPUsCombinedPercSensor createSensorCPUsCombinedPerc();
}
