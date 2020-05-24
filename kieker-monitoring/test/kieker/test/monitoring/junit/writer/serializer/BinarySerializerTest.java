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

package kieker.test.monitoring.junit.writer.serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.dataformat.VariableLengthEncoding;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.collector.ChunkingCollector;
import kieker.monitoring.writer.serializer.BinarySerializer;

import kieker.test.monitoring.junit.writer.collector.TestRawDataStorage; // NOCS
import kieker.test.monitoring.junit.writer.collector.TestRawDataWriter; // NOCS

/**
 * Tests for the default binary serializer.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class BinarySerializerTest {

	public BinarySerializerTest() {
		// Default Constructor
	}

	@Test
	@Ignore // NOCS
	public void testSingleRecord() throws IOException, InterruptedException {
		final String testId = "testSingleRecord";
		final int recordCount = 1;
		final int taskRunInterval = 10;
		final int deferredWriteDelay = 50;

		final IMonitoringController controller = this.createController(testId, taskRunInterval, deferredWriteDelay);

		for (int recordIndex = 0; recordIndex < recordCount; recordIndex++) {
			final OperationExecutionRecord record = new OperationExecutionRecord("op()", "SESS-ID", 0, recordIndex, recordIndex, "host", recordIndex, 1);
			controller.newMonitoringRecord(record);
		}

		// Terminate monitoring and wait for termination
		controller.terminateMonitoring();
		controller.waitForTermination(2000);

		// Retrieve written data from the data storage
		final byte[] data = TestRawDataStorage.getInstance().getData(testId);

		// Check some basic numbers, since full decoding is not yet supported
		final ByteBuffer dataBuffer = ByteBuffer.wrap(data);

		// Read the string table offset (last four bytes), plus 8 bytes for the container header
		dataBuffer.position(data.length - 4);
		final int stringTableOffset = dataBuffer.getInt() + 8;

		// Read the number of strings in the string table
		dataBuffer.position(stringTableOffset);
		final int numberOfStrings = VariableLengthEncoding.decodeInt(dataBuffer);

		// Check the size of the string table
		Assert.assertEquals(9, numberOfStrings);
	}

	@Test
	@Ignore // NOCS
	public void testMultipleRecords() throws InterruptedException {
		final String testId = "testMultipleRecords";
		final int recordCount = 15;
		final int taskRunInterval = 10;
		final int deferredWriteDelay = 50;

		final IMonitoringController controller = this.createController(testId, taskRunInterval, deferredWriteDelay);

		for (int recordIndex = 0; recordIndex < recordCount; recordIndex++) {
			final OperationExecutionRecord record = new OperationExecutionRecord("operation()", "SESS-ID", 0, recordIndex, recordIndex, "hostname", recordIndex, 1);
			controller.newMonitoringRecord(record);
		}

		// Terminate monitoring
		controller.terminateMonitoring();
		controller.waitForTermination(2000);

		// Retrieve written data from the data storage
		final byte[] data = TestRawDataStorage.getInstance().getData(testId);

		// Check some basic numbers, since full decoding is not yet supported
		final ByteBuffer dataBuffer = ByteBuffer.wrap(data);

		// Read the string table offset (last four bytes), plus 8 bytes for the container header
		dataBuffer.position(data.length - 4);
		final int stringTableOffset = dataBuffer.getInt() + 8;

		// Read the number of strings in the string table
		dataBuffer.position(stringTableOffset);
		final int numberOfStrings = VariableLengthEncoding.decodeInt(dataBuffer);

		// Check the size of the string table
		Assert.assertEquals(9, numberOfStrings);
	}

	private IMonitoringController createController(final String testId, final int taskRunInterval, final int deferredWriteDelay) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		configuration.setProperty(ConfigurationKeys.WRITER_CLASSNAME, ChunkingCollector.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_SERIALIZER_CLASSNAME, BinarySerializer.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_WRITER_CLASSNAME, TestRawDataWriter.class.getName());
		configuration.setProperty(ChunkingCollector.CONFIG_TASK_RUN_INTERVAL, taskRunInterval);
		configuration.setProperty(ChunkingCollector.CONFIG_DEFERRED_WRITE_DELAY, deferredWriteDelay);
		configuration.setProperty(TestRawDataWriter.CONFIG_TEST_ID, testId);

		return MonitoringController.createInstance(configuration);
	}

}
