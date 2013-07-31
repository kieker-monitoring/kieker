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

package kieker.test.tools.junit.bridge;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

import kieker.tools.bridge.connector.tcp.TCPMultiServerConnector;

/**
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 */

public class TestTCPMultiServerConnector extends AbstractConnectorTest {

	/**
	 * Default constructor
	 */
	public TestTCPMultiServerConnector() {
		// empty constructor
	}

	@Test
	public void testTCPMultiServerConnector() {
		// start multiple record providing clients
		final ExecutorService executor = Executors.newFixedThreadPool(ConfigurationParameters.STARTED_CLIENTS);
		for (int j = 0; j < ConfigurationParameters.STARTED_CLIENTS; j++) {
			executor.execute(new TCPClientforServer(ConfigurationParameters.TCP_MULTI_PORT));
		}

		// run connector test
		this.initialize(new TCPMultiServerConnector(this.createRecordMap(), ConfigurationParameters.TCP_MULTI_PORT));
		this.deserialize(ConfigurationParameters.STARTED_CLIENTS * ConfigurationParameters.SEND_NUMBER_OF_RECORDS);

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
		this.close(ConfigurationParameters.STARTED_CLIENTS * ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
	}
}
