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

package kieker.test.toolsteetime.junit.writeRead.filesystem;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysisteetime.plugin.reader.filesystem.fsReader.BinaryLogReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writer.compression.NoneCompressionFilter;
import kieker.monitoring.writer.filesystem.AbstractFileWriter;
import kieker.monitoring.writer.filesystem.BinaryFileWriter;

import kieker.test.tools.junit.writeRead.TestDataRepository;
import kieker.test.tools.junit.writeRead.TestProbe;

import teetime.framework.test.StageTester;

/**
 * A warning by the reader should show up that this mode is not supported for binary files.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.5
 */
public class BinaryUnknownTypeTest {

	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final int TIMEOUT_IN_MS = 0;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	public BinaryUnknownTypeTest() {
		super();
	}

	@Test
	public void testIgnoreUnknownRecordType() throws Exception {
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestUnknownRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testUnknownRecordTypes(records, true);

		// we expect that reading abort on the occurrence of EVENT1_UNKNOWN_TYPE, i.e., the remaining lines weren't processed
		Assert.assertThat(analyzedRecords.get(0), CoreMatchers.is(CoreMatchers.equalTo(records.get(0))));
		Assert.assertThat(analyzedRecords.size(), CoreMatchers.is(1));
	}

	@Test
	public void testTerminateUponUnknownRecordType() throws Exception {
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestUnknownRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testUnknownRecordTypes(records, false);

		// we expect that reading abort on the occurrence of EVENT1_UNKNOWN_TYPE, i.e., the remaining lines weren't processed
		Assert.assertThat(analyzedRecords.get(0), CoreMatchers.is(CoreMatchers.equalTo(records.get(0))));
		Assert.assertThat(analyzedRecords.size(), CoreMatchers.is(1));
	}

	@SuppressWarnings({ "PMD.JUnit4TestShouldUseTestAnnotation", "PMD.UnusedFormalParameter" }) // unused parameter: bug in BinaryLogReader mentioned in issue
																								// KIEKER-1540
	private List<IMonitoringRecord> testUnknownRecordTypes(final List<IMonitoringRecord> records, final boolean ignoreUnknownRecordTypes)
			throws Exception {
		// 2. define monitoring config
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationKeys.WRITER_CLASSNAME, BinaryFileWriter.class.getName());
		config.setProperty(WriterController.RECORD_QUEUE_SIZE, "128");
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");
		config.setProperty(AbstractFileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());
		config.setProperty(AbstractFileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());
		final MonitoringController monitoringController = MonitoringController.createInstance(config);

		// 3. initialize the reader
		final String[] monitoringLogDirs = TEST_DATA_REPOSITORY.getAbsoluteMonitoringLogDirNames(this.tmpFolder.getRoot());
		final BinaryLogReader binaryLogReader = new BinaryLogReader(monitoringLogDirs, false);
		final List<IMonitoringRecord> outputList = new LinkedList<>();

		// 4. trigger records
		final TestProbe testProbe = new TestProbe(monitoringController);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		testProbe.triggerRecords(records);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());

		// 5. terminate monitoring
		monitoringController.terminateMonitoring();

		// 6a. wait for termination: monitoring
		monitoringController.waitForTermination(TIMEOUT_IN_MS);

		final String classnameToManipulate = records.get(1).getClass().getName();
		FileContentUtil.replaceStringInMapFiles(monitoringLogDirs, classnameToManipulate, classnameToManipulate + "XYZ");

		// 6b. execute the reader in test configuration
		StageTester.test(binaryLogReader).and().receive(outputList).from(binaryLogReader.getOutputPort()).start();

		// 7. return actual records (sublist is used to exclude the KiekerMetadataRecord sent by the monitoring controller)
		return outputList.subList(1, outputList.size());
	}
}
