package kieker.common.namedRecordPipe;

import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class Pipe {
	private static final Log log = LogFactory.getLog(Pipe.class);

	private final String name;
	private volatile IPipeReader pipeReader;
	private volatile boolean closed;

	public void setPipeReader(final IPipeReader pipeReader) {
		this.pipeReader = pipeReader;
		Pipe.log.info("PipeReader initialized");
	}

	public String getName() {
		return this.name;
	}

	/** No construction employing default constructor */
	@SuppressWarnings("unused")
	private Pipe() {
		this.name = null;
	}

	public Pipe(final String name) {
		this.name = name;
	}

	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord)
			throws PipeException {
		if (this.closed) {
			Pipe.log.error("trying to write to closed pipe");
			throw new PipeException("trying to write to closed pipe");
		}
		this.pipeReader.newMonitoringRecord(monitoringRecord);
	}

	public void close() {
		this.closed = true;
//		try {
//			this.writeMonitoringRecord(MonitoringController.END_OF_MONITORING_MARKER);
//		} catch (final PipeException ex) {
//			Pipe.log.error("Failed to send END_OF_MONITORING_MARKER", ex);
//			// we can't do anything more
//		}
	}
}
