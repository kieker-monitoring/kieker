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
 * Represents the runtime state of a {@link MonitoringController} instance.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class MonitoringControllerState implements IMonitoringControllerState {
	private static final Log log = LogFactory.getLog(MonitoringControllerState.class);

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
	public MonitoringControllerState(final IMonitoringConfiguration monitoringConfiguration) {
		this.monitoringLogWriter = monitoringConfiguration.getMonitoringLogWriter();
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

	private static enum ControllerState { ENABLED, DISABLED, TERMINATED;	}

	/**
	 * @see IMonitoringController#isMonitoringEnabled()
	 * 
	 * @return
	 */
	@Override
	public final boolean isMonitoringEnabled() {
		return this.controllerState.get() == ControllerState.ENABLED;
	}

	/**
	 * @see IMonitoringController#isMonitoringTerminated()
	 * 
	 * @return
	 */
	@Override
	public final boolean isMonitoringTerminated() {
		return this.controllerState.get() == ControllerState.TERMINATED;
	}

	@Override
	public final boolean isMonitoringDisabled() {
		return this.controllerState.get() == ControllerState.DISABLED;
	}

	private volatile boolean debugEnabled;

	/**
	 * Returns whether the debug mode is set to enabled or disabled.
	 * 
	 * @return true if debug is set to enabled, false otherwise
	 */
	@Override
	public final boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	@Override
	public final void enableReplayMode() {
		this.controllerMode = ControllerMode.REPLAY;
	}

	@Override
	public final void enableRealtimeMode() {
		this.controllerMode = ControllerMode.REALTIME;
	}

	private final AtomicReference<ControllerState> controllerState = new AtomicReference<ControllerState>(ControllerState.ENABLED);

	public static enum ControllerMode {

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
	public final String stateInfo() {
		final StringBuilder strB = new StringBuilder();
		strB.append("monitoringDataWriter : ");
		strB.append(this.monitoringLogWriter.getClass().getName());
		strB.append(", monitoringDataWriter config : (below), ");
		strB.append(this.monitoringLogWriter.getInfoString());
		strB.append(", version :");
		strB.append(Version.getVERSION());
		strB.append(", debug :");
		strB.append(this.isDebugEnabled());
		strB.append(", enabled :");
		strB.append(this.isMonitoringEnabled());
		strB.append(", terminated :");
		strB.append(this.isMonitoringTerminated());
		strB.append(", experimentID :");
		strB.append(this.getExperimentId());
		strB.append(", vmname :");
		strB.append(this.getHostName());
		return strB.toString();
	}

	@Override
	public final boolean isReplayMode() {
		return this.controllerMode == ControllerMode.REPLAY;
	}

	@Override
	public final boolean isRealtimeMode() {
		return this.controllerMode == ControllerMode.REALTIME;
	}

	/**
	 * The hostname
	 */
	private volatile String vmName = "unknown";

	@Override
	public final String getHostName() {
		return this.vmName;
	}

	@Override
	public final void setHostName(final String newHostName) {
		MonitoringControllerState.log.debug("The VM has the name " + newHostName + " Thread:" + Thread.currentThread().getId());
		this.vmName = newHostName;
	}

	@Override
	public final boolean enableMonitoring() {
		MonitoringControllerState.log.info("Enabling monitoring");
		/* Requires no synchronization since terminated is a final state */
		if (this.isMonitoringTerminated()) {
			MonitoringControllerState.log.error("Refused to enable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		this.controllerState.set(ControllerState.ENABLED);
		return true;
	}

	@Override
	public final boolean disableMonitoring() {
		MonitoringControllerState.log.info("Disabling monitoring");
		/* Requires no synchronization since terminated is a final state */
		/* the comment is wrong? has nothing to do with final, but it uses an atomic reference, so it still works correctly */
		if (this.isMonitoringTerminated()) {
			MonitoringControllerState.log.error("Refused to disable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		this.controllerState.set(ControllerState.DISABLED);
		return true;
	}

	@Override
	public final void terminateMonitoring() {
		MonitoringControllerState.log.info("Permanently terminating monitoring");
		this.controllerState.set(ControllerState.TERMINATED);
	}

	private final AtomicInteger experimentId = new AtomicInteger(0);

	@Override
	public final int getExperimentId() {
		return this.experimentId.get();
	}

	@Override
	public final int incExperimentId() {
		return this.experimentId.incrementAndGet();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		this.experimentId.set(newExperimentID);
	}

	@Override
	public final void setDebugEnabled(final boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	public final IMonitoringLogWriter monitoringLogWriter;

	@Override
	public final IMonitoringLogWriter getMonitoringLogWriter() {
		return this.monitoringLogWriter;
	}
}
