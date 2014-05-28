/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.monitoring.junit.writer.namedRecordPipe;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kicker.common.record.IMonitoringRecord;
import kicker.common.record.misc.EmptyRecord;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.test.common.junit.AbstractKickerTest;
import kicker.test.monitoring.util.NamedPipeFactory;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class TestPipeWriter extends AbstractKickerTest { // NOCS

	/**
	 * Tests whether the {@link kicker.monitoring.writer.namedRecordPipe.PipeWriter} correctly passes received {@link IMonitoringRecord}s to the
	 * {@link kicker.common.namedRecordPipe.Broker} (which then passes these
	 * to an {@link kicker.common.namedRecordPipe.IPipeReader}).
	 */
	@Test
	public void testNamedPipeWriterPassesRecordsToPipe() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final IMonitoringController monitoringController = NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName);

		// We will now register a custom IPipeReader which receives records through the pipe and collects these in a list. On purpose, we are not using the
		// corresponding PipeReader that comes with Kicker.
		final List<IMonitoringRecord> receivedRecords = NamedPipeFactory.createAndRegisterNamedPipeRecordCollector(pipeName);

		// Send 7 dummy records
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			monitoringController.newMonitoringRecord(new EmptyRecord());
		}

		// Make sure that numRecordsToSend where written.
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, receivedRecords.size());
	}

}
