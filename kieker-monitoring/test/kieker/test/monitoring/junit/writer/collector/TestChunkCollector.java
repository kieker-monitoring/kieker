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

package kieker.test.monitoring.junit.writer.collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

import org.apache.activemq.util.ByteArrayInputStream;
import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.collector.ChunkingCollector;
import kieker.monitoring.writer.serializer.StringSerializer;

/**
 * @author holger
 *
 * @since 1.13
 */
public class TestChunkCollector {

	/**
	 * Tests both writing of chunks due to size (a full chunk can be written) and to time (records remain unwritten for some time).
	 *
	 * @throws IOException
	 *             Not expected
	 */
	@Test
	public void testChunkingSizeAndTime() throws IOException {
		final String testId = "testChunkingSizeAndTime";
		final int recordCount = 25;
		final int deferredWriteDelay = 500;

		final IMonitoringController controller = this.createController(testId, deferredWriteDelay);

		for (int recordIndex = 0; recordIndex < recordCount; recordIndex++) {
			final OperationExecutionRecord record = new OperationExecutionRecord("op()", "SESS-ID", 0, recordIndex, recordIndex, "host", recordIndex, 1);
			controller.newMonitoringRecord(record);
		}

		// Wait until a timed write should have occurred
		LockSupport.parkNanos(2 * deferredWriteDelay * 1000000L);
		controller.terminateMonitoring();

		// Retrieve written data from the data storage
		final byte[] data = TestRawDataStorage.getInstance().getData(testId);
		final List<String> lines = TestChunkCollector.linesFromData(data);

		// Count the number of lines (= records) written
		final int numberOfLines = lines.size();

		// One more line due to the metadata record
		Assert.assertEquals(recordCount + 1, numberOfLines);
	}

	private static List<String> linesFromData(final byte[] data) throws IOException {
		final List<String> lines = new ArrayList<String>();

		final BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
		try {
			while (true) {
				final String currentLine = reader.readLine();

				if (currentLine == null) {
					break;
				}

				lines.add(currentLine);
			}
		} finally {
			reader.close();
		}

		return lines;
	}

	private IMonitoringController createController(final String testId, final int deferredWriteDelay) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, ChunkingCollector.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_SERIALIZER_CLASSNAME, StringSerializer.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_WRITER_CLASSNAME, TestRawDataWriter.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_DEFERRED_WRITE_DELAY, deferredWriteDelay);
		configuration.setProperty(TestRawDataWriter.CONFIG_TEST_ID, testId);

		return MonitoringController.createInstance(configuration);
	}

}
