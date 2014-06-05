/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;

/**
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 * @since 1.8
 */
public class TestTCPSingleServerConnector extends AbstractConnectorTest {

	/**
	 * Default constructor.
	 */
	public TestTCPSingleServerConnector() {
		// empty constructor
	}

	/**
	 * Test a TCP single server connector.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             on lookup failure for the test record
	 */
	@Test
	public void testTCPSingleServerConnector() throws ConnectorDataTransmissionException { // NOPMD
		// start one client for the test
		final Thread clientThread = new Thread(new TCPClientforServer(ConfigurationParameters.TCP_SINGLE_PORT), "T1");
		clientThread.start();

		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(TCPSingleServerConnector.PORT, String.valueOf(ConfigurationParameters.TCP_SINGLE_PORT));
		// test the connector
		this.setConnector(new TCPSingleServerConnector(configuration, this.createLookupEntityMap()));
		this.initialize();
		this.deserialize(ConfigurationParameters.SEND_NUMBER_OF_RECORDS, true);
		this.close(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
	}
}
