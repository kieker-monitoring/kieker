package kieker.monitoring.core;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.monitoring.core.configuration.Configuration;

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
 * @author Jan Waller
 */
public class MonitoringControllerState implements IMonitoringControllerState {
	private static final Log log = LogFactory
			.getLog(MonitoringControllerState.class);

	/** Whether monitoring is terminated */
	private final AtomicBoolean monitoringTerminated = new AtomicBoolean(false);
	/** The name of this controller instance */
	private final String name;
	/** The hostname of this controller instance */
	private final String hostname;
	/** The experimentId of this controller instance */
	private final AtomicInteger experimentId = new AtomicInteger(0);
	/** DebugMode */
	private volatile boolean debug = false;

	/** State of monitoring */
	private volatile boolean writingEnabled = false;

	protected MonitoringControllerState(final Configuration configuration) {
		this.name =
				configuration.getStringProperty(Configuration.CONTROLLER_NAME);
		String hostname =
				configuration.getStringProperty(Configuration.HOST_NAME);
		if (hostname.isEmpty()) {
			hostname = "<UNKNOWN>";
			try {
				hostname = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				MonitoringControllerState.log
						.warn("Failed to retrieve hostname");
			}
		}
		this.hostname = hostname;
		this.experimentId.set(configuration
				.getIntProperty(Configuration.EXPERIMENT_ID));
		this.debug = configuration.getBooleanProperty(Configuration.DEBUG);
		this.writingEnabled =
				configuration
						.getBooleanProperty(Configuration.MONITORING_ENABLED);
	}

	@Override
	public boolean terminateMonitoring() {
		if (!this.monitoringTerminated.getAndSet(true)) {
			MonitoringControllerState.log.info("Controller (" + this.name
					+ ") shutting down");
			return true;
		}
		return false;
	}

	@Override
	public boolean isMonitoringTerminated() {
		return this.monitoringTerminated.get();
	}

	@Override
	public final boolean enableMonitoring() {
		if (this.isMonitoringTerminated()) {
			MonitoringControllerState.log
					.error("Refused to enable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		MonitoringControllerState.log.info("Enabling monitoring");
		this.writingEnabled = true;
		return true;
	}

	/**
	 * Careful! isMonitoringEnabled() != !isMonitoringDisabled()
	 */
	@Override
	public final boolean isMonitoringEnabled() {
		return !this.isMonitoringTerminated() && this.writingEnabled;
	}

	@Override
	public final boolean disableMonitoring() {
		if (this.isMonitoringTerminated()) {
			MonitoringControllerState.log
					.error("Refused to disable monitoring because monitoring has been permanently terminated before");
			return false;
		}
		MonitoringControllerState.log.info("Disabling monitoring");
		this.writingEnabled = false;
		return true;
	}

	/**
	 * Careful! isMonitoringDisabled() != !isMonitoringEnabled()
	 */
	@Override
	public final boolean isMonitoringDisabled() {
		return !this.isMonitoringTerminated() && !this.writingEnabled;
	}

	@Override
	public final void setDebug(final boolean debug) {
		this.debug = debug;
	}

	@Override
	public final boolean isDebug() {
		return this.debug;
	}

	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public final String getHostName() {
		return this.hostname;
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
	public final int getExperimentId() {
		return this.experimentId.get();
	}

	@Override
	public void getState(final StringBuilder sb) {
		sb.append("Name: '");
		sb.append(this.name);
		sb.append("'; Hostname: '");
		sb.append(this.hostname);
		sb.append("'; Terminated: '");
		sb.append(this.isMonitoringTerminated());
		sb.append("'; Debug Mode: '");
		sb.append(this.debug);
		sb.append("'; experimentID: '");
		sb.append(this.getExperimentId());
		sb.append("'");
	}

}