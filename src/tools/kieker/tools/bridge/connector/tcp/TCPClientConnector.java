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
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecord;
import kieker.common.record.MonitoringRecordFactory;
import kieker.common.record.control.StringMapRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;

/**
 * Connects to a remote record source.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class TCPClientConnector extends AbstractTCPConnector {

	private final int port;
	private final String hostname;
	private Socket socket;

	private DataInputStream in;
	/** normal hashmap is sufficient, as TCPClientConnector is not multi-threaded */
	private final Map<Integer, String> stringMap = new HashMap<Integer, String>();
	private final byte[] buffer = new byte[MonitoringRecordFactory.MAX_BUFFER_SIZE];

	/**
	 * Construct a new TCPClientService.
	 * 
	 * @param recordMap
	 *            IMonitoring to id map
	 * @param hostname
	 *            host this service connects to
	 * @param port
	 *            port number where this service connects to
	 */
	public TCPClientConnector(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap, final String hostname, final int port) {
		super(recordMap);
		this.port = port;
		this.hostname = hostname;
	}

	/**
	 * Create the connection to a remote service providing records.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if the given host or IP cannot be found, or an IOException occurs
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		super.initialize();
		try {
			this.socket = new Socket(this.hostname, this.port);
			this.in = new DataInputStream(this.socket.getInputStream());
		} catch (final UnknownHostException e) {
			throw new ConnectorDataTransmissionException("The given host " + this.hostname + " could not be found.", e);
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}

	}

	/**
	 * Closes the data stream and socket.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if an IOException occurs during the close operation
	 */
	public void close() throws ConnectorDataTransmissionException {
		try {
			this.in.close();
			this.socket.close();
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException("Error occured during socket close.", e);
		}
	}

	/**
	 * De-serialize an object reading from the input stream.
	 * 
	 * @return the de-serialized IMonitoringRecord object or null if the stream was terminated by the client.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             when a record is received that ID is unknown or the deserialization fails
	 * @throws ConnectorEndOfDataException
	 *             when the other end hung up or the data stream ends of another reason
	 */
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		final IRecord record = MonitoringRecordFactory.derserializeRecordFromStream(this.in, this.buffer, this.lookupEntityMap, this.stringMap);
		if (record instanceof IMonitoringRecord) {
			return (IMonitoringRecord) record;
		} else {
			if (record instanceof StringMapRecord) {
				this.stringMap.put(((StringMapRecord) record).getKey(), ((StringMapRecord) record).getString());
			}
			return this.deserializeNextRecord();
		}
	}
}
