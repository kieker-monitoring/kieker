package kieker.monitoring.core.sampler;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.WriterController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Andre van Hoorn
 */
public class ScheduledSamplerJob implements Runnable {
	private static final Log log = LogFactory.getLog(ScheduledSamplerJob.class);

	private final IMonitoringController monitoringController;
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
	public ScheduledSamplerJob(final IMonitoringController writerController, final ISampler sensor) {
		this.monitoringController = writerController;
		this.sampler = sensor;
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException {
		try {
			this.sampler.sample(this.monitoringController);
		} catch (final Exception ex) {
			ScheduledSamplerJob.log.error("Exception occurred: ", ex);
			/* Re-throw exception */
			//todo: Why rethrow it?
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
}
