package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.ResourceUtilizationRecord;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarProxy;

/**
 * Logs the combined (i.e., User + Sys + Nice + Wait) cpu utilization for each
 * CPU in the system, retrieved via {@link CpuPerc#getCombined()}, as
 * {@link ResourceUtilizationRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public class CPUsCombinedPercSampler extends AbstractSigarSampler {

	private static final Log log = LogFactory
			.getLog(CPUsCombinedPercSampler.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private CPUsCombinedPercSampler() {
		this(null);
	}

	public CPUsCombinedPercSampler(final SigarProxy sigar) {
		super(sigar);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	@Override
	public void sample(final MonitoringController monitoringController)
			throws Exception {
		final org.hyperic.sigar.CpuPerc[] cpus = this.sigar.getCpuPercList();
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			final double combinedUtilization = curCPU.getCombined();
			final ResourceUtilizationRecord r =
					new ResourceUtilizationRecord(
							MonitoringController.currentTimeNanos(),
							monitoringController.getHostName(),
							CPUsCombinedPercSampler.CPU_RESOURCE_NAME_PREFIX
									+ i, combinedUtilization);
			monitoringController.newMonitoringRecord(r);
			CPUsCombinedPercSampler.log.info("Sigar utilization: "
					+ combinedUtilization + "; " + " Record: " + r);
		}
	}
}