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

package kieker.test.tools.junit.writeRead.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;

import org.junit.Before;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractTestFSWriterReader extends AbstractWriterReaderTest {
	private volatile Class<? extends IMonitoringWriter> testedWriterClazz = AsyncFsWriter.class;

	// TODO: constants are private in AbstractAsyncWriter ... why?
	private static final String CONFIG_ASYNC_WRITER_QUEUESIZE = "QueueSize";
	private static final String CONFIG_ASYNC_WRITER_BEHAVIOR = "QueueFullBehavior";
	private static final String CONFIG_ASYNC_WRITER_SHUTDOWNDELAY = "MaxShutdownDelay";

	protected abstract Class<? extends IMonitoringWriter> getTestedWriterClazz();

	@Override
	@Before
	public void setUp() throws IOException {
		super.setUp();
		this.testedWriterClazz = this.getTestedWriterClazz();
	}

	@Override
	protected IMonitoringController createController(final int numRecordsWritten, final File monitoringLogBaseDir) throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, this.testedWriterClazz.getName());
		config.setProperty(this.testedWriterClazz.getName() + "." + AbstractAsyncFSWriter.CONFIG_TEMP, Boolean.FALSE.toString());
		config.setProperty(this.testedWriterClazz.getName() + "." + AbstractAsyncFSWriter.CONFIG_PATH, monitoringLogBaseDir.getCanonicalPath());
		config.setProperty(this.testedWriterClazz.getName() + "." + AbstractTestFSWriterReader.CONFIG_ASYNC_WRITER_QUEUESIZE,
				Integer.toString(numRecordsWritten * 2));
		config.setProperty(this.testedWriterClazz.getName() + "." + AbstractTestFSWriterReader.CONFIG_ASYNC_WRITER_BEHAVIOR, "0");
		config.setProperty(this.testedWriterClazz.getName() + "." + AbstractTestFSWriterReader.CONFIG_ASYNC_WRITER_SHUTDOWNDELAY, "-1");

		// Give extending classes the chance to refine the configuration
		this.refineConfiguration(config, numRecordsWritten);

		return MonitoringController.createInstance(config);
	}

	protected abstract void refineConfiguration(Configuration config, final int numRecordsWritten);

	@Override
	protected void checkControllerState(final IMonitoringController monitoringController) {
		Assert.assertTrue("Expected monitoring controller to be enabled", monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventFromMonitoringLog);
	}
}
