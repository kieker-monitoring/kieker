package kieker.test.monitoring.junit.writer.namedRecordPipe;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;
import kieker.test.monitoring.junit.util.DummyRecord;
import kieker.test.monitoring.junit.util.NamedPipeFactory;

/**
 * @author Andre van Hoorn
 * 
 */
public class TestPipeWriter extends TestCase {	
	/**
	 * Tests whether the {@link PipeWriter} correctly passes received
	 * {@link IMonitoringRecord}s to the {@link Broker} (which then passes these
	 * to an {@link IPipeReader}).
	 */
	public void testNamedPipeWriterPassesRecordsToPipe() {
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

		/*
		 * Send 7 dummy records
		 */
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			monitoringController.newMonitoringRecord(new DummyRecord());
		}

		/*
		 * Make sure that numRecordsToSend where written.
		 */
		Assert.assertEquals("Unexpected number of records received",
				numRecordsToSend, receivedRecords.size());
	}

}
