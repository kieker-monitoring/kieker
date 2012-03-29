/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AsyncBinaryFsWriter;

/**
 * @author Andre van Hoorn
 */
public class BasicAsyncBinaryFSWriterReaderTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases) // NOCS (MissingCtorCheck)

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
