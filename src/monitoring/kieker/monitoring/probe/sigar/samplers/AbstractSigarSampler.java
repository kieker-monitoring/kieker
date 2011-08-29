package kieker.monitoring.probe.sigar.samplers;

import kieker.monitoring.core.sampler.ISampler;

import org.hyperic.sigar.SigarProxy;

/**
 * Eases the implementation of {@link ISampler}s which collect
 * system-level sensor data via the {@link org.hyperic.sigar.Sigar} API and store this data as {@link kieker.common.record.IMonitoringRecord}s via
 * {@link kieker.monitoring.core.controller.WriterController#newMonitoringRecord(kieker.common.record.kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractSigarSampler implements ISampler {
	protected final SigarProxy sigar;

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor
	 * data.
	 * 
	 * @param sigar
	 */
	public AbstractSigarSampler(final SigarProxy sigar) {
		this.sigar = sigar;
	}
}