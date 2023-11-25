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
package kieker.analysis.generic.source.tcp;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;

import kieker.analysis.generic.source.rewriter.ITraceMetadataRewriter;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;

import teetime.framework.OutputPort;

/**
 * This is the reader for the MultipleConnectionTcpSourceStage.
 *
 * @author Reiner Jung
 * @since 1.15
 */
final class ReaderThread extends Thread {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final Selector readSelector;
	private final Logger logger;

	private final ITraceMetadataRewriter recordRewriter;
	private final OutputPort<IMonitoringRecord> outputPort;
	private boolean active;

	/**
	 * Create a multi stream reader thread.
	 *
	 * @param logger
	 *            logger for the multi tcp reader
	 *
	 * @param readSelector
	 *            channel selector
	 * @param recordRewriter
	 *            record rewriter
	 * @param outputPort
	 *            output port
	 */
	public ReaderThread(final Logger logger, final Selector readSelector, final ITraceMetadataRewriter recordRewriter,
			final OutputPort<IMonitoringRecord> outputPort) {
		this.readSelector = readSelector;
		this.logger = logger;
		this.recordRewriter = recordRewriter;
		this.outputPort = outputPort;
	}

	@Override
	public void run() {
		this.active = true;
		while (this.isAlive() && this.active) {
			try {
				final int readReady = this.readSelector.select();

				if (readReady > 0) {
					final Set<SelectionKey> selectedKeys = this.readSelector.selectedKeys();
					final Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

					while (keyIterator.hasNext()) {
						final SelectionKey key = keyIterator.next();

						this.readFromSocket(key);

						keyIterator.remove();
					}

					selectedKeys.clear();
				} else {
					Thread.sleep(100); // wait for the registration of the new key in MultipleConnectionTypSourceStage
				}
			} catch (final ClosedSelectorException e1) {
				this.logger.error("Selector has already been closed.", e1);
			} catch (final IOException e2) {
				this.logger.info("IO error while reading from connection.");
			} catch (final InterruptedException e) {
				this.logger.warn("Thread.sleep was interrupted.");
			}
		}
	}

	private void readFromSocket(final SelectionKey key) throws IOException {
		boolean endOfStreamReached = false;
		final Connection connection = (Connection) key.attachment();
		final SocketChannel socketChannel = connection.getChannel();

		int bytesRead = socketChannel.read(connection.getBuffer());

		while (bytesRead > 0) {
			bytesRead = socketChannel.read(connection.getBuffer());
		}
		if (bytesRead == -1) {
			endOfStreamReached = true;
		}

		this.processBuffer(connection);

		if (endOfStreamReached || connection.isError()) {
			this.logger.debug("Socket closed: " + socketChannel.getRemoteAddress().toString());
			key.attach(null);
			key.cancel();
			key.channel().close();
		}
	}

	private void processBuffer(final Connection connection) throws IOException {
		final ByteBuffer buffer = connection.getBuffer();

		buffer.flip();

		try {
			while ((buffer.position() + 4) < buffer.limit()) {
				buffer.mark();
				if (!this.onBufferReceived(connection)) {
					return;
				}
			}
			buffer.mark();
			buffer.compact();
		} catch (final BufferUnderflowException ex) {
			this.logger.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
			buffer.reset();
			buffer.compact();
		}
	}

	private boolean onBufferReceived(final Connection connection) throws IOException {
		// identify record class
		if (connection.getBuffer().remaining() < INT_BYTES) {
			return false;
		}
		final int clazzId = connection.getBuffer().getInt();

		if (clazzId == -1) {
			return this.registerRegistryEntry(connection);
		} else {
			return this.deserializeRecord(connection, clazzId);
		}
	}

	private boolean registerRegistryEntry(final Connection connection) {
		// identify string identifier and string length
		if (connection.getBuffer().remaining() < (INT_BYTES
				+ INT_BYTES)) {
			// incomplete record, move back
			connection.getBuffer().reset();
			connection.getBuffer().compact();

			return false;
		} else {
			final int id = connection.getBuffer().getInt();
			final int stringLength = connection.getBuffer().getInt();

			if (connection.getBuffer().remaining() < stringLength) {
				// incomplete record, move back
				connection.getBuffer().reset();
				connection.getBuffer().compact();

				return false;
			} else {
				final byte[] strBytes = new byte[stringLength];
				connection.getBuffer().get(strBytes);
				final String string = new String(strBytes, ENCODING);

				connection.getRegistry().register(id, string);
				return true;
			}
		}
	}

	private boolean deserializeRecord(final Connection connection, final int clazzId) throws IOException {
		final String recordClassName = connection.getRegistry().get(clazzId);

		// identify logging timestamp
		if (connection.getBuffer().remaining() < LONG_BYTES) {
			// incomplete record, move back
			connection.getBuffer().reset();
			connection.getBuffer().compact();

			return false;
		} else {
			final long loggingTimestamp = connection.getBuffer().getLong();

			// identify record data
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
			if (recordFactory == null) {
				this.logger.debug("Unknown class {}: No factory present. Aborting...", recordClassName);
				connection.setError(true);
				return false;
			} else {
				if (connection.getBuffer().remaining() < recordFactory.getRecordSizeInBytes()) {
					// incomplete record, move back
					connection.getBuffer().reset();
					connection.getBuffer().compact();

					return false;
				} else {
					try {
						final IMonitoringRecord record = recordFactory.create(connection.getValueDeserializer());

						this.recordRewriter.rewrite(connection, record, loggingTimestamp, this.outputPort);
						return true;
					} catch (final RecordInstantiationException ex) {
						this.logger.error("Failed to create: " + recordClassName, ex);
						// incomplete record, move back
						connection.getBuffer().reset();
						connection.getBuffer().compact();
						return false;
					}
				}
			}
		}
	}

	public void terminate() {
		this.active = false;
		this.readSelector.wakeup();
	}
}
