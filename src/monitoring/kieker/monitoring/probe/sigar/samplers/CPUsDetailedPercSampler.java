package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.CPUUtilizationRecord;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

/**
 * Logs detailed utilization statistics for each CPU in the system, retrieved
 * from {@link CpuPerc}, as {@link CPUUtilizationRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public class CPUsDetailedPercSampler extends AbstractSigarSampler {

	private static final Log log = LogFactory
			.getLog(CPUsDetailedPercSampler.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private CPUsDetailedPercSampler() {
		this(null);
	}

	public CPUsDetailedPercSampler(final Sigar sigar) {
		super(sigar);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	@Override
	public void sample(final MonitoringController monitoringController)
			throws Exception {
		final org.hyperic.sigar.CpuPerc[] cpus =
				this.sigar.getCpuPercList();
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			final CPUUtilizationRecord r =
					new CPUUtilizationRecord(
							MonitoringController.currentTimeNanos(),
							monitoringController.getHostName(),
							CPUsDetailedPercSampler.CPU_RESOURCE_NAME_PREFIX + i,
							curCPU.getUser(), curCPU.getSys(),
							curCPU.getWait(), curCPU.getNice(),
							curCPU.getIrq(), curCPU.getCombined(), curCPU
									.getIdle());
			monitoringController.newMonitoringRecord(r);
		}
	}
}