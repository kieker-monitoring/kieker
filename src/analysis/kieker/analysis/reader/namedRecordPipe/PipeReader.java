package kieker.analysis.reader.namedRecordPipe;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;
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
	private String pipeName;

	public PipeReader() {
	}

	public PipeReader(final String pipeName) {
		this.initPipe(pipeName);
	}

	private void initPipe(final String pipeName) throws IllegalArgumentException {
		this.pipeName = pipeName;
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
		if (this.pipe == null) {
			PipeReader.log.error("Failed to get Pipe with name " + this.pipeName);
			throw new IllegalArgumentException("Failed to get Pipe with name "
					+ this.pipeName);
		}
		this.pipe.setPipeReader(this);
	}

	@Override
	public boolean read() throws MonitoringLogReaderException {
		// No need to initialize since we receive asynchronously
		return true;
	}

	@Override
	public void init(final String initString) throws IllegalArgumentException {
		final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws
																			// IllegalArgumentException
		this.initPipe(propertyMap.getProperty(PipeReader.PROPERTY_PIPE_NAME));
		PipeReader.log.info("Connected to pipe '" + this.pipeName + "'" + " (" + this.pipe
				+ ")");
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord rec) {
		return super.deliverRecord(rec);
	}
}
