/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.log.replayer.filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.AsciiLogReader;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.writer.filesystem.FileWriter;
import kieker.tools.log.replayer.filter.MonitoringRecordLoggerFilter;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.junit.writeRead.filesystem.KiekerLogDirFilter;

/**
 * Tests the {@link MonitoringRecordLoggerFilter}.
 *
 * @author Andre van Hoorn
 *
 * @since 1.6
 */
public class TestMonitoringRecordLoggerFilter extends AbstractKiekerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestMonitoringRecordLoggerFilter.class);

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

	@Before
	public void before() throws IOException {
		this.tmpFolder.create();
	}

	@After
	public void after() {
		this.tmpFolder.delete();
	}

	private void createControllerConfiguration(final String monitoringPropertiesFn) throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationKeys.WRITER_CLASSNAME, FileWriter.class.getName());

		config.setProperty(FileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());

		// Write configuration to tmp file
		LOGGER.info("Writing monitoring.properties to file '{}'", monitoringPropertiesFn);

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
		final List<IMonitoringRecord> someEvents = new ArrayList<>();
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

	@Test
	public void testControllerKeepsLoggingTimestamp() throws Exception {
		final List<IMonitoringRecord> eventsToWrite = this.provideEvents();
		// The following line is an easy way to test the tests (given monitoringRecords includes at least one record). But don't forget to deactivate afterwards.
		// eventsToWrite.remove(eventsToWrite.size() - 1);
		final List<IMonitoringRecord> eventsFromRecordLoggerFilter = this.testIt(eventsToWrite, true); // includes Assert(s)
		Assert.assertEquals("Unexpected set of records relayed by filter", eventsToWrite, eventsFromRecordLoggerFilter);

		final List<IMonitoringRecord> eventsFromLog = this.readEvents();
		Assert.assertEquals("Unexpected set of records in monitoring log", eventsToWrite, eventsFromLog);

		Assert.assertThat(eventsFromLog.get(0).getLoggingTimestamp(), CoreMatchers.is(eventsToWrite.get(0).getLoggingTimestamp()));
	}

	@Test
	public void testControllerSetsLoggingTimestamp() throws Exception {
		final List<IMonitoringRecord> eventsToWrite = this.provideEvents();
		// The following line is an easy way to test the tests (given monitoringRecords includes at least one record). But don't forget to deactivate afterwards.
		// eventsToWrite.remove(eventsToWrite.size() - 1);

		final List<IMonitoringRecord> eventsFromRecordLoggerFilter = this.testIt(eventsToWrite, false); // includes Assert(s)
		Assert.assertEquals("Unexpected set of records relayed by filter", eventsToWrite, eventsFromRecordLoggerFilter);

		final List<IMonitoringRecord> eventsFromLog = this.readEvents();
		Assert.assertEquals("Unexpected set of records in monitoring log", eventsToWrite, eventsFromLog);

		Assert.assertThat(eventsFromLog.get(0).getLoggingTimestamp(), CoreMatchers.is(eventsToWrite.get(0).getLoggingTimestamp()));
	}

	/**
	 * The actual (parameterized) Test.
	 *
	 * @param eventsToWrite
	 *
	 * @return
	 *
	 * @throws Exception
	 *             If something went wrong during the test.
	 */
	@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
	private List<IMonitoringRecord> testIt(final List<IMonitoringRecord> eventsToWrite, final boolean keepLoggingTimestamps) throws Exception {
		final AnalysisController analysisController = new AnalysisController();

		final ListReader<IMonitoringRecord> reader = new ListReader<>(new Configuration(), analysisController);
		reader.addAllObjects(eventsToWrite);

		Assert.assertTrue(this.tmpFolder.getRoot().exists());
		final File monitoringProperties = this.tmpFolder.newFile();

		this.createControllerConfiguration(monitoringProperties.getAbsolutePath());

		final Configuration recordLoggingFilterConfiguration = new Configuration();

		recordLoggingFilterConfiguration.setProperty(MonitoringRecordLoggerFilter.CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN, monitoringProperties.getPath());
		recordLoggingFilterConfiguration.setProperty(
				ConfigurationKeys.AUTO_SET_LOGGINGTSTAMP,
				Boolean.toString(!keepLoggingTimestamps));

		final MonitoringRecordLoggerFilter loggerFilter = new MonitoringRecordLoggerFilter(recordLoggingFilterConfiguration, analysisController);

		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, loggerFilter, MonitoringRecordLoggerFilter.INPUT_PORT_NAME_RECORD);

		final ListCollectionFilter<IMonitoringRecord> simpleSinkFilter = new ListCollectionFilter<>(new Configuration(), analysisController);

		analysisController.connect(
				loggerFilter, MonitoringRecordLoggerFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				simpleSinkFilter, ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.run();

		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		Assert.assertTrue(this.tmpFolder.getRoot().exists());
		Assert.assertTrue(monitoringProperties.exists());
		return simpleSinkFilter.getList();
	}

	private List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
			LOGGER.warn("An exception occurred", e);
		}

		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogDirFilter());

		Assert.assertNotNull(monitoringLogs);

		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}

		return this.readLog(monitoringLogs);
	}

	private List<IMonitoringRecord> readLog(final String[] monitoringLogDirs) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, "false");
		final AbstractReaderPlugin reader = new AsciiLogReader(readerConfiguration, analysisController);
		final ListCollectionFilter<IMonitoringRecord> sinkPlugin = new ListCollectionFilter<>(new Configuration(), analysisController);

		analysisController.connect(reader, AsciiLogReader.OUTPUT_PORT_NAME_RECORDS, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		return sinkPlugin.getList();
	}
}
