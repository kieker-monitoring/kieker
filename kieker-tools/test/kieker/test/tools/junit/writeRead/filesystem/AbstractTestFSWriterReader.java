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

package kieker.test.tools.junit.writeRead.filesystem; // NOCS (number outer types)

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.analysis.plugin.reader.filesystem.FSReaderNew;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writernew.AbstractMonitoringWriter;
import kieker.monitoring.writernew.filesystem.AsciiFileWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;
import kieker.test.tools.util.StringUtils;

/**
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public abstract class AbstractTestFSWriterReader extends AbstractWriterReaderTest {

	/**
	 * A rule making sure that a temporary folder exists for every test method (which is removed after the test).
	 */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private volatile Class<? extends AbstractMonitoringWriter> testedWriterClazz = AsciiFileWriter.class;

	protected abstract Class<? extends AbstractMonitoringWriter> getTestedWriterClazz();

	/**
	 * Initializes the setup for the test.
	 *
	 * @throws IOException
	 *             If the setup failed.
	 */
	@Before
	public void setUp() throws IOException {
		this.testedWriterClazz = this.getTestedWriterClazz();
	}

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, this.testedWriterClazz.getName());
		config.setProperty(AsciiFileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());

		config.setProperty(WriterController.RECORD_QUEUE_SIZE, Integer.toString(numRecordsWritten * 4));
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "0");
		// config.setProperty(WriterController.CONFIG_SHUTDOWNDELAY, "-1");

		// Give extending classes the chance to refine the configuration
		this.refineWriterConfiguration(config, numRecordsWritten);

		return MonitoringController.createInstance(config);
	}

	/**
	 * Inheriting classes should use this method to refine and enrich the configuration of the writer.
	 *
	 * @param config
	 *            The configuration to refine.
	 * @param numRecordsWritten
	 *            The number of written records.
	 */
	protected abstract void refineWriterConfiguration(Configuration config, final int numRecordsWritten);

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue("Expected monitoring controller to be enabled", monitoringController.isMonitoringEnabled());
	}

	protected abstract void doSomethingBeforeReading(final String[] monitoringLogs) throws IOException;

	@Override
	protected final void doBeforeReading() throws IOException {
		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogDirFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}
		this.doSomethingBeforeReading(monitoringLogs);
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogDirFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}

		return this.readLog(monitoringLogs);
	}

	/**
	 * Replaces the given search String by the given replacement String in all given files.
	 *
	 * @param dirs
	 *            The directories containing the files in question.
	 * @param findString
	 *            The string to search for.
	 * @param replaceByString
	 *            The string that will be used as a substitution.
	 *
	 * @throws IOException
	 *             If something during the file accesses went wrong.
	 */
	protected void replaceStringInMapFiles(final String[] dirs, final String findString, final String replaceByString) throws IOException {
		for (final String curLogDir : dirs) {
			final String[] mapFilesInDir = new File(curLogDir).list(new KiekerMapFileFilter());
			Assert.assertEquals("Unexpected number of map files", 1, mapFilesInDir.length);

			final String curMapFile = curLogDir + File.separator + mapFilesInDir[0];

			this.searchReplaceInFile(curMapFile, findString, replaceByString);
		}

	}

	/**
	 * Replaces the given search String by the given replacement String in the given file.
	 *
	 * @param filename
	 *            The name of the file to be modified.
	 * @param findString
	 *            The string to search for.
	 * @param replaceByString
	 *            The string that will be used as a substitution.
	 *
	 * @throws IOException
	 *             If something during the file access went wrong.
	 */
	private void searchReplaceInFile(final String filename, final String findString, final String replaceByString) throws IOException {
		final String mapFileContent = StringUtils.readOutputFileAsString(new File(filename));
		final String manipulatedContent = mapFileContent.replaceAll(findString, replaceByString);
		PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(filename), false, FSUtil.ENCODING);
			printStream.print(manipulatedContent);
		} finally {
			if (printStream != null) {
				printStream.close();
			}
		}
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventFromMonitoringLog);
	}

	/**
	 * Inheriting classes can use this method to refine the existing configuration by adding more properties.
	 *
	 * @param config
	 *            The configuration to refine.
	 */
	protected abstract void refineFSReaderConfiguration(Configuration config);

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
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, "false");
		this.refineFSReaderConfiguration(readerConfiguration);
		final AbstractReaderPlugin reader = new FSReaderNew(readerConfiguration, analysisController);
		final ListCollectionFilter<IMonitoringRecord> sinkPlugin = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		analysisController.run();

		return sinkPlugin.getList();
	}
}
