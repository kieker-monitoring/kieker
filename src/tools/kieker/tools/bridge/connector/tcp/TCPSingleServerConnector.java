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
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecord;
import kieker.common.record.LookupEntity;
import kieker.common.record.MonitoringRecordFactory;
import kieker.common.record.control.StringMapRecord;
import kieker.tools.bridge.connector.AbstractConnector;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.ConnectorProperty;

/**
 * TCP server connector supporting one client.
 * 
 * All operations provided by this connector must run in the same thread.
 * 
 * @author Reiner Jung
 * 
 * @since 1.8
 */
@ConnectorProperty(cmdName = "tcp-single-server", name = "TCP Single Server Connector",
		description = "TCP server for binary Kieker records. Accepts only one connection.")
public class TCPSingleServerConnector extends AbstractConnector {

	/** Constant holding the name of the port configuration property. */
	public static final String PORT = TCPSingleServerConnector.class.getCanonicalName() + ".port";

	private final int port;

	/**
	 * Internal server socket variable.
	 */
	private ServerSocket serverSocket;

	/** Internal data input stream. */
	private DataInputStream in;
	/** normal hashmap is sufficient, as TCPClientConnector is not multi-threaded */
	private final Map<Integer, String> stringMap = new HashMap<Integer, String>();
	private final byte[] buffer = new byte[MonitoringRecordFactory.MAX_BUFFER_SIZE];

	/**
	 * Create a TCPSingleServerConnector.
	 * 
	 * @param configuration
	 *            Kieker configuration including setup for connectors
	 * 
	 * @param lookupEntityMap
	 *            IMonitoringRecord constructor and TYPES-array to id map
	 */
	public TCPSingleServerConnector(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		super(configuration, lookupEntityMap);
		this.port = this.configuration.getIntProperty(TCPSingleServerConnector.PORT);
	}

	/**
	 * Initializes a server socket and data stream for a single connection.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             it the socket could not be established
	 */
	public void initialize() throws ConnectorDataTransmissionException {
		try {
			this.serverSocket = new ServerSocket(this.port);
			this.in = new DataInputStream(this.serverSocket.accept().getInputStream());
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}

	}

	/**
	 * Close the server connection.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if the closing fails
	 */
	public void close() throws ConnectorDataTransmissionException {
		try {
			this.in.close();
			this.serverSocket.close();
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}

	}

	/**
	 * De-serialize an object reading from the input stream.
	 * 
	 * @return the de-serialized IMonitoringRecord object or null if the stream was terminated by the client.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if an unknown id is received which cannot be mapped to an IMonitoringRecord
	 * @throws ConnectorEndOfDataException
	 *             if the end of the data stream is reached
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
