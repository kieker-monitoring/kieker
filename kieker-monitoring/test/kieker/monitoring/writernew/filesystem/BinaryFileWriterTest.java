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
import org.junit.BeforeClass;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.filesystem.BinFileNameFilter;
import kieker.common.util.filesystem.GZipFileNameFilter;
import kieker.common.util.filesystem.MapFileNameFilter;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class BinaryFileWriterTest {

	private static final Path TEMP_FOLDER = Paths.get("tmp").toAbsolutePath();

	@BeforeClass
	public static void beforeClass() throws IOException {
		Files.createDirectories(TEMP_FOLDER);
	}

	@Test
	public void shouldCreateLogFolder() {
		// test preparation
		final Configuration configuration = new Configuration();
		configuration.setProperty(ConfigurationFactory.HOST_NAME, "testHostName");
		configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "testControllerName");
		configuration.setProperty(BinaryFileWriter.CONFIG_BUFFERSIZE, "8192");
		configuration.setProperty(BinaryFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "1");
		configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "false");
		configuration.setProperty(BinaryFileWriter.CONFIG_PATH, BinaryFileWriterTest.TEMP_FOLDER.toString());

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(configuration);

		// test assertion
		Assert.assertTrue(Files.exists(writer.getLogFolder()));
	}

	@Test
	public void shouldCreateMappingAndRecordFiles() {
		// test preparation
		final Configuration configuration = new Configuration();
		configuration.setProperty(ConfigurationFactory.HOST_NAME, "testHostName");
		configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "testControllerName");
		configuration.setProperty(BinaryFileWriter.CONFIG_BUFFERSIZE, "8192");
		configuration.setProperty(BinaryFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "1");
		configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "false");
		configuration.setProperty(BinaryFileWriter.CONFIG_PATH, BinaryFileWriterTest.TEMP_FOLDER.toString());

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(configuration);
		writer.onStarting();
		writer.writeMonitoringRecord(new EmptyRecord());
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(MapFileNameFilter.INSTANCE);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(BinFileNameFilter.INSTANCE);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(1));
	}

	@Test
	public void shouldCreateMultipleRecordFiles() {
		// test preparation
		final Configuration configuration = new Configuration();
		configuration.setProperty(ConfigurationFactory.HOST_NAME, "testHostName");
		configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "testControllerName");
		configuration.setProperty(BinaryFileWriter.CONFIG_BUFFERSIZE, "8192");
		configuration.setProperty(BinaryFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "false");
		configuration.setProperty(BinaryFileWriter.CONFIG_PATH, BinaryFileWriterTest.TEMP_FOLDER.toString());

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(configuration);
		writer.onStarting();
		writer.writeMonitoringRecord(new EmptyRecord());
		writer.writeMonitoringRecord(new EmptyRecord());
		writer.writeMonitoringRecord(new EmptyRecord());
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(MapFileNameFilter.INSTANCE);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(BinFileNameFilter.INSTANCE);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertTrue(recordFiles[1].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(2));
	}

	@Test
	public void shouldCreateMultipleCompressedRecordFiles() {
		// test preparation
		final Configuration configuration = new Configuration();
		configuration.setProperty(ConfigurationFactory.HOST_NAME, "testHostName");
		configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "testControllerName");
		configuration.setProperty(BinaryFileWriter.CONFIG_BUFFERSIZE, "8192");
		configuration.setProperty(BinaryFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
		configuration.setProperty(BinaryFileWriter.CONFIG_MAXENTRIESINFILE, "2");
		configuration.setProperty(BinaryFileWriter.CONFIG_SHOULD_COMPRESS, "true");
		configuration.setProperty(BinaryFileWriter.CONFIG_PATH, BinaryFileWriterTest.TEMP_FOLDER.toString());

		// test execution
		final BinaryFileWriter writer = new BinaryFileWriter(configuration);
		writer.onStarting();
		final int numRecords = 3;
		for (int i = 0; i < numRecords; i++) {
			writer.writeMonitoringRecord(new EmptyRecord());
		}
		writer.onTerminating();

		// test assertion
		final File storePath = writer.getLogFolder().toFile();

		final File[] mapFiles = storePath.listFiles(MapFileNameFilter.INSTANCE);
		Assert.assertTrue(mapFiles[0].exists());
		Assert.assertThat(mapFiles.length, CoreMatchers.is(1));

		final File[] recordFiles = storePath.listFiles(GZipFileNameFilter.INSTANCE);
		Assert.assertTrue(recordFiles[0].exists());
		Assert.assertTrue(recordFiles[1].exists());
		Assert.assertThat(recordFiles.length, CoreMatchers.is(2));
	}

}
