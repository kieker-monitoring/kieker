/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.tools.junit.bridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.tools.bridge.ServiceContainer;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Pascale Brandt
 * @since 1.8
 */
public class ConnectorTest extends AbstractKiekerTest {

	private boolean setup = false;
	private boolean close = false;

	@Rule
	private final TemporaryFolder tmpFolder = new TemporaryFolder();

	public ConnectorTest() {}

	@Test
	public void firstTest() throws IOException {

		/**
		 * 1st parameter: Configuration is in CLIServerMain
		 * next steps stays in CLIServerMain//
		 * First element is a default configuration like in CLIServerMain(),
		 * the second part starts a new record which is written in the TestServiceConnector class.
		 * 
		 */

		final File path = this.tmpFolder.getRoot();
		int rows = 0;

		// Assert.assertTrue("Directory could not created", dir.mkdir() == false);

		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		final String writer = AsyncFsWriter.class.getName();
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXENTRIESINFILE, "1");
		// The maximal size of the log file must be greater than the expected number of log entries to ensure, that the framework allows to write more records, which
		// we then can detect as failures. Otherwise writing more than expected records would be hindered by the framework itself.
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXLOGFILES, String.valueOf(TestServiceConnector.SEND_NUMBER_OF_RECORDS * 2));
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXLOGSIZE, "-1");
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_TEMP, "false");
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_PATH, path.getAbsolutePath());
		// TODO get configuration from kieker.test.monitoring.junit.writer.filesystem.TestLogRotationMaxLogFilesAyncFsWriter
		// set max file size to 1 record

		final ServiceContainer serviceContainer = new ServiceContainer(configuration,
				new TestServiceConnector(this), false);
		try {
			serviceContainer.run();
		} catch (final ConnectorDataTransmissionException e) {
			System.out.println("Something went wrong: " + e.getMessage());
			e.printStackTrace();
		}

		// TODO check number of written records like in kieker.test.monitoring.junit.writer.filesystem.AbstractTestLogRotationMaxLogFiles.checkMaxLogFiles
		// final File[] logDirs = new File(this.tmpFolder.getRoot().getCanonicalPath()).listFiles();

		// check number of lines in Kieker record file
		try {
			final FileReader fr = new FileReader("");
			final BufferedReader br = new BufferedReader(fr);

			while (br.readLine() != null) {
				rows++;
			}
			rows = rows - 1;
			br.close();

		} catch (final IOException e) {
			System.out.println(e.getMessage());

		}

		Assert.assertTrue("The number of send records is not equal to TestServiceConnector.SEND_NUMBER_OF_RECORDS",
				TestServiceConnector.SEND_NUMBER_OF_RECORDS == rows);

		// Delete iterativ every element from the created folder above

		// if (dir.exists()) {
		// for (final File f : dir.listFiles()) {
		// f.delete();
		// }
		// dir.delete();
		// }

		Assert.assertTrue("Directory is not cleaned", this.tmpFolder.getRoot().exists());
	}

	/**
	 * The following three methods test if the setup, deserialize and close method is getting called.
	 * If that is not the case the error should throw an exception which has to be catched.
	 */
	public void setupCalled() {
		Assert.assertTrue("Connector's setup() method was called more than once.", !this.setup);
		this.setup = true;
		this.close = false;
	}

	public void deserializeCalled() {
		Assert.assertTrue("Connector's deserialize() method called before setup() was called.", this.setup);
	}

	public void closeCalled() {
		Assert.assertTrue("Connector's close() method was called before setup() was called.", this.setup);
		Assert.assertTrue("Connector's close() method was called more than once.", !this.close);
		this.close = true;
		this.setup = false;
	}

}
