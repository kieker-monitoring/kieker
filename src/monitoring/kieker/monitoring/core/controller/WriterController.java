package kieker.monitoring.core.controller;

import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller, Robert von Massow
 */
public final class WriterController extends AbstractController implements IWriterController {
	private static final Log log = LogFactory.getLog(WriterController.class);

	/** the total number of monitoring records received */
	private final AtomicLong numberOfInserts = new AtomicLong(0);
	/** Monitoring Writer */
	private final IMonitoringWriter monitoringWriter;

	public WriterController(final Configuration configuration) {
		this.monitoringWriter = createAndInitialize(IMonitoringWriter.class, configuration.getStringProperty(Configuration.WRITER_CLASSNAME), configuration);
		if (this.monitoringWriter == null) {
			terminate();
			return;
		}
	}

	@Override
	protected final void init() {
		if (this.monitoringWriter != null) {
			try {
				this.monitoringWriter.setController(super.monitoringController);
			} catch (final Exception e) {
				log.error("Error initializing writer", e);
				terminate();
			}
		}
	}
	
	@Override
	protected final void cleanup() {
		WriterController.log.info("Shutting down Writer Controller");
		if (this.monitoringWriter != null) {
			this.monitoringWriter.terminate();
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("WriterController:\n\tNumber of Inserts: '");
		sb.append(this.getNumberOfInserts());
		sb.append("'\n");
		if (this.monitoringWriter != null) {
			sb.append(this.monitoringWriter.toString());
		} else {
			sb.append("\tNo Monitoring Writer available");
		}
		return sb.toString();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			final MonitoringController monitoringController = super.monitoringController;
			if (!monitoringController.isMonitoringEnabled()) { // enabled and not terminated
				return false;
			}
			record.setLoggingTimestamp(monitoringController.getTimeSource().getTime());
			numberOfInserts.incrementAndGet();
			final boolean successfulWriting = this.monitoringWriter.newMonitoringRecord(record);
			if (!successfulWriting) {
				WriterController.log.fatal("Error writing the monitoring data. Will terminate monitoring!");
				this.terminate();
				return false;
			}
			return true;
		} catch (final Throwable ex) {
			WriterController.log.fatal("Exception detected. Will terminate monitoring", ex);
			this.terminate();
			return false;
		}
	}

	@Override
	public final long getNumberOfInserts() {
		return this.numberOfInserts.longValue();
	}
}
