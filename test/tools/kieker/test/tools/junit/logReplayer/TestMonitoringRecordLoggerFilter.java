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

package kieker.test.tools.junit.logReplayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.tools.logReplayer.MonitoringRecordLoggerFilter;

import kieker.test.analysis.util.plugin.filter.SimpleSinkFilter;
import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.analysis.util.plugin.reader.SimpleListReader;
import kieker.test.tools.junit.writeRead.filesystem.KiekerLogDirFilter;

/**
 * Tests the {@link MonitoringRecordLoggerFilter}.
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestMonitoringRecordLoggerFilter {
	private static final Log LOG = LogFactory.getLog(TestMonitoringRecordLoggerFilter.class);

	// parameters for the default list of events to use in the test
	private static final String DEFAULT_EVENTS_SESSION_ID = "A5AgX1itaI";
	private static final String DEFAULT_EVENTS_HOSTNAME = "srv-D8yzPpiD";
	private static final int DEFAULT_EVENTS_NUMBER = 5; // just a basic test with (potentially) at bit more than a hand full of records

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private void createControllerConfiguration(final String monitoringPropertiesFn) throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, AsyncFsWriter.class.getName());

		final String asyncFsWriterPropertyPrefix = AsyncFsWriter.class.getName() + ".";

		config.setProperty(asyncFsWriterPropertyPrefix + AbstractAsyncFSWriter.CONFIG_TEMP, Boolean.FALSE.toString());
		try {
			config.setProperty(AsyncFsWriter.class.getName() + "." + AbstractAsyncFSWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}

		// Write configuration to tmp file
		LOG.info("Writing monitoring.properties to file '" + monitoringPropertiesFn + "'");
		config.store(new FileWriter(monitoringPropertiesFn, /* !append */false), "Created by " + TestMonitoringRecordLoggerFilter.class.getName());
	}

	/**
	 * Returns a list of {@link IMonitoringRecord}s to be used in this test.
	 * Extending classes can override this method to use their own list of
	 * records.
	 * 
	 * @return
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
		someEvents.add(new EmptyRecord()); // this record used to cause problems (#475)
		return someEvents;
	}

	private List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogDirFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}

		return this.readLog(monitoringLogs);
	}

	private List<IMonitoringRecord> readLog(final String[] monitoringLogDirs) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES,
				FSReader.CONFIG_PROPERTY_VALUE_IGNORE_UNKNOWN_RECORD_TYPES_DEFAULT);
		final AbstractReaderPlugin reader = new FSReader(readerConfiguration);
		final SimpleSinkFilter<IMonitoringRecord> sinkPlugin = new SimpleSinkFilter<IMonitoringRecord>(new Configuration());

		analysisController.registerReader(reader);
		analysisController.registerFilter(sinkPlugin);
		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);
		analysisController.run();

		return sinkPlugin.getList();
	}

	@Test
	public void testControllerKeepsLoggingTimestamp() throws Exception {
		this.testIt(true); //
	}

	@Test
	public void testControllerSetsLoggingTimestamp() throws Exception {
		this.testIt(false); //
	}

	/**
	 * The actual (parameterized) Test.
	 * 
	 * @throws Exception
	 */
	private void testIt(final boolean keepLoggingTimestamps) throws Exception { // NOPMD (JUnitTestsShouldIncludeAssert)
		final List<IMonitoringRecord> eventsToWrite = this.provideEvents();
		final long firstLoggingTimestamp = eventsToWrite.get(0).getLoggingTimestamp();

		final AnalysisController analysisController = new AnalysisController();

		final SimpleListReader<IMonitoringRecord> reader = new SimpleListReader<IMonitoringRecord>(new Configuration());
		reader.addAllObjects(eventsToWrite);
		analysisController.registerReader(reader);

		final File monitoringProperties = this.tmpFolder.newFile();
		this.createControllerConfiguration(monitoringProperties.getAbsolutePath());

		final Configuration recordLoggingFilterConfiguration = new Configuration();
		recordLoggingFilterConfiguration.setProperty(MonitoringRecordLoggerFilter.CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN, monitoringProperties.getPath());
		recordLoggingFilterConfiguration.setProperty(
				MonitoringRecordLoggerFilter.CONFIG_PROPERTY_NAME_KEEP_LOGGING_TIMESTAMP,
				Boolean.toString(keepLoggingTimestamps));
		final MonitoringRecordLoggerFilter loggerFilter = new MonitoringRecordLoggerFilter(recordLoggingFilterConfiguration);
		analysisController.registerFilter(loggerFilter);
		analysisController.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, loggerFilter, MonitoringRecordLoggerFilter.INPUT_PORT_NAME_RECORD);

		final SimpleSinkFilter<IMonitoringRecord> simpleSinkFilter = new SimpleSinkFilter<IMonitoringRecord>(new Configuration());
		analysisController.registerFilter(simpleSinkFilter);
		analysisController.connect(loggerFilter, MonitoringRecordLoggerFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, simpleSinkFilter, SimpleSinkFilter.INPUT_PORT_NAME);

		analysisController.run();

		final List<IMonitoringRecord> eventsFromLog = this.readEvents();

		/*
		 * The following line is an easy way to test the tests (given monitoringRecords includes at least one record).
		 * But don't forget to deactivate afterwards.
		 */
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
