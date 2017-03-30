package kieker.test.toolsteetime.junit.writeRead.jms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.jms.JmsWriter;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

import teetime.framework.test.StageTester;

/**
 * @author Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.8
 */
public class BasicJMSWriterReaderTest {
	private static final int TIMEOUT_IN_MS = 0;

	@Test
	public void testWriteRead() throws InterruptedException { // NOPMD (JUnitTestsShouldIncludeAssert)
		final List<IMonitoringRecord> inputRecords = this.provideEvents();

		// Create monitoring controller for JMSWriter
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, JmsWriter.class.getName());
		config.setProperty(JmsWriter.CONFIG_CONTEXTFACTORYTYPE, FakeInitialContextFactory.class.getName());
		config.setProperty(JmsWriter.CONFIG_FACTORYLOOKUPNAME, "ConnectionFactory");
		final MonitoringController monCtrl = MonitoringController.createInstance(config);

		// Start the reader
		final JMSReaderThread jmsReaderThread = new JMSReaderThread("tcp://127.0.0.1:61616/", "queue1", FakeInitialContextFactory.class.getName());
		jmsReaderThread.start();
		Thread.sleep(1000); // wait a second to make sure the reader is ready to read

		this.checkControllerStateBeforeRecordsPassedToController(monCtrl);

		// Send records
		for (final IMonitoringRecord record : inputRecords) {
			monCtrl.newMonitoringRecord(record);
		}

		Thread.sleep(1000); // wait a second to give the FS writer the chance to write the monitoring log.

		this.checkControllerStateAfterRecordsPassedToController(monCtrl);

		final List<IMonitoringRecord> outputRecords = jmsReaderThread.getOutputList();

		// Inspect records (sublist is used to exclude the KiekerMetadataRecord sent by the monitoring controller)
		Assert.assertEquals("Unexpected set of records", inputRecords, outputRecords.subList(1, outputRecords.size()));

		// Need to terminate explicitly, because otherwise, the monitoring log directory cannot be removed
		monCtrl.terminateMonitoring();
		monCtrl.waitForTermination(TIMEOUT_IN_MS);
	}

	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		monitoringController.disableMonitoring();
		Assert.assertFalse(monitoringController.isMonitoringEnabled());
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
	 * Extra thread for JMXReader for testing
	 *
	 * @author Lars Bluemke
	 */
	private class JMSReaderThread extends Thread {
		private final JMSReader jmsReader;
		private final List<IMonitoringRecord> outputList;

		public JMSReaderThread(final String jmsProviderUrl, final String jmsDestination, final String jmsFactoryLookupName) {
			this.jmsReader = new JMSReader(jmsProviderUrl, jmsDestination, jmsFactoryLookupName);
			this.outputList = new LinkedList<>();
		}

		@Override
		public void run() {
			StageTester.test(this.jmsReader).and().receive(this.outputList).from(this.jmsReader.getOutputPort()).start();
		}

		public List<IMonitoringRecord> getOutputList() {
			return this.outputList;
		}
	}
}
