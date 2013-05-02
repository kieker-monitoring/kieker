package bridge.test.tools;

import org.junit.Test;

import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.ServiceContainer;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Pascale Brandt
 */

public class FirstUnitTest extends AbstractKiekerTest {

	public FirstUnitTest() {}

	@Test
	public void firstTest() {

		/**
		 * 1st parameter: Configuration is in CLIServerMain
		 * next steps stays in CLIServerMain//
		 * First element is a default configuration like in CLIServerMain(),
		 * the second part starts a new record which is written in the TestServiceConnector class.
		 * 
		 */
		final ServiceContainer serviceContainer = new ServiceContainer(ConfigurationFactory.createDefaultConfiguration(), new TestServiceConnector());
		try {
			serviceContainer.run();
		} catch (final Exception e) {
			System.out.println("Something went wrong: " + e.getStackTrace());
			e.printStackTrace();
		}

	}

}

/*
 * /**
 * Implements two Methods, first public String toString():
 * Log the status of the controller to the console .
 * 
 * @return a String representation of the current controller.
 * 
 * Second public boolean saveMetadataAsBoolean.
 * It logs the status of the controller to the configured writer.
 * 
 * @return true if successful.
 * 
 * final IMonitoringController monitoringController = MonitoringController.getInstance();
 * 
 * /**
 * ITimeSource also implements two methods, first is getTime().
 * This return the timestamp for the current time.
 * 
 * Second method is again toString(), which do nothing more then returning
 * a String representation of timeSource.
 * 
 * 
 * final ITimeSource timeSource = monitoringController.getTimeSource();
 * 
 * final long startTime = timeSource.getTime();
 * 
 * FirstUnitTest.TestConnector();
 * final long endTime = timeSource.getTime();
 * 
 * /**
 * This create a new record
 * 
 * 
 * monitoringController.newMonitoringRecord(FirstUnitTest.createOperationExecutionRecord("kieker.bridge", 1, startTime, endTime));
 * }
 * 
 * /**
 * This should test the Interface from the ServiceConnector.
 * For that it usually get 'fake' records.
 * For no infinite loop we should stop the recording with sending a 0 to run() method of ServiceContainer class in KDB.
 * 
 * private static void TestConnector() {
 * 
 * }
 * 
 * }
 * 
 * /**
 * Creates a new OperationExecutionRecord with given parameters.
 * OperationExecutionRecord has different Constants which have to be used if f.e. no hostname, session_id etc required.
 * 
 * @param opString
 * this is an operation String which is defined in {@link kieker.common.util.ClassOperationSignaturePair#splitOperationSignatureStr(String)}.
 * 
 * @param traceId
 * 
 * @param tin
 * the execution start timestamp.
 * 
 * @param tout
 * the execution stop timestamp.
 * 
 * @return
 * 
 * private static OperationExecutionRecord createOperationExecutionRecord(final String opString, final long traceId, final long tin, final long tout) {
 * 
 * return new OperationExecutionRecord(opString, OperationExecutionRecord.NO_SESSION_ID, traceId, tin, tout, OperationExecutionRecord.NO_HOSTNAME,
 * OperationExecutionRecord.NO_EOI_ESS, OperationExecutionRecord.NO_EOI_ESS);
 * }
 * }
 */

