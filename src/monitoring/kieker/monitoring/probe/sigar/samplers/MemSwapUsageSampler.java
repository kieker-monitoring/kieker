package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.MemSwapUsageRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.probe.sigar.SigarSamplerFactory;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Swap;

/**
 * Logs memory and swap statistics retrieved from {@link Mem} and {@link Swap},
 * as {@link MemSwapUsageRecord}s via
 * {@link WriterController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 * 
 * @author Andre van Hoorn
 * 
 */
public class MemSwapUsageSampler extends AbstractSigarSampler {


	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor
	 * data. Users should use the factory method {@link SigarSamplerFactory#createSensorMemSwapUsage()}
	 * to acquire an instance rather than calling this constructor directly.
	 * 
	 * @param sigar
	 */
	public MemSwapUsageSampler(final SigarProxy sigar) {
		super(sigar);
	}

	@Override
	public void sample(final IMonitoringController samplingController)
	throws Exception {
		final Mem mem = this.sigar.getMem();
		final Swap swap = this.sigar.getSwap();
		final MemSwapUsageRecord r =
			new MemSwapUsageRecord(samplingController.getTimeSource().getTime(),
					samplingController.getHostName(), mem.getTotal(),
					mem.getActualUsed(), mem.getActualFree(),
					swap.getTotal(), swap.getUsed(), swap.getTotal());
		samplingController.newMonitoringRecord(r);
	}
}