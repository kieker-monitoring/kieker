package kieker.monitoring.probe.sigar.loggerJobs;

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
public class CPUsDetailedPercLogger extends AbstractSigarLogger {

	private static final Log log = LogFactory
			.getLog(CPUsDetailedPercLogger.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private CPUsDetailedPercLogger() {
		this(null, null);
	}

	public CPUsDetailedPercLogger(final Sigar sigar,
			final MonitoringController monitoringController) {
		super(sigar, monitoringController);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	private void logCPUsPerc(final org.hyperic.sigar.CpuPerc[] cpus) {
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			final CPUUtilizationRecord r =
					new CPUUtilizationRecord(
							MonitoringController.currentTimeNanos(),
							this.getHostname(),
							CPUsDetailedPercLogger.CPU_RESOURCE_NAME_PREFIX + i,
							curCPU.getUser(), curCPU.getSys(),
							curCPU.getWait(), curCPU.getNice(),
							curCPU.getIrq(), curCPU.getCombined(), curCPU
									.getIdle());
			this.logRecord(r);
		}
	}

	@Override
	protected void concreteRun() throws Exception {
		final org.hyperic.sigar.CpuPerc[] cpus =
				this.getSigar().getCpuPercList();
		this.logCPUsPerc(cpus);
	}
}