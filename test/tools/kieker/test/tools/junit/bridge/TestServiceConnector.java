package kieker.test.tools.junit.bridge;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.bridge.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * @author Pascale Brandt
 */

public class TestServiceConnector implements IServiceConnector {

	static public int SEND_NUMBER_OF_RECORDS = 20;

	private final ConnectorTest unitTest;
	private int count = 0;

	public TestServiceConnector(final ConnectorTest unitTest) {
		this.unitTest = unitTest;
	}

	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		if (this.count < SEND_NUMBER_OF_RECORDS) {
			this.unitTest.deserializeCalled();
			this.count++;
			return new OperationExecutionRecord("kieker.bridge", OperationExecutionRecord.NO_SESSION_ID, 1, 0, 0,
					OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS,
					OperationExecutionRecord.NO_EOI_ESS);
		} else {
			throw new ConnectorEndOfDataException("End of data reached");
		}
	}

	/**
	 * This method start setupCalled() in the ConnectorTest to look
	 * for if the setup will be called in the correct way.
	 */
	public void setup() throws ConnectorDataTransmissionException {
		this.unitTest.setupCalled();
	}

	/**
	 * This method does the same thing like the method above but do everything
	 * with the closeCalled() method.
	 */
	public void close() throws ConnectorDataTransmissionException {
		this.unitTest.closeCalled();
	}

}
