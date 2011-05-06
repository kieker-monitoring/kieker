package kieker.test.monitoring.junit.core;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.test.monitoring.junit.util.DummyRecord;
import kieker.test.monitoring.junit.util.NamedPipeFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestMonitoringControllerRecordsPassedInMonitoringStates extends TestCase {

	public void testRecordsPassedToWriterWhenEnabled() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController =
				NamedPipeFactory
						.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords =
				NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		Assert.assertTrue("Failed to enable monitoring", monitoringController.enableMonitoring());
		monitoringController.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 1, receivedRecords.size());
		monitoringController.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenDisabled() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController =
				NamedPipeFactory
						.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords =
				NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		Assert.assertTrue("Failed to disable monitoring", monitoringController.disableMonitoring());
		monitoringController.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, receivedRecords.size());
		monitoringController.terminateMonitoring();
	}

	public void testNoRecordsPassedToWriterWhenTerminated() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController =
				NamedPipeFactory
						.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords =
				NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		monitoringController.terminateMonitoring();
		monitoringController.newMonitoringRecord(new DummyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, receivedRecords.size());
	}
}
