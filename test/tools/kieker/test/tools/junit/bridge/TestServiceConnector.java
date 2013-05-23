package kieker.test.tools.junit.bridge;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * @author Pascale Brandt
 */

public class TestServiceConnector implements IServiceConnector {

	private final ConnectorTest unitTest;

	public TestServiceConnector(final ConnectorTest unitTest) {
		this.unitTest = unitTest;
	}

	public IMonitoringRecord deserialize() throws Exception {

		this.unitTest.deserializeCalled();

		return new OperationExecutionRecord("kieker.bridge", OperationExecutionRecord.NO_SESSION_ID, 1, 0, 0,
				OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS,
				OperationExecutionRecord.NO_EOI_ESS);

	}

	/**
	 * This method start setupCalled() in the ConnectorTest to look
	 * for if the setup will be called in the correct way.
	 */
	public void setup() throws Exception {
		this.unitTest.setupCalled();
	}

	/**
	 * This method does the same thing like the method above but do everything
	 * with the closeCalled() method.
	 */
	public void close() throws Exception {
		this.unitTest.closeCalled();
	}

}
