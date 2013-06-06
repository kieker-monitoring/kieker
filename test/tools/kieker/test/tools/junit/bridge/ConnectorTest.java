package kieker.test.tools.junit.bridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.ServiceContainer;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Pascale Brandt
 */

public class ConnectorTest extends AbstractKiekerTest {

	private boolean setup = false;
	private boolean close = false;

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
		final String path = "/tmp/kdb/";
		final File dir = new File(path);
		int rows = 0;

		if (dir.mkdir()) {
			System.out.println(dir + " - konnte erstellt werden.");
		} else {
			// TODO make this an assertions
			System.out.println(dir + " - konnte nicht erstellt werden.");
		}

		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty("kieker.monitoring.writer.filesystem.AsyncFsWriter.storeInJavaIoTmpdir", "false");
		configuration.setProperty("kieker.monitoring.writer.filesystem.AsyncFsWriter.customStoragePath", path);

		final ServiceContainer serviceContainer = new ServiceContainer(configuration,
				new TestServiceConnector(this));
		try {
			serviceContainer.run();
		} catch (final Exception e) {
			System.out.println("Something went wrong: " + e.getStackTrace());
			e.printStackTrace();
		}

		// check number of lines in Kieker record file
		try {
			final FileReader fr = new FileReader("");
			final BufferedReader br = new BufferedReader(fr);
			String line;

			while ((line = br.readLine()) != null) {
				rows++;
			}
			rows = rows - 1;
			br.close();

		} catch (final IOException e) {
			System.out.println(e.getMessage());

		}

		Assert.assertTrue("The number of send records is not equal to TestServiceConnector.SEND_NUMBER_OF_RECORDS",
				TestServiceConnector.SEND_NUMBER_OF_RECORDS == rows);

		// http://stackoverflow.com/questions/779519/delete-files-recursively-in-java
		dir.delete(); // deletes the whole directory from path /tmp/kdb/data/
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
