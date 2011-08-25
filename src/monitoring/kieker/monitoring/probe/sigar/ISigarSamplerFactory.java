package kieker.monitoring.probe.sigar;

import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

import org.hyperic.sigar.Sigar;

/**
 * Defines the list of methods to be provided by a factory for {@link Sigar}-based 
 * {@link AbstractSigarSampler}s.
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
