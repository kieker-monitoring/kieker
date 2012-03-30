/***************************************************************************
 * Copyright 2012 by
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

package kieker.test.tools.junit.writeRead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;

import org.junit.Test;

/**
 * TODO: The idea is to make this class independent of FS, i.e., to provide a basic
 * test for each writer/reader
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractWriterReaderTest {

	// parameters for the default list of events to use in the test
	private static final String DEFAULT_EVENTS_SESSION_ID = "Mn51D97t0";
	private static final String DEFAULT_EVENTS_HOSTNAME = "srv-LURS0EMw";
	private static final int DEFAULT_EVENTS_NUMBER = 5; // just a basic test with (potentially) at bit more than a hand full of records

	/**
	 * Returns an {@link IMonitoringController} initialized with the respective FS Writer.
	 * 
	 * @param numRecordsWritten
	 * @return
	 */
	protected abstract IMonitoringController createController(final int numRecordsWritten) throws Exception;

	/**
	 * Checks if the given {@link IMonitoringController} is in the expected state after having passed
	 * the records to the controller.
	 * 
	 * @param monitoringController
	 * @throws Exception
	 */
	protected abstract void checkControllerStateAfterRecordsPassedToController(IMonitoringController monitoringController) throws Exception;

	/**
	 * Check if the given set of records is as expected.
	 * 
	 * @param monitoringRecords
	 */
	protected abstract void inspectRecords(List<IMonitoringRecord> eventsPassedToController, List<IMonitoringRecord> eventFromMonitoringLog) throws Exception;

	protected abstract boolean terminateBeforeLogInspection();

	/**
	 * Returns a list of {@link IMonitoringRecord}s to be used in this test.
	 * Extending classes can override this method to use their own list of
	 * records.
	 * 
	 * @return
	 */
	protected List<IMonitoringRecord> provideEvents() {
		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		for (int i = 0; i < AbstractWriterReaderTest.DEFAULT_EVENTS_NUMBER; i = someEvents.size()) {
			final List<AbstractTraceEvent> nextBatch =
					BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(i, i, AbstractWriterReaderTest.DEFAULT_EVENTS_SESSION_ID,
							AbstractWriterReaderTest.DEFAULT_EVENTS_HOSTNAME).eventList();
			someEvents.addAll(nextBatch);
		}
		someEvents.add(new EmptyRecord()); // this record used to cause problems (#475)
		return someEvents;
	}

	/**
	 * Returns the list of records read from the previously written monitoring log.
	 * 
	 * @return
	 * @throws AnalysisConfigurationException
	 */
	protected abstract List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException;

	/**
	 * Provides implementing classes to do something before reading the log, e.g., manipulating it.
	 * 
	 * @throws IOException
	 */
	protected void doBeforeReading() throws IOException {}

	/**
	 * The actual Test. Note that this should be the only {@link Test} in this class.
	 * 
	 * @throws InterruptedException
	 * @throws AnalysisConfigurationException
	 * @throws Exception
	 */
	@Test
	public void testSimpleLog() throws Exception { // NOPMD (JUnitTestsShouldIncludeAssert)
		final List<IMonitoringRecord> someEvents = this.provideEvents();

		/*
		 * Write batch of records:
		 */
		final IMonitoringController ctrl = this.createController(someEvents.size());
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

		/*
		 * The following line is an easy way to test the tests (given monitoringRecords includes at least one record).
		 * But don't forget to deactivate afterwards.
		 */
		// monitoringRecords.remove(monitoringRecords.size() - 1);

		this.inspectRecords(someEvents, monitoringRecords);

		if (!this.terminateBeforeLogInspection()) {
			// need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
			ctrl.terminateMonitoring();
		}
	}
}
