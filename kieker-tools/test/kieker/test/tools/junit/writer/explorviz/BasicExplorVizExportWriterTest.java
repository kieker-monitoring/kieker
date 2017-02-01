/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writer.explorviz;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writernew.explorviz.ExplorVizTcpWriter;

import kieker.test.tools.junit.writeRead.TestAnalysis;
import kieker.test.tools.junit.writeRead.TestDataRepository;
import kieker.test.tools.junit.writeRead.TestProbe;

/**
 * Simple test to ensure that ExplorViz is receiving all sent records from the Kieker ExplorVizExportWriter correctly.
 *
 * @author Micky Singh Multani, Christian Wulf
 *
 * @since 1.11
 */
public class BasicExplorVizExportWriterTest {

	private static final String PORT = "10555";

	private static final TestDataRepository TEST_DATA_REPOSITORY = new TestDataRepository();
	private static final int TIMEOUT_IN_MS = 0;

	public BasicExplorVizExportWriterTest() {
		super();
	}

	@Test
	public void testExplorvizCommunication() throws Exception {
		// define records to be triggered by the test probe
		final List<IMonitoringRecord> records = TEST_DATA_REPOSITORY.newTestEventRecords();

		// define monitoring config
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, ExplorVizTcpWriter.class.getName());
		config.setProperty(ExplorVizTcpWriter.CONFIG_PORT, BasicExplorVizExportWriterTest.PORT);

		// define analysis config
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(ExplorVizReader.CONFIG_PROPERTY_NAME_PORT, BasicExplorVizExportWriterTest.PORT);

		// declare controllers and start the analysis before monitoring
		final TestAnalysis analysis = new TestAnalysis(readerConfiguration, ExplorVizReader.class);
		analysis.startInNewThread();
		final MonitoringController monitoringController = MonitoringController.createInstance(config);

		// trigger records
		final TestProbe testProbe = new TestProbe(monitoringController);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		testProbe.triggerRecords(records);
		Assert.assertTrue(monitoringController.isMonitoringEnabled());

		// terminate monitoring
		monitoringController.terminateMonitoring();

		// wait for termination
		monitoringController.waitForTermination(TIMEOUT_IN_MS);
		analysis.waitForTermination(TIMEOUT_IN_MS);

		// read actual records
		final List<IMonitoringRecord> analyzedRecords = analysis.getList();

		// compare actual and expected records
		this.inspectRecords(analyzedRecords, records);
	}

	private void inspectRecords(final List<IMonitoringRecord> eventsFromMonitoringLog, final List<IMonitoringRecord> eventsPassedToController) throws Exception {
		for (int i = 0; i < eventsPassedToController.size(); i++) {
			final IMonitoringRecord eventPassedToController = eventsPassedToController.get(i);
			final IMonitoringRecord eventFromLog = eventsFromMonitoringLog.get(i);

			if (eventFromLog instanceof CustomAfterOperationFailedEvent) {
				final AfterOperationFailedEvent sentRecord = (AfterOperationFailedEvent) eventPassedToController;
				final CustomAfterOperationFailedEvent receivedRecord = (CustomAfterOperationFailedEvent) eventFromLog;
				Assert.assertEquals(
						"Unexpected set of records - sent record does not match received record",
						(sentRecord.getTimestamp() + ", " + sentRecord.getTraceId() + ", " + sentRecord.getOrderIndex()
								+ ", " + sentRecord.getCause()),
						(receivedRecord.getTimestamp() + ", " + receivedRecord.getTraceId() + ", " + receivedRecord.getOrderIndex() + ", "
								+ receivedRecord.getCause()));

			} else if (eventFromLog instanceof CustomAfterOperationEvent) {
				final AfterOperationEvent sentRecord = (AfterOperationEvent) eventPassedToController;
				final CustomAfterOperationEvent receivedRecord = (CustomAfterOperationEvent) eventFromLog;
				Assert.assertEquals("Unexpected set of records - sent record does not match received record",
						(sentRecord.getTimestamp() + ", " + sentRecord.getTraceId() + ", " + sentRecord.getOrderIndex()),
						(receivedRecord.getTimestamp() + ", " + receivedRecord.getTraceId() + ", " + receivedRecord.getOrderIndex()));

			} else if (eventFromLog instanceof BeforeOperationEvent) {
				final BeforeOperationEvent sentRecord = (BeforeOperationEvent) eventPassedToController;
				final BeforeOperationEvent receivedRecord = (BeforeOperationEvent) eventFromLog;
				Assert.assertEquals(
						"Unexpected set of records - sent record does not match received record",
						(sentRecord.getTimestamp() + ", " + sentRecord.getTraceId() + ", " + sentRecord.getOrderIndex()
								+ ", " + sentRecord.getOperationSignature() + ", " + sentRecord.getClassSignature()),
						(receivedRecord.getTimestamp() + ", " + receivedRecord.getTraceId() + ", " + receivedRecord.getOrderIndex() + ", "
								+ receivedRecord.getOperationSignature() + ", " + receivedRecord.getClassSignature()));

			} else if (eventFromLog instanceof RegistryRecord) {
				final RegistryRecord sentRecord = (RegistryRecord) eventPassedToController;
				final RegistryRecord receivedRecord = (RegistryRecord) eventFromLog;
				Assert.assertEquals(
						"Unexpected set of records - sent record does not match received record",
						(sentRecord.getId() + ", " + sentRecord.getString()), (receivedRecord.getId() + ", " + receivedRecord.getString()));
			}
		}
	}

}
