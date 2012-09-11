/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.printStream;

import junit.framework.Assert;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.PrintStreamWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractPrintStreamWriterTest extends AbstractWriterReaderTest {

	protected static final String SYSTEM_NEWLINE_STRING = System.getProperty("line.separator");

	/**
	 * Returns the name of the stream to write to. In addition to a file name,
	 * the constants <i>STDOUT</i> and <i>STDERR</i> can be used.
	 * 
	 * @return
	 */
	protected abstract String provideStreamName();

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, PrintStreamWriter.class.getName());
		config.setProperty(PrintStreamWriter.STREAM, this.provideStreamName());
		return MonitoringController.createInstance(config);
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}
}
