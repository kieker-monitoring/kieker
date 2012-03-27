package kieker.test.tools.junit.writeRead.filesystem.simple;

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.SyncFsWriter;
import kieker.test.tools.junit.writeRead.filesystem.AbstractTestFSWriterReader;

/**
 * 
 * @author André van Hoorn
 * 
 */
public class SyncFSWriterReaderTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases)

	private static final boolean FLUSH = true;

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return SyncFsWriter.class;
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return !SyncFSWriterReaderTest.FLUSH;
	}

	@Override
	protected void refineConfiguration(final Configuration config, final int numRecordsWritten) {
		config.setProperty(this.getClass().getName() + "." + SyncFsWriter.CONFIG_FLUSH, Boolean.toString(SyncFSWriterReaderTest.FLUSH));
		// TODO: additional configuration parameters
	}

}
