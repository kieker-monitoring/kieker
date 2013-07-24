package kieker.test.tools.junit.bridge;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;

public class TestTCPSingleServerConnector {

	/**
	 * Default constructor
	 */
	public TestTCPSingleServerConnector() {
		// empty constructor
	}

	public void testTCPSingleServerConnector() {

		final Thread firstThread = new Thread(new TCPClientforServer(), "T1");
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
				Assert.assertEquals("Tin is not equal", record.getTin(), ConfigurationParameters.TEST_TIN);
				Assert.assertEquals("Tout is not equal", record.getTout(), ConfigurationParameters.TEST_TOUT);
				Assert.assertEquals("TraceId is not equal", record.getTraceId(), ConfigurationParameters.TEST_TRACE_ID);
				Assert.assertEquals("Eoi is not equal", record.getEoi(), ConfigurationParameters.TEST_EOI);
				Assert.assertEquals("Ess is not equal", record.getEss(), ConfigurationParameters.TEST_ESS);
				Assert.assertEquals("Hostname is not equal", record.getHostname(), ConfigurationParameters.TEST_HOSTNAME);
				Assert.assertEquals("OperationSignature is not equal", record.getOperationSignature(), ConfigurationParameters.TEST_OPERATION_SIGNATURE);
				Assert.assertEquals("SessionId is not equal", record.getSessionId(), ConfigurationParameters.TEST_SESSION_ID);
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
