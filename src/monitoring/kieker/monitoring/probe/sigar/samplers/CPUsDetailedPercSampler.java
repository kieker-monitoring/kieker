package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.CPUUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.timer.ITimeSource;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarProxy;

/**
 * Logs detailed utilization statistics for each CPU in the system, retrieved
 * from {@link CpuPerc}, as {@link CPUUtilizationRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public final class CPUsDetailedPercSampler extends AbstractSigarSampler {

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given
	 * {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method
	 * {@link kieker.monitoring.probe.sigar.SigarSamplerFactory#createSensorCPUsDetailedPerc()} to acquire an
	 * instance rather than calling this constructor directly.
	 * 
	 * @param sigar
	 */
	public CPUsDetailedPercSampler(final SigarProxy sigar) {
		super(sigar);
	}

	@Override
	public void sample(final IMonitoringController samplingController)
			throws Exception {
		final CpuPerc[] cpus = this.sigar.getCpuPercList();
		final ITimeSource timesource = samplingController.getTimeSource();
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			// final double combinedUtilization = curCPU.getCombined();
			final CPUUtilizationRecord r =
					new CPUUtilizationRecord(timesource.getTime(),
							samplingController.getHostName(),
							Integer.toString(i), curCPU.getUser(),
							curCPU.getSys(), curCPU.getWait(),
							curCPU.getNice(), curCPU.getIrq(),
							curCPU.getCombined(), curCPU.getIdle());
			samplingController.newMonitoringRecord(r);
			// CPUsDetailedPercSampler.log.info("Sigar utilization: " +
			// combinedUtilization + "; " + " Record: " + r);
		}
	}
}