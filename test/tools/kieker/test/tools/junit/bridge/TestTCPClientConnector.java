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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.tcp.TCPClientConnector;

import kieker.test.tools.junit.bridge.TCPServerForClient.TestTCPConfiguration;

/**
 * Test the TCPClientConnector, by creating a TCP server, which provides Kieker IMonitoringRecords to the connector.
 * This test class starts the server, which will then wait for a connection from the client. After connection is sends
 * SEND_NUMBER_OF_RECORDS records and terminates the connection.
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 */
public class TestTCPClientConnector {

	/**
	 * Test all methods of {@link kieker.tools.bridge.connector.tcp.TCPClientConnector}.
	 * Testing single methods do not work, as the connector is stateful.
	 */
	@Test
	public void testTCPClientConnector() {

		final Thread firstThread = new Thread(new TCPServerForClient(), "T1");
		firstThread.start();
		// setup connector, requires a record map
		final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> map = new ConcurrentHashMap<Integer, Class<? extends IMonitoringRecord>>();

		map.put(1, OperationExecutionRecord.class);

		final TCPClientConnector connector = new TCPClientConnector(map, ConfigurationParameters.HOSTNAME, ConfigurationParameters.PORT);

		// Call initialize() once
		try {
			connector.initialize();
		} catch (final ConnectorDataTransmissionException e) {
			Assert.assertTrue("Mistake in initialize \n" + e.getMessage() + "\n" + ConfigurationParameters.PORT, false);
		}

		try {
			Thread.sleep(10000);
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
		}

		// Call deserialize()
		for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
			try {
				final OperationExecutionRecord record = (OperationExecutionRecord) connector.deserializeNextRecord();
				Assert.assertEquals("Tin is not equal", record.getTin(), TestTCPConfiguration.testTin);
				Assert.assertEquals("Tout is not equal", record.getTout(), TestTCPConfiguration.testTout);
				Assert.assertEquals("TraceId is not equal", record.getTraceId(), TestTCPConfiguration.testTraceId);
				Assert.assertEquals("Eoi is not equal", record.getEoi(), TestTCPConfiguration.testEoi);
				Assert.assertEquals("Ess is not equal", record.getEss(), TestTCPConfiguration.testEss);
				Assert.assertEquals("Hostname is not equal", record.getHostname(), TestTCPConfiguration.testHostName);
				Assert.assertEquals("OperationSignature is not equal", record.getOperationSignature(), TestTCPConfiguration.testOperationSignature);
				Assert.assertEquals("SessionId is not equal", record.getSessionId(), TestTCPConfiguration.testSessionId);
			} catch (final ConnectorDataTransmissionException e) {
				Assert.assertTrue("Mistake in Deserialize \n" + e.getMessage(), false);
			} catch (final ConnectorEndOfDataException e) {
				Assert.assertTrue("Connector has not terminated" + e.getMessage(), false);
			}
		}

		// Call close() once

		try {
			connector.close();
		} catch (final ConnectorDataTransmissionException e) {
			Assert.fail(e.getMessage());
		}
	}
}
