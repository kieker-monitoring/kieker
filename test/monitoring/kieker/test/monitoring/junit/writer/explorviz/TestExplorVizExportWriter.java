/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.writer.explorviz;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.explorviz.ExplorVizExportWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * Simple test to ensure that ExplorViz is receiving all sent records from the Kieker ExplorVizExportWriter correctly.
 * 
 * @author Micky Singh Multani
 * 
 * @since 1.11
 */
public class TestExplorVizExportWriter extends AbstractWriterReaderTest {

	private static final String PORT = "10555";

	private volatile ListCollectionFilter<IMonitoringRecord> sinkFilter = null; // NOPMD (init for findbugs)
	private volatile AnalysisController analysisController = null; // NOPMD (init for findbugs)
	private volatile AnalysisControllerThread analysisThread = null; // NOPMD (init for findbugs)

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws IllegalStateException, AnalysisConfigurationException,
			InterruptedException {
		this.analysisController = new AnalysisController();

		final Configuration readerConfig = new Configuration();
		readerConfig.setProperty(ExplorVizReader.CONFIG_PROPERTY_NAME_PORT, TestExplorVizExportWriter.PORT);
		final ExplorVizReader explorvizReader = new ExplorVizReader(readerConfig, this.analysisController);
		this.sinkFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), this.analysisController);
		this.analysisController.connect(explorvizReader, ExplorVizReader.OUTPUT_PORT_NAME_RECORDS, this.sinkFilter, ListCollectionFilter.INPUT_PORT_NAME);
		this.analysisThread = new AnalysisControllerThread(this.analysisController);
		this.analysisThread.start();

		Thread.sleep(1000);

		final Configuration monitoringConfig = ConfigurationFactory.createDefaultConfiguration();
		monitoringConfig.setProperty(ConfigurationFactory.WRITER_CLASSNAME, ExplorVizExportWriter.class.getName());
		monitoringConfig.setProperty(ExplorVizExportWriter.CONFIG_PORT, TestExplorVizExportWriter.PORT);
		return MonitoringController.createInstance(monitoringConfig);
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		monitoringController.terminateMonitoring();
		this.analysisThread.awaitTermination();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
		Assert.assertEquals(AnalysisController.STATE.RUNNING, this.analysisController.getState());
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) throws Exception {

		for (int i = 0; i < eventsPassedToController.size(); i++) {

			if (eventFromMonitoringLog.get(i + 2) instanceof CustomAfterOperationFailedEvent) {
				final AfterOperationFailedEvent sentRecord = (AfterOperationFailedEvent) eventsPassedToController.get(i);
				final CustomAfterOperationFailedEvent receivedRecord = (CustomAfterOperationFailedEvent) eventFromMonitoringLog.get(i + 2);
				Assert.assertEquals(
						"Unexpected set of records ",
						(sentRecord.getTimestamp() + ", " + sentRecord.getTraceId() + ", " + sentRecord.getOrderIndex()
								+ ", " + sentRecord.getCause()),
						(receivedRecord.getTimestamp() + ", " + receivedRecord.getTraceId() + ", " + receivedRecord.getOrderIndex() + ", "
						+ receivedRecord.getCause()));

			} else if (eventFromMonitoringLog.get(i + 2) instanceof CustomAfterOperationEvent) {
				final AfterOperationEvent sentRecord = (AfterOperationEvent) eventsPassedToController.get(i);
				final CustomAfterOperationEvent receivedRecord = (CustomAfterOperationEvent) eventFromMonitoringLog.get(i + 2);
				Assert.assertEquals("Unexpected set of records", (sentRecord.getTimestamp() + ", " + sentRecord.getTraceId() + ", " + sentRecord.getOrderIndex()),
						(receivedRecord.getTimestamp() + ", " + receivedRecord.getTraceId() + ", " + receivedRecord.getOrderIndex()));

			} else if (eventFromMonitoringLog.get(i + 2) instanceof BeforeOperationEvent) {
				final BeforeOperationEvent sentRecord = (BeforeOperationEvent) eventsPassedToController.get(i);
				final BeforeOperationEvent receivedRecord = (BeforeOperationEvent) eventFromMonitoringLog.get(i + 2);
				Assert.assertEquals(
						"Unexpected set of records",
						(sentRecord.getTimestamp() + ", " + sentRecord.getTraceId() + ", " + sentRecord.getOrderIndex()
								+ ", " + sentRecord.getOperationSignature() + ", " + sentRecord.getClassSignature()),
						(receivedRecord.getTimestamp() + ", " + receivedRecord.getTraceId() + ", " + receivedRecord.getOrderIndex() + ", "
								+ receivedRecord.getOperationSignature() + ", " + receivedRecord.getClassSignature()));

			} else if (eventFromMonitoringLog.get(i + 2) instanceof RegistryRecord) {
				final RegistryRecord sentRecord = (RegistryRecord) eventsPassedToController.get(i);
				final RegistryRecord receivedRecord = (RegistryRecord) eventFromMonitoringLog.get(i + 2);
				Assert.assertEquals(
						"Unexpected set of records",
						(sentRecord.getId() + ", " + sentRecord.getString()), (receivedRecord.getId() + ", " + receivedRecord.getString()));
			}
		}
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return false;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		return this.sinkFilter.getList();
	}

	@Override
	protected List<IMonitoringRecord> provideEvents() {

		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		final Object[] testValues1 = { 22L, 11L, 101, "BeOpEv", "BeforeOperationEvent" };
		final Object[] testValues2 = { 6L, 8L, 120, "AfOpEv", "AfterOperationEvent" };
		final Object[] testValues3 = { 10L, 12L, 150, "AfOpFaEv", "AfterOperationFailedEvent", "<cause>" };

		final BeforeOperationEvent testBeforeOperationEvent = new BeforeOperationEvent(testValues1);
		final AfterOperationEvent testAfterOperationEvent = new AfterOperationEvent(testValues2);
		final AfterOperationFailedEvent testAfterOperationFailedEvent = new AfterOperationFailedEvent(testValues3);
		final RegistryRecord testRegistryRecord = new RegistryRecord(10, "testRegistry");

		someEvents.add(testBeforeOperationEvent);
		someEvents.add(testAfterOperationEvent);
		someEvents.add(testAfterOperationFailedEvent);
		someEvents.add(testRegistryRecord);

		return someEvents;
	}

}
