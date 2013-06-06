package kieker.test.tools.junit.bridge;

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
	public void firstTest() {

		/**
		 * 1st parameter: Configuration is in CLIServerMain
		 * next steps stays in CLIServerMain//
		 * First element is a default configuration like in CLIServerMain(),
		 * the second part starts a new record which is written in the TestServiceConnector class.
		 * 
		 */
		final String path = "/tmp/kdb/";

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

		// TODO check if the number of send records is equal to TestServiceConnector.SEND_NUMBER_OF_RECORDS,
		// you have to get the information from the Kieker record log

		// find file /tmp/kdb/kieker-*/kieker-*.dat
		// count lines in file

		// remove all files and directories in /tmp/kdb/ (including /tmp/kdb/)
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
