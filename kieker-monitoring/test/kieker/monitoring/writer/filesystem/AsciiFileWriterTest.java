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

package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.configuration.Configuration;
import kieker.common.record.io.IValueSerializer;
import kieker.common.record.io.TextValueSerializer;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.filesystem.FileExtensionFilter;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.writer.compression.NoneCompressionFilter;
import kieker.monitoring.writer.compression.ZipCompressionFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class AsciiFileWriterTest {

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS recommends that this is private. JUnit test wants this public.

	private Configuration configuration;

	private Path writerPath;

	/**
	 * Test class.
	 */
	public AsciiFileWriterTest() {
		// empty constructor.
	}

	/**
	 * Shared setup for the tests.
	 */
	@Before
	public void before() {
		this.writerPath = Paths.get(this.tmpFolder.getRoot().getAbsolutePath());
		this.configuration = new Configuration();
		this.configuration.setProperty(ConfigurationKeys.HOST_NAME, "testHostName");
		this.configuration.setProperty(ConfigurationKeys.CONTROLLER_NAME, "testControllerName");
		this.configuration.setProperty(FileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(FileWriter.CONFIG_BUFFERSIZE, "32768");
		this.configuration.setProperty(FileWriter.CONFIG_MAXLOGFILES, String.valueOf(Integer.MAX_VALUE));
		this.configuration.setProperty(FileWriter.CONFIG_MAXLOGSIZE, String.valueOf(Integer.MAX_VALUE));
		this.configuration.setProperty(FileWriter.CONFIG_PATH, this.writerPath.toString());
	}

	/**
	 * Test whether the log directory is created correctly.
	 *
	 * @throws IOException
	 */
	@Test
	public void shouldCreateLogFolder() throws IOException {
		// test preparation
		this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "1");
		this.configuration.setProperty(FileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());
		this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
		this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

		new FileWriter(this.configuration);

		final Path kiekerPath = Files.list(this.writerPath).findFirst().get();

		// test assertion
		Assert.assertTrue(Files.exists(kiekerPath));
	}

	/**
	 * Test whether the mapping file is created correctly.
	 *
	 * @throws IOException
	 */
	@Test
	public void shouldCreateMappingAndRecordFiles() throws IOException {
		// test preparation
		this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "1");
		this.configuration.setProperty(FileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());

		this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
		this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

		final FileWriter writer = new FileWriter(this.configuration);

		writer.onStarting();
		final EmptyRecord record = new EmptyRecord();
		FilesystemTestUtil.writeMonitoringRecords(writer, 1, record);
		writer.onTerminating();

		// test assertion
		final Path kiekerPath = Files.list(this.writerPath).findFirst().get();
		final File storePath = kiekerPath.toFile();

		final File[] mapFiles = storePath.listFiles(FileExtensionFilter.MAP);
		Assert.assertNotNull(mapFiles);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(FileExtensionFilter.DAT);
		Assert.assertNotNull(recordFiles);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(1));
	}

	/**
	 * Test whether the upper limit of entries per file in honored.
	 *
	 * @throws IOException
	 */
	@Test
	public void shouldCreateMultipleRecordFiles() throws IOException {
		// test preparation
		this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(FileWriter.CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());

		this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
		this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

		final FileWriter writer = new FileWriter(this.configuration);

		writer.onStarting();
		final EmptyRecord record = new EmptyRecord();
		FilesystemTestUtil.writeMonitoringRecords(writer, 3, record);
		writer.onTerminating();

		// test assertion
		final Path kiekerPath = Files.list(this.writerPath).findFirst().get();
		final File storePath = kiekerPath.toFile();

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

	/**
	 * Test whether compression setting works.
	 *
	 * @throws IOException
	 */
	@Test
	public void shouldCreateMultipleCompressedRecordFiles() throws IOException {
		// test preparation
		this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "2");
		this.configuration.setProperty(FileWriter.CONFIG_COMPRESSION_FILTER, ZipCompressionFilter.class.getName());

		this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
		this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

		final FileWriter writer = new FileWriter(this.configuration);

		writer.onStarting();
		final EmptyRecord record = new EmptyRecord();
		FilesystemTestUtil.writeMonitoringRecords(writer, 3, record);
		writer.onTerminating();

		// test assertion
		final Path kiekerPath = Files.list(this.writerPath).findFirst().get();
		final File storePath = kiekerPath.toFile();

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

	/**
	 * Test behavior regarding max log files. Should rotate.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testMaxLogFiles() throws IOException {
		// test preparation
		this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "2");
		final int[] maxLogFilesValues = { -1, 0, 1, 2 };
		final int[] numRecordsToWriteValues = { 0, 1, 2, 3, 10 };
		final int[][] expectedNumRecordFilesValues = { { 1, 1, 1, 2, 5, }, { 1, 1, 1, 2, 5 }, { 1, 1, 1, 1, 1 }, { 1, 1, 1, 2, 2 } };

		for (int i = 0; i < maxLogFilesValues.length; i++) {
			final int maxLogFiles = maxLogFilesValues[i];

			for (int j = 0; j < numRecordsToWriteValues.length; j++) {
				final int numRecordsToWrite = numRecordsToWriteValues[j];
				final int expectedNumRecordFiles = expectedNumRecordFilesValues[i][j];

				// test preparation
				this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "2");
				this.configuration.setProperty(FileWriter.CONFIG_MAXLOGFILES, String.valueOf(maxLogFiles));
				this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
				this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

				final FileWriter writer = new FileWriter(this.configuration);

				final EmptyRecord record = new EmptyRecord();
				// test execution
				FilesystemTestUtil.executeFileWriterTest(numRecordsToWrite, writer, record);

				final File storePath = Files.list(this.writerPath).findFirst().get().toFile();

				// test assertion
				final String reasonMessage = "Passed arguments: maxLogFiles=" + maxLogFiles + ", numRecordsToWrite=" + numRecordsToWrite;
				final File[] recordFiles = storePath.listFiles(FileExtensionFilter.DAT);
				Assert.assertNotNull(recordFiles);
				Assert.assertThat(reasonMessage, recordFiles.length, CoreMatchers.is(expectedNumRecordFiles));

				FilesystemTestUtil.deleteContent(this.writerPath);
			}
		}
	}

	/**
	 * Test whether the max log size.
	 *
	 * @throws Exception
	 *             on IO errors
	 */
	@Test
	public void testMaxLogSize() throws Exception {
		final CharBuffer charBuffer = CharBuffer.allocate(1024);
		final IValueSerializer serializer = TextValueSerializer.create(charBuffer);
		final EmptyRecord record = new EmptyRecord();
		record.serialize(serializer);
		charBuffer.flip();
		final String recordString = record.getLoggingTimestamp() + ";" + charBuffer.toString();
		// size = $ + compressed record class name (0) + ; + record.toString + newLine (\n or \n\r)
		final int recordSizeInBytes = 1 + 1 + 1 + recordString.length() + System.lineSeparator().length(); // 7=3+2+1/2

		// semantics of the tuple: (maxMegaBytesPerFile, megaBytesToWrite, expectedNumRecordFiles)
		final int[][] testInputTuples = {
			{ -1, 0, 1 }, { -1, 1, 1 },
			{ 0, 0, 1 }, { 0, 1, 1 },
			{ 1, 0, 1 }, { 1, 1, 1 },
			{ 1, 2, 2 }, { 1, 3, 2 },
		};

		for (final int[] testInputTuple : testInputTuples) { // NOPMD
			final int maxMegaBytesPerFile = testInputTuple[0];
			final int megaBytesToWrite = testInputTuple[1];
			final int expectedNumRecordFiles = testInputTuple[2];

			// test preparation
			final int numRecordsToWrite = (1024 * 1024 * megaBytesToWrite) / recordSizeInBytes;
			this.configuration.setProperty(FileWriter.CONFIG_MAXENTRIESINFILE, "-1");
			this.configuration.setProperty(FileWriter.CONFIG_MAXLOGSIZE, String.valueOf(maxMegaBytesPerFile));
			this.configuration.setProperty(FileWriter.CONFIG_MAXLOGFILES, "2");
			this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
			this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

			final FileWriter writer = new FileWriter(this.configuration);

			// test execution
			FilesystemTestUtil.executeFileWriterTest(numRecordsToWrite, writer, record);

			final File storePath = Files.list(this.writerPath).findFirst().get().toFile();

			// test assertion
			final String reasonMessage = "Passed arguments: maxMegaBytesPerFile=" + maxMegaBytesPerFile + ", megaBytesToWrite=" + megaBytesToWrite;
			final File[] recordFiles = storePath.listFiles(FileExtensionFilter.DAT);
			Assert.assertNotNull(recordFiles);
			Assert.assertThat(reasonMessage, recordFiles.length, CoreMatchers.is(expectedNumRecordFiles));

			FilesystemTestUtil.deleteContent(this.writerPath);
		}
	}

	/**
	 * Test valid log directory.
	 *
	 * @throws IOException
	 */
	@Test
	public void testValidLogFolder() throws IOException {
		final String passedConfigPathName = this.tmpFolder.getRoot().getAbsolutePath();
		this.configuration.setProperty(FileWriter.CONFIG_PATH, passedConfigPathName);
		this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
		this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

		new FileWriter(this.configuration);

		final Path kiekerPath = Files.list(Paths.get(passedConfigPathName)).findFirst().get();
		Assert.assertThat(kiekerPath.toAbsolutePath().toString(), CoreMatchers.startsWith(passedConfigPathName));
	}

	/**
	 * Test log directory missing in configuration.
	 *
	 * @throws IOException
	 *             on IO errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNonDirectoryConfigPath() throws IOException {
		final String passedConfigPathName = this.tmpFolder.newFile().getAbsolutePath();
		this.configuration.setProperty(FileWriter.CONFIG_PATH, passedConfigPathName);
		this.configuration.setProperty(FileWriter.CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getCanonicalName());
		this.configuration.setProperty(FileWriter.CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getCanonicalName());

		new FileWriter(this.configuration);

		final String defaultDir = System.getProperty("java.io.tmpdir");
		final Path kiekerPath = Files.list(Paths.get(passedConfigPathName)).findFirst().get();
		Assert.assertThat(kiekerPath.toAbsolutePath().toString(), CoreMatchers.startsWith(defaultDir));
	}

}
