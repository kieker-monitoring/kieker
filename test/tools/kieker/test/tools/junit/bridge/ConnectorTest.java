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

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Pascale Brandt
 */

public class ConnectorTest extends AbstractKiekerTest {

	private boolean setup = false;
	private boolean close = false;

	public ConnectorTest() {}

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder();

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
		// TODO comment: why 2 * SEND_
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXLOGFILES, String.valueOf(TestServiceConnector.SEND_NUMBER_OF_RECORDS * 2));
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXLOGSIZE, "-1");
		// TODO nutze Konstanten aus AbstractAsyncFSWriter
		configuration.setProperty("kieker.monitoring.writer.filesystem.AsyncFsWriter.storeInJavaIoTmpdir", "false");
		configuration.setProperty("kieker.monitoring.writer.filesystem.AsyncFsWriter.customStoragePath", path.getAbsolutePath());
		// TODO get configuration from kieker.test.monitoring.junit.writer.filesystem.TestLogRotationMaxLogFilesAyncFsWriter
		// set max file size to 1 record

		final ServiceContainer serviceContainer = new ServiceContainer(configuration,
				new TestServiceConnector(this), false);
		try {
			serviceContainer.run();
		} catch (final Exception e) {
			System.out.println("Something went wrong: " + e.getStackTrace());
			e.printStackTrace();
		}

		// TODO check number of written records like in kieker.test.monitoring.junit.writer.filesystem.AbstractTestLogRotationMaxLogFiles.checkMaxLogFiles
		// final File[] logDirs = new File(this.tmpFolder.getRoot().getCanonicalPath()).listFiles();

		// check number of lines in Kieker record file
		try {
			final FileReader fr = new FileReader("");
			final BufferedReader br = new BufferedReader(fr);
			final String line = br.readLine();

			while (line != null) {
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
		/*
		 * if (dir.exists())
		 * {
		 * for (final File f : dir.listFiles())
		 * {
		 * f.delete();
		 * }
		 * dir.delete();
		 * }
		 */

		Assert.assertTrue("Directory is not cleaned", this.tmpFolder.getRoot().exists() == true);
	}

	/**
	 * The following three methods test if the setup, deserialize and close method is getting called.
	 * If that is not the case the error should throw an exception which has to be catched.
	 */
	public void setupCalled() {
		Assert.assertTrue("Connector's setup() method was called more than once.", this.setup == false);
		this.setup = true;
		this.close = false;
	}

	public void deserializeCalled() {
		Assert.assertTrue("Connector's deserialize() method called before setup() was called.", this.setup == true);
	}

	public void closeCalled() {
		Assert.assertTrue("Connector's close() method was called before setup() was called.", this.setup == true);
		Assert.assertTrue("Connector's close() method was called more than once.", this.close == false);
		this.close = true;
		this.setup = false;
	}

}
