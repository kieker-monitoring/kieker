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

package kieker.monitoring.core.controller;

import java.lang.Thread.State;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.queue.BlockingQueueDecorator;
import kieker.monitoring.queue.behavior.BlockOnFailedInsertBehavior;
import kieker.monitoring.queue.behavior.BypassQueueBehavior;
import kieker.monitoring.queue.behavior.CountOnFailedInsertBehavior;
import kieker.monitoring.queue.behavior.DoNotInsertBehavior;
import kieker.monitoring.queue.behavior.InsertBehavior;
import kieker.monitoring.queue.behavior.TerminateOnFailedInsertBehavior;
import kieker.monitoring.queue.putstrategy.PutStrategy;
import kieker.monitoring.queue.takestrategy.TakeStrategy;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.MonitoringWriterThread;

/**
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller, Robert von Massow
 *
 * @since 1.3
 */
public final class WriterController extends AbstractController implements IWriterController {

	public static final String PREFIX = WriterController.class.getName() + ".";
	/**
	 * The name of the configuration determining the size of the queue of this
	 * writer.
	 */
	public static final String RECORD_QUEUE_SIZE = "RecordQueueSize";
	/**
	 * The name of the configuration determining the insert behavior to the queue of
	 * the writer.
	 */
	public static final String RECORD_QUEUE_INSERT_BEHAVIOR = "RecordQueueInsertBehavior";
	/** The fully qualified name of the queue to be used for the records. */
	public static final String RECORD_QUEUE_FQN = "RecordQueueFQN";

	/** The fully qualified name of the put strategy */
	public static final String QUEUE_PUT_STRATEGY = "QueuePutStrategy";

	/** The fully qualified name of the take strategy */
	public static final String QUEUE_TAKE_STRATEGY = "QueueTakeStrategy";

	private static final Logger LOGGER = LoggerFactory.getLogger(WriterController.class);
	/** Monitoring Writer. */
	private AbstractMonitoringWriter monitoringWriter; // NOPMD (so far, cannot be made final due to the
														// MonitoringController)
	/** Whether or not to automatically log the metadata record. */
	private final boolean logMetadataRecord;
	/** the capacity of the queue. */
	private final int queueCapacity;
	/**
	 * the synchronized, blocking queue used for the communication between the
	 * monitored application's threads and the writer thread.
	 */
	private final BlockingQueue<IMonitoringRecord> writerQueue;

	private MonitoringWriterThread monitoringWriterThread; // NOPMD (so far, cannot be made final due to the
															// MonitoringController)

	private InsertBehavior<IMonitoringRecord> insertBehavior; // NOPMD (so far, cannot be made final due to the
																// MonitoringController)

	// private Disruptor<IMonitoringRecordEvent> disruptor;

	// private RingBuffer<IMonitoringRecordEvent> ringBuffer;

	// private int numInsertedRecords;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for the controller.
	 */
	public WriterController(final Configuration configuration) {
		super(configuration);
		this.logMetadataRecord = configuration.getBooleanProperty(ConfigurationConstants.META_DATA);

		this.queueCapacity = configuration.getIntProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_SIZE);
		final String queueFqn = configuration
				.getStringProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_FQN);

		final Queue<IMonitoringRecord> queue = this.newQueue(queueFqn, this.queueCapacity);
		if (queue instanceof BlockingQueue) {
			this.writerQueue = (BlockingQueue<IMonitoringRecord>) queue;
		} else {
			final String takeStrategyFqn = configuration.getStringProperty(PREFIX + QUEUE_PUT_STRATEGY,
					"kieker.monitoring.queue.takestrategy.SCBlockingTakeStrategy");
			final TakeStrategy takeStrategy = newTakeStrategy(takeStrategyFqn);
			final String putStrategyFqn = configuration.getStringProperty(PREFIX + QUEUE_TAKE_STRATEGY, "kieker.monitoring.queue.putstrategy.SPBlockingPutStrategy");
			final PutStrategy putStrategy = newPutStrategy(putStrategyFqn);
			this.writerQueue = new BlockingQueueDecorator<>(queue, putStrategy, takeStrategy);
		}

		final String writerClassName = configuration.getStringProperty(ConfigurationConstants.WRITER_CLASSNAME);
		this.monitoringWriter = AbstractController.createAndInitialize(AbstractMonitoringWriter.class, writerClassName,
				configuration);
		if (this.monitoringWriter == null) {
			this.terminate();
			return; // TODO should throw an exception! and then monitoringWriter can be declared
					// final
			// throw new IllegalStateException("monitoringWriter may not be null");
		}

		this.monitoringWriterThread = new MonitoringWriterThread(this.monitoringWriter, this.writerQueue);

		int recordQueueInsertBehavior = configuration
				.getIntProperty(WriterController.PREFIX + WriterController.RECORD_QUEUE_INSERT_BEHAVIOR);
		if ((recordQueueInsertBehavior < 0) || (recordQueueInsertBehavior > 5)) {
			WriterController.LOGGER.warn("Unknown value '{}' for {}{}; using default value 0",
					recordQueueInsertBehavior, WriterController.PREFIX, WriterController.RECORD_QUEUE_INSERT_BEHAVIOR);
			recordQueueInsertBehavior = 0;
		}

		switch (recordQueueInsertBehavior) {
		case 1:
			this.insertBehavior = new BlockOnFailedInsertBehavior<>(this.writerQueue);
			break;
		case 2:
			this.insertBehavior = new CountOnFailedInsertBehavior<>(this.writerQueue);
			break;
		case 3:
			this.insertBehavior = new DoNotInsertBehavior<>();
			break;
		case 4:
			// try {
			// this.initDisruptor(configuration);
			// } catch (final IOException e) {
			// throw new IllegalStateException(e);
			// }
			// this.insertBehavior = new
			// DisruptorInsertBehavior<IMonitoringRecord>(this.ringBuffer);
			break;
		case 5:
			this.insertBehavior = new BypassQueueBehavior(this.monitoringWriter);
			break;
		default:
			this.insertBehavior = new TerminateOnFailedInsertBehavior<>(this.writerQueue);
			break;
		}
	}

	// private void initDisruptor(final Configuration configuration) throws
	// IOException {
	// final EventFactory<IMonitoringRecordEvent> eventFactory = new
	// NewRecordEventFactory();
	// final int bufferSize = 1024;
	// final ThreadFactory threadFactory = new ThreadFactory() {
	// @Override
	// public Thread newThread(final Runnable r) {
	// final Thread thread = new Thread(r);
	// thread.setDaemon(true);
	// return thread;
	// }
	// };
	//
	// this.disruptor = new Disruptor<IMonitoringRecordEvent>(eventFactory,
	// bufferSize, threadFactory);
	//
	// final SharedConnection connection = new SharedConnection(configuration);
	// final EventHandler<? super IMonitoringRecordEvent> handler1 = new
	// DisruptorTcpWriter(configuration, connection);
	// // allowing more than one consumes breaks the assumptions that the records
	// are sent in order of occurrence
	// // final EventHandler<? super IMonitoringRecordEvent> handler2 = new
	// DisruptorTcpWriter(configuration,
	// connection);
	//
	// this.disruptor.handleEventsWith(handler1);
	// this.disruptor.start();
	//
	// this.ringBuffer = this.disruptor.getRingBuffer();
	// }

	private TakeStrategy newTakeStrategy(final String strategyName) {
		try {
			Class<?> strategyClass = Class.forName(strategyName);
			final Constructor<? extends TakeStrategy> constructor = (Constructor<? extends TakeStrategy>) strategyClass.getConstructor();
			return constructor.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			LOGGER.warn("An exception occurred", e);
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	private PutStrategy newPutStrategy(final String strategyName) {
		try {
			Class<?> strategyClass = Class.forName(strategyName);
			final Constructor<? extends PutStrategy> constructor = (Constructor<? extends PutStrategy>) strategyClass.getConstructor();
			return constructor.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			LOGGER.warn("An exception occurred", e);
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	/**
	 * @param queueFqn
	 *            the fully qualified queue name
	 * @param capacity
	 *            the (initial) capacity of the queue
	 * @return a new instance of the queue indicated by its <code>queueFQN</code>.
	 *         Such instance is created by invoking the constructor with a single
	 *         parameter of type <code>int</code>.
	 */
	@SuppressWarnings("unchecked")
	private Queue<IMonitoringRecord> newQueue(final String queueFqn, final int capacity) {
		try {
			final Class<?> clazz = Class.forName(queueFqn);
			@SuppressWarnings("rawtypes")
			final Class<? extends Queue> queueClass = clazz.asSubclass(Queue.class);
			@SuppressWarnings("rawtypes")
			final Constructor<? extends Queue> constructor = queueClass.getConstructor(int.class);
			return constructor.newInstance(capacity);
		} catch (final ClassNotFoundException | InstantiationException e) {
			WriterController.LOGGER.warn("An exception occurred", e);
			throw new IllegalStateException(e);
		} catch (final NoSuchMethodException | SecurityException e) {
			WriterController.LOGGER.warn("An exception occurred", e);
			throw new IllegalStateException(e);
		} catch (final IllegalAccessException | IllegalArgumentException e) {
			WriterController.LOGGER.warn("An exception occurred", e);
			throw new IllegalStateException(e);
		} catch (final InvocationTargetException e) {
			WriterController.LOGGER.warn("An exception occurred", e);
			throw new IllegalStateException(e);
		}
	}

	// default
	boolean isLogMetadataRecord() { // NOPMD (package-private)
		return this.logMetadataRecord;
	}

	@Override
	protected final void init() {
		WriterController.LOGGER.debug("Initializing Writer Controller");

		if (this.monitoringWriterThread != null) {
			this.monitoringWriterThread.start();
		}
	}

	@Override
	protected final void cleanup() {
		WriterController.LOGGER.debug("Shutting down Writer Controller");

		if (this.monitoringWriterThread != null) {
			this.monitoringWriterThread.terminate();
		}

		// if (this.disruptor != null) {
		// this.disruptor.shutdown();
		// }

		// LOG.info("block durations: {}" + this.insertBehavior.toString());
		// System.out.println("block durations: " + this.insertBehavior.toString());
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(256) // NOPMD (consecutive calls of append with string literals)
				.append("WriterController:").append("\n\tQueue type: ").append(this.writerQueue.getClass())
				.append("\n\tQueue capacity: ").append(this.queueCapacity)
				.append("\n\tInsert behavior (a.k.a. QueueFullBehavior): ").append(this.insertBehavior.toString())
				.append('\n');
		if (this.monitoringWriter != null) {
			sb.append(this.monitoringWriter.toString());
		} else {
			sb.append("\tNo Monitoring Writer available");
		}
		sb.append('\n');
		return sb.toString();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		final boolean recordSent = this.insertBehavior.insert(record);
		if (!recordSent) {
			WriterController.LOGGER.error("Error writing the monitoring data. Will terminate monitoring!");
			this.terminate();
		}

		return recordSent;
	}

	@Override
	public void waitForTermination(final long timeoutInMs) throws InterruptedException {
		if (this.monitoringWriterThread != null) {
			this.monitoringWriterThread.join(timeoutInMs);
		}
	}

	/**
	 * Used in tests only.
	 */
	// default
	State getStateOfMonitoringWriterThread() { // NOPMD (package-private)
		return this.monitoringWriterThread.getState();
	}
}
