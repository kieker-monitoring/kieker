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
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentMap;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;

/**
 * Connects to a remote record source.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class TCPClientConnector extends AbstractTCPConnector {
	// TODO Consider to make the buffer length variable by configuration. Otherwise: Why is this an appropriate size? (Nils)
	private static final int BUF_LEN = 65536;

	private final int port;
	private final String hostname;
	private Socket socket;

	private final byte[] buffer = new byte[BUF_LEN];

	private DataInputStream in;

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
						final int resultLen = this.in.read(this.buffer, 0, bufLen);
						if (resultLen == bufLen) {
							values[i] = new String(this.buffer, 0, bufLen, "UTF-8");
						} else {
							throw new ConnectorDataTransmissionException(bufLen + " bytes expected, but only " + resultLen + " bytes received.");
						}
					} else { // reference types
						throw new ConnectorDataTransmissionException("References are not yet supported.");
					}
				}

				return recordProperty.getConstructor().newInstance(new Object[] { values });
			} else {
				throw new ConnectorDataTransmissionException("Record type " + id + " is not registered.");
			}
		} catch (final java.net.SocketException e) {
			throw new ConnectorEndOfDataException("End of stream", e);
		} catch (final java.io.EOFException e) {
			throw new ConnectorEndOfDataException("End of stream", e);
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
