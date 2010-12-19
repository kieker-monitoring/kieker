package kieker.monitoring.probe.sigar.sensors;

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
public class CPUsCombinedPercSensor extends AbstractTriggeredSigarSensor {

	private static final Log log = LogFactory
			.getLog(CPUsCombinedPercSensor.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private CPUsCombinedPercSensor() {
		this(null);
	}

	public CPUsCombinedPercSensor(final Sigar sigar) {
		super(sigar);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	@Override
	public void senseAndLog(final MonitoringController monitoringController)
			throws Exception {
		final org.hyperic.sigar.CpuPerc[] cpus =
				this.sigar.getCpuPercList();
		for (int i = 0; i < cpus.length; i++) {
			final ResourceUtilizationRecord r =
					new ResourceUtilizationRecord(
							MonitoringController.currentTimeNanos(),
							monitoringController.getHostName(),
							CPUsCombinedPercSensor.CPU_RESOURCE_NAME_PREFIX + i,
							cpus[i].getCombined());
			monitoringController.newMonitoringRecord(r);
		}
	}
}