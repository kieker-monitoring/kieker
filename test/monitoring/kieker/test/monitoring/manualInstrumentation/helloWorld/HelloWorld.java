package kieker.test.monitoring.manualInstrumentation.helloWorld;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Andre van Hoorn
 */
public class HelloWorld {

	public static void main(String args[]) {
		System.out.println("Hello");

		final IMonitoringController monitoringController = MonitoringController.getInstance();
		final ITimeSource timeSource = monitoringController.getTimeSource();

		/* recording of the start time of doSomething */
		long startTime = timeSource.getTime();
		doSomething();
		long endTime = timeSource.getTime();
		monitoringController.newMonitoringRecord(new OperationExecutionRecord("kieker.component", "method", 1, startTime, endTime));
	}

	static void doSomething() {
		System.out.println("doing something");
		/* .. some application logic does something meaningful .. */
	}
}
