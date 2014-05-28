/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.tools.junit.logReplayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kicker.analysis.AnalysisController;
import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.analysis.plugin.filter.forward.ListCollectionFilter;
import kicker.analysis.plugin.reader.AbstractReaderPlugin;
import kicker.analysis.plugin.reader.filesystem.FSReader;
import kicker.analysis.plugin.reader.list.ListReader;
import kicker.common.configuration.Configuration;
import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.common.record.IMonitoringRecord;
import kicker.common.record.flow.trace.AbstractTraceEvent;
import kicker.common.record.misc.EmptyRecord;
import kicker.monitoring.core.configuration.ConfigurationFactory;
import kicker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kicker.monitoring.writer.filesystem.AsyncFsWriter;
import kicker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kicker.test.common.junit.AbstractKickerTest;
import kicker.test.tools.junit.writeRead.filesystem.KickerLogDirFilter;
import kicker.tools.logReplayer.filter.MonitoringRecordLoggerFilter;

/**
 * Tests the {@link MonitoringRecordLoggerFilter}.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class TestMonitoringRecordLoggerFilter extends AbstractKickerTest {
	private static final Log LOG = LogFactory.getLog(TestMonitoringRecordLoggerFilter.class);

	// parameters for the default list of events to use in the test
	private static final String DEFAULT_EVENTS_SESSION_ID = "A5AgX1itaI";
	private static final String DEFAULT_EVENTS_HOSTNAME = "srv-D8yzPpiD";
	private static final int DEFAULT_EVENTS_NUMBER = 5; // just a basic test with (potentially) at bit more than a hand full of records

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	/**
	 * Default constructor.
	 */
	public TestMonitoringRecordLoggerFilter() {
		// empty default constructor
	}

	private void createControllerConfiguration(final String monitoringPropertiesFn) throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, AsyncFsWriter.class.getName());

		config.setProperty(AsyncFsWriter.class.getName() + "." + AbstractAsyncFSWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());

		// Write configuration to tmp file
		LOG.info("Writing monitoring.properties to file '" + monitoringPropertiesFn + "'");

		OutputStream os = null;
		try {
			os = new FileOutputStream(monitoringPropertiesFn, false); // !append
			config.store(os, "Created by " + TestMonitoringRecordLoggerFilter.class.getName());
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

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
			// note that the loggingTimestamp is not set (i.e., it is -1)
			someEvents.addAll(nextBatch);
		}
		someEvents.add(new EmptyRecord());
		return someEvents;
	}

	private List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KickerLogDirFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}

		return this.readLog(monitoringLogs);
	}

	private List<IMonitoringRecord> readLog(final String[] monitoringLogDirs) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, "false");
		final AbstractReaderPlugin reader = new FSReader(readerConfiguration, analysisController);
		final ListCollectionFilter<IMonitoringRecord> sinkPlugin = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		return sinkPlugin.getList();
	}

	@Test
	public void testControllerKeepsLoggingTimestamp() throws Exception {
		Assert.assertTrue(true); // just to get rid of strange PMD behavior
		this.testIt(true); // includes Assert(s)
	}

	@Test
	public void testControllerSetsLoggingTimestamp() throws Exception {
		Assert.assertTrue(true); // just to get rid of strange PMD behavior
		this.testIt(false); // includes Assert(s)
	}

	/**
	 * The actual (parameterized) Test.
	 * 
	 * @throws Exception
	 *             If something went wrong during the test.
	 */
	private void testIt(final boolean keepLoggingTimestamps) throws Exception { // NOPMD (JUnitTestsShouldIncludeAssert)
		final List<IMonitoringRecord> eventsToWrite = this.provideEvents();
		final long firstLoggingTimestamp = eventsToWrite.get(0).getLoggingTimestamp();

		final AnalysisController analysisController = new AnalysisController();

		final ListReader<IMonitoringRecord> reader = new ListReader<IMonitoringRecord>(new Configuration(), analysisController);
		reader.addAllObjects(eventsToWrite);

		final File monitoringProperties = this.tmpFolder.newFile();
		this.createControllerConfiguration(monitoringProperties.getAbsolutePath());

		final Configuration recordLoggingFilterConfiguration = new Configuration();
		recordLoggingFilterConfiguration.setProperty(MonitoringRecordLoggerFilter.CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN, monitoringProperties.getPath());
		recordLoggingFilterConfiguration.setProperty(
				ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP,
				Boolean.toString(!keepLoggingTimestamps));
		final MonitoringRecordLoggerFilter loggerFilter = new MonitoringRecordLoggerFilter(recordLoggingFilterConfiguration, analysisController);

		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, loggerFilter, MonitoringRecordLoggerFilter.INPUT_PORT_NAME_RECORD);

		final ListCollectionFilter<IMonitoringRecord> simpleSinkFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(loggerFilter, MonitoringRecordLoggerFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, simpleSinkFilter,
				ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<IMonitoringRecord> eventsFromLog = this.readEvents();

		// The following line is an easy way to test the tests (given monitoringRecords includes at least one record). But don't forget to deactivate afterwards.
		// eventsToWrite.remove(eventsToWrite.size() - 1);

		Assert.assertEquals("Unexpected set of records in monitoring log", eventsToWrite, eventsFromLog);

		Assert.assertEquals("Unexpected set of records relayed by filter", eventsToWrite, simpleSinkFilter.getList());

		if (keepLoggingTimestamps) {
			Assert.assertEquals("Expected logging timestamps to be untouched by the controller", firstLoggingTimestamp, eventsFromLog.get(0).getLoggingTimestamp());
		} else {
			// note that firstLoggingTimestamp is actually -1 for each record in this test
			Assert.assertTrue("Expected logging timestamps to be untouched by the controller", firstLoggingTimestamp != eventsFromLog.get(0).getLoggingTimestamp());
		}
	}
}
