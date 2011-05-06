package kieker.test.monitoring.junit.writer.namedRecordPipe;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;
import kieker.test.monitoring.junit.core.util.DummyRecord;

/**
 * @author Andre van Hoorn
 * 
 */
public class TestPipeWriter extends TestCase {

	private final static AtomicInteger nextPipeId = new AtomicInteger(0);
	private final static String PIPE_NAME_PREFIX = "pipeName_";

	/**
	 * This method should be used in tests to generate unique names for
	 * {@link Configuration}s with {@link PipeWriter}s and {@link PipedReader}s
	 * in order to avoid naming conflicts.
	 * 
	 * @return
	 */
	public static String createPipeName() {
		return TestPipeWriter.PIPE_NAME_PREFIX
				+ TestPipeWriter.nextPipeId.getAndIncrement();
	}

	/**
	 * Creates a new {@link IMonitoringController} instance with the writer
	 * being a {@link PipeWriter} with the given name.
	 * 
	 * @param pipeName
	 * @return
	 */
	public static IMonitoringController createMonitoringControllerWithNamedPipe(
			final String pipeName) {
		final Configuration configuration =
				Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.WRITER_CLASSNAME,
				PipeWriter.class.getName());
		configuration.setProperty(PipeWriter.CONFIG__PIPENAME, pipeName);
		final IMonitoringController monitoringController =
				MonitoringController.createInstance(configuration);
		return monitoringController;
	}
	
	/**
	 * Tests whether the {@link PipeWriter} correctly passes received
	 * {@link IMonitoringRecord}s to the {@link Broker} (which than passes these
	 * to an {@link IPipeReader}).
	 */
	public void testNamedPipeWriterPassesRecordsToPipe() {
		final String pipeName = TestPipeWriter.createPipeName();
		final IMonitoringController monitoringController =
				TestPipeWriter
						.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords =
				new ArrayList<IMonitoringRecord>();
		final Pipe namedPipe = Broker.getInstance().acquirePipe(pipeName);
		namedPipe.setPipeReader(new IPipeReader() {

			@Override
			public boolean newMonitoringRecord(final IMonitoringRecord record) {
				return receivedRecords.add(record);
			}

			@Override
			public void notifyPipeClosed() {
			}
		});

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
