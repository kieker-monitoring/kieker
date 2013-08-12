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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecord;
import kieker.common.record.LookupEntity;
import kieker.common.record.MonitoringRecordFactory;
import kieker.common.record.control.StringMapRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;

/**
 * Handles one TCP connection for the multi server.
 * 
 * @author Reiner Jung
 * @since 1.8
 * 
 */
public class TCPMultiServerConnectionRunnable implements Runnable {
	private static final Log LOG = LogFactory.getLog(TCPMultiServerConnectionRunnable.class);

	private final Socket socket;
	private final byte[] buffer = new byte[MonitoringRecordFactory.MAX_BUFFER_SIZE];

	private final ConcurrentMap<Integer, LookupEntity> lookupEntityMap;

	private final BlockingQueue<IMonitoringRecord> recordQueue;

	private volatile boolean active;

	private final ConcurrentMap<Integer, String> stringMap;

	/**
	 * Create a service thread.
	 * 
	 * @param socket
	 *            service socket
	 * @param lookupEntityMap
	 *            map for constructor and parameter fields of records combined with the record id
	 * @param recordQueue
	 *            Queue of the server to retrieve all deserialized records
	 */
	public TCPMultiServerConnectionRunnable(final Socket socket,
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap,
			final ConcurrentMap<Integer, String> stringMap,
			final BlockingQueue<IMonitoringRecord> recordQueue) {
		this.socket = socket;
		this.lookupEntityMap = lookupEntityMap;
		this.stringMap = stringMap;
		this.recordQueue = recordQueue;
	}

	/**
	 * Main loop of the connection runnable.
	 */
	public void run() {
		this.active = true;
		try {
			final DataInputStream in = new DataInputStream(this.socket.getInputStream());
			while (this.active) {
				try {
					this.recordQueue.put(this.deserialize(in));
				} catch (final InterruptedException e) {
					this.active = false;
					LOG.warn("Listener " + Thread.currentThread().getId() + " died.", e);
				} catch (final ConnectorDataTransmissionException e) {
					this.active = false;
					LOG.warn("Listener " + Thread.currentThread().getId() + " died.", e);
				} catch (final ConnectorEndOfDataException e) {
					this.active = false;
					LOG.info("Listener " + Thread.currentThread().getId() + " terminated at end of stream.");
				}
			}
			in.close();
			this.socket.close();
		} catch (final IOException e) {
			this.active = false;
			LOG.warn("IO exception occurred. Cause " + e.getMessage());
		}
	}

	/**
	 * Deserialize a received record.
	 * 
	 * @param input
	 *            the input data stream
	 * 
	 * @return a new IMonitoringRecord
	 * @throws Exception
	 *             throws IOException when unknown record ID is read.
	 */
	private IMonitoringRecord deserialize(final DataInputStream input) throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		final IRecord record = MonitoringRecordFactory.derserializeRecordFromStream(input, this.buffer, this.lookupEntityMap, this.stringMap);
		if (record instanceof IMonitoringRecord) {
			return (IMonitoringRecord) record;
		} else {
			if (record instanceof StringMapRecord) {
				this.stringMap.put(((StringMapRecord) record).getKey(), ((StringMapRecord) record).getString());
			}
			return this.deserialize(input);
		}
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

}
