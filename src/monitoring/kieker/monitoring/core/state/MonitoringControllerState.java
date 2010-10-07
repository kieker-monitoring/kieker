package kieker.monitoring.core.state;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import kieker.common.util.Version;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.writer.IMonitoringLogWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * Represents the runtime state of a {@link MonitoringController} instance.
 * 
 * @author Andre van Hoorn
 */
public final class MonitoringControllerState implements IMonitoringControllerState {
	private static final Log log = LogFactory
			.getLog(MonitoringControllerState.class);

	/**
	 * Instances must not be created using this constructor.
	 */
	@SuppressWarnings("unused")
	private MonitoringControllerState() {
		this.monitoringLogWriter = null;
	}

	/**
	 * Constructs an instance initialized based on the given
	 * {@link IMonitoringConfiguration}.
	 * 
	 * @param monitoringConfiguration
	 */
	public MonitoringControllerState(
			final IMonitoringConfiguration monitoringConfiguration) {
		this.monitoringLogWriter = monitoringConfiguration
				.getMonitoringLogWriter();
		this.setDebugEnabled(monitoringConfiguration.isDebugEnabled());
		if (monitoringConfiguration.isMonitoringEnabled()) {
			this.enableMonitoring();
		} else {
			this.disableMonitoring();
		}
		this.setHostName(monitoringConfiguration.getHostName());
		this.enableRealtimeMode();
	}

	private volatile ControllerMode controllerMode = ControllerMode.REALTIME;

	private enum ControllerState {

		ENABLED, DISABLED, TERMINATED;
	}

	/**
	 * @see IMonitoringController#isMonitoringEnabled()
	 * 
	 * @return
	 */
	@Override
	public boolean isMonitoringEnabled() {
		return this.controllerState.get() == ControllerState.ENABLED;
	}

	/**
	 * @see IMonitoringController#isMonitoringTerminated()
	 * 
	 * @return
	 */
	@Override
	public boolean isMonitoringTerminated() {
		return this.controllerState.get() == ControllerState.TERMINATED;
	}

	@Override
	public boolean isMonitoringDisabled() {
		return this.controllerState.get() == ControllerState.DISABLED;
	}

	private volatile boolean debugEnabled;

	/**
	 * Returns whether the debug mode is set to enabled or disabled.
	 * 
	 * @return true if debug is set to enabled, false otherwise
	 */
	@Override
	public boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	@Override
	public void enableReplayMode() {
		this.controllerMode = ControllerMode.REPLAY;
	}

	@Override
	public void enableRealtimeMode() {
		this.controllerMode = ControllerMode.REALTIME;
	}

	private final AtomicReference<ControllerState> controllerState = new AtomicReference<ControllerState>(
			ControllerState.ENABLED);

	public enum ControllerMode {

		/**
		 * The loggingTimestamp is not set by the newMonitoringRecord method.
		 * This is required to replay recorded traces with the original
		 * timestamps.
		 */
		REPLAY,
		/**
		 * The controller sets the loggingTimestamp of incoming records
		 * according to the current time.
		 */
		REALTIME
	}

	@Override
	public String stateInfo() {
		final StringBuilder strB = new StringBuilder();

		strB.append("monitoringDataWriter : "
				+ this.monitoringLogWriter.getClass().getName());
		strB.append(",");
		strB.append(" monitoringDataWriter config : (below), "
				+ this.monitoringLogWriter.getInfoString());
		strB.append(",");
		strB.append(" version :" + Version.getVERSION() + ", debug :"
				+ this.isDebugEnabled() + ", enabled :"
				+ this.isMonitoringEnabled() + ", terminated :"
				+ this.isMonitoringTerminated() + ", experimentID :"
				+ this.getExperimentId() + ", vmname :" + this.getHostName());

		return strB.toString();
	}

	@Override
	public boolean isReplayMode() {
		return this.controllerMode == ControllerMode.REPLAY;
	}

	@Override
	public boolean isRealtimeMode() {
		return this.controllerMode == ControllerMode.REALTIME;
	}

	/**
	 * The hostname
	 */
	private volatile String vmName = "unknown";

	@Override
	public String getHostName() {
		return this.vmName;
	}

	@Override
	public void setHostName(final String newHostName) {
		MonitoringControllerState.log.info("The VM has the NEW name "
				+ newHostName + " Thread:" + Thread.currentThread().getId());
		this.vmName = newHostName;
	}

	@Override
	public boolean enableMonitoring() {
		MonitoringControllerState.log.info("Enabling monitoring");
		/* Requires no synchronization since terminated is a final state */
		if (this.isMonitoringTerminated()) {
			MonitoringControllerState.log
					.error("Refused to enable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		this.controllerState.set(ControllerState.ENABLED);
		return true;
	}

	@Override
	public boolean disableMonitoring() {
		MonitoringControllerState.log.info("Disabling monitoring");
		/* Requires no synchronization since terminated is a final state */
		if (this.isMonitoringTerminated()) {
			MonitoringControllerState.log
					.error("Refused to disable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		this.controllerState.set(ControllerState.DISABLED);
		return true;
	}

	@Override
	public void terminateMonitoring() {
		MonitoringControllerState.log
				.info("Permanently terminating monitoring");
		this.controllerState.set(ControllerState.TERMINATED);
	}

	private final AtomicInteger experimentId = new AtomicInteger(0);

	@Override
	public int getExperimentId() {
		return this.experimentId.get();
	}

	@Override
	public int incExperimentId() {
		return this.experimentId.incrementAndGet();
	}

	@Override
	public void setExperimentId(final int newExperimentID) {
		this.experimentId.set(newExperimentID);
	}

	@Override
	public void setDebugEnabled(final boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	public final IMonitoringLogWriter monitoringLogWriter;

	@Override
	public IMonitoringLogWriter getMonitoringLogWriter() {
		return this.monitoringLogWriter;
	}
}
