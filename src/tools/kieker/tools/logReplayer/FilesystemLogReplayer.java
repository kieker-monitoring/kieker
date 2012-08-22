/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.logReplayer;

import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;

/**
 * An implementation of the {@link AbstractLogReplayer}, using the {@link FSReader} to replay {@link kieker.common.record.IMonitoringRecord}s from a list of file
 * system monitoring logs.
 * 
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer extends AbstractLogReplayer {
	// private static final Log LOG = LogFactory.getLog(FilesystemLogReplayer.class);

	private final String[] inputDirs;

	public FilesystemLogReplayer(final String monitoringConfigurationFile, final boolean realtimeMode, final boolean keepOriginalLoggingTimestamps,
			final int numRealtimeWorkerThreads, final long ignoreRecordsBeforeTimestamp, final long ignoreRecordsAfterTimestamp,
			final String[] inputDirs) {
		super(monitoringConfigurationFile, realtimeMode, keepOriginalLoggingTimestamps, numRealtimeWorkerThreads, ignoreRecordsBeforeTimestamp,
				ignoreRecordsAfterTimestamp);
		// Java 1.5 compatibility
		this.inputDirs = new String[inputDirs.length];
		System.arraycopy(inputDirs, 0, this.inputDirs, 0, inputDirs.length);
		// for Java 1.6+:
		// this.inputDirs = Arrays.copyOf(inputDirs, inputDirs.length);

	}

	@Override
	protected AbstractReaderPlugin createReader() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(this.inputDirs));
		// TODO: we might want to pull this out as a property
		configuration.setProperty(FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, Boolean.toString(true));
		return new FSReader(configuration);
	}

	@Override
	protected String readerOutputPortName() {
		return FSReader.OUTPUT_PORT_NAME_RECORDS;
	}

}
