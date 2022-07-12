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

import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

/**
 * Stores all information concerning a TCP connection to control monitoring probes.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public class TcpControlConnection {

	private final String ip;
	private final String serviceComponent;
	private final int port;
	private final SingleSocketTcpWriter tcpWriter;

	/**
	 * Only initiates complete data sets.
	 *
	 * @param ip
	 *            The IP the tcpWriter is connected to.
	 * @param serviceComponent
	 *            The name of the component.
	 * @param port
	 *            The port the tcpWriter is connected to.
	 * @param tcpWriter
	 *            the TCP writer which has the connection established.
	 */
	public TcpControlConnection(final String ip, final int port, final String serviceComponent,
			final SingleSocketTcpWriter tcpWriter) {
		this.ip = ip;
		this.serviceComponent = serviceComponent;
		this.port = port;
		this.tcpWriter = tcpWriter;
	}

	public String getIp() {
		return this.ip;
	}

	public String getServiceComponent() {
		return this.serviceComponent;
	}

	public int getPort() {
		return this.port;
	}

	public SingleSocketTcpWriter getTcpWriter() {
		return this.tcpWriter;
	}

}
