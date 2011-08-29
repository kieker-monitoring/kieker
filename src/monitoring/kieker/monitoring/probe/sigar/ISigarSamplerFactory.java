package kieker.monitoring.probe.sigar;

import kieker.monitoring.probe.sigar.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

/**
 * Defines the list of methods to be provided by a factory for {@link org.hyperic.sigar.Sigar}-based 
 * {@link kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler}s.
 * 
 * @author Andre van Hoorn
 * 
 */
public interface ISigarSamplerFactory {

	/**
	 * Creates an instance of {@link MemSwapUsageSampler}.
	 * 
	 * @return the created instance.
	 */
	public MemSwapUsageSampler createSensorMemSwapUsage();

	/**
	 * Creates an instance of {@link CPUsDetailedPercSampler}.
	 * 
	 * @return the created instance.
	 */
	public CPUsDetailedPercSampler createSensorCPUsDetailedPerc();

	/**
	 * Creates an instance of {@link CPUsCombinedPercSampler}.
	 * 
	 * @return the created instance.
	 */
	public CPUsCombinedPercSampler createSensorCPUsCombinedPerc();
}
