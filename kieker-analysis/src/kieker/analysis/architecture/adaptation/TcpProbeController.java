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
package kieker.analysis.architecture.adaptation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.adaptation.events.AbstractTcpControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpActivationControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpActivationParameterControlEvent;
import kieker.analysis.architecture.adaptation.events.TcpDeactivationControlEvent;
import kieker.common.configuration.Configuration;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.ActivationParameterEvent;
import kieker.common.record.remotecontrol.AddParameterValueEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.common.record.remotecontrol.UpdateParameterEvent;
import kieker.monitoring.writer.tcp.ConnectionTimeoutException;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

/**
 * Controller to send remote control events for probes to given addresses. Establishes TCP
 * connections and keeps them open.
 *
 * Please note that this feature interacts with monitoring probes over an un-encrypted and insecure connection.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public class TcpProbeController implements IProbeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TcpProbeController.class);
	private static final int CONN_TIMEOUT_IN_MS = 100;

	/**
	 * Saves already established connections, the key pattern is "ip:port".
	 */
	private final Map<String, TcpControlConnection> knownAddresses = new HashMap<>();

	/**
	 * Create the probe controller.
	 */
	public TcpProbeController() {
		// empty default constructor
	}

	/**
	 * Convenience method for {@link AbstractControlEvent control events}.
	 *
	 * @param event
	 *            The event that contains the information for remote control
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	@Override
	public void controlProbe(final AbstractTcpControlEvent event) throws RemoteControlFailedException {
		TcpProbeController.LOGGER.debug("control probe [{}] [{}] [{}]", event.getServiceComponent(), event.getIp(),
				event.getPort());

		final String ip = event.getIp();
		final int port = event.getPort();
		final String hostname = event.getServiceComponent();
		final String pattern = event.getOperationSignature();

		if (event instanceof TcpActivationControlEvent) {
			if (event instanceof TcpActivationParameterControlEvent) {
				this.activateOperationMonitoringWithParameters(ip, port, hostname, pattern,
						((TcpActivationParameterControlEvent) event).getParameters());
			} else {
				this.activateOperationMonitoring(ip, port, hostname, pattern);
			}
		} else if (event instanceof TcpDeactivationControlEvent) {
			this.deactivateOperationMonitoring(ip, port, hostname, pattern);
		} else {
			TcpProbeController.LOGGER.error("Received Unknown TCP control event: {}", event.getClass().getName());
		}
	}

	/**
	 * Updates the given parameters for a probe.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param pattern
	 *            The pattern of the method that should is monitored.
	 * @param parameters
	 *            The map of parameters to be set, the key is the name and the values the values for
	 *            the parameter.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void updateProbeParameter(final String ip, final int port, final String hostname, final String pattern,
			final Map<String, List<String>> parameters) throws RemoteControlFailedException {

		for (final Entry<String, List<String>> parameter : parameters.entrySet()) {
			final String[] parameterArray = parameter.getValue().toArray(new String[parameter.getValue().size()]);
			this.sendTcpCommand(ip, port, hostname,
					new UpdateParameterEvent(pattern, parameter.getKey(), parameterArray));
		}
	}

	/**
	 * Activates monitoring of a method on one monitored application via TCP.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param operationSignature
	 *            The operation signature of the method that should be monitored.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void activateOperationMonitoring(final String ip, final int port, final String hostname,
			final String operationSignature) throws RemoteControlFailedException {
		this.sendTcpCommand(ip, port, hostname, new ActivationEvent(operationSignature));
	}

	/**
	 * Activates monitoring of a method on one monitored application via TCP and transfers
	 * parameter.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param operationSignature
	 *            The operation signature of the method that should be monitored.
	 * @param parameters
	 *            The map of parameters to be set, the key is the name and the values are the values
	 *            for the parameter.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void activateOperationMonitoringWithParameters(final String ip, final int port, final String hostname,
			final String operationSignature, final Map<String, List<String>> parameters)
			throws RemoteControlFailedException {
		for (final Entry<String, List<String>> parameter : parameters.entrySet()) {
			this.sendTcpCommand(ip, port, hostname,
					new ActivationParameterEvent(operationSignature, parameter.getKey(), new String[0]));
			for (final String value : parameter.getValue()) {
				this.sendTcpCommand(ip, port, hostname,
						new AddParameterValueEvent(operationSignature, parameter.getKey(), value));
			}
		}
	}

	/**
	 * Deactivates monitoring of a method on one monitored application via TCP.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param serviceComponent
	 *            The name of the component which is using this IP and port.
	 * @param operationSignature
	 *            The operation signature of the method that should no longer be monitored.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void deactivateOperationMonitoring(final String ip, final int port, final String serviceComponent,
			final String operationSignature) throws RemoteControlFailedException {
		this.sendTcpCommand(ip, port, serviceComponent, new DeactivationEvent(operationSignature));
	}

	private void sendTcpCommand(final String ip, final int port, final String serviceComponent,
			final IRemoteControlEvent monitoringRecord) throws RemoteControlFailedException {
		final String writerKey = ip + ":" + port;
		final SingleSocketTcpWriter tcpWriter;

		TcpControlConnection currentConnection = this.knownAddresses.get(writerKey);

		// if host was never used or an other module was there before, create a new connection
		if ((currentConnection == null) || (currentConnection.getServiceComponent() != serviceComponent)) {
			currentConnection = new TcpControlConnection(ip, port, serviceComponent, this.createNewTcpWriter(ip, port));
			this.knownAddresses.put(writerKey, currentConnection);
		}
		tcpWriter = currentConnection.getTcpWriter();

		if (tcpWriter == null) {
			throw new RemoteControlFailedException("TCP Writer was not found");
		}
		// currently we have no means to check if the write process was successful or the channel is
		// still active
		tcpWriter.writeMonitoringRecord(monitoringRecord);
		TcpProbeController.LOGGER.debug(
				String.format("Send record %s to %s on port: %d", monitoringRecord.getClass().getName(), ip, port));
	}

	private SingleSocketTcpWriter createNewTcpWriter(final String hostname, final int port)
			throws RemoteControlFailedException {
		final Configuration configuration = new Configuration();

		configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, hostname);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, port);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS,
				TcpProbeController.CONN_TIMEOUT_IN_MS);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_FLUSH, true);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_BUFFERSIZE, 655350);
		final SingleSocketTcpWriter tcpWriter;
		try {
			tcpWriter = new SingleSocketTcpWriter(configuration);
			tcpWriter.onStarting();
		} catch (final IOException | ConnectionTimeoutException e) {
			// runtime exception is thrown after timeout
			TcpProbeController.LOGGER
					.debug(String.format("Could not create TCP connections to %s on port %d", hostname, port), e);
			throw new RemoteControlFailedException(String.format(
					"Could not create TCP connections to %s on port %d, writer was not created.", hostname, port), e);
		}
		return tcpWriter;
	}

	/**
	 * Checks if a host is known. The searched pattern is ip:port.
	 *
	 * @param ip
	 *            the IP of the host.
	 * @param port
	 *            the used port of the TCP connections
	 * @return true, if the connections to the host was already established.
	 */
	public boolean isKnownHost(final String ip, final int port) {
		return this.knownAddresses.keySet().contains(ip + ":" + port);
	}

}
