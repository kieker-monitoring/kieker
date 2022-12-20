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
package kieker.analysis.architecture.adaptation.events;

/**
 * Contains the information needed to control a certain probe via TCP.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public abstract class AbstractTcpControlEvent {

	private String ip;
	private int port;
	private String serviceComponent;

	private final String operationSignature;

	private final long triggerTimestamp;

	/**
	 * Creates a complete control event.
	 *
	 * @param ip
	 *            Address of the monitored application.
	 * @param port
	 *            Port of the TCP controller.
	 * @param serviceComponent
	 *            The name of the component which is using this IP and port.
	 * @param pattern
	 *            The pattern of the method that should be monitored.
	 * @param triggerTimestamp
	 *            original trigger timestamp
	 */
	public AbstractTcpControlEvent(final String ip, final int port, final String serviceComponent, final String pattern,
			final long triggerTimestamp) {
		this(pattern, triggerTimestamp);
		this.ip = ip;
		this.port = port;
		this.serviceComponent = serviceComponent;
	}

	/**
	 * Creates a new control event without content except the pattern.
	 *
	 * @param operationSignature
	 *            The pattern of the method that should be monitored.
	 * @param triggerTimestamp
	 *            original trigger timestamp
	 */
	public AbstractTcpControlEvent(final String operationSignature, final long triggerTimestamp) {
		this.operationSignature = operationSignature;
		this.triggerTimestamp = triggerTimestamp;
	}

	public String getIp() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	public String getServiceComponent() {
		return this.serviceComponent;
	}

	public String getOperationSignature() {
		return this.operationSignature;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public void setHostname(final String hostname) {
		this.serviceComponent = hostname;
	}

	public long getTriggerTimestamp() {
		return this.triggerTimestamp;
	}

}
