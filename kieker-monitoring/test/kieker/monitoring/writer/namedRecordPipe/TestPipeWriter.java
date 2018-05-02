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

package kieker.monitoring.writer.namedRecordPipe;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.NamedPipeFactory;

/**
 * @author Andre van Hoorn
 *
 * @since 1.3
 */
public class TestPipeWriter extends AbstractKiekerTest { // NOCS

	public TestPipeWriter() {
		super();
	}

	/**
	 * Tests whether the {@link kieker.monitoring.writer.namedRecordPipe.PipeWriter} correctly passes received {@link IMonitoringRecord}s to the
	 * {@link kieker.common.namedRecordPipe.Broker} (which then passes these
	 * to an {@link kieker.common.namedRecordPipe.IPipeReader}).
	 *
	 * @throws InterruptedException
	 */
	@Test
	public void testNamedPipeWriterPassesRecordsToPipe() throws InterruptedException {
		final String pipeName = NamedPipeFactory.createPipeName();
		final MonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kieker.
		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		// Send 7 dummy records
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			monitoringController.newMonitoringRecord(new EmptyRecord());
		}

		monitoringController.terminateMonitoring();
		monitoringController.waitForTermination(5000);

		// Make sure that numRecordsToSend where written.
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, receivedRecords.size());
	}

}
