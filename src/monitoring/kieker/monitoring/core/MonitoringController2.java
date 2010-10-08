package kieker.monitoring.core;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.core.state.IMonitoringControllerState;
import kieker.monitoring.core.state.MonitoringControllerState;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;
import kieker.monitoring.writer.util.async.ShutdownHook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */

/**
 * Will become the new Monitoring Controller as soon as it is properly tested.
 * 
 * @author Andre van Hoorn, Matthias Rohr
 * 
 */
public final class MonitoringController2 implements IMonitoringController {

	/**
	 * Used to notify the writer threads that monitoring ended.
	 */
	public static final IMonitoringRecord END_OF_MONITORING_MARKER = MonitoringController.END_OF_MONITORING_MARKER;

	private static final Log log = LogFactory
			.getLog(MonitoringController2.class);

	/**
	 * Offset used to determine the number of nanoseconds since 1970-1-1. This
	 * is necessary since System.nanoTime() returns the elapsed nanoseconds
	 * since *some* fixed but arbitrary time.)
	 */
	private static final long offsetA = System.currentTimeMillis() * 1000000
			- System.nanoTime();

	/**
	 * The singleton instance of the monitoring controller
	 */
	private static final MonitoringController2 SINGLETON_INSTANCE = new MonitoringController2(
			MonitoringConfiguration.createSingletonConfiguration());

	/**
	 * Returns the singleton instance.
	 */
	public static MonitoringController2 getInstance() {
		// TODO: change to lazy initialization

		return MonitoringController2.SINGLETON_INSTANCE;
	}

	/**
	 * The name of this controller instance
	 */
	private final String instanceName;

	/**
	 * The monitoring log writer used
	 */
	private final IMonitoringLogWriter monitoringLogWriter;

	/**
	 * Used to track the total number of monitoring records received while the
	 * controller has been enabled
	 */
	private final AtomicLong numberOfInserts = new AtomicLong(0);

	private final ShutdownHook shutdownhook;

	/**
	 * Runtime state of the monitoring controller
	 */
	private final IMonitoringControllerState state;

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private MonitoringController2() {
		this.state = null;
		this.monitoringLogWriter = null;
		this.instanceName = null;
		this.shutdownhook = null;
	}

	/**
	 * Creates a configuration controller with the given name and configuration.
	 * 
	 * @param configuration
	 * @param instanceName
	 */
	public MonitoringController2(final IMonitoringConfiguration configuration) {
		this.state = new MonitoringControllerState(configuration);
		/* Cache value of writer for faster access */
		this.monitoringLogWriter = this.state.getMonitoringLogWriter();
		this.instanceName = configuration.getName();

		final Vector<AbstractWorkerThread> worker = this.monitoringLogWriter
				.getWorkers(); // may be null
		this.shutdownhook = new ShutdownHook(this);
		if (worker != null) {
			for (final AbstractWorkerThread w : worker) {
				this.registerWorker(w);
			}
		}
		try {
			Runtime.getRuntime().addShutdownHook(this.shutdownhook);
		} catch (final Exception e) {
			MonitoringController2.log.warn("Failed to add shutdownHook", e);
		}

		MonitoringController2.log
				.info("Initialization completed.\n Writer Info: "
						+ this.getConnectorInfo());
	}

	/**
	 * Returns the timestamp for the current time. The value corresponds to the
	 * number of nanoseconds elapsed since January 1, 1970 UTC.
	 */
	public final long currentTimeNanos() {
		return System.nanoTime() + MonitoringController2.offsetA;
	}

	@Override
	public boolean disableMonitoring() {
		return this.state.disableMonitoring();
	}

	@Override
	public boolean enableMonitoring() {
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
	public void enableRealtimeMode() {
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
	public void enableReplayMode() {
		this.state.enableReplayMode();
	}

	/**
	 * Returns a human-readable information string about the current controller
	 * configuration.
	 * 
	 * @return the information string
	 */
	public String getConnectorInfo() {
		return this.state.stateInfo();
	}

	/**
	 * Returns a human-readable string with the current date and time.
	 * 
	 * @return the date/time string.
	 */
	public String getDateString() {
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
	public IMonitoringLogWriter getMonitoringLogWriter() {
		return this.monitoringLogWriter;
	}

	/**
	 * Shows how many inserts have been performed since last restart of the
	 * execution environment.
	 */
	public long getNumberOfInserts() {
		return this.numberOfInserts.longValue();
	}

	/**
	 * Return the version name of this controller instance.
	 * 
	 * @return the version name
	 */
	public String getVersion() {
		return Version.getVERSION();
	}

	/**
	 * Increments the experiment ID by 1 and returns the new value.
	 * 
	 */
	public synchronized int incExperimentId() {
		return this.state.incExperimentId();
	}

	@Override
	public boolean isDebugEnabled() {
		return this.state.isDebugEnabled();
	}

	@Override
	public boolean isMonitoringDisabled() {
		return this.state.isMonitoringDisabled();
	}

	@Override
	public boolean isMonitoringEnabled() {
		return this.state.isMonitoringEnabled();
	}

	@Override
	public boolean isMonitoringTerminated() {
		return this.state.isMonitoringTerminated();
	}

	/**
	 * Returns whether controller is in real-time mode state.
	 * 
	 * @see #enableRealtimeMode()
	 * @see #enableReplayMode()
	 * 
	 * @return true if the controller is in real-time mode, false otherwise
	 */
	public boolean isRealtimeMode() {
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
	public boolean isReplayMode() {
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
				record.setLoggingTimestamp(this.currentTimeNanos());
			}
			if (!this.monitoringLogWriter.newMonitoringRecord(record)) {
				MonitoringController2.log
						.fatal("Error writing the monitoring data. Will terminate monitoring!");
				this.terminateMonitoring();
				return false;
			}
			return true;
		} catch (final Exception ex) {
			MonitoringController2.log.error(
					"Caught an Exception. Will terminate monitoring", ex);
			this.terminateMonitoring();
			return false;
		}
	}

	/**
	 * See TpmonShutdownHook.registerWorker
	 * 
	 * @param newWorker
	 */
	private void registerWorker(final AbstractWorkerThread newWorker) {
		this.shutdownhook.registerWorker(newWorker);
	}

	@Override
	public void setDebugEnabled(final boolean enableDebug) {
		this.state.setDebugEnabled(enableDebug);
	}

	/**
	 * Sets the experiment ID to the given value.
	 * 
	 * @param newExperimentID
	 */
	public void setExperimentId(final int newExperimentID) {
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

	@Override
	public void terminateMonitoring() {
		if (this.monitoringLogWriter != null) {
			/* if the initialization of the writer failed, it is set to null */
			if (!this.monitoringLogWriter
					.newMonitoringRecord(MonitoringController2.END_OF_MONITORING_MARKER)) {
				MonitoringController2.log.error("Failed to terminate writer");
			}
		}
		/* Must be set after the END_OF_MONITORING_MARKER was sent */
		this.state.terminateMonitoring();
	}
}
