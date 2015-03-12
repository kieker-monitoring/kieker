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

package kieker.test.monitoring.junit.core.controller;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.IMonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedPipeFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public class TestMonitoringControllerRecordsPassedInMonitoringStates extends AbstractKiekerTest { // NOCS

	/**
	 * Test if records are passed to the writer when monitoring is enabled.
	 */
	@Test
	public void testRecordsPassedToWriterWhenEnabled() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kieker.

		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		Assert.assertTrue("Failed to enable monitoring", monitoringController.enableMonitoring());
		monitoringController.newMonitoringRecord(new EmptyRecord());
		Assert.assertEquals("Unexpected number of records received", 1, receivedRecords.size());
		monitoringController.terminateMonitoring();
	}

	/**
	 * Test if records are not passed to the writer when monitoring is disabled.
	 */
	@Test
	public void testNoRecordsPassedToWriterWhenDisabled() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController =
				NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kieker.
		final List<IMonitoringRecord> receivedRecords =
				NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		Assert.assertTrue("Failed to disable monitoring", monitoringController.disableMonitoring());
		monitoringController.newMonitoringRecord(new EmptyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, receivedRecords.size());
		monitoringController.terminateMonitoring();
	}

	/**
	 * Test if records are not passed to the writer when monitoring is terminated.
	 */
	@Test
	public void testNoRecordsPassedToWriterWhenTerminated() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController =
				NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kieker.
		final List<IMonitoringRecord> receivedRecords =
				NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		monitoringController.terminateMonitoring();
		monitoringController.newMonitoringRecord(new EmptyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, receivedRecords.size());
	}
}
