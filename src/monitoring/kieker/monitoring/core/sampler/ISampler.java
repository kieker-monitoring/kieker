package kieker.monitoring.core.sampler;

import kieker.monitoring.core.controller.IWriterController;
import kieker.monitoring.core.controller.SamplingController;

/**
 * @author Andre van Hoorn
 */
public interface ISampler {

	/**
	 * Triggers this {@link ISampler} to perform a measurement and to pass the data
	 * to the given {@link SamplingController}.
	 *
	 * @throws Exception thrown to indicate an error.
	 */
	public void sample(final IWriterController writerController) throws Exception;
}
