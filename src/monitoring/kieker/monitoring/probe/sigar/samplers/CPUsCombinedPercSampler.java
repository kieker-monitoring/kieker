package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.ResourceUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.timer.ITimeSource;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarProxy;

/**
 * Logs the combined (i.e., User + Sys + Nice + Wait) cpu utilization for each
 * CPU in the system, retrieved via {@link CpuPerc#getCombined()}, as {@link ResourceUtilizationRecord}s via
 * {@link WriterController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 */
public class CPUsCombinedPercSampler extends AbstractSigarSampler {

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given
	 * {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method
	 * {@link SigarSamplerFactory#createSensorCPUsCombinedPerc()} to acquire an
	 * instance rather than calling this constructor directly.
	 * 
	 * @param sigar
	 */
	public CPUsCombinedPercSampler(final SigarProxy sigar) {
		super(sigar);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	@Override
	public void sample(final IMonitoringController samplingController) throws Exception {
		final CpuPerc[] cpus = this.sigar.getCpuPercList();
		final ITimeSource timesource = samplingController.getTimeSource();
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			final double combinedUtilization = curCPU.getCombined();
			final ResourceUtilizationRecord r = new ResourceUtilizationRecord(timesource.getTime(),
					samplingController.getHostName(), CPUsCombinedPercSampler.CPU_RESOURCE_NAME_PREFIX + i, combinedUtilization);
			samplingController.newMonitoringRecord(r);
			// CPUsCombinedPercSampler.log.info("Sigar utilization: " + combinedUtilization + "; " + " Record: " + r);
		}
	}
}