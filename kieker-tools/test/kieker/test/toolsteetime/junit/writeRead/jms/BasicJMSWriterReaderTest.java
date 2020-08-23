/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.toolsteetime.junit.writeRead.jms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.reader.jms.JMSReaderStage;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationConstants;
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

	/**
	 * Empty default constructor.
	 */
	public BasicJMSWriterReaderTest() {
		// empty constructor
	}

	@Test
	public void testWriteRead() throws InterruptedException { // NOPMD (JUnitTestsShouldIncludeAssert)
		final List<IMonitoringRecord> inputRecords = this.provideEvents();

		// Create monitoring controller for JMSWriter
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, JmsWriter.class.getName());
		config.setProperty(JmsWriter.CONFIG_CONTEXTFACTORYTYPE, FakeInitialContextFactory.class.getName());
		config.setProperty(JmsWriter.CONFIG_FACTORYLOOKUPNAME, "ConnectionFactory");
		final MonitoringController monCtrl = MonitoringController.createInstance(config);

		// Start the reader
		final JMSReaderThread jmsReaderThread = new JMSReaderThread("tcp://127.0.0.1:61616/", "queue1",
				FakeInitialContextFactory.class.getName());
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

		// Inspect records (sublist is used to exclude the KiekerMetadataRecord sent by
		// the monitoring controller)
		Assert.assertEquals("Unexpected set of records", inputRecords, outputRecords.subList(1, outputRecords.size()));

		// Need to terminate explicitly, because otherwise, the monitoring log directory
		// cannot be removed
		monCtrl.terminateMonitoring();
		monCtrl.waitForTermination(TIMEOUT_IN_MS);
	}

	protected void checkControllerStateBeforeRecordsPassedToController(
			final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	protected void checkControllerStateAfterRecordsPassedToController(
			final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		monitoringController.disableMonitoring();
		Assert.assertFalse(monitoringController.isMonitoringEnabled());
	}

	/**
	 * Returns a list of {@link IMonitoringRecord}s to be used in this test.
	 * Extending classes can override this method to use their own list of records.
	 *
	 * @return A list of records.
	 */
	protected List<IMonitoringRecord> provideEvents() {
		final List<IMonitoringRecord> someEvents = new ArrayList<>();
		for (int i = 0; i < 5; i = someEvents.size()) {
			final List<AbstractTraceEvent> nextBatch = Arrays.asList(BookstoreEventRecordFactory
					.validSyncTraceAdditionalCallEventsGap(i, i, "Mn51D97t0", "srv-LURS0EMw").getTraceEvents());
			someEvents.addAll(nextBatch);
		}
		someEvents.add(new EmptyRecord()); // this record used to cause problems (#475)
		return someEvents;
	}

	/**
	 * Executes the JmsReaderStage.
	 *
	 * @author Lars Bluemke
	 */
	private static class JMSReaderThread extends Thread {
		private final JMSReaderStage jmsReaderStage;
		private final List<IMonitoringRecord> outputElements;

		public JMSReaderThread(final String jmsProviderUrl, final String jmsDestination,
				final String jmsFactoryLookupName) {
			this.jmsReaderStage = new JMSReaderStage(jmsProviderUrl, jmsDestination, jmsFactoryLookupName);
			this.outputElements = new LinkedList<>();
		}

		@Override
		public void run() {
			StageTester.test(this.jmsReaderStage).and().receive(this.outputElements)
					.from(this.jmsReaderStage.getOutputPort()).start();
		}

		public List<IMonitoringRecord> getOutputList() {
			return this.outputElements;
		}
	}

	// private static class AnalysisConfiguration extends
	// teetime.framework.Configuration {
	// private final JMSReaderStage jmsReaderStage;
	// private final CollectorSink<IMonitoringRecord> collectorSink;
	//
	// public AnalysisConfiguration(final String jmsProviderUrl, final String
	// jmsDestination,
	// final String jmsFactoryLookupName) {
	// this.jmsReaderStage = new JMSReaderStage(jmsProviderUrl, jmsDestination,
	// jmsFactoryLookupName);
	// this.collectorSink = new CollectorSink<>();
	//
	// this.from(this.jmsReaderStage).end(this.collectorSink);
	// }
	//
	// public List<IMonitoringRecord> getOutputElements() {
	// return this.collectorSink.getElements();
	// }
	// }
}
