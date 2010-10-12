package kieker.analysis.reader.namedRecordPipe;

import java.util.concurrent.CountDownLatch;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class PipeReader extends AbstractMonitoringLogReader implements
		IPipeReader {
	public static final String PROPERTY_PIPE_NAME = "pipeName";
	private static final Log log = LogFactory.getLog(PipeReader.class);

	private volatile Pipe pipe;

	/**
	 * Returns the pipe from which the reader receives its monitoring records.
	 * One reason to require the pipe is to close it by a call to
	 * {@link Pipe#close()}.
	 */
	public Pipe getPipe() {
		return this.pipe;
	}

	public PipeReader() {
	}

	public PipeReader(final String pipeName) {
		this.initPipe(pipeName);
	}

	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	private void initPipe(final String pipeName)
			throws IllegalArgumentException {
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
		if (this.pipe == null) {
			PipeReader.log.error("Failed to get Pipe with name " + pipeName);
			throw new IllegalArgumentException("Failed to get Pipe with name "
					+ pipeName);
		} else {
			PipeReader.log.info("Connectod to named pipe '" + this.pipe.getName() + "'");
		}
		this.pipe.setPipeReader(this);
	}

	/**
	 * Blocks until the associated pipe is being closed.
	 */
	@Override
	public boolean read() {
		// No need to initialize since we receive asynchronously
		try {
			this.terminationLatch.await();
			PipeReader.log.info("Pipe closed. Will terminate.");
		} catch (final InterruptedException e) {
			PipeReader.log.error("Received InterruptedException", e);
			return false;
		}
		return true;
	}

	@Override
	public void init(final String initString) throws IllegalArgumentException {
		final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws
																				// IllegalArgumentException
		this.initPipe(propertyMap.getProperty(PipeReader.PROPERTY_PIPE_NAME));
		PipeReader.log.info("Connected to pipe '" + this.pipe.getName() + "'"
				+ " (" + this.pipe + ")");
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord rec) {
		return super.deliverRecord(rec);
	}

	@Override
	public void notifyPipeClosed() {
		/* Notify main thread */
		this.terminationLatch.countDown();
	}
}
