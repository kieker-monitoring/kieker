/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.monitoring.manualInstrumentation.storageOnly;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * This is a small test for the part of Kieker that stores monitoring data.
 * Kieker's monitoring API is manually invoked to collect monitoring data.
 * Therefore, the instrumentation and logic in the monitoring points (the
 * aspects) of Kieker is not used.
 * 
 * The main purpose of this test is to isolate configuration and installation
 * problems and to get Kieker running.
 * 
 * Just compile and start it with the Kieker library in the classpath
 * (no javaagent required).
 * 
 * If in kieker.monitoring.properties file system storage (store in database = false) is
 * selected, a new file (kieker*.dat) with monitoring data should be created in
 * the folder specified in kieker.monitoring.properties (default: /tmp).
 * 
 * @author Matthias Rohr
 * 
 *         History: 2008-05-05 small refactoring for first release
 *         2008-03-30 initial version
 */
public final class StorageOnly {
	private static final int NUMBER_OF_EVENTS = 1000;
	private static final IMonitoringController CTRL = MonitoringController.getInstance();
	private static final String VM_NAME = StorageOnly.CTRL.getHostname();

	private StorageOnly() {}

	public static void main(final String[] args) {
		try {
			System.out.println("Starting test by adding " + StorageOnly.NUMBER_OF_EVENTS + " monitoring events");
			for (int i = 0; i < StorageOnly.NUMBER_OF_EVENTS; i++) {
				final OperationExecutionRecord record = new OperationExecutionRecord((i % 2) + "component." +
						(i % 4) + "method", "sessionid", 3333, 123123L, 123124L, StorageOnly.VM_NAME, i, i); // NOCS (MagicNumberCheck)
				StorageOnly.CTRL.newMonitoringRecord(record);
			}
			System.out.println("Sleeping for 8 seconds");
			Thread.sleep(8000); // NOCS (MagicNumberCheck)
			System.out.println(StorageOnly.NUMBER_OF_EVENTS + " more monitoring points");
			for (int i = 0; i < StorageOnly.NUMBER_OF_EVENTS; i++) {
				final OperationExecutionRecord record = new OperationExecutionRecord((i % 2) + "component." +
						(i % 4) + "method", "sessionid", 3333, 123123L, 123124L, StorageOnly.VM_NAME, i + 10000, i); // NOCS (MagicNumberCheck)
				StorageOnly.CTRL.newMonitoringRecord(record);
			}
			System.out.println("Sleeping for 60 seconds");
			Thread.sleep(10000); // NOCS (MagicNumberCheck)
			StorageOnly.CTRL.terminateMonitoring();
		} catch (final InterruptedException ex) {
			System.out.println("Exception:" + ex);
			ex.printStackTrace();
		}
	}
}
