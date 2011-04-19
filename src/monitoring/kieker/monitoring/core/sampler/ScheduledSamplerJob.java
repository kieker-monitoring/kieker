package kieker.monitoring.core.sampler;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IWriterController;
import kieker.monitoring.core.controller.WriterController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public class ScheduledSamplerJob implements Runnable {
	private static final Log log = LogFactory.getLog(ScheduledSamplerJob.class);

	private final IWriterController writerController;
	private final ISampler sampler;

	/**
	 * Constructs a new {@link ScheduledSamplerJob} with the given parameters.
	 * 
	 * @param monitoringController
	 *          used to log the sampled data (represented as {@link IMonitoringRecord}s) via
	 *          {@link WriterController#newMonitoringRecord(IMonitoringRecord)}
	 * @param sampler
	 *          sampler to be trigger via {@link ISampler#sample(WriterController)}
	 */
	public ScheduledSamplerJob(final IWriterController writerController, final ISampler sensor) {
		this.writerController = writerController;
		this.sampler = sensor;
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException {
		try {
			this.sampler.sample(this.writerController);
		} catch (final Exception ex) {
			ScheduledSamplerJob.log.error("Exception occurred: ", ex);
			/* Re-throw exception */
			//todo: Why rethrow it?
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
}
