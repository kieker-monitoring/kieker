/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.tcp.SingleSocketRecordReader;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.listener.MonitoringCommandListener;

/**
 * Enables remote control of probes (like (de-)activation) via TCP.
 * Thereby special records are used to pass the messages.
 *
 * @author Marc Adolf
 * @since 1.14
 *
 */
public class TCPController extends AbstractController implements IRemoteController {

	/**
	 * The size for the message buffer
	 */
	private static final int BUFFER_SIZE = 65535;
	/** The log for this component. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TCPController.class);
	// maybe not necessary
	private final String domain;
	private SingleSocketRecordReader tcpReader;
	private boolean tcpEnabled;
	private final Thread thread;

	/**
	 * Creates a new TCPController needs the {@link MonitoringController} to start and connect the TCP receiver.
	 *
	 * @param configuration
	 *            containing all related variables for the TCP settings.
	 * @param monitoringController
	 *            the controller which is connected to the TCP receiver and where the commands are send to.
	 */
	protected TCPController(final Configuration configuration, final MonitoringController monitoringController) {
		super(configuration);

		final IRecordReceivedListener listener = new MonitoringCommandListener(monitoringController);
		this.domain = configuration.getStringProperty(ConfigurationKeys.ACTIVATE_TCP_DOMAIN);
		try {
			final int port = Integer.parseInt(configuration.getStringProperty(ConfigurationKeys.ACTIVATE_TCP_REMOTE_PORT));
			this.tcpEnabled = configuration.getBooleanProperty(ConfigurationKeys.ACTIVATE_TCP);
			this.tcpReader = new SingleSocketRecordReader(port, BUFFER_SIZE, TCPController.LOGGER, listener);

		} catch (final NumberFormatException e) {
			this.tcpEnabled = false;
			TCPController.LOGGER.error("Could not parse port for the TCPController, deactivating this option. Received string was: {}",
					configuration.getStringProperty(ConfigurationKeys.ACTIVATE_TCP_REMOTE_PORT));
		}
		this.thread = new Thread(this.tcpReader);

	}

	@Override
	public String getControllerDomain() {
		return this.domain;
	}

	@Override
	protected void init() {
		if (this.tcpEnabled) {
			LOGGER.info("TCP reader for remote commands started");
			this.thread.start();
		}

	}

	@Override
	protected void cleanup() {
		if (this.tcpEnabled) {
			LOGGER.info("TCP reader for remote commands terminated");
			this.tcpReader.terminate();
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(255);
		sb.append("TCPController: ");
		if (this.tcpEnabled) {
			sb.append("TCP enabled (Domain: '");
			sb.append(this.domain);
			sb.append("')\n");
		} else {
			sb.append("TCP disabled\n");
		}
		return sb.toString();
	}

}
