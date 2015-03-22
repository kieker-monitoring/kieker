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

package kieker.test.monitoring.junit.writer.filesystem;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.AbstractAsyncWriter;
import kieker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kieker.monitoring.writer.filesystem.AsyncBinaryFsWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class TestLogRotationMaxLogSizeAsyncBinaryFsWriter extends AbstractTestLogRotationMaxLogSize {

	/**
	 * Default constructor.
	 */
	public TestLogRotationMaxLogSizeAsyncBinaryFsWriter() {
		super(4 + 8); // int and long
	}

	@Override
	protected IMonitoringController createController(final String path, final int maxEntriesInFile, final int maxLogSize) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationFactory.METADATA, "false");
		configuration.setProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, "false"); // needed for constant size
		final String writer = AsyncBinaryFsWriter.class.getName();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, writer);
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_PATH, path);
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXENTRIESINFILE, String.valueOf(maxEntriesInFile));
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXLOGFILES, "-1");
		configuration.setProperty(writer + '.' + AbstractAsyncFSWriter.CONFIG_MAXLOGSIZE, String.valueOf(maxLogSize));
		configuration.setProperty(writer + '.' + AbstractAsyncWriter.CONFIG_QUEUESIZE, "1000000");
		return MonitoringController.createInstance(configuration);
	}
}
