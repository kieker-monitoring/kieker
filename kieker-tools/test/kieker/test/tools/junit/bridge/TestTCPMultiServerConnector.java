/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.tcp.TCPMultiServerConnector;

/**
 * 
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 * @since 1.8
 */

public class TestTCPMultiServerConnector extends AbstractConnectorTest {

	/**
	 * Default constructor.
	 */
	public TestTCPMultiServerConnector() {
		// empty constructor
	}

	/**
	 * Testing a TCP multi server connector.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             on lookup failure for the test record
	 */
	@Test
	public void testTCPMultiServerConnector() throws ConnectorDataTransmissionException {
		// start multiple record providing clients
		final ExecutorService executor = Executors.newFixedThreadPool(ConfigurationParameters.NUMBER_OF_TEST_THREADS);
		for (int j = 0; j < ConfigurationParameters.NUMBER_OF_TEST_THREADS; j++) {
			executor.execute(new TCPClientforServer(ConfigurationParameters.TCP_MULTI_PORT));
		}

		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(TCPMultiServerConnector.PORT, String.valueOf(ConfigurationParameters.TCP_MULTI_PORT));
		// test the connector
		this.setConnector(new TCPMultiServerConnector(configuration, this.createLookupEntityMap()));
		this.initialize();
		this.deserialize(ConfigurationParameters.NUMBER_OF_TEST_THREADS * ConfigurationParameters.SEND_NUMBER_OF_RECORDS, false);

		// shutdown multiple clients
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				Assert.fail(e.getMessage());
			}
		}

		// close the test
		this.close(ConfigurationParameters.NUMBER_OF_TEST_THREADS * ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
	}
}
