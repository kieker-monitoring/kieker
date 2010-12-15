package kieker.monitoring.probe.sigar.loggerJobs;

import kieker.common.record.MemSwapUsageRecord;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
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
public class MemSwapUsageLogger extends AbstractSigarLogger {

	private static final Log log = LogFactory.getLog(MemSwapUsageLogger.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private MemSwapUsageLogger() {
		this(null, null);
	}

	public MemSwapUsageLogger(final Sigar sigar,
			final MonitoringController monitoringController) {
		super(sigar, monitoringController);
	}

	private void logMemSwapUsage(final org.hyperic.sigar.CpuPerc[] cpus)
			throws SigarException {
		final Mem mem = this.getSigar().getMem();
		final Swap swap = this.getSigar().getSwap();

		final MemSwapUsageRecord r =
				new MemSwapUsageRecord(MonitoringController.currentTimeNanos(),
						this.getHostname(), mem.getTotal(),
						mem.getActualUsed(), mem.getActualFree(),
						swap.getTotal(), swap.getUsed(), swap.getTotal());
		this.logRecord(r);
	}

	@Override
	protected void concreteRun() throws Exception {
		final org.hyperic.sigar.CpuPerc[] cpus =
				this.getSigar().getCpuPercList();
		this.logMemSwapUsage(cpus);
	}
}