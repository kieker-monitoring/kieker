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

package kieker.test.tools.junit.writeRead.filesystem.unknownTypes;

import java.util.List;

import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;

import org.junit.Assert;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TerminateOnFirstUnknownTypeRegularFileTest extends AbstractUnknownTypeTest { // NOPMD NOCS (TestClassWithoutTestCases)
	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncFsWriter.class;
	}

	@Override
	protected void refineWriterConfiguration(final Configuration config, final int numRecordsWritten) {
		config.setProperty(this.getClass().getName() + "." + AsyncFsWriter.CONFIG_FLUSH, Boolean.TRUE.toString());
		// TODO: additional configuration parameters
	}

	/**
	 * Here, we make sure that the reader aborts on the first occurrence of an unknown type.
	 */
	@Override
	protected void refineFSReaderConfiguration(final Configuration config) {
		config.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.FALSE.toString());
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		// we expect that reading abort on the occurrence of EVENT1_UNKNOWN_TYPE, i.e., the remaining lines weren't processed
		Assert.assertEquals("Expected one record", 1, eventFromMonitoringLog.size());
		Assert.assertEquals("Unexpected record", EVENT0_KNOWN_TYPE, eventFromMonitoringLog.get(0));
	}
}
