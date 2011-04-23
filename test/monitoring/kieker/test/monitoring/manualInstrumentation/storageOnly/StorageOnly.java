package kieker.test.monitoring.manualInstrumentation.storageOnly;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * This is a small test for the part of Tpmon that stores monitoring data.
 * Tpmon's monitoring API is manually invoked to collect monitoring data.
 * Therefore, the instrumentation and logic in the monitoring points (the
 * aspects) of Tpmon is not used.
 * 
 * The main purpose of this test is to isolate configuration and installation
 * problems and to get Tpmon running.
 * 
 * Just compile and start it with the KiekerTpmonCTW library in the classpath
 * (no javaagent required).
 * 
 * If in tpmon.properties file system storage (store in database = false) is
 * selected, a new file (tpmon*.dat) with monitoring data should be created in
 * the folder specified in tpmon.properties (default: /tmp).
 *
 * @author Matthias Rohr
 * 
 *         History: 2008-05-05 small refactoring for first release
 *         2008-03-30 initial version
 */
public class StorageOnly {
	private static final int numberOfEvents = 1000;
	private static final IMonitoringController ctrl = MonitoringController.getInstance();
	private static final String vmName = StorageOnly.ctrl.getHostName();

	public static void main(final String args[]) {
		try {
			System.out.printf("Starting test by adding %d monitoring events\n", StorageOnly.numberOfEvents);
			for (int i = 0; i < StorageOnly.numberOfEvents; i++) {
				final OperationExecutionRecord record = new OperationExecutionRecord(i % 2 + "component", i % 4 + "method", "sessionid", 3333, 123123L,
						123124L, StorageOnly.ctrl.getHostName(), i, i);
				record.hostName = StorageOnly.vmName;
				StorageOnly.ctrl.newMonitoringRecord(record);
			}
			System.out.println("Sleeping for 8 seconds");
			Thread.sleep(8000);
			System.out.printf("%d more monitoring points\n", StorageOnly.numberOfEvents);
			for (int i = 0; i < StorageOnly.numberOfEvents; i++) {
				final OperationExecutionRecord record = new OperationExecutionRecord(i % 2 + "component", i % 4 + "method", "sessionid", 3333, 123123L,
						123124L, StorageOnly.ctrl.getHostName(), i + 10000, i);
				record.hostName = StorageOnly.vmName;
				StorageOnly.ctrl.newMonitoringRecord(record);
			}
			System.out.println("Sleeping for 60 seconds");
			Thread.sleep(10000);
			StorageOnly.ctrl.terminateMonitoring();
		} catch (final InterruptedException ex) {
			System.out.println("Exception:" + ex);
			ex.printStackTrace();
		}
	}
}
