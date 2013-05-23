/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.bridge.connector;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.connector.jms.JMSEmbeddedConnector;
import kieker.tools.bridge.connector.jms.JMSClientConnector;
import kieker.tools.bridge.connector.tcp.TCPClientConnector;
import kieker.tools.bridge.connector.tcp.TCPMultiServerConnector;
import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;

// TODO: add documentation

/**
 * 
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public final class ServiceConnectorFactory {

	/**
	 * 
	 * @param recordMap
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 */
	public static IServiceConnector createJMSServiceConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap,
			final String username, final String password, final URI url) {
		return new JMSClientConnector(recordMap, username, password, url);
	}

	/**
	 * 
	 * @param recordMap
	 * @param port
	 * @return
	 * @throws URISyntaxException
	 */
	public static IServiceConnector createJMSEmbeddedServiceConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap, final int port)
			throws URISyntaxException {
		return new JMSEmbeddedConnector(recordMap, port);
	}

	/**
	 * 
	 * @param recordMap
	 * @param port
	 * @return
	 */
	public static IServiceConnector createTCPSingleServerServiceConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap, final int port) {
		return new TCPSingleServerConnector(recordMap, port);
	}

	/**
	 * 
	 * @param recordMap
	 * @param port
	 * @return
	 */
	public static IServiceConnector createTCPMultiServerServiceConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap, final int port) {
		return new TCPMultiServerConnector(recordMap, port);
	}

	/**
	 * 
	 * @param recordMap
	 * @param hostname
	 * @param port
	 * @return
	 */
	public static IServiceConnector createTCPClientServiceConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap, final String hostname, final int port) {
		return new TCPClientConnector(recordMap, hostname, port);
	}
}
