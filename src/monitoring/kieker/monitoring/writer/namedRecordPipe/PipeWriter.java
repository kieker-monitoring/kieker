package kieker.monitoring.writer.namedRecordPipe;


import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 */
public final class PipeWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(PipeWriter.class);

	private static final String PREFIX = PipeWriter.class.getName() + ".";
	private static final String PIPENAME = PipeWriter.PREFIX + "pipeName";
	private final Pipe pipe;

	public PipeWriter(final Configuration configuration) {
		super(configuration);
		final String pipeName = this.configuration.getStringProperty(PipeWriter.PIPENAME);
		if (pipeName.isEmpty()) {
			PipeWriter.log.error("Invalid or missing value for property '" + PipeWriter.PIPENAME + "': '" + pipeName + "'");
			throw new IllegalArgumentException("Invalid or missing value for property '" + PipeWriter.PIPENAME + "': '" + pipeName + "'");
		}
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
		if (this.pipe == null) {
			PipeWriter.log.error("Failed to get pipe with name:" + pipeName);
			throw new IllegalArgumentException("Failed to get pipe with name:" + pipeName);
		}
	}

	@Override
	public final void terminate() {
		if (this.pipe != null) {
			this.pipe.close();
		}
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		return this.pipe.writeMonitoringRecord(monitoringRecord);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tConnected to pipe: '");
		sb.append(this.pipe.getName());
		sb.append("'");
		return sb.toString();
	}

	/**
	 * Nothing to do
	 */
	@Override
	protected void init() {
	}
}
