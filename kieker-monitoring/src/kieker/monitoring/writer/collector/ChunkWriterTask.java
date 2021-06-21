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
package kieker.monitoring.writer.collector;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.raw.IRawDataWriter;
import kieker.monitoring.writer.serializer.IMonitoringRecordSerializer;

/**
 * Writer task to write records collected by the collector.
 * Used within {@link ChunkingCollector}.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 *
 */
class ChunkWriterTask implements Runnable {

	private final ByteBuffer buffer;

	private final IMonitoringRecordSerializer serializer;

	private final IRawDataWriter writer;

	private final int outputChunkSize;

	private final long deferredWriteDelayNs;

	private volatile long nextWriteTime;

	private final ChunkingCollector collector;

	/**
	 * Create a chunk writer task.
	 *
	 * @param collector
	 *            collector for chunks
	 * @param outputChunkSize
	 *            size of the chunks
	 * @param deferredWriteDelayMs
	 *            write delay??
	 * @param outputBufferSize
	 *            buffer size
	 * @param serializer
	 *            serializer
	 * @param writer
	 *            data writer
	 */
	public ChunkWriterTask(final ChunkingCollector collector, final int outputChunkSize, final int deferredWriteDelayMs, final int outputBufferSize,
			final IMonitoringRecordSerializer serializer,
			final IRawDataWriter writer) {
		this.collector = collector;
		this.serializer = serializer;
		this.writer = writer;
		this.outputChunkSize = outputChunkSize;
		this.deferredWriteDelayNs = deferredWriteDelayMs * 1000000L;
		this.buffer = ByteBuffer.allocate(outputBufferSize);

		this.updateNextWriteTime();
	}

	@Override
	public void run() {
		final Queue<IMonitoringRecord> queue = this.collector.getRecordQueue();
		int numberOfPendingRecords = queue.size();
		final int chunkSize = this.outputChunkSize;

		// Write records if at least one chunk can be filled completely
		if (numberOfPendingRecords >= chunkSize) {
			// Write as many chunks as possible
			do {
				this.writeChunk(queue, chunkSize);
				numberOfPendingRecords = queue.size();
			} while (numberOfPendingRecords >= chunkSize);

			// Update the last-write time and return
			this.updateNextWriteTime();
			return;
		}

		// If no chunk can be filled, check whether the deferred write interval has expired
		final long currentTime = System.nanoTime();

		if ((numberOfPendingRecords > 0) && (currentTime >= this.nextWriteTime)) {
			// Write the pending records
			this.writeChunk(queue, numberOfPendingRecords);
			this.updateNextWriteTime(currentTime);
		}
	}

	public void initialize() {
		this.writer.onInitialization();
		this.serializer.onInitialization();
	}

	public void terminate() {
		this.flush();

		this.serializer.onTermination();
		this.writer.onTermination();
	}

	public void flush() {
		final Queue<IMonitoringRecord> queue = this.collector.getRecordQueue();
		final int chunkSize = this.outputChunkSize;
		int numberOfPendingRecords = queue.size();

		// Put the remaining records into chunks and write them
		while (numberOfPendingRecords > 0) {
			final int currentChunkSize;

			if (numberOfPendingRecords > chunkSize) {
				currentChunkSize = chunkSize;
			} else {
				currentChunkSize = numberOfPendingRecords;
			}

			this.writeChunk(queue, currentChunkSize);
			numberOfPendingRecords -= currentChunkSize;
		}
	}

	private void writeChunk(final Queue<IMonitoringRecord> queue, final int chunkSize) {
		final List<IMonitoringRecord> chunk = new ArrayList<>(chunkSize);

		for (int recordIndex = 0; recordIndex < chunkSize; recordIndex++) {
			// Due to checks at the call sites, writeChunk is only called with a chunk size
			// not smaller than the queue's length to avoid poll() returning null values.
			final IMonitoringRecord record = queue.poll();
			chunk.add(record);
		}

		// Serialize and write the data
		final ByteBuffer outputBuffer = this.buffer;
		outputBuffer.rewind();
		final int bytesWritten = this.serializer.serializeRecords(chunk, outputBuffer);
		this.writer.writeData(outputBuffer, 0, bytesWritten);
	}

	private void updateNextWriteTime() {
		this.updateNextWriteTime(System.nanoTime());
	}

	private void updateNextWriteTime(final long currentTime) {
		this.nextWriteTime = (currentTime + this.deferredWriteDelayNs);
	}

}
