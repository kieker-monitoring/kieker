package kieker.test.toolsteetime.junit.writeRead.jmx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.reader.jmx.JMXReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.jmx.JmxWriter;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

import teetime.framework.test.StageTester;

public class BasicJMXWriterReaderTest {

	private static final int TIMEOUT_IN_MS = 0;

	private static final String DOMAIN = "kieker.monitoring";
	private static final String CONTROLLER = "MonitoringController";
	private static final String PORT = "59999";
	private static final String LOGNAME = "MonitoringLog";

	@Test
	public void testWriteRead() throws Exception {
		final List<IMonitoringRecord> someEvents = this.provideEvents();

		// Create monitoring controller for JMXWriter
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_DOMAIN, BasicJMXWriterReaderTest.DOMAIN);
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER_NAME, BasicJMXWriterReaderTest.CONTROLLER);
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE, "true");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_FALLBACK, "false");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_NAME, "JMXServer");
		config.setProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_PORT, BasicJMXWriterReaderTest.PORT);
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, JmxWriter.class.getName());
		config.setProperty(JmxWriter.CONFIG_DOMAIN, "");
		config.setProperty(JmxWriter.CONFIG_LOGNAME, BasicJMXWriterReaderTest.LOGNAME);
		final MonitoringController monCtrl = MonitoringController.createInstance(config);

		// Start the JMXReader
		final JMXReaderThread jmxReaderThread = new JMXReaderThread(false, null, BasicJMXWriterReaderTest.DOMAIN, BasicJMXWriterReaderTest.LOGNAME,
				Integer.parseInt(BasicJMXWriterReaderTest.PORT), "localhost");
		jmxReaderThread.start();

		this.checkControllerStateBeforeRecordsPassedToController(monCtrl);

		// Send records
		for (final IMonitoringRecord record : someEvents) {
			monCtrl.newMonitoringRecord(record);
		}

		Thread.sleep(1000); // wait a second to give the writer the chance to write the monitoring log.

		this.checkControllerStateAfterRecordsPassedToController(monCtrl);

		final List<IMonitoringRecord> monitoringRecords = jmxReaderThread.getOutputList();

		// Inspect records
		Assert.assertEquals("Unexpected set of records", someEvents, monitoringRecords);

		// Need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
		monCtrl.terminateMonitoring();
		monCtrl.waitForTermination(TIMEOUT_IN_MS);

	}

	/**
	 * Returns a list of {@link IMonitoringRecord}s to be used in this test. Extending classes can override this method to use their own list of records.
	 *
	 * @return A list of records.
	 */
	protected List<IMonitoringRecord> provideEvents() {
		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		for (int i = 0; i < 5; i = someEvents.size()) {
			final List<AbstractTraceEvent> nextBatch = Arrays.asList(
					BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(i, i, "Mn51D97t0",
							"srv-LURS0EMw").getTraceEvents());
			someEvents.addAll(nextBatch);
		}
		someEvents.add(new EmptyRecord()); // this record used to cause problems (#475)
		return someEvents;
	}

	/**
	 * Checks if the given {@link IMonitoringController} is in the expected state before having passed
	 * the records to the controller.
	 *
	 * @param monitoringController
	 *            The monitoring controller in question.
	 *
	 * @throws Exception
	 *             If something went wrong during the check.
	 */
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		// Test the JMX Controller
		final JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + BasicJMXWriterReaderTest.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(BasicJMXWriterReaderTest.DOMAIN, "type", BasicJMXWriterReaderTest.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final Object tmpObj = MBeanServerInvocationHandler.newProxyInstance(mbServer, controllerObjectName, IMonitoringController.class, false);
		final IMonitoringController ctrlJMX = (IMonitoringController) tmpObj; // NOCS // NOPMD (required for the cast not being removed by Java 1.6 editors)

		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}

	/**
	 * Checks if the given {@link IMonitoringController} is in the expected state after having passed
	 * the records to the controller.
	 *
	 * @param monitoringController
	 *            The monitoring controller in question.
	 *
	 * @throws Exception
	 *             If something went wrong during the check.
	 */
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		// Test the JMX Controller
		final JMXServiceURL serviceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + BasicJMXWriterReaderTest.PORT + "/jmxrmi");
		final ObjectName controllerObjectName = new ObjectName(BasicJMXWriterReaderTest.DOMAIN, "type", BasicJMXWriterReaderTest.CONTROLLER);

		final JMXConnector jmx = JMXConnectorFactory.connect(serviceURL);
		final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();

		final Object tmpObj = MBeanServerInvocationHandler.newProxyInstance(mbServer, controllerObjectName, IMonitoringController.class, false);
		final IMonitoringController ctrlJMX = (IMonitoringController) tmpObj; // NOCS // NOPMD (required for the cast not being removed by Java 1.6 editors)

		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertTrue(ctrlJMX.isMonitoringEnabled());

		Assert.assertTrue(ctrlJMX.disableMonitoring());

		Assert.assertFalse(monitoringController.isMonitoringEnabled());
		Assert.assertFalse(ctrlJMX.isMonitoringEnabled());

		jmx.close();
	}

	/**
	 * Extra thread for JMXReader for testing
	 *
	 * @author Lars Bluemke
	 */
	private class JMXReaderThread extends Thread {
		private final JMXReader jmxReader;
		private final List<IMonitoringRecord> outputList;

		public JMXReaderThread(final boolean silentreconnect, final JMXServiceURL serviceURL, final String domain, final String logname,
				final int port, final String server) {
			this.jmxReader = new JMXReader(silentreconnect, serviceURL, domain, logname, port, server);
			this.outputList = new LinkedList<>();
		}

		@Override
		public void run() {
			StageTester.test(this.jmxReader).and().receive(this.outputList).from(this.jmxReader.getOutputPort()).start();
		}

		public List<IMonitoringRecord> getOutputList() {
			return this.outputList;
		}
	}
}
