/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
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
	// string buffer size (#1052)
	private static final int BUF_LEN = 65536;

	private static final Log LOG = LogFactory.getLog(TCPMultiServerConnectionRunnable.class);

	private final Socket socket;
	private final byte[] buffer = new byte[BUF_LEN];

	private final ConcurrentMap<Integer, LookupEntity> lookupEntityMap;

	private final BlockingQueue<IMonitoringRecord> recordQueue;

	private volatile boolean active;

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
			final BlockingQueue<IMonitoringRecord> recordQueue) {
		this.socket = socket;
		this.lookupEntityMap = lookupEntityMap;
		this.recordQueue = recordQueue;
	}

	/**
	 * Main loop of the connection runnable.
	 */
	@Override
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
	 * @param in
	 *            the input data stream
	 * 
	 * @return a new IMonitoringRecord
	 * @throws Exception
	 *             throws IOException when unknown record ID is read.
	 */
	private IMonitoringRecord deserialize(final DataInputStream in) throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			final Integer id = in.readInt();
			final LookupEntity recordProperty = this.lookupEntityMap.get(id);
			if (recordProperty != null) {
				final Object[] values = new Object[recordProperty.getParameterTypes().length];

				for (int i = 0; i < recordProperty.getParameterTypes().length; i++) {
					final Class<?> parameterType = recordProperty.getParameterTypes()[i];
					if (boolean.class.equals(parameterType)) {
						values[i] = in.readBoolean();
					} else if (Boolean.class.equals(parameterType)) {
						values[i] = Boolean.valueOf(in.readBoolean());
					} else if (byte.class.equals(parameterType)) {
						values[i] = in.readByte();
					} else if (Byte.class.equals(parameterType)) {
						values[i] = Byte.valueOf(in.readByte());
					} else if (short.class.equals(parameterType)) { // NOPMD
						values[i] = in.readShort();
					} else if (Short.class.equals(parameterType)) {
						values[i] = Short.valueOf(in.readShort());
					} else if (int.class.equals(parameterType)) {
						values[i] = in.readInt();
					} else if (Integer.class.equals(parameterType)) {
						values[i] = Integer.valueOf(in.readInt());
					} else if (long.class.equals(parameterType)) {
						values[i] = in.readLong();
					} else if (Long.class.equals(parameterType)) {
						values[i] = Long.valueOf(in.readLong());
					} else if (float.class.equals(parameterType)) {
						values[i] = in.readFloat();
					} else if (Float.class.equals(parameterType)) {
						values[i] = Float.valueOf(in.readFloat());
					} else if (double.class.equals(parameterType)) {
						values[i] = in.readDouble();
					} else if (Double.class.equals(parameterType)) {
						values[i] = Double.valueOf(in.readDouble());
					} else if (String.class.equals(parameterType)) {
						final int bufLen = in.readInt();
						in.readFully(this.buffer, 0, bufLen);
						values[i] = new String(this.buffer, 0, bufLen, "UTF-8");
					} else { // reference types
						throw new ConnectorDataTransmissionException("References are not yet supported.");
					}
				}

				return recordProperty.getConstructor().newInstance(values);
			} else {
				throw new IOException("Record type " + id + " is not registered.");
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

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

}
