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

package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.filesystem.FileExtensionFilter;
import kieker.monitoring.core.configuration.ConfigurationKeys;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class AsciiFileWriterTest {

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS recommends that this is private. JUnit test wants this public.

	private Configuration configuration;

	public AsciiFileWriterTest() {
		super();
	}

	@Before
	public void before() {
		this.configuration = new Configuration();
		this.configuration.setProperty(ConfigurationKeys.HOST_NAME, "testHostName");
		this.configuration.setProperty(ConfigurationKeys.CONTROLLER_NAME, "testControllerName");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGFILES, String.valueOf(Integer.MAX_VALUE));
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGSIZE, String.valueOf(Integer.MAX_VALUE));
		this.configuration.setProperty(AsciiFileWriter.CONFIG_PATH, this.tmpFolder.getRoot().getAbsolutePath());
	}

	@Test
	public void shouldCreateLogFolder() {
		// test preparation
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "1");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());

		// test execution
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);

		// test assertion
		Assert.assertTrue(Files.exists(writer.getLogFolder()));
	}

	@Test
	public void shouldCreateMappingAndRecordFiles() {
		// test preparation
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "1");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());

		// test execution
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, 1);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertNotNull(mapFiles);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.DAT);
		Assert.assertNotNull(recordFiles);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(1));
	}

	@Test
	public void shouldCreateMultipleRecordFiles() {
		// test preparation
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());

		// test execution
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, 3);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertNotNull(mapFiles);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.DAT);
		Assert.assertNotNull(recordFiles);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertTrue(recordFiles[1].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(2));
	}

	@Test
	public void shouldCreateMultipleCompressedRecordFiles() {
		// test preparation
		this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(AsciiFileWriter.CONFIG_COMPRESSION_FILTER, ZipCompressionFilter.class.getName());

		// test execution
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);
		writer.onStarting();
		FilesystemTestUtil.writeMonitoringRecords(writer, 3);
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertNotNull(mapFiles);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.ZIP);
		Assert.assertNotNull(recordFiles);
		Assert.assertThat(recordFiles[0].length(), CoreMatchers.is(CoreMatchers.not(0L)));
		Assert.assertThat(recordFiles[1].length(), CoreMatchers.is(CoreMatchers.not(0L)));
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
				final int expectedNumRecordFiles = expectedNumRecordFilesValues[i][j];

				// test preparation
				this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "2");
				this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGFILES, String.valueOf(maxLogFiles));
				final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);

				// test execution
				final File storePath = FilesystemTestUtil.executeFileWriterTest(numRecordsToWrite, writer);

				// test assertion
				final String reasonMessage = "Passed arguments: maxLogFiles=" + maxLogFiles + ", numRecordsToWrite=" + numRecordsToWrite;
				final File[] recordFiles = storePath.listFiles(writer.getFileNameFilter());
				Assert.assertNotNull(recordFiles);
				Assert.assertThat(reasonMessage, recordFiles.length, CoreMatchers.is(expectedNumRecordFiles));
			}
		}
	}

	@Test
	public void testMaxLogSize() throws Exception {
		// size = $ + compressed record class name (=0) + ; + record.toString + newLine (\n or \n\r)
		final int recordSizeInBytes = 1 + 1 + 1 + new EmptyRecord().toString().length() + System.lineSeparator().length(); // 7=3+2+1/2

		// semantics of the tuple: (maxMegaBytesPerFile, megaBytesToWrite, expectedNumRecordFiles)
		final int[][] testInputTuples = {
			{ -1, 0, 0 }, { -1, 1, 1 },
			{ 0, 0, 0 }, { 0, 1, 1 },
			{ 1, 0, 0 }, { 1, 1, 1 }, { 1, 2, 2 }, { 1, 3, 2 },
		};

		for (final int[] testInputTuple : testInputTuples) {
			final int maxMegaBytesPerFile = testInputTuple[0];
			final int megaBytesToWrite = testInputTuple[1];
			final int expectedNumRecordFiles = testInputTuple[2];

			// test preparation
			final int numRecordsToWrite = (1024 * 1024 * megaBytesToWrite) / recordSizeInBytes;
			this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "-1");
			this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGSIZE, String.valueOf(maxMegaBytesPerFile));
			this.configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGFILES, "2");
			final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);

			// test execution
			final File storePath = FilesystemTestUtil.executeFileWriterTest(numRecordsToWrite, writer);

			// test assertion
			final String reasonMessage = "Passed arguments: maxMegaBytesPerFile=" + maxMegaBytesPerFile + ", megaBytesToWrite=" + megaBytesToWrite;
			final File[] recordFiles = storePath.listFiles(writer.getFileNameFilter());
			Assert.assertNotNull(recordFiles);
			Assert.assertThat(reasonMessage, recordFiles.length, CoreMatchers.is(expectedNumRecordFiles));
		}
	}

	@Test
	public void testValidLogFolder() {
		final String passedConfigPathName = this.tmpFolder.getRoot().getAbsolutePath();
		this.configuration.setProperty(AsciiFileWriter.CONFIG_PATH, passedConfigPathName);
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);

		Assert.assertThat(writer.getLogFolder().toAbsolutePath().toString(), CoreMatchers.startsWith(passedConfigPathName));
	}

	@Test
	public void testEmptyConfigPath() {
		final String passedConfigPathName = "";
		this.configuration.setProperty(AsciiFileWriter.CONFIG_PATH, passedConfigPathName);
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);

		final String defaultDir = System.getProperty("java.io.tmpdir");
		Assert.assertThat(writer.getLogFolder().toAbsolutePath().toString(), CoreMatchers.startsWith(defaultDir));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNonDirectoryConfigPath() throws IOException {
		final String passedConfigPathName = this.tmpFolder.newFile().getAbsolutePath();
		this.configuration.setProperty(AsciiFileWriter.CONFIG_PATH, passedConfigPathName);
		final AsciiFileWriter writer = new AsciiFileWriter(this.configuration);

		final String defaultDir = System.getProperty("java.io.tmpdir");
		Assert.assertThat(writer.getLogFolder().toAbsolutePath().toString(), CoreMatchers.startsWith(defaultDir));
	}

}
