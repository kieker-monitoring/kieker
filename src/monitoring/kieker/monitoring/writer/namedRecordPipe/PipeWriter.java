package kieker.monitoring.writer.namedRecordPipe;

import java.util.Vector;

import kieker.common.namedRecordPipe.Pipe;
import kieker.common.namedRecordPipe.PipeException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class PipeWriter implements IMonitoringLogWriter {
	private static final Log log = LogFactory.getLog(PipeWriter.class);

	private static final String PROPERTY_PIPE_NAME = "pipeName";
	private Pipe pipe;
	private String pipeName;

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			this.pipe.writeMonitoringRecord(monitoringRecord);
		} catch (final PipeException ex) {
			PipeWriter.log.error("PipeException occured", ex);
			return false;
		}
		return true;
	}

	@Override
	public boolean init(final String initString) throws IllegalArgumentException {
		final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws
																			// IllegalArgumentException
		this.pipeName = propertyMap.getProperty(PipeWriter.PROPERTY_PIPE_NAME);
		if ((this.pipeName == null) || (this.pipeName.length() == 0)) {
			PipeWriter.log.error("Invalid or missing pipeName value for property '"
					+ PipeWriter.PROPERTY_PIPE_NAME + "'");
			throw new IllegalArgumentException(
					"Invalid or missing pipeName value:" + this.pipeName);
		}
		this.pipe = Broker.getInstance().acquirePipe(this.pipeName);
		if (this.pipe == null) {
			PipeWriter.log.error("Failed to get pipe with name:" + this.pipeName);
			throw new IllegalArgumentException("Failed to get pipe with name:"
					+ this.pipeName);
		}
		PipeWriter.log.info("Connected to pipe '" + this.pipeName + "'" + " (" + this.pipe
				+ ")");
		return true;
	}

	@Override
	public Vector<AbstractWorkerThread> getWorkers() {
		return new Vector<AbstractWorkerThread>();
	}

	@Override
	public String getInfoString() {
		final StringBuilder strB = new StringBuilder();

		strB.append("pipeName : " + this.pipeName);

		return strB.toString();
	}

}
