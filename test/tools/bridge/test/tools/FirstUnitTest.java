package bridge.test.tools;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Pascale Brandt
 */

public class FirstUnitTest extends AbstractKiekerTest {

	private FirstUnitTest() {}

	public static void main(final String[] args) {

		final IMonitoringController monitoringController = MonitoringController.getInstance();
		final ITimeSource timeSource = monitoringController.getTimeSource();

		final long startTime = timeSource.getTime();
		FirstUnitTest.TestConnector();
		final long endTime = timeSource.getTime();

		/* This start a new record */
		monitoringController.newMonitoringRecord(FirstUnitTest.createOperationExecutionRecord("kieker.component.method()", 1, startTime, endTime));
	}

	private static void TestConnector() {

		// System.out.println("blabla");
	}

	private static OperationExecutionRecord createOperationExecutionRecord(final String opString, final long traceId, final long tin, final long tout) {

		return new OperationExecutionRecord(opString, OperationExecutionRecord.NO_SESSION_ID, traceId, tin, tout, OperationExecutionRecord.NO_HOSTNAME,
				OperationExecutionRecord.NO_EOI_ESS, OperationExecutionRecord.NO_EOI_ESS);
	}
}
