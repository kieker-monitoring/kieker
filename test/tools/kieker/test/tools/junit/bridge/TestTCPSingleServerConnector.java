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

import org.junit.Test;

import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;

/**
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
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
	 */
	@Test
	public void testTCPSingleServerConnector() { // NOPMD
		// start one client for the test
		final Thread clientThread = new Thread(new TCPClientforServer(ConfigurationParameters.TCP_SINGLE_PORT), "T1");
		clientThread.start();

		// run test
		this.setConnector(new TCPSingleServerConnector(this.createRecordMap(), ConfigurationParameters.TCP_SINGLE_PORT));
		this.initialize();
		this.deserialize(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
		this.close(ConfigurationParameters.SEND_NUMBER_OF_RECORDS);
	}
}
