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

package kieker.test.tools.junit.writeRead.filesystem;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.plugin.reader.filesystem.AsciiLogReader;
import kieker.analysis.tt.writeRead.TestDataRepository;
import kieker.analysis.tt.writeRead.TestProbe;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writer.compression.NoneCompressionFilter;
import kieker.monitoring.writer.compression.ZipCompressionFilter;
import kieker.monitoring.writer.filesystem.FileWriter;

import kieker.test.tools.junit.writeRead.TestAnalysis;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class AsciiWriterReaderTest {

	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final int TIMEOUT_IN_MS = 0;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	public AsciiWriterReaderTest() {
		super();
	}

	@Test
	public void testUncompressedAsciiCommunication() throws Exception {
		// 1. define records to be triggered by the test probe
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testAsciiCommunication(records, NoneCompressionFilter.class.getName());

		// 8. compare actual and expected records
		Assert.assertThat(analyzedRecords, CoreMatchers.is(CoreMatchers.equalTo(records)));
	}

	@Test
	public void testCompressedAsciiCommunication() throws Exception {
		// 1. define records to be triggered by the test probe
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testAsciiCommunication(records, ZipCompressionFilter.class.getName());

		// 8. compare actual and expected records
		Assert.assertThat(analyzedRecords, CoreMatchers.is(CoreMatchers.equalTo(records)));
	}

	@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
	private List<IMonitoringRecord> testAsciiCommunication(final List<IMonitoringRecord> records, final String compressionMethod) throws Exception {
		// 2. define monitoring config
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, FileWriter.class.getName());
		config.setProperty(WriterController.RECORD_QUEUE_SIZE, "128");
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");
		config.setProperty(FileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());
		config.setProperty(FileWriter.CONFIG_COMPRESSION_FILTER, compressionMethod);
		final MonitoringController monitoringController = MonitoringController.createInstance(config);

		// 3. define analysis config
		final String[] monitoringLogDirs = TEST_DATA_REPOSITORY.getAbsoluteMonitoringLogDirNames(this.tmpFolder.getRoot());

		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, "false");
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_SHOULD_DECOMPRESS, !compressionMethod.equals(NoneCompressionFilter.class.getName())); // NOCS
		final TestAnalysis analysis = new TestAnalysis(readerConfiguration, AsciiLogReader.class);

		// 4. trigger records
		final TestProbe testProbe = new TestProbe(monitoringController);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		testProbe.triggerRecords(records);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());

		// 5. terminate monitoring
		monitoringController.terminateMonitoring();

		// 6. wait for termination
		monitoringController.waitForTermination(TIMEOUT_IN_MS);
		analysis.startAndWaitForTermination();

		// 7. read actual records
		return analysis.getList();
	}
}
