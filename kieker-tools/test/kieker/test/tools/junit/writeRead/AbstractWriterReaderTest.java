/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.IMonitoringController;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public abstract class AbstractWriterReaderTest extends AbstractKiekerTest {

	// parameters for the default list of events to use in the test
	private static final String DEFAULT_EVENTS_SESSION_ID = "Mn51D97t0";
	private static final String DEFAULT_EVENTS_HOSTNAME = "srv-LURS0EMw";
	private static final int DEFAULT_EVENTS_NUMBER = 5; // just a basic test with (potentially) at bit more than a hand full of records

	/**
	 * @param numRecordsWritten
	 *
	 * @return An {@link IMonitoringController} initialized with the respective FS Writer.
	 */
	protected abstract IMonitoringController createController(final int numRecordsWritten) throws Exception;

	/**
	 * Checks if the given {@link IMonitoringController} is in the expected state after having passed
	 * the records to the controller.
	 *
	 * @param monitoringController
	 *            The monitoring controller in question.
	 *
	 * @throws Exception
	 *             If something went wrong during the check.
	 */
	protected abstract void checkControllerStateAfterRecordsPassedToController(IMonitoringController monitoringController) throws Exception;

	/**
	 * Checks if the given {@link IMonitoringController} is in the expected state before having passed
	 * the records to the controller.
	 *
	 * @param monitoringController
	 *            The monitoring controller in question.
	 *
	 * @throws Exception
	 *             If something went wrong during the check.
	 */
	protected abstract void checkControllerStateBeforeRecordsPassedToController(IMonitoringController monitoringController) throws Exception;

	/**
	 * Check if the given set of records is as expected.
	 *
	 * @param eventsPassedToController
	 *            The events which have been passed to the controller.
	 * @param eventFromMonitoringLog
	 *            The events from the monitoring log.
	 */
	protected abstract void inspectRecords(List<IMonitoringRecord> eventsPassedToController, List<IMonitoringRecord> eventFromMonitoringLog) throws Exception;

	protected abstract boolean terminateBeforeLogInspection();

	/**
	 * Returns a list of {@link IMonitoringRecord}s to be used in this test. Extending classes can override this method to use their own list of records.
	 *
	 * @return A list of records.
	 */
	protected List<IMonitoringRecord> provideEvents() {
		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		for (int i = 0; i < DEFAULT_EVENTS_NUMBER; i = someEvents.size()) {
			final List<AbstractTraceEvent> nextBatch = Arrays.asList(
					BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(i, i, DEFAULT_EVENTS_SESSION_ID,
							DEFAULT_EVENTS_HOSTNAME).getTraceEvents());
			someEvents.addAll(nextBatch);
		}
		someEvents.add(new EmptyRecord()); // this record used to cause problems (#475)
		return someEvents;
	}

	/**
	 * Returns the list of records read from the previously written monitoring log.
	 *
	 * @return The list of records.
	 *
	 * @throws Exception
	 *             If something went wrong during the reading.
	 */
	protected abstract List<IMonitoringRecord> readEvents() throws Exception;

	/**
	 * Provides implementing classes to do something before reading the log, e.g., manipulating it.
	 *
	 * @throws IOException
	 *             If something went wrong during the manipulation of the log.
	 */
	protected void doBeforeReading() throws IOException {} // NOPMD (empty default implementation)

	/**
	 * The actual Test. Note that this should be the only {@link Test} in this class.
	 *
	 * @throws Exception
	 *             If something went wrong during the test.
	 */
	@Test
	public void testSimpleLog() throws Exception { // NOPMD (JUnitTestsShouldIncludeAssert)
		final List<IMonitoringRecord> someEvents = this.provideEvents();

		// Write batch of records:
		final IMonitoringController ctrl = this.createController(someEvents.size());

		this.checkControllerStateBeforeRecordsPassedToController(ctrl);

		for (final IMonitoringRecord record : someEvents) {
			ctrl.newMonitoringRecord(record);
		}

		Thread.sleep(1000); // wait a second to give the FS writer the chance to write the monitoring log.

		this.checkControllerStateAfterRecordsPassedToController(ctrl);

		if (this.terminateBeforeLogInspection()) {
			// need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
			ctrl.terminateMonitoring();
		}

		this.doBeforeReading();

		final List<IMonitoringRecord> monitoringRecords = this.readEvents();

		// The following line is an easy way to test the tests (given monitoringRecords includes at least one record). But don't forget to deactivate afterwards.
		// monitoringRecords.remove(monitoringRecords.size() - 1);

		this.inspectRecords(someEvents, monitoringRecords);

		if (!this.terminateBeforeLogInspection()) {
			// need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
			ctrl.terminateMonitoring();
		}
	}
}
