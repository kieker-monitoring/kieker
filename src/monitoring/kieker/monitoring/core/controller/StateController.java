/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.Configuration;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class StateController extends AbstractController implements IStateController {
	private static final Log LOG = LogFactory.getLog(StateController.class);

	private volatile boolean monitoringEnabled = false;
	private final String name;
	private final String hostname;
	private final AtomicInteger experimentId = new AtomicInteger(0);

	protected StateController(final Configuration configuration) {
		this.name = configuration.getStringProperty(Configuration.CONTROLLER_NAME);
		this.experimentId.set(configuration.getIntProperty(Configuration.EXPERIMENT_ID));
		this.monitoringEnabled = configuration.getBooleanProperty(Configuration.MONITORING_ENABLED);
		String hostnameTmp = configuration.getStringProperty(Configuration.HOST_NAME);
		if (hostnameTmp.isEmpty()) {
			hostnameTmp = "<UNKNOWN>";
			try {
				hostnameTmp = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				StateController.LOG.warn("Failed to retrieve hostname");
			}
		}
		this.hostname = hostnameTmp;
	}

	@Override
	protected void init() {
		// do nothing
	}

	@Override
	protected final void cleanup() {
		StateController.LOG.debug("Shutting down State Controller");
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Status: '");
		if (this.isMonitoringTerminated()) {
			sb.append("terminated");
		} else if (this.isMonitoringEnabled()) {
			sb.append("enabled");
		} else {
			sb.append("disabled");
		}
		sb.append("'\n");
		sb.append("\tName: '");
		sb.append(this.name);
		sb.append("'; Hostname: '");
		sb.append(this.hostname);
		sb.append("'; experimentID: '");
		sb.append(this.getExperimentId());
		sb.append("'\n");
		return sb.toString();
	}

	@Override
	public final boolean terminateMonitoring() {
		final MonitoringController monitoringController = super.monitoringController;
		if (monitoringController != null) {
			return monitoringController.terminate();
		} else {
			StateController.LOG.warn("Shutting down Monitoring before it is correctly initialized");
			return false;
		}
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return super.isTerminated();
	}

	@Override
	public final boolean enableMonitoring() {
		if (this.isMonitoringTerminated()) {
			StateController.LOG.error("Refused to enable monitoring because monitoring has been permanently terminated");
			return false;
		}
		StateController.LOG.info("Enabling monitoring");
		this.monitoringEnabled = true;
		return true;
	}

	@Override
	public final boolean disableMonitoring() {
		StateController.LOG.info("Disabling monitoring");
		this.monitoringEnabled = false;
		return true;
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return !super.isTerminated() && this.monitoringEnabled;
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
}
