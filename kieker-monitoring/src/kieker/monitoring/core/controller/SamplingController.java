/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class SamplingController extends AbstractController implements ISamplingController {
	private static final Log LOG = LogFactory.getLog(SamplingController.class);

	/** Executes the {@link kieker.monitoring.sampler.sigar.samplers.AbstractSigarSampler}s. */
	final ScheduledThreadPoolExecutor periodicSensorsPoolExecutor; // NOPMD NOCS (package visible)

	/**
	 * Creates a new instance of this class using the given configuration to initialize the class.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this controller.
	 */
	protected SamplingController(final Configuration configuration) {
		super(configuration);
		final int threadPoolSize = configuration.getIntProperty(ConfigurationFactory.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE);
		if (threadPoolSize > 0) {
			this.periodicSensorsPoolExecutor = new ScheduledThreadPoolExecutor(threadPoolSize, new DaemonThreadFactory(), new RejectedExecutionHandler());
			// this.periodicSensorsPoolExecutor.setMaximumPoolSize(threadPoolSize); // not used in this class
			this.periodicSensorsPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
			this.periodicSensorsPoolExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		} else {
			this.periodicSensorsPoolExecutor = null; // NOPMD
		}
	}

	@Override
	protected final void init() {
		// do nothing
	}

	@Override
	protected final void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down Sampling Controller");
		}
		if (this.periodicSensorsPoolExecutor != null) {
			this.periodicSensorsPoolExecutor.shutdown();
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("Sampling Controller: ");
		if (this.periodicSensorsPoolExecutor != null) {
			sb.append("Periodic Sensor available: Poolsize: '");
			sb.append(this.periodicSensorsPoolExecutor.getPoolSize());
			sb.append("'; Scheduled Tasks: '");
			sb.append(this.periodicSensorsPoolExecutor.getTaskCount());
			sb.append('\''); // no \n in last controller
		} else {
			sb.append("No periodic Sensor available"); // no \n in last controller
		}
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ScheduledSamplerJob schedulePeriodicSampler(final ISampler sensor, final long initialDelay, final long period, final TimeUnit timeUnit) {
		if (null == this.periodicSensorsPoolExecutor) {
			LOG.warn("Won't schedule periodic sensor since Periodic Sampling is deactivated.");
			return null;
		}
		final ScheduledSamplerJob job = new ScheduledSamplerJob(super.monitoringController, sensor);
		// we need to keep the future for later cancellation/removal
		final ScheduledFuture<?> future = this.periodicSensorsPoolExecutor.scheduleAtFixedRate(job, initialDelay, period, timeUnit);
		job.setFuture(future);
		return job;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean removeScheduledSampler(final ScheduledSamplerJob sensorJob) {
		if (null == this.periodicSensorsPoolExecutor) {
			LOG.warn("Won't schedule periodic sensor since Periodic Sampling is deactivated.");
			return false;
		}
		final ScheduledFuture<?> future = sensorJob.getFuture();
		if (future != null) {
			future.cancel(false); // do not interrupt when running
		} else {
			LOG.warn("ScheduledFuture of ScheduledSamplerJob null: " + sensorJob);
		}
		final boolean success = this.periodicSensorsPoolExecutor.remove(sensorJob);
		this.periodicSensorsPoolExecutor.purge();
		return success;
	}

	/**
	 * @author Jan Waller
	 */
	private static final class RejectedExecutionHandler implements java.util.concurrent.RejectedExecutionHandler {
		private static final Log LOG = LogFactory.getLog(RejectedExecutionHandler.class);

		public RejectedExecutionHandler() {
			// empty default constructor
		}

		@Override
		public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
			LOG.error("Exception caught by RejectedExecutionHandler for Runnable " + r + " and ThreadPoolExecutor " + executor);
		}
	}

	/**
	 * A thread factory to create daemon threads.
	 * 
	 * @see java.util.concurrent.Executors.DefaultThreadFactory
	 * 
	 * @author Jan Waller
	 */
	private static final class DaemonThreadFactory implements ThreadFactory {
		private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

		public DaemonThreadFactory() {
			// empty default constructor
		}

		@Override
		public Thread newThread(final Runnable r) {
			final Thread t = this.defaultThreadFactory.newThread(r);
			t.setDaemon(true);
			return t;
		}
	}
}
