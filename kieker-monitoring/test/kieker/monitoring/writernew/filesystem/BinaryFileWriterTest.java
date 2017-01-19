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

package kieker.monitoring.writernew.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.util.filesystem.FileExtensionFilter;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class BinaryFileWriterTest {

	private static final Path TEMP_FOLDER = Paths.get("tmp").toAbsolutePath();

	private Configuration configuration;

	@BeforeClass
	public static void beforeClass() throws IOException {
		Files.createDirectories(TEMP_FOLDER);
	}

	@Before
	public void before() {
		this.configuration = new Configuration();
		this.configuration.setProperty(ConfigurationFactory.HOST_NAME, "testHostName");
		this.configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "testControllerName");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_BUFFERSIZE, "8192");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_MAXLOGFILES, String.valueOf(Integer.MAX_VALUE));
		this.configuration.setProperty(BinaryFileWriter.CONFIG_PATH, BinaryFileWriterTest.TEMP_FOLDER.toString());
	}

	@Test
	public void shouldCreateLogFolder() {
		// test preparation
		this.configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "1");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "false");

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(this.configuration);

		// test assertion
		Assert.assertTrue(Files.exists(writer.getLogFolder()));
	}

	@Test
	public void shouldCreateMappingAndRecordFiles() {
		// test preparation
		this.configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "1");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "false");

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, 1);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.BIN);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(1));
	}

	@Test
	public void shouldCreateMultipleRecordFiles() {
		// test preparation
		this.configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "false");

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, 3);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.BIN);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertTrue(recordFiles[1].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(2));
	}

	@Test
	public void shouldCreateMultipleCompressedRecordFiles() {
		// test preparation
		this.configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "true");

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, 3);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.GZIP);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertTrue(recordFiles[1].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(2));
	}

	@Test
	public final void testMaxLogFiles() {
		// test preparation
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		final int[] maxLogFilesValues = { -1, 0, 1, 2 };
		final int[] numRecordsToWriteValues = { 0, 1, 2, 3, 10 };
		final int[][] expectedNumRecordFilesValues = { { 0, 1, 1, 2, 5, }, { 0, 1, 1, 2, 5 }, { 0, 1, 1, 1, 1 }, { 0, 1, 1, 2, 2 } };

		for (int i = 0; i < maxLogFilesValues.length; i++) {
			final int maxLogFiles = maxLogFilesValues[i];

			for (int j = 0; j < numRecordsToWriteValues.length; j++) {
				final int numRecordsToWrite = numRecordsToWriteValues[j];

				// test execution
				final File[] recordFiles = this.executeMaxLogFilesTest(maxLogFiles, numRecordsToWrite);

				// test assertion
				final String reasonMessage = "Passed arguments: maxLogFiles=" + maxLogFiles + ", numRecordsToWrite=" + numRecordsToWrite;
				final int expectedNumRecordFiles = expectedNumRecordFilesValues[i][j];
				Assert.assertThat(reasonMessage, recordFiles.length, CoreMatchers.is(expectedNumRecordFiles));
			}
		}
	}

	private File[] executeMaxLogFilesTest(final int maxLogFiles, final int numRecordsToWrite) {
		// test preparation
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGFILES, String.valueOf(maxLogFiles));

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, numRecordsToWrite);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		return storePath.listFiles(FileExtensionFilter.BIN);
	}
}
