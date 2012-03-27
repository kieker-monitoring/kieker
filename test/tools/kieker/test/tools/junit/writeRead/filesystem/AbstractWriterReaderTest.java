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

package kieker.test.tools.junit.writeRead.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * TODO: The idea is to make this class independent of FS, i.e., to provide a basic
 * test for each writer/reader
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractWriterReaderTest extends TestCase {
	@Rule
	private final TemporaryFolder tmpFolder = new TemporaryFolder();

	@Override
	@Before
	public void setUp() throws IOException {
		this.tmpFolder.create();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		this.tmpFolder.delete();
	}

	/**
	 * Returns an {@link IMonitoringController} initialized with the respective FS Writer.
	 * 
	 * @param numRecordsWritten
	 * @param monitoringLogBaseDir
	 * @return
	 * @throws IOException
	 */
	protected abstract IMonitoringController createController(final int numRecordsWritten, final File monitoringLogBaseDir) throws IOException;

	/**
	 * Checks if the given {@link IMonitoringController} is in the expected state after having written
	 * the records.
	 * 
	 * @param monitoringController
	 */
	protected abstract void checkControllerState(IMonitoringController monitoringController);

	/**
	 * Check if the given set of records is as expected.
	 * 
	 * @param monitoringRecords
	 */
	protected abstract void inspectRecords(List<IMonitoringRecord> eventsPassedToController, List<IMonitoringRecord> eventFromMonitoringLog);

	protected abstract boolean terminateBeforeLogInspection();

	private List<IMonitoringRecord> readLog(final String[] monitoringLogDirs) {
		final AnalysisController analysisController = new AnalysisController();
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_RECORD_TYPE_SELECTION, FSReader.CONFIG_VALUE_NAME_RECORD_TYPE_SELECTION_ANY);
		final AbstractReaderPlugin reader = new FSReader(readerConfiguration);
		final SimpleSinkPlugin<IMonitoringRecord> sinkPlugin = new SimpleSinkPlugin<IMonitoringRecord>(new Configuration());

		analysisController.registerReader(reader);
		analysisController.registerFilter(sinkPlugin);
		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		analysisController.run();

		return sinkPlugin.getList();
	}

	/**
	 * The actual Test. Note that this should be the only {@link Test} in this class.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testSimpleLog() throws IOException, InterruptedException { // NOPMD (JUnitTestsShouldIncludeAssert)
		final String sessionId = "Mn51D97t0";
		final String hostname = "srv-LURS0EMw";

		final int minNumberOfEventsToGenerate = 5; // just a basic test with (potentially) at bit more than a hand full of records

		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		for (int i = 0; i < minNumberOfEventsToGenerate; i = someEvents.size()) {
			final List<AbstractTraceEvent> nextBatch =
					BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(i, i, sessionId, hostname).eventList();
			someEvents.addAll(nextBatch);
		}

		/*
		 * Write batch of records:
		 */
		final IMonitoringController ctrl = this.createController(someEvents.size(), this.tmpFolder.getRoot());
		for (final IMonitoringRecord record : someEvents) {
			ctrl.newMonitoringRecord(record);
		}

		Thread.sleep(1000); // wait a second to give the FS writer the chance to write the monitoring log.

		this.checkControllerState(ctrl);

		if (this.terminateBeforeLogInspection()) {
			// need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
			ctrl.terminateMonitoring();
		}

		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogDirFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}

		final List<IMonitoringRecord> monitoringRecords = this.readLog(monitoringLogs);

		this.inspectRecords(someEvents, monitoringRecords);

		if (!this.terminateBeforeLogInspection()) {
			// need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
			ctrl.terminateMonitoring();
		}
	}
}

/**
 * Accepts Kieker file system monitoring logs.
 * 
 * @author Andre van Hoorn
 * 
 */
class KiekerLogDirFilter implements FilenameFilter { // NOPMD (TestClassWithoutTestCases)
	public static final String LOG_DIR_PREFIX = "kieker-"; // TODO: do we have this constant in the FS Writer(s)?
	public static final String MAP_FILENAME = "kieker.map"; // TODO: do we have this constant in the FS Writer(s)?

	public boolean accept(final File dir, final String name) {
		if (!name.startsWith(KiekerLogDirFilter.LOG_DIR_PREFIX)) {
			return false;
		}

		final String potentialDirFn = dir.getAbsolutePath() + File.separatorChar + name;

		final File potentialDir = new File(potentialDirFn);

		if (!potentialDir.isDirectory()) {
			return false;
		}

		final String[] kiekerMapFiles = potentialDir.list(new FilenameFilter() {
			/**
			 * Accepts directories containing a `kieker.map` file.
			 */
			public boolean accept(final File dir, final String name) {
				return name.equals(KiekerLogDirFilter.MAP_FILENAME);
			}
		});
		return kiekerMapFiles.length == 1;
	}
}
