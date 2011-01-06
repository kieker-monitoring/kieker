package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.ISampler;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;

/**
 * Eases the implementation of {@link ISampler}s which collect
 * system-level sensor data via the {@link Sigar} API and store this data as
 * {@link IMonitoringRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractSigarSampler implements ISampler {

	private static final Log log = LogFactory
			.getLog(AbstractSigarSampler.class);

	protected final SigarProxy sigar;

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private AbstractSigarSampler() {
		this(null);
	}

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given
	 * {@link SigarProxy} instance used to retrieve the sensor data.
	 * 
	 * @param sigar
	 */
	public AbstractSigarSampler(final SigarProxy sigar) {
		this.sigar = sigar;
	}
}