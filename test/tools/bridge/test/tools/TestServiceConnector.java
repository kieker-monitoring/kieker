package bridge.test.tools;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.bridge.connector.IServiceConnector;

public class TestServiceConnector implements IServiceConnector {

	public IMonitoringRecord deserialize() throws Exception {

		return new OperationExecutionRecord("kieker.bridge", OperationExecutionRecord.NO_SESSION_ID, 1, 0, 0,
				OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS, OperationExecutionRecord.NO_EOI_ESS);

	}

	public void setup() throws Exception {
		// TODO Auto-generated method stub

	}

	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

}
