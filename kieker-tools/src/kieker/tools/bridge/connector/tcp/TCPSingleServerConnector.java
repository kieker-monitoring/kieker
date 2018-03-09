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

package kieker.tools.bridge.connector.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentMap;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
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

	// string buffer size (#1052)
	private static final int BUF_LEN = 65536;

	private final int port;

	/**
	 * Internal server socket variable.
	 */
	private ServerSocket serverSocket;

	/**
	 * Internal data input stream.
	 */
	private DataInputStream in;

	private final byte[] buffer = new byte[BUF_LEN];

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
	@Override
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
	@Override
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
	@Override
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		// read structure ID
		try {
			final Integer id = this.in.readInt();
			final LookupEntity recordProperty = this.lookupEntityMap.get(id);
			if (recordProperty != null) {
				final Object[] values = new Object[recordProperty.getParameterTypes().length];

				for (int i = 0; i < recordProperty.getParameterTypes().length; i++) {
					final Class<?> parameterType = recordProperty.getParameterTypes()[i];
					if (boolean.class.equals(parameterType)) {
						values[i] = this.in.readBoolean();
					} else if (Boolean.class.equals(parameterType)) {
						values[i] = Boolean.valueOf(this.in.readBoolean());
					} else if (byte.class.equals(parameterType)) {
						values[i] = this.in.readByte();
					} else if (Byte.class.equals(parameterType)) {
						values[i] = Byte.valueOf(this.in.readByte());
					} else if (short.class.equals(parameterType)) { // NOPMD
						values[i] = this.in.readShort();
					} else if (Short.class.equals(parameterType)) {
						values[i] = Short.valueOf(this.in.readShort());
					} else if (int.class.equals(parameterType)) {
						values[i] = this.in.readInt();
					} else if (Integer.class.equals(parameterType)) {
						values[i] = Integer.valueOf(this.in.readInt());
					} else if (long.class.equals(parameterType)) {
						values[i] = this.in.readLong();
					} else if (Long.class.equals(parameterType)) {
						values[i] = Long.valueOf(this.in.readLong());
					} else if (float.class.equals(parameterType)) {
						values[i] = this.in.readFloat();
					} else if (Float.class.equals(parameterType)) {
						values[i] = Float.valueOf(this.in.readFloat());
					} else if (double.class.equals(parameterType)) {
						values[i] = this.in.readDouble();
					} else if (Double.class.equals(parameterType)) {
						values[i] = Double.valueOf(this.in.readDouble());
					} else if (String.class.equals(parameterType)) {
						final int bufLen = this.in.readInt();
						this.in.readFully(this.buffer, 0, bufLen);
						values[i] = new String(this.buffer, 0, bufLen, "UTF-8");
					} else { // reference types
						throw new ConnectorDataTransmissionException("References are not yet supported.");
					}
				}

				return recordProperty.getConstructor().newInstance(values);
			} else {
				throw new ConnectorDataTransmissionException("Record type " + id + " is not registered.");
			}
		} catch (final java.net.SocketException e) {
			throw new ConnectorEndOfDataException("End of stream", e);
		} catch (final java.io.EOFException e) {
			throw new ConnectorEndOfDataException("End of stream during an read operation", e);
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException("Read error", e);
		} catch (final InstantiationException e) {
			throw new ConnectorDataTransmissionException("Instantiation error", e);
		} catch (final IllegalAccessException e) {
			throw new ConnectorDataTransmissionException("Access to fields are restricted", e);
		} catch (final IllegalArgumentException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final InvocationTargetException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}
}
