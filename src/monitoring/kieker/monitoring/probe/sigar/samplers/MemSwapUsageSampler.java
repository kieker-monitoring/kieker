package kieker.monitoring.probe.sigar.samplers;

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
public class MemSwapUsageSampler extends AbstractSigarSampler {

	private static final Log log = LogFactory.getLog(MemSwapUsageSampler.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private MemSwapUsageSampler() {
		this(null);
	}

	public MemSwapUsageSampler(final Sigar sigar) {
		super(sigar);
	}

	@Override
	public void sample(final MonitoringController monitoringController)
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