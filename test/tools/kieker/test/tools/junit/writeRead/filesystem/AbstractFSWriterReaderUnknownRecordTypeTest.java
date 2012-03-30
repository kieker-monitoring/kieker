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

package kieker.test.tools.junit.writeRead.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.BranchingRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.monitoring.writer.filesystem.SyncFsWriter;

/**
 * 
 * @author André van Hoorn
 * 
 */
public class AbstractFSWriterReaderUnknownRecordTypeTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases) // NOCS (MissingCtorCheck)
	private static final boolean FLUSH = true;

	private static final EmptyRecord EVENT0_KNOWN_TYPE = new EmptyRecord();
	private static final BranchingRecord EVENT1_UNKNOWN_TYPE = new BranchingRecord();
	private static final EmptyRecord EVENT2_KNOWN_TYPE = new EmptyRecord();
	private static final EmptyRecord EVENT3_KNOWN_TYPE = new EmptyRecord();

	private static final Class<? extends IMonitoringRecord> EVENT_CLASS_TO_MANIPULATE = EVENT2_KNOWN_TYPE.getClass();

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncFsWriter.class;
	}

	@Override
	protected List<IMonitoringRecord> provideEvents() {
		final List<IMonitoringRecord> events = new ArrayList<IMonitoringRecord>();
		events.add(EVENT0_KNOWN_TYPE);
		events.add(EVENT1_UNKNOWN_TYPE);
		events.add(EVENT2_KNOWN_TYPE);
		events.add(EVENT3_KNOWN_TYPE);
		return events;
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		// FIXME: continue!
		// super.inspectRecords(eventsPassedToController, eventFromMonitoringLog);
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return !AbstractFSWriterReaderUnknownRecordTypeTest.FLUSH;
	}

	@Override
	protected void refineConfiguration(final Configuration config, final int numRecordsWritten) {
		config.setProperty(this.getClass().getName() + "." + SyncFsWriter.CONFIG_FLUSH, Boolean.toString(AbstractFSWriterReaderUnknownRecordTypeTest.FLUSH));
		// TODO: additional configuration parameters
	}

	@Override
	protected void doSomethingBeforeReading(final String[] monitoringLogs) throws IOException {
		final String classnameToManipulate = EVENT_CLASS_TO_MANIPULATE.getName();
		this.replaceStringInMapFiles(monitoringLogs, classnameToManipulate, classnameToManipulate + "XYZ");
	}
}
