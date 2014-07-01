/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.test.tools.junit.writeRead.filesystem;

import java.io.File;
import java.util.List;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AbstractAsyncZipWriter;
import kieker.monitoring.writer.filesystem.AsyncAsciiZipWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public class BasicAsyncAsciiZipWriterReaderTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases) // NOCS (MissingCtorCheck)

	/**
	 * Default constructor.
	 */
	public BasicAsyncAsciiZipWriterReaderTest() {
		// empty default constructor
	}

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncAsciiZipWriter.class;
	}

	@Override
	protected void refineWriterConfiguration(final Configuration config, final int numRecordsWritten) {
		config.setProperty(AsyncAsciiZipWriter.class.getName() + '.' + AbstractAsyncZipWriter.CONFIG_MAXENTRIESINFILE, "1");
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return true; // because the AsyncAsciiZipWriter does not flush
	}

	@Override
	protected void doSomethingBeforeReading(final String[] monitoringLogs) {
		// we'll keep the log untouched
	}

	@Override
	protected void refineFSReaderConfiguration(final Configuration config) {
		// no need to refine
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws AnalysisConfigurationException {
		final String[] monitoringLogs = this.tmpFolder.getRoot().list(new KiekerLogZipFilter());
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = this.tmpFolder.getRoot().getAbsoluteFile() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}
		return this.readLog(monitoringLogs);
	}
}
