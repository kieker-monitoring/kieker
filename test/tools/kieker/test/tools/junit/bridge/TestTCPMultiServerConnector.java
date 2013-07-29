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
import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;

/**
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 */

public class TestTCPMultiServerConnector {

	/**
	 * Default constructor
	 */
	public TestTCPMultiServerConnector() {
		// empty constructor
	}

	@Test
	public void TestTCPMultiServerConnector() {

		for (int j = 0; j < ConfigurationParameters.STARTED_CLIENTS; j++) {
			final Thread firstThread = new Thread(new TCPClientforServer(), "T" + j);
			System.out.println("T" + j);
			firstThread.start();

			final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> map = new ConcurrentHashMap<Integer, Class<? extends IMonitoringRecord>>();

			map.put(1, OperationExecutionRecord.class);

			final TCPSingleServerConnector connector = new TCPSingleServerConnector(map, ConfigurationParameters.PORT);

			// Call initialize
			try {
				connector.initialize();
			} catch (final ConnectorDataTransmissionException e) {
				Assert.assertTrue("Mistake in initialize \n" + e.getMessage() + "\n" + ConfigurationParameters.PORT, false);
			}

			// Call deserialize()
			for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
				try {
					final OperationExecutionRecord record = (OperationExecutionRecord) connector.deserializeNextRecord();
					// TODO I assume you swapped the expected and the actual value here (Nils)
					Assert.assertEquals("Tin is not equal", record.getTin(), ConfigurationParameters.TEST_TIN);
					Assert.assertEquals("Tout is not equal", record.getTout(), ConfigurationParameters.TEST_TOUT);
					Assert.assertEquals("TraceId is not equal", record.getTraceId(), ConfigurationParameters.TEST_TRACE_ID);
					Assert.assertEquals("Eoi is not equal", record.getEoi(), ConfigurationParameters.TEST_EOI);
					Assert.assertEquals("Ess is not equal", record.getEss(), ConfigurationParameters.TEST_ESS);
					Assert.assertEquals("Hostname is not equal", record.getHostname(), ConfigurationParameters.TEST_HOSTNAME);
					Assert.assertEquals("OperationSignature is not equal", record.getOperationSignature(), ConfigurationParameters.TEST_OPERATION_SIGNATURE);
					Assert.assertEquals("SessionId is not equal", record.getSessionId(), ConfigurationParameters.TEST_SESSION_ID);
				} catch (final ConnectorDataTransmissionException e) {
					Assert.fail("Mistake in Deserialize " + e.getMessage());
				} catch (final ConnectorEndOfDataException e) {
					Assert.fail("Connector has not terminated" + e.getMessage());
				}
			}

			// Call close() once

			try {
				connector.close();
			} catch (final ConnectorDataTransmissionException e) {
				Assert.fail(e.getMessage());
			}
		}
		// if (!ConfigurationParameters.SEND_NUMBER_OF_RECORDS * ConfigurationParameters.STARTED_CLIENTS == received record) {
		// Assert.assertTrue("Number of send records*started Clients is not equal to number of received records", false);
		// }

	}

}
