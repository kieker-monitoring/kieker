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
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.filesystem.SyncFsWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class TestLogRotationMaxLogSizeSyncFsWriter extends AbstractTestLogRotationMaxLogSize {

	/**
	 * Default constructor.
	 */
	public TestLogRotationMaxLogSizeSyncFsWriter() {
		super(3 + new EmptyRecord().toString().length() + System.getProperty("line.separator").length());
	}

	@Override
	protected IMonitoringController createController(final String path, final int maxEntriesInFile, final int maxLogSize) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationFactory.METADATA, "false");
		configuration.setProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, "false"); // needed for constant size
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, SyncFsWriter.class.getName());
		configuration.setProperty(SyncFsWriter.CONFIG_PATH, path);
		configuration.setProperty(SyncFsWriter.CONFIG_MAXENTRIESINFILE, String.valueOf(maxEntriesInFile));
		configuration.setProperty(SyncFsWriter.CONFIG_MAXLOGFILES, "-1");
		configuration.setProperty(SyncFsWriter.CONFIG_MAXLOGSIZE, String.valueOf(maxLogSize));
		return MonitoringController.createInstance(configuration);
	}
}
