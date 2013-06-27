/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.bridge.connector.tcp;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;

/**
 * TCP server connector supporting multiple clients.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class TCPMultiServerConnector extends AbstractTCPConnector {

	private static final int QUEUE_CAPACITY = 10;

	private static final int NUMBER_OF_THREADS = 10;

	private static final long SHUTDOWN_TIMEOUT = 20L;

	private final int port;
	private BlockingQueue<IMonitoringRecord> recordQueue;
	private ExecutorService executor;

	/**
	 * Construct new TCPMultiServerService.
	 * 
	 * @param recordMap
	 *            IMonitoringRecord to id map
	 * @param port
	 *            TCP port the service listens to
	 */
	public TCPMultiServerConnector(final ConcurrentMap<Integer, Class<IMonitoringRecord>> recordMap, final int port) {
		super(recordMap);
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.tcp.AbstractTCPConnector#initialize()
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		super.initialize();
		// do not move, in future these properties will be handled by the kieker configuration
		this.recordQueue = new ArrayBlockingQueue<IMonitoringRecord>(QUEUE_CAPACITY);
		this.executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

		// The port listener must run in its own thread to accept new connections
		try {
			this.executor.execute(new TCPMultiServerPortListenerRunnable(this.port, this.recordQueue,
					this.lookupEntityMap, this.executor));
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException("Failed to open server socket", e);
		}
	}

	/**
	 * Stop all service threads to handle TCP communication and empty the record queue.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if the thread shutdown is interrupted or fails, the graceful wait to empty the queue
	 *             fails or the queue is not emptied after a waiting period
	 */
	public void close() throws ConnectorDataTransmissionException {
		for (final Runnable runnable : this.executor.shutdownNow()) {
			if (runnable instanceof TCPMultiServerPortListenerRunnable) {
				((TCPMultiServerPortListenerRunnable) runnable).setActive(false);
			} else if (runnable instanceof TCPMultiServerConnectionRunnable) {
				((TCPMultiServerConnectionRunnable) runnable).setActive(false);
			}
		}
		try {
			this.executor.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			throw new ConnectorDataTransmissionException("Server shutdown failed.", e);
		}
		// check if the record queue is empty. If be graceful
		if (!this.recordQueue.isEmpty()) {
			try {
				this.wait(SHUTDOWN_TIMEOUT);
			} catch (final InterruptedException e) {
				throw new ConnectorDataTransmissionException("Interrupted while waitig for queue cleanup.", e);
			}
			if (!this.recordQueue.isEmpty()) {
				throw new ConnectorDataTransmissionException("Failed to store all received records.");
			}
		}
	}

	/**
	 * Implements the deserializeNextRecord interface. Fetches deserialized data from the recordQeue
	 * which is filled by the connection listener threads.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if the record reading is interrupted
	 * @throws ConnectorEndOfDataException
	 *             if end of all data streams are reached
	 */
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			return this.recordQueue.take();
		} catch (final InterruptedException e) {
			if (this.recordQueue.isEmpty()) {
				throw new ConnectorEndOfDataException("End of all streams reached");
			} else {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			}
		}
	}

}
