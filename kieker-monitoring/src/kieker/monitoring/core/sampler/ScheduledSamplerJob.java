/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.core.sampler;

import java.util.concurrent.ScheduledFuture;

import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class ScheduledSamplerJob implements Runnable {
	// private static final Log LOG = LogFactory.getLog(ScheduledSamplerJob.class);

	private final IMonitoringController monitoringController;
	private final ISampler sampler;
	private volatile ScheduledFuture<?> future;

	/**
	 * Constructs a new {@link ScheduledSamplerJob} with the given parameters.
	 * 
	 * @param monitoringController
	 *            used to log the sampled data (represented as {@link kieker.common.record.IMonitoringRecord}s) via
	 *            {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}.
	 * @param sensor
	 *            sampler to be trigger via {@link kieker.monitoring.core.sampler.ISampler#sample(IMonitoringController)}
	 */
	public ScheduledSamplerJob(final IMonitoringController monitoringController, final ISampler sensor) {
		this.monitoringController = monitoringController;
		this.sampler = sensor;
	}

	/**
	 * Throws a {@link RuntimeException} if an error occurred.
	 */
	@Override
	public final void run() throws RuntimeException { // NOCS (IllegalThrowsCheck)
		try {
			this.sampler.sample(this.monitoringController);
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			// Re-throw exception because run must throw RuntimeException
			throw new RuntimeException(ex.getMessage(), ex); // NOPMD (RuntimeException)
		}
	}

	/**
	 * 
	 * @param future
	 *            The new future object.
	 */
	public void setFuture(final ScheduledFuture<?> future) {
		this.future = future;
	}

	/**
	 * 
	 * @return the {@link ScheduledFuture} which allows to cancel future
	 *         executions of this {@link ScheduledSamplerJob}.
	 */
	public ScheduledFuture<?> getFuture() {
		return this.future;
	}
}
