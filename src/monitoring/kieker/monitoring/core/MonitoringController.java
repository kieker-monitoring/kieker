package kieker.monitoring.core;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.core.state.IMonitoringControllerState;
import kieker.monitoring.core.state.MonitoringControllerState;
import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;
import kieker.monitoring.writer.IMonitoringLogWriter;

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
 * 
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller
 * 
 */
public final class MonitoringController implements IMonitoringController, ISamplingController {
	private static final Log log = LogFactory.getLog(MonitoringController.class);

	/**
	 * Offset used to determine the number of nanoseconds since 1970-1-1. This
	 * is necessary since System.nanoTime() returns the elapsed nanoseconds
	 * since *some* fixed but arbitrary time.)
	 */
	private static final long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();

	/** The name of this controller instance */
	private final String instanceName; 
	/** The monitoring log writer used */
	private final IMonitoringLogWriter monitoringLogWriter; 
	/** Used to track the total number of monitoring records received while the controller has been enabled */
	private final AtomicLong numberOfInserts = new AtomicLong(0);
	private final ShutdownHook shutdownhook;
	/** Runtime state of the monitoring controller */
	private final IMonitoringControllerState state;
	/** Executes the {@link AbstractSigarSampler}s. */
	private final ScheduledThreadPoolExecutor periodicSensorsPoolExecutor;

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		private static final MonitoringController SINGLETON_INSTANCE = new MonitoringController(
				MonitoringConfiguration.createSingletonConfiguration());
	}
	public final static MonitoringController getInstance() {
		return LazyHolder.SINGLETON_INSTANCE;
	}
	@SuppressWarnings("unused")
	private MonitoringController() {
		this.instanceName = null;
		this.monitoringLogWriter = null;
		this.shutdownhook = null;
		this.state = null;
		this.periodicSensorsPoolExecutor = null;
	}
	
	/**
	 * Creates a configuration controller with the given name and configuration.
	 * 
	 * @param configuration
	 * @param instanceName
	 */
	//TODO: should be private?!?
	public MonitoringController(final IMonitoringConfiguration configuration) {
		this.state = new MonitoringControllerState(configuration);
		/* Cache value of writer for faster access */
		//TODO: not sure if this is really faster!!!
		this.monitoringLogWriter = this.state.getMonitoringLogWriter();
		this.instanceName = configuration.getName();
		this.shutdownhook = new ShutdownHook(this); // Dangerous! escaping this in constructor!
		this.periodicSensorsPoolExecutor = this.createPeriodicSensorsPoolExecutor(configuration);
		try {
			Runtime.getRuntime().addShutdownHook(this.shutdownhook);
		} catch (final Exception ex) {
			MonitoringController.log.warn("Failed to add shutdownHook", ex);
		}
		MonitoringController.log.info("Initialization completed.\n Writer Info: " + this.getConnectorInfo());
	}

	private final ScheduledThreadPoolExecutor createPeriodicSensorsPoolExecutor(final IMonitoringConfiguration configuration) {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
				configuration.getPeriodicSensorsExecutorPoolSize(),
				/*
				 * Handler for failed sensor executions that simply logs
				 * notifications.
				 */
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor) {
						MonitoringController.log.error("Exception caught by RejectedExecutionHandler for Runnable " + r
								+ " and ThreadPoolExecutor " + executor);

					}
				});
		executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		return executor;
	}

	/**
	 * Returns the timestamp for the current time. The value corresponds to the
	 * number of nanoseconds elapsed since January 1, 1970 UTC.
	 */
	public final static long currentTimeNanos() {
		return System.nanoTime() + MonitoringController.offsetA;
	}

	@Override
	public final boolean disableMonitoring() {
		return this.state.disableMonitoring();
	}

	@Override
	public final boolean enableMonitoring() {
		return this.state.enableMonitoring();
	}

	/**
	 * Enables the default mode that the logging timestamp of monitoring records
	 * received via {@link #newMonitoringRecord(IMonitoringRecord)} is set to
	 * the current timestamp.
	 * 
	 * @see IMonitoringRecord#getLoggingTimestamp()
	 * @see #enableReplayMode()
	 */
	public final void enableRealtimeMode() {
		this.state.enableRealtimeMode();
	}

	/**
	 * Enables the mode that the logging timestamp of monitoring records
	 * received via {@link #newMonitoringRecord(IMonitoringRecord)} is not set.
	 * This mode is, for example, helpful to replay recorded traces with their
	 * original timestamps.
	 * 
	 * @see IMonitoringRecord#getLoggingTimestamp()
	 * @see #enableRealtimeMode()
	 */
	public final void enableReplayMode() {
		this.state.enableReplayMode();
	}

	/**
	 * Returns a human-readable information string about the current controller
	 * configuration.
	 * 
	 * @return the information string
	 */
	public final String getConnectorInfo() {
		return this.state.stateInfo();
	}

	/**
	 * Returns a human-readable string with the current date and time.
	 * 
	 * @return the date/time string.
	 */
	public final static String getDateString() {
		return java.util.Calendar.getInstance().getTime().toString();
	}

	/**
	 * Returns the experiment ID.
	 * 
	 * @return
	 */
	public final int getExperimentId() {
		return this.state.getExperimentId();
	}

	/**
	 * The vmName which defaults to the hostname, and may be set by the
	 * control-servlet. The vmName will be part of the monitoring data and
	 * allows to assing observations in cases where the software system is
	 * deployed on more than one host.
	 * 
	 * When you want to distinguish multiple Virtual Machines on one host, you
	 * have to set the vmName manually (e.g., via the control-servlet, or by
	 * directly implementing a call to MonitoringController.setVmname(...).
	 */
	public final String getHostName() {
		return this.state.getHostName();
	}

	/**
	 * Returns the name of this controller.
	 * 
	 * @return
	 */
	public String getInstanceName() {
		return this.instanceName;
	}

	@Override
	public final IMonitoringLogWriter getMonitoringLogWriter() {
		return this.monitoringLogWriter;
	}

	/**
	 * Shows how many inserts have been performed since last restart of the
	 * execution environment.
	 */
	public final long getNumberOfInserts() {
		return this.numberOfInserts.longValue();
	}

	/**
	 * Return the version name of this controller instance.
	 * 
	 * @return the version name
	 */
	public final static String getVersion() {
		return Version.getVERSION();
	}

	/**
	 * Increments the experiment ID by 1 and returns the new value.
	 * 
	 */
	//TODO: why synchronized? Thread-safe -> every access has to be synchronized!! better volatile variable? (faster)
	public final synchronized int incExperimentId() {
		return this.state.incExperimentId();
	}

	@Override
	public final boolean isDebugEnabled() {
		return this.state.isDebugEnabled();
	}

	@Override
	public final boolean isMonitoringDisabled() {
		return this.state.isMonitoringDisabled();
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return this.state.isMonitoringEnabled();
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return this.state.isMonitoringTerminated();
	}

	/**
	 * Returns whether controller is in real-time mode state.
	 * .currentTimeNanos()
	 * 
	 * @see #enableRealtimeMode()
	 * @see #enableReplayMode()
	 * 
	 * @return true if the controller is in real-time mode, false otherwise
	 */
	public final boolean isRealtimeMode() {
		return this.state.isRealtimeMode();
	}

	/**
	 * Returns whether the controller is in replay mode state.
	 * 
	 * @see #enableRealtimeMode()
	 * @see #enableReplayMode()
	 * 
	 * @return true if the controller is in replay mode, false otherwise
	 */
	public final boolean isReplayMode() {
		return this.state.isReplayMode();
	}

	/**
	 * 
	 * If the controller is in replay mode (usually, this is only required to
	 * replay already recorded log data), the logMonitoringRecord method does
	 * not set the logging timestamp of the passed monitoring record.
	 * 
	 * @see IMonitoringController#newMonitoringRecord(IMonitoringRecord)
	 * 
	 */
	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			if (!this.isMonitoringEnabled()) {
				return false;
			}
			this.numberOfInserts.incrementAndGet();
			if (this.isRealtimeMode()) {
				record.setLoggingTimestamp(MonitoringController.currentTimeNanos());
			}
			if (!this.monitoringLogWriter.newMonitoringRecord(record)) {
				MonitoringController.log.fatal("Error writing the monitoring data. Will terminate monitoring!");
				this.terminateMonitoring();
				return false;
			}
			return true;
		} catch (final Exception ex) {
			MonitoringController.log.error("Caught an Exception. Will terminate monitoring", ex);
			this.terminateMonitoring();
			return false;
		}
	}

	@Override
	public final void setDebugEnabled(final boolean enableDebug) {
		this.state.setDebugEnabled(enableDebug);
	}

	/**
	 * Sets the experiment ID to the given value.
	 * 
	 * @param newExperimentID
	 */
	public final void setExperimentId(final int newExperimentID) {
		this.state.setExperimentId(newExperimentID);
	}

	/**
	 * Allows to set an own vmName, a field in the monitoring data to
	 * distinguish multiple hosts / vms in a system. This method is for instance
	 * used by the Kieker.Monitoring control servlet.
	 * 
	 * The vmName defaults to the hostname.
	 * 
	 * When you want to distinguish multiple Virtual Machines on one host, you
	 * have to set the vmName manually (e.g., via the control-servlet, or by
	 * directly implementing a call to MonitoringController.setVmname(...).
	 * 
	 * @param newHostName
	 */
	public final void setHostName(final String newHostName) {
		this.state.setHostName(newHostName);
	}

	/**
	 * log messages may not be visible during / after shutdown!
	 */
	@Override
	public final void terminateMonitoring() {
		//TODO: can't use Logger, may already have shutdown!
		MonitoringController.log.info("Monitoring controller (" + this.instanceName + ") terminates monitoring");
		// we should terminate first, so no new data will be collected!
		this.state.terminateMonitoring();
		if (this.periodicSensorsPoolExecutor != null) {
			this.periodicSensorsPoolExecutor.shutdown();
		}
		if (this.monitoringLogWriter != null) {
			monitoringLogWriter.terminate();
		}
		MonitoringController.log.info("Shutdown completed");
	}

	@Override
	public final synchronized ScheduledSamplerJob schedulePeriodicSampler(
			final ISampler sensor, final long initialDelay, final long period, final TimeUnit timeUnit) {
		if (this.periodicSensorsPoolExecutor.getCorePoolSize() < 1) {
			MonitoringController.log.warn("Won't schedule periodic sensor since core pool size <1: "
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
