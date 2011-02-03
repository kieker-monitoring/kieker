package kieker.monitoring.core;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;
import kieker.monitoring.writer.IMonitoringWriter;

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
public final class MonitoringController implements IMonitoringController, IReplayController, ISamplingController {
	private static final Log log = LogFactory.getLog(MonitoringController.class);

	/**
	 * Offset used to determine the number of nanoseconds since 1970-1-1. This
	 * is necessary since System.nanoTime() returns the elapsed nanoseconds
	 * since *some* fixed but arbitrary time.)
	 */
	private static final long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();

	/** The name of this controller instance */
	private final String instanceName;
	/** Used to track the total number of monitoring records received while the controller has been enabled */
	private final AtomicLong numberOfInserts = new AtomicLong(0);
	/** Executes the {@link AbstractSigarSampler}s. */
	private final ScheduledThreadPoolExecutor periodicSensorsPoolExecutor;

	// Runtime state of the monitoring controller
	private final IMonitoringWriter monitoringLogWriter;
	private final AtomicInteger experimentId = new AtomicInteger(0);
	private volatile boolean monitoringTerminated = false;
	private volatile boolean monitoringRealtimeMode = true;
	private volatile boolean monitoringEnabled = false;
	private volatile boolean debugEnabled;
	private volatile String vmName;
	
	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		private static final MonitoringController SINGLETON_INSTANCE = 
			new MonitoringController(MonitoringConfiguration.createSingletonConfiguration());
	}
	public final static MonitoringController getInstance() {
		return LazyHolder.SINGLETON_INSTANCE;
	}

	/**
	 * Creates a configuration controller with the given name and configuration.
	 * 
	 * @param configuration
	 * @param instanceName
	 */
	// TODO: should be private?!? or at least somehow protected!
	public MonitoringController(final IMonitoringConfiguration configuration) {
		if (configuration == null) {
			// Should currently not happen!
			MonitoringController.log.error("Failed to create Monitoring Configuration");
			this.instanceName = "Error Creating Configuration";
			this.periodicSensorsPoolExecutor = null;
			this.monitoringLogWriter = null;
			terminateMonitoring();
			return;
		}
		this.instanceName = configuration.getName();
		this.vmName = configuration.getHostName();
		this.debugEnabled = configuration.isDebugEnabled();
		// initialize Writer
		monitoringLogWriter = configuration.getMonitoringLogWriter();
		if (monitoringLogWriter == null) {
			MonitoringController.log.error("Failed to create Writer");
			this.periodicSensorsPoolExecutor = null;
			terminateMonitoring();
			return;
		}
		monitoringLogWriter.start();
		this.periodicSensorsPoolExecutor = this.createPeriodicSensorsPoolExecutor(configuration);
		try {
			// Dangerous! escaping "this" in constructor!
			Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
		} catch (final Exception ex) {
			MonitoringController.log.warn("Failed to add shutdownHook", ex);
		}
		if (configuration.isMonitoringEnabled()) {
			enableMonitoring();
		} else {
			disableMonitoring();
		}
		MonitoringController.log.info("Initialization completed.\nWriter Info: " + this.getConnectorInfo());
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
			if (!isMonitoringEnabled()) { //enabled and not terminated
				return false;
			}
			numberOfInserts.incrementAndGet();
			if (monitoringRealtimeMode) {
				record.setLoggingTimestamp(MonitoringController.currentTimeNanos());
			}
			if (!monitoringLogWriter.newMonitoringRecord(record)) {
				MonitoringController.log.fatal("Error writing the monitoring data. Will terminate monitoring!");
				terminateMonitoring();
				return false;
			}
			return true;
	} catch (final Exception ex) {
			MonitoringController.log.fatal("Exception detected. Will terminate monitoring", ex);
			terminateMonitoring();
			return false;
		}
	}

	/**
	 * Returns the timestamp for the current time. The value corresponds to the
	 * number of nanoseconds elapsed since January 1, 1970 UTC.
	 */
	//TODO: should be moved somewhere else? Utility function! At least in an interface!
	public final static long currentTimeNanos() {
		return System.nanoTime() + MonitoringController.offsetA;
	}
	
	/**
	 * Returns a human-readable string with the current date and time.
	 * 
	 * @return the date/time string.
	 */
	//TODO: should be moved somewhere else? Utility function! At least in an interface!
	public final static String getDateString() {
		return java.util.Calendar.getInstance().getTime().toString();
	}
	
	@Override
	public final void terminateMonitoring() {
		// TODO: Logger is problematic, may already have shutdown!
		MonitoringController.log.info("Monitoring controller (" + this.instanceName + ") terminates monitoring");
		// we should terminate first, so no new data will be collected!
		monitoringTerminated = true;
		if (this.periodicSensorsPoolExecutor != null) {
			this.periodicSensorsPoolExecutor.shutdown();
		}
		if (this.monitoringLogWriter != null) {
			monitoringLogWriter.terminate();
		}
		MonitoringController.log.info("Shutdown completed");
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return monitoringTerminated;
	}

	@Override
	public final boolean enableMonitoring() {
		if (monitoringTerminated) {
			MonitoringController.log.error("Refused to enable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		MonitoringController.log.info("Enabling monitoring");
		monitoringEnabled = true;
		return true;
	}
	
	/**
	 * Careful!
	 * isMonitoringEnabled() != !isMonitoringDisabled()
	 */
	@Override
	public final boolean isMonitoringEnabled() {
		return !monitoringTerminated && monitoringEnabled;
	}
	
	@Override
	public final boolean disableMonitoring() {
		if (monitoringTerminated) {
			MonitoringController.log.error("Refused to disable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		MonitoringController.log.info("Disabling monitoring");
		monitoringEnabled = false;
		return true;
	}

	/**
	 * Careful!
	 * isMonitoringDisabled() != !isMonitoringEnabled()
	 */
	@Override
	public final boolean isMonitoringDisabled() {
		return !monitoringTerminated && !monitoringEnabled;
	}
	
	@Override
	public final void enableRealtimeMode() {
		monitoringRealtimeMode = true;
	}

	@Override
	public final boolean isRealtimeMode() {
		return monitoringRealtimeMode;
	}
	
	@Override
	public final void enableReplayMode() {
		monitoringRealtimeMode = false;
	}

	@Override
	public final boolean isReplayMode() {
		return !monitoringRealtimeMode;
	}
	
	@Override
	public final void setDebugEnabled(final boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	@Override
	public final boolean isDebugEnabled() {
		return debugEnabled;
	}

	@Override
	public final IMonitoringWriter getMonitoringLogWriter() {
		return monitoringLogWriter;
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
		MonitoringController.log.debug("The VM has the name " + newHostName + " Thread:" + Thread.currentThread().getId());
		vmName = newHostName;
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
		return vmName;
	}

	/**
	 * Increments the experiment ID by 1 and returns the new value.
	 * 
	 */
	public final int incExperimentId() {
		return experimentId.incrementAndGet();
	}

	/**
	 * Sets the experiment ID to the given value.
	 * 
	 * @param newExperimentID
	 */
	public final void setExperimentId(final int newExperimentID) {
		experimentId.set(newExperimentID);
	}
	
	/**
	 * Returns the experiment ID.
	 * 
	 * @return
	 */
	public final int getExperimentId() {
		return experimentId.get();
	}

	/**
	 * Returns a human-readable information string about the current controller
	 * configuration.
	 * 
	 * @return the information string
	 */
	public final String getConnectorInfo() {
		final StringBuilder strB = new StringBuilder();
		//TODO: careful: NULLpointer exception if no writer!
		strB.append("monitoringDataWriter : ");
		strB.append(monitoringLogWriter.getClass().getName());
		strB.append(", monitoringDataWriter config : (below), ");
		strB.append(monitoringLogWriter.getInfoString());
		strB.append(", version :");
		strB.append(Version.getVERSION());
		strB.append(", debug :");
		strB.append(isDebugEnabled());
		strB.append(", enabled :");
		strB.append(isMonitoringEnabled());
		strB.append(", terminated :");
		strB.append(isMonitoringTerminated());
		strB.append(", experimentID :");
		strB.append(getExperimentId());
		strB.append(", vmname :");
		strB.append(getHostName());
		return strB.toString();
	}

	/**
	 * Returns the name of this controller.
	 * 
	 * @return
	 */
	public final String getInstanceName() {
		return instanceName;
	}

	/**
	 * Shows how many inserts have been performed since last restart of the execution environment.
	 */
	public final long getNumberOfInserts() {
		return numberOfInserts.longValue();
	}

	/**
	 * Return the version name of this controller instance.
	 * 
	 * @return the version name
	 */
	public final static String getVersion() {
		return Version.getVERSION();
	}

	private final ScheduledThreadPoolExecutor createPeriodicSensorsPoolExecutor(final IMonitoringConfiguration configuration) {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(configuration.getPeriodicSensorsExecutorPoolSize(),
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
	
	@Override
	public final synchronized ScheduledSamplerJob schedulePeriodicSampler(final ISampler sensor, final long initialDelay,
			final long period, final TimeUnit timeUnit) {
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
