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

package kieker.test.monitoring.junit.writer.collector;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.collector.ChunkingCollector;
import kieker.monitoring.writer.serializer.StringSerializer;

/**
 * Basic tests for the chunking record collector.
 *
 * @author holger
 *
 * @since 1.13
 */
public final class TestChunkingCollector {

	private static final Charset CHARSET = Charset.forName("UTF-8");

	public TestChunkingCollector() {
		// Default Constructor
	}

	/**
	 * Tests both writing of chunks due to size (a full chunk can be written) and to time (records remain unwritten for
	 * some time).
	 *
	 * @throws IOException
	 *             Not expected
	 * @throws InterruptedException
	 */
	@Test
	public void testChunkingSizeAndTime() throws IOException, InterruptedException {
		final String testId = "testChunkingSizeAndTime";
		final int recordCount = 25;
		final int deferredWriteDelayInMs = 500;

		final IMonitoringController controller = this.createController(testId, deferredWriteDelayInMs);

		for (int recordIndex = 0; recordIndex < recordCount; recordIndex++) {
			final OperationExecutionRecord record = new OperationExecutionRecord("op()", "SESS-ID", 0, recordIndex,
					recordIndex, "host", recordIndex, 1);
			controller.newMonitoringRecord(record);
		}

		// Wait until a timed write should have occurred
		Thread.sleep(2 * deferredWriteDelayInMs);
		controller.terminateMonitoring();
		controller.waitForTermination(deferredWriteDelayInMs * 1000000L);

		// Retrieve written data from the data storage
		final byte[] data = TestRawDataStorage.getInstance().getData(testId);
		final List<String> lines = TestChunkingCollector.linesFromData(data);

		// Count the number of lines (= records) written
		final int numberOfLines = lines.size();

		// +1 due to one more line for the metadata record
		Assert.assertEquals(recordCount + 1, numberOfLines);
	}

	private static List<String> linesFromData(final byte[] data) throws IOException {
		final List<String> lines = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(data), CHARSET))) {
			while (true) {
				final String currentLine = reader.readLine();

				if (currentLine == null) {
					break;
				}

				lines.add(currentLine);
			}
		}

		return lines;
	}

	private IMonitoringController createController(final String testId, final int deferredWriteDelay) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		configuration.setProperty(ConfigurationConstants.WRITER_CLASSNAME, ChunkingCollector.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_SERIALIZER_CLASSNAME, StringSerializer.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_WRITER_CLASSNAME, TestRawDataWriter.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_DEFERRED_WRITE_DELAY, deferredWriteDelay);
		configuration.setProperty(TestRawDataWriter.CONFIG_TEST_ID, testId);

		return MonitoringController.createInstance(configuration);
	}

}
