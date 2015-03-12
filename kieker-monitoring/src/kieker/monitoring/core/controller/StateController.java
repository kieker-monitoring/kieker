/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
 * 
 * @since 1.3
 */
public final class StateController extends AbstractController implements IStateController {
	private static final Log LOG = LogFactory.getLog(StateController.class);

	private volatile boolean monitoringEnabled;
	private final String name;
	private final String hostname;
	private final AtomicInteger experimentId = new AtomicInteger(0);
	private final boolean debug;

	/**
	 * Creates a new instance of this class using the given parameter.
	 * 
	 * @param configuration
	 *            The configuration which will be used to initialize the controller.
	 */
	protected StateController(final Configuration configuration) {
		super(configuration);
		this.name = configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME);
		this.experimentId.set(configuration.getIntProperty(ConfigurationFactory.EXPERIMENT_ID));
		this.monitoringEnabled = configuration.getBooleanProperty(ConfigurationFactory.MONITORING_ENABLED);
		this.debug = configuration.getBooleanProperty(ConfigurationFactory.DEBUG);
		String hostnameTmp = configuration.getStringProperty(ConfigurationFactory.HOST_NAME);
		if (hostnameTmp.length() == 0) {
			hostnameTmp = "<UNKNOWN>";
			try {
				hostnameTmp = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				LOG.warn("Failed to retrieve hostname", ex);
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
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down State Controller");
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("Status: '");
		if (this.isMonitoringTerminated()) {
			sb.append("terminated");
		} else if (this.isMonitoringEnabled()) {
			sb.append("enabled");
		} else {
			sb.append("disabled");
		}
		sb.append("'\n\tName: '");
		sb.append(this.name);
		sb.append("'; Hostname: '");
		sb.append(this.hostname);
		sb.append("'; experimentID: '");
		sb.append(this.getExperimentId());
		sb.append("'\n");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean terminateMonitoring() {
		if (super.monitoringController != null) {
			return super.monitoringController.terminate();
		} else {
			LOG.warn("Shutting down Monitoring before it is correctly initialized");
			return false;
		}
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return super.isTerminated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean enableMonitoring() {
		if (this.isMonitoringTerminated()) {
			LOG.error("Refused to enable monitoring because monitoring has been permanently terminated");
			return false;
		}
		LOG.info("Enabling monitoring");
		this.monitoringEnabled = true;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean disableMonitoring() {
		LOG.info("Disabling monitoring");
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
	public final String getHostname() {
		return this.hostname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int incExperimentId() {
		return this.experimentId.incrementAndGet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setExperimentId(final int newExperimentID) {
		this.experimentId.set(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return this.experimentId.get();
	}

	@Override
	public final boolean isDebug() {
		return this.debug;
	}
}
