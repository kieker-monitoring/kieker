/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.thread.DaemonThreadFactory;
import kieker.monitoring.core.controller.ControllerFactory;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.raw.IRawDataWriter;
import kieker.monitoring.writer.serializer.IMonitoringRecordSerializer;

/**
 * Chunking collector for monitoring records. The collected records are written if a chunk is
 * "full", or if no records have been written for some time (see 'deferred write delay'). This
 * collector employs a writer task, which runs regularly and writes chunks if enough records have
 * been collected or the deferred write delay has expired.
 * <p/>
 * <b>Configuration hints:</b> The collector has several configuration parameters which depend
 * on one another. In particular, the queue size should be chosen large enough so that the queue
 * does not fill up in a single task run interval. In addition, the output buffer needs to be
 * large enough to hold a completely serialized chunk, and therefore depends on the chunk size.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
@ReceiveUnfilteredConfiguration
public class ChunkingCollector extends AbstractMonitoringWriter {

	// If multiple workers are required, synchronization between writer tasks has
	// to be considered.
	private static final int NUMBER_OF_WORKERS = 1;

	// Default size for the input queue (in records)
	private static final int DEFAULT_QUEUE_SIZE = 16384;

	// Default deferred write delay (in milliseconds)
	private static final int DEFAULT_DEFERRED_WRITE_DELAY = 500;

	// Default chunk size (in records)
	private static final int DEFAULT_CHUNK_SIZE = 16;

	// Default output buffer size (in bytes)
	private static final int DEFAULT_OUTPUT_BUFFER_SIZE = 65536;

	// Default run task run interval (in milliseconds)
	private static final int DEFAULT_TASK_RUN_INTERVAL = 20;

	private static final String PREFIX = ChunkingCollector.class.getName() + ".";

	/** The name of the configuration property for the serializer class name. */
	public static final String CONFIG_SERIALIZER_CLASSNAME = PREFIX + "serializer"; // NOCS (afterPREFIX)

	/** The name of the configuration property for the writer class name. */
	public static final String CONFIG_WRITER_CLASSNAME = PREFIX + "writer"; // NOCS (afterPREFIX)

	/** The name of the configuration property for the deferred write delay. */
	public static final String CONFIG_DEFERRED_WRITE_DELAY = PREFIX + "deferredWriteDelay"; // NOCS (afterPREFIX)

	/** The name of the configuration property for the queue size. */
	public static final String CONFIG_QUEUE_SIZE = PREFIX + "queueSize"; // NOCS (afterPREFIX)

	/** The name of the configuration property for the chunk size. */
	public static final String CONFIG_CHUNK_SIZE = PREFIX + "chunkSize"; // NOCS (afterPREFIX)

	/** The name of the configuration property for the output buffer size. */
	public static final String CONFIG_OUTPUT_BUFFER_SIZE = PREFIX + "outputBufferSize"; // NOCS (afterPREFIX)

	/** The name of the configuration property for the writer task interval. */
	public static final String CONFIG_TASK_RUN_INTERVAL = PREFIX + "taskRunInterval"; // NOCS (afterPREFIX)
	
	/** The type of queue to use. */
	public static final String CONFIG_QUEUE_TYPE = PREFIX + "queueType"; // NOCS (afterPREFIX)

	/** The time unit for the writer task interval. */
	private static final TimeUnit TASK_RUN_INTERVAL_TIME_UNIT = TimeUnit.MILLISECONDS;
	
	private static final Log LOG = LogFactory.getLog(ChunkingCollector.class);

	private final Queue<IMonitoringRecord> recordQueue;

	private final ScheduledExecutorService scheduledExecutor;
	private final int taskRunInterval;
	private final ChunkWriterTask writerTask;

	public ChunkingCollector(final Configuration configuration) {
		super(configuration);

		// Initialize the queue and the executor service
		final int queueSize = configuration.getIntProperty(CONFIG_QUEUE_SIZE, DEFAULT_QUEUE_SIZE);
		final String queueType = configuration.getStringProperty(CONFIG_QUEUE_TYPE, "");
		this.taskRunInterval = configuration.getIntProperty(CONFIG_TASK_RUN_INTERVAL, DEFAULT_TASK_RUN_INTERVAL);

		this.recordQueue = this.createQueue(queueType, queueSize);
		this.scheduledExecutor = Executors.newScheduledThreadPool(NUMBER_OF_WORKERS, new DaemonThreadFactory());

		// Instantiate serializer and writer
		final ControllerFactory controllerFactory = ControllerFactory.getInstance(configuration);
		final String serializerName = configuration.getStringProperty(CONFIG_SERIALIZER_CLASSNAME);
		final IMonitoringRecordSerializer serializer = controllerFactory.createAndInitialize(IMonitoringRecordSerializer.class, serializerName, configuration);
		final String writerName = configuration.getStringProperty(CONFIG_WRITER_CLASSNAME);
		final IRawDataWriter writer = controllerFactory.createAndInitialize(IRawDataWriter.class, writerName, configuration);
				
		// Instantiate the writer task
		final int deferredWriteDelayMs = configuration.getIntProperty(CONFIG_DEFERRED_WRITE_DELAY, DEFAULT_DEFERRED_WRITE_DELAY);
		final int chunkSize = configuration.getIntProperty(CONFIG_CHUNK_SIZE, DEFAULT_CHUNK_SIZE);
		final int outputBufferSize = configuration.getIntProperty(CONFIG_OUTPUT_BUFFER_SIZE, DEFAULT_OUTPUT_BUFFER_SIZE);

		this.writerTask = new ChunkWriterTask(chunkSize, deferredWriteDelayMs, outputBufferSize, serializer, writer);
	}
	
	@SuppressWarnings("unchecked")
	private Queue<IMonitoringRecord> createQueue(final String queueTypeName, final int queueSize) {
		if (queueTypeName == null) {
			return this.createDefaultQueue(queueSize);
		}
		
		try {
			// Instantiate the queue of the given type. We assume that the queue has a constructor that takes the size as its only parameter.
			final Class<?> queueClass = Class.forName(queueTypeName);
			final Constructor<?> constructor = queueClass.getConstructor(int.class);
			return (Queue<IMonitoringRecord>) constructor.newInstance(queueSize);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// Instantiate default queue type if the desired queue type cannot be instantiated
			LOG.error("Error instantiating queue type " + queueTypeName + ". Using default queue type instead.", e);
			return this.createDefaultQueue(queueSize);
		}
	}
	
	private Queue<IMonitoringRecord> createDefaultQueue(final int queueSize) {
		return new ArrayBlockingQueue<>(queueSize);
	}

	@Override
	public void onStarting() {
		this.scheduledExecutor.scheduleAtFixedRate(this.writerTask, 0, this.taskRunInterval, TASK_RUN_INTERVAL_TIME_UNIT);		
		this.writerTask.initialize();
	}

	@Override
	public void onTerminating() {
		// Terminate scheduled execution and write remaining chunks, if any
		this.scheduledExecutor.shutdown();
		
		try {
			// Wait for the executor to shut down
			this.scheduledExecutor.awaitTermination(Long.MAX_VALUE, TASK_RUN_INTERVAL_TIME_UNIT);
		} catch (final InterruptedException e) {
			LOG.warn("Awaiting termination of the scheduled executor was interrupted.", e);
		}
		
		this.writerTask.terminate();
	}

	private boolean enqueueRecord(final IMonitoringRecord record) {
		for (int tryNumber = 0; tryNumber < 10; tryNumber++) { // try up to 10 times to enqueue a record
			final boolean recordEnqueued = this.recordQueue.offer(record);
			
			if (recordEnqueued) {
				return true;
			}
		}
		
		LOG.error("Failed to add new monitoring record to queue (maximum number of attempts reached).");
		return false;
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		this.enqueueRecord(record);
	}

	/**
	 * Writer task to write records collected by the collector.
	 * @author Holger Knoche
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

		public ChunkWriterTask(final int outputChunkSize, final int deferredWriteDelayMs, final int outputBufferSize, final IMonitoringRecordSerializer serializer,
				final IRawDataWriter writer) {
			this.serializer = serializer;
			this.writer = writer;
			this.outputChunkSize = outputChunkSize;
			this.deferredWriteDelayNs = deferredWriteDelayMs * 1000000L;
			this.buffer = ByteBuffer.allocate(outputBufferSize);

			this.updateNextWriteTime();
		}

		@Override
		@SuppressWarnings("synthetic-access")
		public void run() {
			final Queue<IMonitoringRecord> queue = ChunkingCollector.this.recordQueue;
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

		@SuppressWarnings("synthetic-access")
		public void flush() {
			final Queue<IMonitoringRecord> queue = ChunkingCollector.this.recordQueue;
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
			final List<IMonitoringRecord> chunk = new ArrayList<IMonitoringRecord>(chunkSize);

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

}
