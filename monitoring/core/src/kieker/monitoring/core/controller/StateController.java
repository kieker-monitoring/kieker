/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationConstants;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class StateController extends AbstractController implements IStateController {
	private static final Logger LOGGER = LoggerFactory.getLogger(StateController.class);

	private volatile boolean monitoringEnabled;
	private final String name;
	private final String hostname;
	private final String applicationName;
	private final AtomicInteger experimentId = new AtomicInteger(0);
	private final boolean debug;

	private IStateListener stateListener;

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param configuration
	 *            The configuration which will be used to initialize the controller.
	 */
	protected StateController(final Configuration configuration) {
		super(configuration);
		this.name = configuration.getStringProperty(ConfigurationConstants.CONTROLLER_NAME);
		this.experimentId.set(configuration.getIntProperty(ConfigurationConstants.EXPERIMENT_ID));
		this.applicationName = configuration.getStringProperty(ConfigurationConstants.APPLICATION_NAME);
		this.monitoringEnabled = configuration.getBooleanProperty(ConfigurationConstants.MONITORING_ENABLED);
		this.debug = configuration.getBooleanProperty(ConfigurationConstants.DEBUG);
		String hostnameTmp = configuration.getStringProperty(ConfigurationConstants.HOST_NAME);
		if (hostnameTmp.length() == 0) {
			hostnameTmp = "<UNKNOWN>";
			try {
				hostnameTmp = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				LOGGER.warn("Failed to retrieve hostname", ex);
			}
		}
		this.hostname = hostnameTmp;
	}

	@Override
	protected void init() {
		// do nothing
	}

	@Override
	protected void cleanup() {
		LOGGER.debug("Shutting down State Controller");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("Status: '");
		if (this.isMonitoringTerminated()) {
			sb.append("terminated");
		} else if (this.isMonitoringEnabled()) {
			sb.append("enabled");
		} else {
			sb.append("disabled");
		}
		sb.append("'\n\tName: '")
				.append(this.name)
				.append("'; Hostname: '")
				.append(this.hostname)
				.append("'; experimentID: '")
				.append(this.getExperimentId())
				.append("'\n");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean terminateMonitoring() {
		if (super.monitoringController != null) {
			return super.monitoringController.terminate();
		} else {
			LOGGER.warn("Shutting down Monitoring before it is correctly initialized");
			return false;
		}
	}

	@Override
	public boolean isMonitoringTerminated() {
		return super.isTerminated();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean enableMonitoring() {
		if (this.isMonitoringTerminated()) {
			LOGGER.error("Refused to enable monitoring because monitoring has been permanently terminated");
			return false;
		}
		LOGGER.info("Enabling monitoring");

		if (this.stateListener != null) {
			this.stateListener.beforeEnableMonitoring();
		}

		this.monitoringEnabled = true;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean disableMonitoring() {
		LOGGER.info("Disabling monitoring");
		this.monitoringEnabled = false;
		return true;
	}

	@Override
	public boolean isMonitoringEnabled() {
		return !super.isTerminated() && this.monitoringEnabled;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getHostname() {
		return this.hostname;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int incExperimentId() {
		return this.experimentId.incrementAndGet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExperimentId(final int newExperimentID) {
		this.experimentId.set(newExperimentID);
	}

	@Override
	public int getExperimentId() {
		return this.experimentId.get();
	}

	@Override
	public boolean isDebug() {
		return this.debug;
	}

	public void setStateListener(final IStateListener stateListener) {
		this.stateListener = stateListener;
	}

	public String getApplicationName() {
		return this.applicationName;
	}
}
