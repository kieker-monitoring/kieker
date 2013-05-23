package kieker.test.tools.junit.bridge;

import org.junit.Test;

import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.ServiceContainer;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Pascale Brandt
 */

public class ConnectorTest extends AbstractKiekerTest {

	private boolean setup = false;
	private boolean deserialize = false;
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
		final ServiceContainer serviceContainer = new ServiceContainer(ConfigurationFactory.createDefaultConfiguration(), new TestServiceConnector(this));
		try {
			serviceContainer.run();
		} catch (final Exception e) {
			System.out.println("Something went wrong: " + e.getStackTrace());
			e.printStackTrace();
		}

	}

	/**
	 * The following three methods test if the setup, deserialize and close method is getting called.
	 * If that is not the case the error should throw an exception which has to be catched.
	 */
	public void setupCalled() {
		if (this.setup == false)
		{
			this.setup = true;

		} else {
			System.out.println("The setup was called once. But before this test should call the setup!");
		}
	}

	public void deserializeCalled() {
		if (this.deserialize == false) {
			this.deserialize = true;
		} else {
			System.out.println("The desereialize method was called once. But before this test should call it!");
		}
	}

	public void closeCalled() {
		if (this.close == false) {
			this.close = true;
		} else {
			System.out.println("The close method was called once. But before this test should call it!");
		}
	}

}
