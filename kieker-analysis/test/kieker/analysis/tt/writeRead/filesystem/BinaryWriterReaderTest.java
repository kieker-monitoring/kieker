/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.tt.writeRead.filesystem;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.tt.reader.filesystem.fsReader.BinaryLogReader;
import kieker.analysis.tt.writeRead.TestDataRepository;
import kieker.analysis.tt.writeRead.TestProbe;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writer.compression.NoneCompressionFilter;
import kieker.monitoring.writer.compression.ZipCompressionFilter;
import kieker.monitoring.writer.filesystem.BinaryLogStreamHandler;
import kieker.monitoring.writer.filesystem.FileWriter;

import teetime.framework.test.StageTester;

/**
 * @author Christian Wulf, Lars Bluemke
 *
 * @since 1.13
 */
public class BinaryWriterReaderTest {

	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final int TIMEOUT_IN_MS = 0;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	public BinaryWriterReaderTest() {
		super();
	}

	@Test
	public void testUncompressedBinaryCommunication() throws Exception {
		// 1. define records to be triggered by the test probe
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testBinaryCommunication(records, false);

		// 8. compare actual and expected records
		Assert.assertThat(analyzedRecords, CoreMatchers.is(CoreMatchers.equalTo(records)));
	}

	@Test
	public void testCompressedBinaryCommunication() throws Exception {
		// 1. define records to be triggered by the test probe
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testBinaryCommunication(records, true);

		// 8. compare actual and expected records
		Assert.assertThat(analyzedRecords, CoreMatchers.is(CoreMatchers.equalTo(records)));
	}

	@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
	private List<IMonitoringRecord> testBinaryCommunication(final List<IMonitoringRecord> records, final boolean shouldDecompress) throws Exception {
		final String compressionFilter = shouldDecompress ? ZipCompressionFilter.class.getName() : NoneCompressionFilter.class.getName(); // NOCS (declarative)

		// 2. define monitoring config
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, FileWriter.class.getName());
		config.setProperty(WriterController.RECORD_QUEUE_SIZE, "128");
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");
		config.setProperty(FileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());
		config.setProperty(FileWriter.CONFIG_COMPRESSION_FILTER, compressionFilter);
		config.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, BinaryLogStreamHandler.class.getCanonicalName());
		final MonitoringController monitoringController = MonitoringController.createInstance(config);

		// 3. initialize the reader
		final String[] monitoringLogDirs = TEST_DATA_REPOSITORY.getAbsoluteMonitoringLogDirNames(this.tmpFolder.getRoot());
		final BinaryLogReader binaryLogReader = new BinaryLogReader(monitoringLogDirs, shouldDecompress);
		final List<IMonitoringRecord> outputList = new LinkedList<>();

		// 4. trigger records
		final TestProbe testProbe = new TestProbe(monitoringController);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		testProbe.triggerRecords(records);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());

		// 5. terminate monitoring
		monitoringController.terminateMonitoring();
		monitoringController.waitForTermination(TIMEOUT_IN_MS);

		// 6. execute the reader in test configuration
		StageTester.test(binaryLogReader).and().receive(outputList).from(binaryLogReader.getOutputPort()).start();

		// 7. return actual records (sublist is used to exclude the KiekerMetadataRecord sent by the monitoring controller)
		return outputList.subList(1, outputList.size());
	}
}
