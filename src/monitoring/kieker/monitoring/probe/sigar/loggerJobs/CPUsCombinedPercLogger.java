package kieker.monitoring.probe.sigar.loggerJobs;

import kieker.common.record.ResourceUtilizationRecord;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

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
public class CPUsCombinedPercLogger extends AbstractSigarLogger {

	private static final Log log = LogFactory
			.getLog(CPUsCombinedPercLogger.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private CPUsCombinedPercLogger() {
		this(null, null);
	}

	public CPUsCombinedPercLogger(final Sigar sigar,
			final MonitoringController monitoringController) {
		super(sigar, monitoringController);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	private void logCPUsPerc(final org.hyperic.sigar.CpuPerc[] cpus) {
		for (int i = 0; i < cpus.length; i++) {
			final ResourceUtilizationRecord r =
					new ResourceUtilizationRecord(
							MonitoringController.currentTimeNanos(),
							this.getHostname(),
							CPUsCombinedPercLogger.CPU_RESOURCE_NAME_PREFIX + i,
							cpus[i].getCombined());
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