package kieker.monitoring.probe.sigar;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.sigar.sensors.AbstractTriggeredSigarSensor;
import kieker.monitoring.probe.sigar.sensors.CPUsCombinedPercSensor;
import kieker.monitoring.probe.sigar.sensors.CPUsDetailedPercSensor;
import kieker.monitoring.probe.sigar.sensors.MemSwapUsageSensor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Sigar;

/**
 * Provides factory methods for {@link AbstractTriggeredSigarSensor}s.
 * 
 * @author Andre van Hoorn
 */
public class SigarTriggeredSensorFactory implements
		ISigarTriggeredSensorFactory {

	private static final Log log = LogFactory
			.getLog(SigarTriggeredSensorFactory.class);

	/**
	 * http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
	 * 
	 * @author Andre van Hoorn
	 * 
	 */
	private static class LazyHolder {
		/**
		 * The singleton instance of the {@link SigarTriggeredSensorFactory}
		 */
		private static final SigarTriggeredSensorFactory SINGLETON_INSTANCE =
				new SigarTriggeredSensorFactory();
	}

	/**
	 * Returns the singleton instance of the {@link SigarTriggeredSensorFactory}
	 * which uses the singleton instance of the {@link MonitoringController
	 * retrieved via {@link MonitoringController#getInstance()}.
	 * 
	 * The size of the internal thread pool used to serve the sensing and
	 * logging jobs is set to
	 * {@link SigarTriggeredSensorFactory#DEFAULT_EXECUTOR_THREAD_POOL_SIZE}.
	 */
	public static SigarTriggeredSensorFactory getInstance() {
		return LazyHolder.SINGLETON_INSTANCE;
	}

	/**
	 * {@link Sigar} instance used to retrieve the data to be logged.
	 */
	private final Sigar sigar;

	/**
	 * Used by {@link #getInstance()} to construct the singleton instance.
	 */
	private SigarTriggeredSensorFactory() {
		this(new Sigar());
	}

	/**
	 * Constructs a {@link SigarTriggeredSensorFactory} with the given
	 * parameters.
	 * 
	 * @param name
	 * @param monitoringController
	 */
	public SigarTriggeredSensorFactory(final Sigar sigar) {
		this.sigar = sigar;
	}

	@Override
	public CPUsCombinedPercSensor createSensorCPUsCombinedPerc() {
		final CPUsCombinedPercSensor l =
				new CPUsCombinedPercSensor(this.sigar);
		return l;
	}

	@Override
	public CPUsDetailedPercSensor createSensorCPUsDetailedPerc() {
		final CPUsDetailedPercSensor l =
				new CPUsDetailedPercSensor(this.sigar);
		return l;
	}

	@Override
	public MemSwapUsageSensor createSensorMemSwapUsage() {
		final MemSwapUsageSensor l =
				new MemSwapUsageSensor(this.sigar);
		return l;
	}
}