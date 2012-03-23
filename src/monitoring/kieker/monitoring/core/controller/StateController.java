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

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;

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
		super(configuration);
		this.name = configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME);
		this.experimentId.set(configuration.getIntProperty(ConfigurationFactory.EXPERIMENT_ID));
		this.monitoringEnabled = configuration.getBooleanProperty(ConfigurationFactory.MONITORING_ENABLED);
		String hostnameTmp = configuration.getStringProperty(ConfigurationFactory.HOST_NAME);
		if (hostnameTmp.length() == 0) {
			hostnameTmp = "<UNKNOWN>";
			try {
				hostnameTmp = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				StateController.LOG.warn("Failed to retrieve hostname", ex);
			}
		}
		this.hostname = hostnameTmp;
	}

	@Override
	protected final void init() {
		// do nothing
	}

	@Override
	protected final void cleanup() {
		if (StateController.LOG.isDebugEnabled()) {
			StateController.LOG.debug("Shutting down State Controller");
		}
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

	public final boolean terminateMonitoring() {
		if (super.monitoringController != null) {
			return super.monitoringController.terminate();
		} else {
			StateController.LOG.warn("Shutting down Monitoring before it is correctly initialized");
			return false;
		}
	}

	public final boolean isMonitoringTerminated() {
		return super.isTerminated();
	}

	public final boolean enableMonitoring() {
		if (this.isMonitoringTerminated()) {
			StateController.LOG.error("Refused to enable monitoring because monitoring has been permanently terminated");
			return false;
		}
		StateController.LOG.info("Enabling monitoring");
		this.monitoringEnabled = true;
		return true;
	}

	public final boolean disableMonitoring() {
		StateController.LOG.info("Disabling monitoring");
		this.monitoringEnabled = false;
		return true;
	}

	public final boolean isMonitoringEnabled() {
		return !super.isTerminated() && this.monitoringEnabled;
	}

	public final String getName() {
		return this.name;
	}

	public final String getHostname() {
		return this.hostname;
	}

	public final int incExperimentId() {
		return this.experimentId.incrementAndGet();
	}

	public final void setExperimentId(final int newExperimentID) {
		this.experimentId.set(newExperimentID);
	}

	public final int getExperimentId() {
		return this.experimentId.get();
	}
}
