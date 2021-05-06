/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.adaptation.events;

import java.util.List;
import java.util.Map;

/**
 * Represents an event for the update of parameters of a probe via TCP.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public class TcpUpdateParameterEvent extends AbstractTcpControlEvent implements IParameterEvent {

	private final Map<String, List<String>> parameters;

	/**
	 * Creates a parameter update control event without content except the pattern and parameters.
	 *
	 * @param operationSignature
	 *            The operation signature of the method that is monitored.
	 * @param triggerTimestamp
	 *            original trigger timestamp
	 * @param parameters
	 *            a map of parameters and a list of each entry per parameter
	 *
	 */
	public TcpUpdateParameterEvent(final String operationSignature, final long triggerTimestamp,
			final Map<String, List<String>> parameters) {
		super(operationSignature, triggerTimestamp);
		this.parameters = parameters;
	}

	/**
	 * Creates a complete parameter update control event.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param pattern
	 *            The pattern of the method that is monitored.
	 * @param triggerTimestamp
	 *            original trigger timestamp
	 * @param parameters
	 *            a map of parameters and a list of each entry per parameter
	 *
	 */
	public TcpUpdateParameterEvent(final String ip, final int port, final String hostname, final String pattern,
			final long triggerTimestamp, final Map<String, List<String>> parameters) {
		super(ip, port, hostname, pattern, triggerTimestamp);
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.iobserve.utility.tcp.events.IParameterEvent#getParameters()
	 */
	@Override
	public Map<String, List<String>> getParameters() {
		return this.parameters;
	}

}
