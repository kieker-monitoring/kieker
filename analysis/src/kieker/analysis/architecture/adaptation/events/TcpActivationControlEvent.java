/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.adaptation.events;

/**
 * Represents an event for the activation of a probe via TCP.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public class TcpActivationControlEvent extends BasicTcpControlEvent {

	/**
	 * Creates a complete activation control event.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param hostname
	 *            The name of the component which is using this IP and port.
	 * @param pattern
	 *            The pattern of the method that should be monitored.
	 * @param triggerTimestamp
	 *            original trigger timestamp
	 */
	public TcpActivationControlEvent(final String ip, final int port, final String hostname, final String pattern,
			final long triggerTimestamp) {
		super(ip, port, hostname, pattern, triggerTimestamp);
	}

	/**
	 * Creates a new activation control event without content except the pattern.
	 *
	 * @param pattern
	 *            The pattern of the method that should be monitored.
	 * @param triggerTimestamp
	 *            original trigger timestamp
	 */
	public TcpActivationControlEvent(final String pattern, final long triggerTimestamp) {
		super(pattern, triggerTimestamp);
	}

}
