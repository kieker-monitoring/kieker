package kieker.test.tools.junit.writeRead.filesystem.simple;

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AsyncBinaryFsWriter;
import kieker.test.tools.junit.writeRead.filesystem.AbstractTestFSWriterReader;

/**
 * 
 * @author André van Hoorn
 * 
 */
public class AsyncBinaryFSWriterReaderTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases)

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncBinaryFsWriter.class;
	}

	@Override
	protected void refineConfiguration(final Configuration config, final int numRecordsWritten) {
		// TODO: additional configuration parameters
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return true; // because the AsyncBinaryFsWriter doesn't flush
	}

}
