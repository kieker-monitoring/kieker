/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.AbstractConnector;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.ConnectorProperty;

/**
 * TCP server connector supporting multiple clients.
 *
 * @author Reiner Jung
 *
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
@ConnectorProperty(cmdName = "tcp-server", name = "TCP Multi Server Connector", description = "TCP server for binary Kieker records. Accepts multiple connections.")
public class TCPMultiServerConnector extends AbstractConnector {

	/** Constant holds name for the port property . */
	public static final String PORT = TCPMultiServerConnector.class.getCanonicalName() + ".port";

	private static final int QUEUE_CAPACITY = 10;

	private static final int NUMBER_OF_THREADS = 10;

	private static final long SHUTDOWN_TIMEOUT = 5L;

	private final int port;

	private volatile BlockingQueue<IMonitoringRecord> recordQueue;

	/** executor pool for connection handling threads. Is only accessed in this thread. */
	private ExecutorService executor;

	/**
	 * Create a TCPMultiServerConnector.
	 *
	 * @param configuration
	 *            Kieker configuration including setup for connectors
	 *
	 * @param lookupEntityMap
	 *            IMonitoringRecord constructor and TYPES-array to id map
	 */
	public TCPMultiServerConnector(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		super(configuration, lookupEntityMap);
		this.port = this.configuration.getIntProperty(TCPMultiServerConnector.PORT);
	}

	/**
	 * Initializes internal queues and an executor pool for communication.
	 *
	 * @see kieker.tools.bridge.connector.AbstractConnector#initialize()
	 *
	 * @throws ConnectorDataTransmissionException
	 *             when the server socket cannot be acquired
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		// do not move, in future these properties will be handled by the kieker configuration
		this.recordQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
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
	@Override
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
		// check if the record queue is empty. If not be graceful
		if (!this.recordQueue.isEmpty()) {
			try {
				int retries = 0;
				while ((retries < 5) && !this.recordQueue.isEmpty()) {
					this.wait(SHUTDOWN_TIMEOUT);
					retries++;
				}
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
	 * @return one IMonitoringRecord per call
	 */
	@Override
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			return this.recordQueue.take();
		} catch (final InterruptedException e) {
			if (this.recordQueue.isEmpty()) {
				throw new ConnectorEndOfDataException("End of all streams reached", e);
			} else {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			}
		}
	}

}
