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
package kieker.monitoring.core.controller.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.slf4j.Logger;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Represents a TCP reader which reads and reconstructs Kieker records from a
 * single TCP stream.
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class TcpRecordReader implements Runnable {

	private static final Charset ENCODING = Charset.forName("UTF-8");
	private static final int CONNECTION_CLOSED_BY_CLIENT = -1;

	private final Logger logger; // NOPMD allows to use the subclass name while logging

	private final int port;
	private final int bufferCapacity;
	private volatile boolean terminated;

	private final boolean respawn;

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<>();
	private final IRecordReceivedListener listener;
	private final CachedRecordFactoryCatalog recordFactories = new CachedRecordFactoryCatalog();

	/**
	 * Create single socket reader.
	 *
	 * @param port
	 *            port to listen to
	 * @param bufferCapacity
	 *            buffer capacity
	 * @param logger
	 *            logger to be used
	 * @param listener
	 *            listener to trigger on received records
	 */
	public TcpRecordReader(final int port, final int bufferCapacity, final Logger logger,
			final IRecordReceivedListener listener) {
		this(port, bufferCapacity, logger, false, listener);
	}

	/**
	 * Create single socket reader.
	 *
	 * @param port
	 *            port to listen to
	 * @param bufferCapacity
	 *            buffer capacity
	 * @param logger
	 *            logger to be used
	 * @param respawn
	 *            true when reader should respawn after lost connection
	 * @param listener
	 *            listener to trigger on received records
	 */
	public TcpRecordReader(final int port, final int bufferCapacity, final Logger logger,
			final boolean respawn, final IRecordReceivedListener listener) {
		this.port = port;
		this.bufferCapacity = bufferCapacity;
		this.logger = logger;
		this.respawn = respawn;
		this.listener = listener;
	}

	@Override
	public void run() {
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port));

			do {
				this.logger.debug("Listening on port {}", this.port);
				final SocketChannel socketChannel = serversocket.accept();
				try {
					final ByteBuffer buffer = ByteBuffer.allocateDirect(this.bufferCapacity);
					while (socketChannel.read(buffer) != CONNECTION_CLOSED_BY_CLIENT && !this.terminated) {
						this.process(buffer);
					}
				} finally {
					socketChannel.close();
				}
			} while (!this.terminated && this.respawn);
		} catch (final IOException ex) {
			this.logger.error("Error while receiving control commands.", ex);
		} finally {
			if (null != serversocket) {
				try {
					serversocket.close();
				} catch (final IOException e) {
					this.logger.debug("Failed to close TCP connection.", e);
				}
			}
		}
	}

	/**
	 * Gracefully terminates this TCP reader.
	 */
	public void terminate() {
		this.terminated = true;
	}

	public int getPort() {
		return this.port;
	}

	public boolean onBufferReceived(final ByteBuffer buffer) {
		// identify record class
		if (buffer.remaining() >= AbstractMonitoringRecord.TYPE_SIZE_INT) {
			final int clazzId = buffer.getInt();

			if (clazzId == -1) {
				return this.registerEntry(buffer);
			} else {
				return this.deserializeRecord(clazzId, buffer);
			}
		} else {
			return false;
		}
	}

	private boolean registerEntry(final ByteBuffer buffer) {
		// identify string identifier and string length
		if (buffer.remaining() >= AbstractMonitoringRecord.TYPE_SIZE_INT * 2) {
			final int id = buffer.getInt(); // NOPMD (id must be read before stringLength)
			final int stringLength = buffer.getInt();

			if (buffer.remaining() < stringLength) {
				return false;
			} else {
				final byte[] strBytes = new byte[stringLength];
				buffer.get(strBytes);
				final String string = new String(strBytes, TcpRecordReader.ENCODING);

				this.readerRegistry.register(id, string);

				return true;
			}
		} else {
			return false;
		}
	}

	private boolean deserializeRecord(final int clazzId, final ByteBuffer buffer) {
		// identify logging timestamp
		if (buffer.remaining() >= AbstractMonitoringRecord.TYPE_SIZE_LONG) {
			final long loggingTimestamp = buffer.getLong(); // NOPMD (timestamp must be read before checking the buffer
															// for record size)

			final String recordClassName = this.readerRegistry.get(clazzId);
			if (recordClassName != null) {
				// identify record data
				final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories
						.get(recordClassName);

				if (buffer.remaining() >= recordFactory.getRecordSizeInBytes()) {
					try {
						final IMonitoringRecord record = recordFactory
								.create(BinaryValueDeserializer.create(buffer, this.readerRegistry));
						record.setLoggingTimestamp(loggingTimestamp);

						this.listener.onRecordReceived(record);
						return true;
					} catch (final java.nio.BufferUnderflowException ex) {
						this.logger.warn("Cannot create {}; missing data in byte buffer. Buffer remaining {}",
								recordClassName, buffer.remaining());
						return false;
					} catch (final RecordInstantiationException ex) {
						this.logger.error("Failed to create {}", recordClassName, ex);
						return false;
					}
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private void process(final ByteBuffer buffer) {
		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				buffer.mark();
				final boolean success = this.onBufferReceived(buffer);
				if (!success) {
					buffer.reset();
					buffer.compact();
					return;
				}
			}
			buffer.clear();
		} catch (final BufferUnderflowException ex) {
			this.logger.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
			buffer.reset();
			buffer.compact();
		}
	}

}
