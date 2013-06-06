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
		final String dirName = "data";
		final File dir = new File(path + dirName);
		int rows = 0;

		if (dir.mkdir()) {
			System.out.println(dir + " - konnte nicht erstellt werden.");
		} else {
			System.out.println(dir + " - konnte nicht erstellt werden.");
		}

		// TODO create dir

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

		try {

			final FileReader fr = new FileReader("test.txt"); // instead of test.txt fill the correct filename
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
		// TODO check if the number of send records is equal to TestServiceConnector.SEND_NUMBER_OF_RECORDS,
		// you have to get the information from the Kieker record log
		Assert.assertTrue("The number of send records is not equal to TestServiceConnector.SEND_NUMBER_OF_RECORDS",
				TestServiceConnector.SEND_NUMBER_OF_RECORDS == rows);
		// find file /tmp/kdb/kieker-*/kieker-*.dat
		// count lines in file

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
