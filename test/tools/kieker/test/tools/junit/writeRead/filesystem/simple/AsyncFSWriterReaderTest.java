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
public class AsyncFSWriterReaderTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases)
	private static final boolean FLUSH = true;

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncFsWriter.class;
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return !AsyncFSWriterReaderTest.FLUSH;
	}

	@Override
	protected void refineConfiguration(final Configuration config, final int numRecordsWritten) {
		config.setProperty(this.getClass().getName() + "." + SyncFsWriter.CONFIG_FLUSH, Boolean.toString(AsyncFSWriterReaderTest.FLUSH));
		// TODO: additional configuration parameters
	}
}
