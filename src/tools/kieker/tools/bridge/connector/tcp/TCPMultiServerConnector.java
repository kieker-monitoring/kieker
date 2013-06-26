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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;

/**
 * 
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
							new ServiceThread(TCPMultiServerConnector.this.serverSocket.accept(),
									TCPMultiServerConnector.this);
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

	public void close() throws ConnectorDataTransmissionException {
		this.active = false;
		try {
			this.serverSocket.close();
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException("Cannot close server socket.", e);
		}
	}

	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			return this.recordQueue.take();
		} catch (final InterruptedException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	public BlockingQueue<IMonitoringRecord> getRecordQueue() {
		return this.recordQueue;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	/**
	 * 
	 * @author Reiner Jung
	 * @since 1.8
	 * 
	 */
	class ServiceThread implements Runnable {
		private static final int BUF_LEN = 65536;

		private final Log LOG = LogFactory.getLog(ServiceThread.class);

		private final Socket socket;
		private final byte[] buffer = new byte[BUF_LEN];

		private final TCPMultiServerConnector parent;

		/**
		 * Create a service thread.
		 * 
		 * @param socket
		 *            service socket
		 */
		public ServiceThread(final Socket socket, final TCPMultiServerConnector parent) {
			this.socket = socket;
			this.parent = parent;
		}

		public void run() {
			try {
				final DataInputStream in = new DataInputStream(this.socket.getInputStream());
				while (this.parent.isActive()) {
					try {
						this.parent.getRecordQueue().put(this.deserialize(in));
					} catch (final InterruptedException e) {
						this.parent.setActive(false);
						this.LOG.warn("Listener " + Thread.currentThread().getId() + " died. Cause " + e.getMessage());
					} catch (final ConnectorDataTransmissionException e) {
						this.parent.setActive(false);
						this.LOG.warn("Listener " + Thread.currentThread().getId() + " died. Cause " + e.getMessage());
					} catch (final ConnectorEndOfDataException e) {
						this.parent.setActive(false);
						this.LOG.warn("Listener " + Thread.currentThread().getId() + " died. Cause " + e.getMessage());
					}
				}
				in.close();
				this.socket.close();
			} catch (final IOException e) {
				this.LOG.warn("IO exception occurred. Cause " + e.getMessage());
			}
		}

		/**
		 * Deserialize a received record.
		 * 
		 * @param in
		 * 
		 * @return a new IMonitoringRecord
		 * @throws Exception
		 *             throws IOException when unknown record ID is read.
		 */
		private IMonitoringRecord deserialize(final DataInputStream in) throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
			try {
				final Integer id = in.readInt();
				final LookupEntity recordProperty = TCPMultiServerConnector.this.lookupEntityMap.get(id);
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
							final int resultLen = in.read(this.buffer, 0, bufLen);
							if (resultLen == bufLen) {
								values[i] = new String(this.buffer, 0, bufLen, "UTF-8");
							} else {
								throw new ConnectorDataTransmissionException(bufLen + " bytes expected, but only " + resultLen + " bytes received.");
							}
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
	}

}
