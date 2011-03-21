package kieker.monitoring.core;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;

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
abstract class SamplingController extends WriterController implements ISamplingController {
	private static final Log log = LogFactory.getLog(SamplingController.class);

	/** Executes the {@link AbstractSigarSampler}s. */
	private final ScheduledThreadPoolExecutor periodicSensorsPoolExecutor;
	
	protected SamplingController(final Configuration configuration) {
		super(configuration);
		if (isMonitoringTerminated()) {
			this.periodicSensorsPoolExecutor = null;
			return;
		}
		periodicSensorsPoolExecutor = new ScheduledThreadPoolExecutor(
				configuration.getIntProperty(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE),
					 // Handler for failed sensor executions that simply logs notifications.
					new RejectedExecutionHandler() {
						@Override
						public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
							SamplingController.log.error("Exception caught by RejectedExecutionHandler for Runnable " + r
									+ " and ThreadPoolExecutor " + executor);
						}
					});
		periodicSensorsPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		periodicSensorsPoolExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	@Override
	public boolean terminateMonitoring() {
		if (super.terminateMonitoring()) {
			// TODO: Logger may be problematic, may already have shutdown!
			SamplingController.log.info("Shutting down Sampling Controller");
			if (this.periodicSensorsPoolExecutor != null) {
				this.periodicSensorsPoolExecutor.shutdown();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String getState() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getState().trim());
		if (periodicSensorsPoolExecutor != null) {
			sb.append("\nPeriodic Sensor available: Current Poolsize: '");
			sb.append(periodicSensorsPoolExecutor.getPoolSize());
			sb.append("'; Scheduled Tasks: '");
			sb.append(periodicSensorsPoolExecutor.getTaskCount());
			sb.append("'\n");
		} else {
			sb.append("\nNo periodic Sensor available\n");
		}
		return sb.toString();
	}
	
	@Override
	public final synchronized ScheduledSamplerJob schedulePeriodicSampler(
			final ISampler sensor, final long initialDelay, final long period, final TimeUnit timeUnit) {
		if (periodicSensorsPoolExecutor.getCorePoolSize() < 1) {
			SamplingController.log.warn("Won't schedule periodic sensor since core pool size <1: "
					+ this.periodicSensorsPoolExecutor.getCorePoolSize());
			return null;
		}
		final ScheduledSamplerJob job = new ScheduledSamplerJob(this, sensor);
		this.periodicSensorsPoolExecutor.scheduleAtFixedRate(job, initialDelay, period, timeUnit);
		return job;
	}

	@Override
	public final synchronized boolean removeScheduledSampler(final ScheduledSamplerJob sensorJob) {
		return this.periodicSensorsPoolExecutor.remove(sensorJob);
	}
}
