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
package kieker.test.tools.junit.bridge;

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.tcp.TCPClientConnector;

/**
 * Test the TCPClientConnector, by creating a TCP server, which provides Kieker IMonitoringRecords to the connector.
 * This test class starts the server, which will then wait for a connection from the client. After connection is sends
 * SEND_NUMBER_OF_RECORDS records and terminates the connection.
 *
 * @author Reiner Jung, Pascale Brandt
 *
 * @since 1.8
 */
public class TestTCPClientConnector extends AbstractConnectorTest {

	/**
	 * Default constructor.
	 */
	public TestTCPClientConnector() {
		// empty constructor
	}

	/**
	 * Test all methods of {@link kieker.tools.bridge.connector.tcp.TCPClientConnector}.
	 * Testing single methods do not work, as the connector is stateful.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             on lookup failure for the test record
	 * @throws InterruptedException
	 *             when the test is interrupted
	 */
	@Test
	public void testTCPClientConnector() throws ConnectorDataTransmissionException, InterruptedException { // NOPMD
		// Start a record providing server for the TCPClientConnector
		final TCPServerForClient server = new TCPServerForClient(ConfigurationParameters.TCP_CLIENT_PORT);
		final Thread serverThread = new Thread(server, "T1");
		serverThread.start();
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(TCPClientConnector.HOSTNAME, ConfigurationParameters.HOSTNAME);
		configuration.setProperty(TCPClientConnector.PORT, String.valueOf(ConfigurationParameters.TCP_CLIENT_PORT));

		while (!server.isListening()) {
			Thread.sleep(100);
		}

		this.setConnector(new TCPClientConnector(configuration, this.createLookupEntityMap()));
		this.initialize();
		this.deserialize(ConfigurationParameters.SEND_NUMBER_OF_RECORDS, true);
		this.close(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
	}
}
