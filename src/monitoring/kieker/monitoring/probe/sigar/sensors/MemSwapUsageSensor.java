package kieker.monitoring.probe.sigar.sensors;

import kieker.common.record.MemSwapUsageRecord;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;

/**
 * Logs memory and swap statistics retrieved from {@link Mem} and {@link Swap},
 * as {@link MemSwapUsageRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public class MemSwapUsageSensor extends AbstractTriggeredSigarSensor {

	private static final Log log = LogFactory.getLog(MemSwapUsageSensor.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private MemSwapUsageSensor() {
		this(null);
	}

	public MemSwapUsageSensor(final Sigar sigar) {
		super(sigar);
	}

	@Override
	public void senseAndLog(final MonitoringController monitoringController)
			throws Exception {
		final Mem mem = this.sigar.getMem();
		final Swap swap = this.sigar.getSwap();

		final MemSwapUsageRecord r =
				new MemSwapUsageRecord(MonitoringController.currentTimeNanos(),
						monitoringController.getHostName(), mem.getTotal(),
						mem.getActualUsed(), mem.getActualFree(),
						swap.getTotal(), swap.getUsed(), swap.getTotal());
		monitoringController.newMonitoringRecord(r);
	}
}