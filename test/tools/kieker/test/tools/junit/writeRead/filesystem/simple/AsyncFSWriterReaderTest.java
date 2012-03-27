package kieker.test.tools.junit.writeRead.filesystem.simple;

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.monitoring.writer.filesystem.SyncFsWriter;
import kieker.test.tools.junit.writeRead.filesystem.AbstractTestFSWriterReader;

/**
 * 
 * @author André van Hoorn
 * 
 */
public class AsyncFSWriterReaderTest extends AbstractTestFSWriterReader {
	private final boolean flush = true;

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncFsWriter.class;
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return !this.flush;
	}

	@Override
	protected void refineConfiguration(final Configuration config, final int numRecordsWritten) {
		config.setProperty(this.getClass().getName() + "." + SyncFsWriter.CONFIG_FLUSH, Boolean.toString(this.flush));
		// TODO: additional configuration parameters
	}
}
