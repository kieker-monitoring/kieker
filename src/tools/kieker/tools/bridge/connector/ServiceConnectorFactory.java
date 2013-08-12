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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.LookupEntity;
import kieker.common.record.MonitoringRecordFactory;
import kieker.tools.bridge.connector.jms.JMSClientConnector;
import kieker.tools.bridge.connector.jms.JMSEmbeddedConnector;
import kieker.tools.bridge.connector.tcp.TCPClientConnector;
import kieker.tools.bridge.connector.tcp.TCPMultiServerConnector;
import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;

/**
 * This factory is an attempt to simplify the instantiation of new
 * connector types. However, we need to re-think this and move configuration to the Kieker configuration.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public final class ServiceConnectorFactory {

	/**
	 * Empty default constructor.
	 */
	private ServiceConnectorFactory() {

	}

	/**
	 * Create a JMS service connector.
	 * 
	 * @param recordMap
	 *            map containing ids for types and types for the deserializer
	 * @param username
	 *            JMS service user name
	 * @param password
	 *            JMS service password
	 * @param url
	 *            URL to access the JMS service and queue
	 * @return Returns a connector instance
	 */
	public static IServiceConnector createJMSServiceConnector(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap,
			final String username, final String password, final URI url) {
		return new JMSClientConnector(recordMap, username, password, url);
	}

	/**
	 * Create a JMS service connector and an embedded JMS service.
	 * 
	 * @param recordMap
	 *            map containing ids for types and types for the deserializer
	 * @param port
	 *            Port for the JMS service to create
	 * @return Returns a connector instance
	 * @throws URISyntaxException
	 *             if an internal error occurred
	 */
	public static IServiceConnector createJMSEmbeddedServiceConnector(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap, final int port)
			throws URISyntaxException {
		return new JMSEmbeddedConnector(recordMap, port);
	}

	/**
	 * Create a TCP server connector which can handle only one incoming connection for records.
	 * 
	 * @param recordMap
	 *            map containing ids for types and types for the deserializer
	 * @param port
	 *            Port the TCP server listens to
	 * @return Returns a connector instance
	 */
	public static IServiceConnector createTCPSingleServerServiceConnector(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap,
			final int port) {
		return new TCPSingleServerConnector(recordMap, port);
	}

	/**
	 * Create a TCP server connector which can handle multiple incoming connection for records.
	 * 
	 * @param recordMap
	 *            map containing ids for types and types for the deserializer
	 * @param port
	 *            Port the TCP server listens to
	 * @return Returns a connector instance
	 */
	public static IServiceConnector createTCPMultiServerServiceConnector(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap,
			final int port) {
		return new TCPMultiServerConnector(recordMap, port);
	}

	/**
	 * Create a TCP client connector which connects itself to a record source.
	 * 
	 * @param recordMap
	 *            map containing ids for types and types for the deserializer
	 * @param hostname
	 *            Hostname or IP address where the client connects to
	 * @param port
	 *            Port of the remote service
	 * @return Returns a connector instance
	 */
	public static IServiceConnector createTCPClientServiceConnector(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap,
			final String hostname,
			final int port) {
		return new TCPClientConnector(recordMap, hostname, port);
	}

	/**
	 * Calculates the lookup table from the record map.
	 * 
	 * @param recordMap
	 *            A map containing ids and IMonitoringRecord types
	 * @return A map containing record ids referencing constructor and field information
	 * @throws MonitoringRecordException
	 *             if the lookup table compilation fails
	 */
	public static ConcurrentMap<Integer, LookupEntity> createLookupEntityMap(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap)
			throws MonitoringRecordException {
		final ConcurrentMap<Integer, LookupEntity> lookupEntityMap = new ConcurrentHashMap<Integer, LookupEntity>();
		for (final int key : recordMap.keySet()) {
			final Class<? extends IMonitoringRecord> type = recordMap.get(key);
			lookupEntityMap.put(key, MonitoringRecordFactory.createLookupEntity(type));
		}
		return lookupEntityMap;
	}
}
