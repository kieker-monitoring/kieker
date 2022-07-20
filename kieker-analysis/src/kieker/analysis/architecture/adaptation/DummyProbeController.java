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
import kieker.analysis.architecture.adaptation.events.TcpParameterControlEvent;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.ActivationParameterEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.common.record.remotecontrol.IRemoteParameterControlEvent;
import kieker.common.record.remotecontrol.UpdateParameterEvent;

/**
 * Controller to send remote control events for probes to given addresses. Establishes TCP
 * connections and keeps them open.
 *
 * @author Marc Adolf
 * @since 1.14
 */
public class DummyProbeController implements IProbeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyProbeController.class);

	/**
	 * Saves already established connections, the key pattern is "ip:port".
	 */
	private final Map<String, TcpControlConnection> knownAddresses = new HashMap<>();

	/**
	 * Create the probe controller.
	 */
	public DummyProbeController() {
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
		DummyProbeController.LOGGER.debug("control probe host=[{}] ip=[{}] port=[{}]", event.getServiceComponent(),
				event.getIp(), event.getPort());

		final String ip = event.getIp();
		final int port = event.getPort();
		final String hostname = event.getServiceComponent();
		final String pattern = event.getOperationSignature();

		if (event instanceof TcpActivationControlEvent) {
			if (event instanceof TcpActivationParameterControlEvent) {
				this.activateParameterMonitoredPattern(ip, port, hostname, pattern,
						((TcpActivationParameterControlEvent) event).getParameters());
			} else {
				this.activateMonitoredPattern(ip, port, hostname, pattern);
			}
		} else if (event instanceof TcpDeactivationControlEvent) {
			this.deactivateMonitoredPattern(ip, port, hostname, pattern);
		} else if (event instanceof TcpParameterControlEvent) {
			this.updateProbeParameter(ip, port, hostname, pattern, ((TcpParameterControlEvent) event).getParameters());
		} else {
			DummyProbeController.LOGGER.error("Received Unknown TCP control event: {}", event.getClass().getName());
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
			final String[] parameterArray = this.computeParameterArray(parameter.getValue());
			this.sendTcpCommand(ip, port, hostname,
					new UpdateParameterEvent(pattern, parameter.getKey(), parameterArray));
		}
	}

	/**
	 * Activates monitoring of a method (pattern) on one monitored application via TCP.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param pattern
	 *            The pattern of the method that should be monitored.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void activateMonitoredPattern(final String ip, final int port, final String hostname, final String pattern)
			throws RemoteControlFailedException {
		this.sendTcpCommand(ip, port, hostname, new ActivationEvent(pattern));
	}

	/**
	 * Activates monitoring of a method (pattern) on one monitored application via TCP and transfers
	 * parameter.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param operationSignature
	 *            The pattern of the method that should be monitored.
	 * @param parameters
	 *            The map of parameters to be set, the key is the name and the values the values for
	 *            the parameter.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void activateParameterMonitoredPattern(final String ip, final int port, final String hostname,
			final String operationSignature, final Map<String, List<String>> parameters)
			throws RemoteControlFailedException {
		for (final Entry<String, List<String>> parameter : parameters.entrySet()) {
			final String[] parameterArray = this.computeParameterArray(parameter.getValue());
			this.sendTcpCommand(ip, port, hostname,
					new ActivationParameterEvent(operationSignature, parameter.getKey(), parameterArray));
		}
	}

	/**
	 * Deactivates monitoring of a method (pattern) on one monitored application via TCP.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param pattern
	 *            The pattern of the method that should no longer be monitored.
	 * @throws RemoteControlFailedException
	 *             if the connection can not be established within a set timeout.
	 */
	public void deactivateMonitoredPattern(final String ip, final int port, final String hostname, final String pattern)
			throws RemoteControlFailedException {
		this.sendTcpCommand(ip, port, hostname, new DeactivationEvent(pattern));
	}

	private void sendTcpCommand(final String ip, final int port, final String hostname,
			final IRemoteControlEvent monitoringRecord) throws RemoteControlFailedException {
		final String writerKey = ip + ":" + port;

		TcpControlConnection currentConnection = this.knownAddresses.get(writerKey);

		// if host was never used or an other module was there before, create a new connection
		if (currentConnection == null || currentConnection.getServiceComponent() != hostname) {
			currentConnection = new TcpControlConnection(ip, port, hostname, null);
			this.knownAddresses.put(writerKey, currentConnection);
		}

		DummyProbeController.LOGGER.debug("Event time={} size={} pattern={}", monitoringRecord.getLoggingTimestamp(),
				monitoringRecord.getSize(), monitoringRecord.getPattern());

		final Class<?>[] types = monitoringRecord.getValueTypes();
		final String[] values = monitoringRecord.getValueNames();

		DummyProbeController.LOGGER.debug("--------------- Provided attributes of the event ---------------");

		for (int i = 0; i < types.length; i++) {
			final Class<?> type = monitoringRecord.getValueTypes()[i];
			DummyProbeController.LOGGER.debug("\t attribute type={}", type.getCanonicalName());
			if (i < values.length) {
				final String value = monitoringRecord.getValueNames()[i];
				DummyProbeController.LOGGER.debug("\t attribute name={}", value);
			}
		}

		if (monitoringRecord instanceof IRemoteParameterControlEvent) {
			DummyProbeController.LOGGER.debug("--------------- Parameter Control Event ---------------");
			this.printParameters(((IRemoteParameterControlEvent) monitoringRecord).getName(),
					((IRemoteParameterControlEvent) monitoringRecord).getValues());
			DummyProbeController.LOGGER.debug("-------------------------------------------------------");

		}

		DummyProbeController.LOGGER.debug("Send record {} to {} on port: {}", monitoringRecord.getClass().getName(), ip,
				port);
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

	private String[] computeParameterArray(final List<String> parameters) {
		return parameters.toArray(new String[parameters.size()]);
	}

	private void printParameters(final String name, final String[] values) {
		DummyProbeController.LOGGER.debug(">> {}", name);
		for (final String value : values) {
			DummyProbeController.LOGGER.debug("\t - {}", value);
		}
	}
}
