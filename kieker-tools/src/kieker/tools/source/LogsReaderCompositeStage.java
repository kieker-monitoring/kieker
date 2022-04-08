/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kieker.analysis.source.ISourceCompositeStage;
import kieker.analysis.source.file.DirectoryReaderStage;
import kieker.analysis.source.file.DirectoryScannerStage;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;

/**
 * Read one or more Kieker log directories consecutively.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class LogsReaderCompositeStage extends CompositeStage implements ISourceCompositeStage {

	public static final String PREFIX = LogsReaderCompositeStage.class.getCanonicalName() + ".";
	public static final String LOG_DIRECTORIES = PREFIX + "logDirectories";
	public static final String DATA_BUFFER_SIZE = PREFIX + "bufferSize";
	public static final String VERBOSE = PREFIX + "verbose";

	private static final int DEFAULT_BUFFER_SIZE = 8192;

	private final DirectoryScannerStage directoryScannerStage; // NOPMD this stays here for documentation purposes
	private final DirectoryReaderStage directoryReaderStage;

	/**
	 * Creates a composite stage to scan and read a set of Kieker log directories.
	 *
	 * @param configuration
	 *            configuration for the enclosed filters
	 */
	public LogsReaderCompositeStage(final Configuration configuration) {
		final String[] directoryNames = configuration.getStringArrayProperty(LOG_DIRECTORIES, ":");
		final List<File> directories = new ArrayList<>(directoryNames.length);

		for (final String name : directoryNames) {
			directories.add(new File(name));
		}

		final int dataBufferSize = configuration.getIntProperty(DATA_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
		final boolean verbose = configuration.getBooleanProperty(VERBOSE, false);

		this.directoryScannerStage = new DirectoryScannerStage(directories);
		this.directoryReaderStage = new DirectoryReaderStage(verbose, dataBufferSize);

		this.connectPorts(this.directoryScannerStage.getOutputPort(), this.directoryReaderStage.getInputPort());
	}

	/**
	 * Creates a composite stage to scan and read a set of Kieker log directories.
	 *
	 * @param directories
	 *            list of directories to read
	 * @param verbose
	 *            report on every read log file
	 * @param dataBufferSize
	 *            buffer size of the data file reader (null == use default setting)
	 */
	public LogsReaderCompositeStage(final List<File> directories, final boolean verbose, final Integer dataBufferSize) {
		this.directoryScannerStage = new DirectoryScannerStage(directories);
		this.directoryReaderStage = new DirectoryReaderStage(verbose, dataBufferSize == null ? DEFAULT_BUFFER_SIZE : dataBufferSize); // NOCS inline conditional

		this.connectPorts(this.directoryScannerStage.getOutputPort(), this.directoryReaderStage.getInputPort());
	}

	/**
	 * Creates a composite stage to scan and read a set of Kieker log directories.
	 *
	 * @param directory
	 *            list of directories to read
	 * @param verbose
	 *            report on every read log file
	 * @param dataBufferSize
	 *            buffer size of the data file reader (null == use default setting)
	 */
	public LogsReaderCompositeStage(final File directory, final boolean verbose, final Integer dataBufferSize) {
		final List<File> directories = new ArrayList<>();
		directories.add(directory);

		this.directoryScannerStage = new DirectoryScannerStage(directories);
		this.directoryReaderStage = new DirectoryReaderStage(verbose, dataBufferSize == null ? DEFAULT_BUFFER_SIZE : dataBufferSize); // NOCS inline conditional

		this.connectPorts(this.directoryScannerStage.getOutputPort(), this.directoryReaderStage.getInputPort());
	}

	@Override
	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.directoryReaderStage.getOutputPort();
	}

}
