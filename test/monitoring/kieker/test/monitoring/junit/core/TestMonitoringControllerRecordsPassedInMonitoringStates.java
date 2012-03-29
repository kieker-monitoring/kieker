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

package kieker.test.monitoring.junit.core;

import java.util.List;

import junit.framework.Assert;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.test.monitoring.junit.util.NamedPipeFactory;

import org.junit.Test;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestMonitoringControllerRecordsPassedInMonitoringStates { // NOCS

	@Test
	public void testRecordsPassedToWriterWhenEnabled() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		Assert.assertTrue("Failed to enable monitoring", monitoringController.enableMonitoring());
		monitoringController.newMonitoringRecord(new EmptyRecord());
		Assert.assertEquals("Unexpected number of records received", 1, receivedRecords.size());
		monitoringController.terminateMonitoring();
	}

	@Test
	public void testNoRecordsPassedToWriterWhenDisabled() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		Assert.assertTrue("Failed to disable monitoring", monitoringController.disableMonitoring());
		monitoringController.newMonitoringRecord(new EmptyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, receivedRecords.size());
		monitoringController.terminateMonitoring();
	}

	@Test
	public void testNoRecordsPassedToWriterWhenTerminated() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		/*
		 * We will now register a custom IPipeReader which receives records
		 * through the pipe and collects these in a list. On purpose, we are not
		 * using the corresponding PipeReader that comes with Kieker.
		 */
		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		monitoringController.terminateMonitoring();
		monitoringController.newMonitoringRecord(new EmptyRecord());
		Assert.assertEquals("Unexpected number of records received", 0, receivedRecords.size());
	}
}
