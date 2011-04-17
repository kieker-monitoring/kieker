package kieker.monitoring.core;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;

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
 * @author Andre van Hoorn, Jan Waller
 */
public class SamplingController implements ISamplingController {
	private static final Log log = LogFactory.getLog(SamplingController.class);

	/** Executes the {@link AbstractSigarSampler}s. */
	private final ScheduledThreadPoolExecutor periodicSensorsPoolExecutor;

	private IMonitoringController monController;

	protected SamplingController(final int threadPoolSize) {
		// FIXME: caller should check in advance
		this.periodicSensorsPoolExecutor = new ScheduledThreadPoolExecutor(
				threadPoolSize,
				// Handler for failed sensor executions that simply logs notifications.
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
						SamplingController.log.error("Exception caught by RejectedExecutionHandler for Runnable " + r
								+ " and ThreadPoolExecutor " + executor);
					}
				});
		this.periodicSensorsPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		this.periodicSensorsPoolExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	@Override
	public boolean terminateMonitoring() {
		// TODO: Logger may be problematic, may already have shutdown!
		SamplingController.log.info("Shutting down Sampling Controller");
		if (this.periodicSensorsPoolExecutor != null) {
			this.periodicSensorsPoolExecutor.shutdown();
		}
		return true;
	}

	@Override
	public void getState(final StringBuilder sb) {
		if (this.periodicSensorsPoolExecutor != null) {
			sb.append("\nPeriodic Sensor available: Current Poolsize: '");
			sb.append(this.periodicSensorsPoolExecutor.getPoolSize());
			sb.append("'; Scheduled Tasks: '");
			sb.append(this.periodicSensorsPoolExecutor.getTaskCount());
			sb.append("'\n");
		} else {
			sb.append("\nNo periodic Sensor available\n");
		}
	}

	@Override
	public final synchronized ScheduledSamplerJob schedulePeriodicSampler(
			final ISampler sensor, final long initialDelay, final long period, final TimeUnit timeUnit) {
		if (this.periodicSensorsPoolExecutor.getCorePoolSize() < 1) {
			SamplingController.log.warn("Won't schedule periodic sensor since core pool size <1: "
					+ this.periodicSensorsPoolExecutor.getCorePoolSize());
			return null;
		}
		final ScheduledSamplerJob job = new ScheduledSamplerJob(this.monController, sensor);
		this.periodicSensorsPoolExecutor.scheduleAtFixedRate(job, initialDelay, period, timeUnit);
		return job;
	}

	@Override
	public final synchronized boolean removeScheduledSampler(final ScheduledSamplerJob sensorJob) {
		return this.periodicSensorsPoolExecutor.remove(sensorJob);
	}

	public IMonitoringController getMonController() {
		return this.monController;
	}

	public void setMonController(final IMonitoringController monController) {
		this.monController = monController;
	}

}
