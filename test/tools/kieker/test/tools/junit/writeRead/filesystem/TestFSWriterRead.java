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

import junit.framework.Assert;
import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestFSWriterRead extends AbstractFSTest {
	private static final Class<? extends IMonitoringWriter> testedWriterClazz = AsyncFsWriter.class;

	// TODO: constant is private in AbstractAsyncWriter ... why?
	private static final String CONFIG_ASYNC_WRITER_QUEUE_SIZE = "QueueSize";

	@Override
	protected IMonitoringController createController(final int numRecordsWritten, final File monitoringLogBaseDir) throws IOException {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();

		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, TestFSWriterRead.testedWriterClazz.getName());
		config.setProperty(TestFSWriterRead.testedWriterClazz.getName() + "." + AbstractAsyncFSWriter.CONFIG_TEMP, Boolean.FALSE.toString());
		config.setProperty(TestFSWriterRead.testedWriterClazz.getName() + "." + AbstractAsyncFSWriter.CONFIG_PATH, monitoringLogBaseDir.getCanonicalPath());
		config.setProperty(TestFSWriterRead.testedWriterClazz.getName() + "." + TestFSWriterRead.CONFIG_ASYNC_WRITER_QUEUE_SIZE, Integer.toString(numRecordsWritten));

		// TODO: explicitly set remaining values

		final IMonitoringController ctrl = MonitoringController.createInstance(config);
		return ctrl;
	}

	@Override
	protected void checkControllerState(final IMonitoringController monitoringController) {
		// TODO: do we have to wait for some time?

		Assert.assertTrue("Expected monitoring controller to be enabled", monitoringController.isMonitoringEnabled());
	}
}
