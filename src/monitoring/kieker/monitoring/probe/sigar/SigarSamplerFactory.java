package kieker.monitoring.probe.sigar;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Sigar;

/**
 * Provides factory methods for {@link AbstractSigarSampler}s.
 * 
 * @author Andre van Hoorn
 */
public class SigarSamplerFactory implements
		ISigarSamplerFactory {

	private static final Log log = LogFactory
			.getLog(SigarSamplerFactory.class);

	/**
	 * http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
	 * 
	 * @author Andre van Hoorn
	 * 
	 */
	private static class LazyHolder {
		/**
		 * The singleton instance of the {@link SigarSamplerFactory}
		 */
		private static final SigarSamplerFactory SINGLETON_INSTANCE =
				new SigarSamplerFactory();
	}

	/**
	 * Returns the singleton instance of the {@link SigarSamplerFactory}
	 * which uses the singleton instance of the {@link MonitoringController
	 * retrieved via {@link MonitoringController#getInstance()}.
	 * 
	 * The size of the internal thread pool used to serve the sensing and
	 * logging jobs is set to
	 * {@link SigarSamplerFactory#DEFAULT_EXECUTOR_THREAD_POOL_SIZE}.
	 */
	public static SigarSamplerFactory getInstance() {
		return LazyHolder.SINGLETON_INSTANCE;
	}

	/**
	 * {@link Sigar} instance used to retrieve the data to be logged.
	 */
	private final Sigar sigar;

	/**
	 * Used by {@link #getInstance()} to construct the singleton instance.
	 */
	private SigarSamplerFactory() {
		this(new Sigar());
	}

	/**
	 * Constructs a {@link SigarSamplerFactory} with the given
	 * parameters.
	 * 
	 * @param name
	 * @param monitoringController
	 */
	public SigarSamplerFactory(final Sigar sigar) {
		this.sigar = sigar;
	}

	@Override
	public CPUsCombinedPercSampler createSensorCPUsCombinedPerc() {
		final CPUsCombinedPercSampler l =
				new CPUsCombinedPercSampler(this.sigar);
		return l;
	}

	@Override
	public CPUsDetailedPercSampler createSensorCPUsDetailedPerc() {
		final CPUsDetailedPercSampler l =
				new CPUsDetailedPercSampler(this.sigar);
		return l;
	}

	@Override
	public MemSwapUsageSampler createSensorMemSwapUsage() {
		final MemSwapUsageSampler l =
				new MemSwapUsageSampler(this.sigar);
		return l;
	}
}