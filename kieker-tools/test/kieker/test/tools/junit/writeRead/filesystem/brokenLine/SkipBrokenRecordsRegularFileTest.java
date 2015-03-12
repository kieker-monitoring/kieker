/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.filesystem.brokenLine;

import java.io.IOException;

import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;

import kieker.test.tools.junit.writeRead.filesystem.unknownTypes.ContinueAfterUnknownTypeRegularFileTest;

/**
 * We slightly misuse the functionality of {@link ContinueAfterUnknownTypeRegularFileTest}:
 * Instead of replacing the type entry by an invalid one, we replace by just another
 * valid type that does not match the type. We want to make sure that only these records
 * are ignored, but the readers processes the remaining part of the file.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.5
 */
public class SkipBrokenRecordsRegularFileTest extends ContinueAfterUnknownTypeRegularFileTest { // NOPMD NOCS (TestClassWithoutTestCases)

	/**
	 * Here, we make sure that the reader aborts on the first occurrence of an unknown type
	 * (a case which does not occur in this test).
	 * 
	 * @param config
	 *            The configuration to modify.
	 */
	@Override
	protected void refineFSReaderConfiguration(final Configuration config) {
		config.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.FALSE.toString());
	}

	@Override
	protected void doSomethingBeforeReading(final String[] monitoringLogs) throws IOException {
		final String classnameToManipulate = EVENT1_UNKNOWN_TYPE.getClass().getName();
		this.replaceStringInMapFiles(monitoringLogs, classnameToManipulate, OperationExecutionRecord.class.getName());
	}
}
