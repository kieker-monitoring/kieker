package kieker.monitoring.writer;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A writer that does nothing but consuming records.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public class DummyWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(DummyWriter.class);

	public DummyWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return true; // we don't care about incoming records
	}

	@Override
	public void terminate() {
		DummyWriter.log.info(this.getClass().getName() + " shutting down");
	}

	@Override
	public void init() {
	}
}
