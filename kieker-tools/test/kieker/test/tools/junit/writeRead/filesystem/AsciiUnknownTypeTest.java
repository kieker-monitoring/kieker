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

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.plugin.reader.filesystem.AsciiLogReader;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.LogImplJUnit;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.writernew.filesystem.AsciiFileWriter;

import kieker.test.tools.junit.writeRead.TestAnalysis;
import kieker.test.tools.junit.writeRead.TestDataRepository;
import kieker.test.tools.junit.writeRead.TestProbe;

/**
 * A warning by the reader should show up that this mode is not supported for binary files.
 *
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public class AsciiUnknownTypeTest {

	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final int TIMEOUT_IN_MS = 0;

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	public AsciiUnknownTypeTest() {
		super();
	}

	@Before
	public void before() {
		LogImplJUnit.disableThrowable(MonitoringRecordException.class);
	}

	@After
	public void after() {
		LogImplJUnit.reset();
	}

	@Test
	public void testIgnoreUnknownRecordType() throws Exception {
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestUnknownRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testUnknownRecordTypes(records, true);

		// we expect that EVENT1_UNKNOWN_TYPE and EVENT3_UNKNOWN_TYPE are simply ignored
		Assert.assertThat(analyzedRecords.get(0), CoreMatchers.is(CoreMatchers.equalTo(records.get(0))));
		Assert.assertThat(analyzedRecords.get(1), CoreMatchers.is(CoreMatchers.equalTo(records.get(2))));
		Assert.assertThat(analyzedRecords.size(), CoreMatchers.is(2));
	}

	@Test
	public void testTerminateUponUnknownRecordType() throws Exception {
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestUnknownRecords();

		final List<IMonitoringRecord> analyzedRecords = this.testUnknownRecordTypes(records, false);

		// we expect that reading abort on the occurrence of EVENT1_UNKNOWN_TYPE, i.e., the remaining lines weren't processed
		Assert.assertThat(analyzedRecords.get(0), CoreMatchers.is(CoreMatchers.equalTo(records.get(0))));
		Assert.assertThat(analyzedRecords.size(), CoreMatchers.is(1));
	}

	@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
	private List<IMonitoringRecord> testUnknownRecordTypes(final List<IMonitoringRecord> records, final boolean ignoreUnknownRecordTypes) throws Exception {
		// 2. define monitoring config
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, AsciiFileWriter.class.getName());
		config.setProperty(WriterController.RECORD_QUEUE_SIZE, "128");
		config.setProperty(WriterController.RECORD_QUEUE_INSERT_BEHAVIOR, "1");
		config.setProperty(AsciiFileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getCanonicalPath());
		config.setProperty(AsciiFileWriter.CONFIG_SHOULD_COMPRESS, "false");
		final MonitoringController monitoringController = MonitoringController.createInstance(config);

		// 3. define analysis config
		final String[] monitoringLogDirs = TEST_DATA_REPOSITORY.getAbsoluteMonitoringLogDirNames(this.tmpFolder.getRoot());

		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(monitoringLogDirs));
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.toString(ignoreUnknownRecordTypes));
		readerConfiguration.setProperty(AsciiLogReader.CONFIG_SHOULD_DECOMPRESS, "false");
		final TestAnalysis analysis = new TestAnalysis(readerConfiguration, AsciiLogReader.class);

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
		// 6b. wait for termination: analysis
		analysis.startAndWaitForTermination();

		// 7. read actual records
		return analysis.getList();
	}
}
