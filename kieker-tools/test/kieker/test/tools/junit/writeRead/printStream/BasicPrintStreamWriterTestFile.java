/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.printStream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writer.print.PrintStreamWriter;

import kieker.test.tools.junit.writeRead.TestDataRepository;
import kieker.test.tools.junit.writeRead.TestProbe;
import kieker.test.tools.util.StringUtils;

/**
 * @author Andre van Hoorn, Christian Wulf
 *
 * @since 1.5
 */
public class BasicPrintStreamWriterTestFile {

	private static final String OUTPUT_BASE_FN = "S0fYvPsI.out"; // the name doesn't matter
	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final int TIMEOUT_IN_MS = 0;
	/** This constant contains the correct line separator for the current system. */
	private static final String SYSTEM_NEWLINE_STRING = System.getProperty("line.separator");

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (must be public)

	/**
	 * Default constructor.
	 */
	public BasicPrintStreamWriterTestFile() {
		// empty default constructor
	}

	@Test
	public void logFileShouldContainEachRecord() throws Exception {
		// 1. define records to be triggered by the test probe
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestRecords();
		final File outputFile = this.tmpFolder.newFile(BasicPrintStreamWriterTestFile.OUTPUT_BASE_FN);

		this.executeTest(records, outputFile.getAbsolutePath());

		// 8. compare actual and expected records
		final String fileContentAsString = StringUtils.readOutputFileAsString(outputFile);

		for (final IMonitoringRecord rec : records) {
			// note that this format needs to be adjusted if the writer's format changes
			final StringBuilder inputRecordStringBuilder = new StringBuilder()
					.append(rec.getClass().getSimpleName())
					.append(": ")
					.append(rec).append(SYSTEM_NEWLINE_STRING);
			final String curLine = inputRecordStringBuilder.toString();

			// "Record '" + curLine + "' not found in output stream: '" + outputString + "'"
			Assert.assertThat(fileContentAsString, CoreMatchers.containsString(curLine));
		}
	}

	@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
	private List<IMonitoringRecord> executeTest(final List<IMonitoringRecord> records, final String outputFileName) throws InterruptedException {
		// 2. define monitoring config
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(WriterController.RECORD_QUEUE_SIZE, "128");
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, PrintStreamWriter.class.getName());
		config.setProperty(PrintStreamWriter.STREAM, outputFileName);
		final MonitoringController monitoringController = MonitoringController.createInstance(config);

		// 4. trigger records
		final TestProbe testProbe = new TestProbe(monitoringController);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		testProbe.triggerRecords(records);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());

		// 5. terminate monitoring
		monitoringController.terminateMonitoring();

		// 6. wait for termination
		monitoringController.waitForTermination(TIMEOUT_IN_MS);

		// 7. read actual records
		// We cannot do anything meaningful here, because there's nothing like a PrintStreamReader.
		// We'll return an empty List and use our own buffer when evaluating the result.
		return new ArrayList<IMonitoringRecord>();
	}

}
