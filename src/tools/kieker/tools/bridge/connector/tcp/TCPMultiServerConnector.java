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
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

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

	private final int port;
	private BlockingQueue<IMonitoringRecord> recordQueue;

	private ServerSocket serverSocket;

	private boolean active;

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
		this.active = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.tcp.AbstractTCPConnector#initialize()
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		super.initialize();
		this.recordQueue = new ArrayBlockingQueue<IMonitoringRecord>(QUEUE_CAPACITY);
		try {
			this.serverSocket = new ServerSocket(this.port);
			final Runnable server = new Runnable() {

				public void run() {
					// accept client connections
					try {
						while (TCPMultiServerConnector.this.isActive()) {
							// TODO is this broke or does this work and why? It seams to be ugly!!
							new TCPMultiServerConnectionThread(TCPMultiServerConnector.this.serverSocket.accept(),
									TCPMultiServerConnector.this,
									TCPMultiServerConnector.this.lookupEntityMap,
									TCPMultiServerConnector.this.recordQueue);
						}
					} catch (final IOException e) {
						TCPMultiServerConnector.this.setActive(false);
					}
				}
			};
			server.run();
		} catch (final IOException e1) {
			throw new ConnectorDataTransmissionException("Cannot open server socket at port " + this.port, e1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.IServiceConnector#close()
	 */
	public void close() throws ConnectorDataTransmissionException {
		this.active = false;
		try {
			this.serverSocket.close();
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException("Cannot close server socket.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.IServiceConnector#deserializeNextRecord()
	 */
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			return this.recordQueue.take();
		} catch (final InterruptedException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

}
