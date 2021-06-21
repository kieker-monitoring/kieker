/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.print.PrintStreamWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public abstract class AbstractPrintStreamWriterTest extends AbstractWriterReaderTest {

	/** This constant contains the correct line separator for the current system. */
	protected static final String SYSTEM_NEWLINE_STRING = System.getProperty("line.separator");

	/**
	 * Returns the name of the stream to write to. In addition to a file name, the constants <i>STDOUT</i> and <i>STDERR</i> can be used.
	 *
	 * @return The stream name.
	 */
	protected abstract String provideStreamName();

	@Override
	protected MonitoringController createController(final int numRecordsWritten) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationConstants.WRITER_CLASSNAME, PrintStreamWriter.class.getName());
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

	@Override
	protected boolean terminateBeforeLogInspection() { // NOPMD (method is not empty, just returning false)
		return false;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() {
		// We cannot do anything meaningful here, because there's nothing like a PrintStreamReader. We'll return an empty List and use our own buffer when evaluating
		// the result.
		return new ArrayList<>();
	}

	protected void checkRecords(final String outputString, final List<IMonitoringRecord> eventsPassedToController) {
		for (final IMonitoringRecord rec : eventsPassedToController) {
			final StringBuilder inputRecordStringBuilder = new StringBuilder();
			inputRecordStringBuilder
					// note that this format needs to be adjusted if the writer's format changes
					.append(rec.getClass().getSimpleName())
					.append(": ")
					.append(rec).append(AbstractPrintStreamWriterTest.SYSTEM_NEWLINE_STRING);
			final String curLine = inputRecordStringBuilder.toString();
			Assert.assertTrue("Record '" + curLine + "' not found in output stream: '" + outputString + "'",
					outputString.indexOf(curLine) != -1);
		}
	}
}
