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

package kieker.test.tools.junit.writeRead.database;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.database.AsyncDbWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class TestAsyncDbWriterReader extends AbstractTestDbWriterReader { // NOPMD (TestClassWithoutTestCases)
	/**
	 * Default constructor.
	 */
	public TestAsyncDbWriterReader() {
		// empty default constructor
	}

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws Exception {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.METADATA, "false");
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, AsyncDbWriter.class.getName());
		config.setProperty(AsyncDbWriter.CONFIG_DRIVERCLASSNAME, DRIVERCLASSNAME);
		config.setProperty(AsyncDbWriter.CONFIG_CONNECTIONSTRING, this.getConnectionString() + ";create=true");
		config.setProperty(AsyncDbWriter.CONFIG_TABLEPREFIX, TABLEPREFIX);
		config.setProperty(AsyncDbWriter.CONFIG_NRCONN, "2");
		return MonitoringController.createInstance(config);
	}

	@Override
	protected boolean terminateBeforeLogInspection() { // NOPMD (empty method)
		return true; // might be better to terminate async writers ...
	}
}
