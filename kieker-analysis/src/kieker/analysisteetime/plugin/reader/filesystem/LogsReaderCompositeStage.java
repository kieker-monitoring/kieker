/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysisteetime.plugin.reader.filesystem;

import java.io.File;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;

/**
 * Read one or more Kieker log directories consecutively.
 *
 * @author Reiner Jung
 *
 */
public class LogsReaderCompositeStage extends CompositeStage {

	public static final String PREFIX = LogsReaderCompositeStage.class.getCanonicalName() + ".";
	public static final String LOG_DIRECTORIES = PREFIX + "logDirectories";

	private final DirectoryScannerStage directoryScannerStage;
	private final DirectoryReaderStage directoryReaderStage;

	public LogsReaderCompositeStage(final Configuration configuration) {

		final String[] directoryNames = configuration.getStringArrayProperty(LOG_DIRECTORIES, ":");
		final File[] directories = new File[directoryNames.length];
		int i = 0;
		for (final String name : directoryNames) {
			directories[i++] = new File(name);
		}

		final AbstractMapReader mapReader = new TextMapReader();

		this.directoryScannerStage = new DirectoryScannerStage(directories);
		this.directoryReaderStage = new DirectoryReaderStage(mapReader, configuration);

		this.connectPorts(this.directoryScannerStage.getOutputPort(), this.directoryReaderStage.getInputPort());
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.directoryReaderStage.getOutputPort();
	}

}
