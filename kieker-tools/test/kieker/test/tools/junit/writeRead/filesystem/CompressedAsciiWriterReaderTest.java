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

package kieker.test.tools.junit.writeRead.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.AsciiReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writernew.filesystem.AsciiFileWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class CompressedAsciiWriterReaderTest extends AbstractWriterReaderTest {

	private static final boolean FLUSH = false;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private final boolean shouldDecompress = true;

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws Exception {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, AsciiFileWriter.class.getName());

		config.setProperty(WriterController.RECORD_QUEUE_SIZE, Integer.toString(numRecordsWritten * 4));
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "0");

		config.setProperty(AsciiFileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());
		config.setProperty(AsciiFileWriter.CONFIG_FLUSH, Boolean.toString(FLUSH));
		config.setProperty(AsciiFileWriter.CONFIG_SHOULD_COMPRESS, Boolean.toString(this.shouldDecompress));

		return MonitoringController.createInstance(config);
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue("Expected monitoring controller to be enabled", monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) throws Exception {
		Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventFromMonitoringLog);
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return !CompressedAsciiWriterReaderTest.FLUSH;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws Exception {
		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogDirFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}

		return this.readLog(monitoringLogs);
	}

	/**
	 * This method can be used to read monitoring records from the given directories.
	 *
	 * @param monitoringLogDirs
	 *            The directories containing the monitoring logs.
	 * @return A list containing all monitoring records.
	 *
	 * @throws AnalysisConfigurationException
	 *             If something went wrong during the reading.
	 */
	protected List<IMonitoringRecord> readLog(final String[] monitoringLogDirs) throws AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(AsciiReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(AsciiReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, "false");
		readerConfiguration.setProperty(AsciiReader.CONFIG_SHOULD_DECOMPRESS, String.valueOf(this.shouldDecompress));

		final AbstractReaderPlugin reader = new AsciiReader(readerConfiguration, analysisController);
		final ListCollectionFilter<IMonitoringRecord> sinkPlugin = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(reader, AsciiReader.OUTPUT_PORT_NAME_RECORDS, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		analysisController.run();

		return sinkPlugin.getList();
	}

	@Override
	protected void doBeforeReading() throws IOException {
		super.doBeforeReading();
		try {
			// workaround: sleep to give the writer time to write out all records before the reader tries to read it.
			// better: wait for the monitoring controller and its sub controllers to terminate.
			Thread.sleep(1000);
		} catch (final InterruptedException e) {
			// do nothing
		}
	}
	// @Test
	// public void testPlainCommunication() throws Exception {
	// this.shouldDecompress = false;
	// this.testSimpleLog();
	// }
	//
	// @Test
	// public void testCompressedCommunication() throws Exception {
	// this.shouldDecompress = true;
	// this.testSimpleLog();
	// }
}
