/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * This is a reader which reads the records from a TCP port.
 *
 * @author Jan Waller
 *
 * @since 1.8
 * @deprecated 1.15 replaced in the TeeTime port by a generic TCP stage
 */
@Deprecated
@Plugin(description = "A reader which reads records from a TCP port", outputPorts = {
	@OutputPort(name = TCPReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class, description = "Output Port of the TCPReader")
}, configuration = {
	@Property(name = TCPReader.CONFIG_PROPERTY_NAME_PORT1, defaultValue = "10133", description = "The first port of the server used for the TCP connection."),
	@Property(name = TCPReader.CONFIG_PROPERTY_NAME_PORT2, defaultValue = "10134", description = "The second port of the server used for the TCP connection.")
})
public final class TCPReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT1 = "port1";
	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT2 = "port2";

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private volatile Thread readerThread;
	private volatile TCPStringReader tcpStringReader;
	private volatile boolean terminated;

	private final int port1;
	private final int port2;
	private final ReaderRegistry<String> stringRegistry = new ReaderRegistry<>();
	private final CachedRecordFactoryCatalog cachedRecordFactoryCatalog = CachedRecordFactoryCatalog.getInstance();

	public TCPReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.port1 = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT1);
		this.port2 = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT2);
	}

	@Override
	public boolean init() {
		this.tcpStringReader = new TCPStringReader(this.port2, this.stringRegistry);
		this.tcpStringReader.start();
		return super.init();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT1, Integer.toString(this.port1));
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT2, Integer.toString(this.port2));
		return configuration;
	}

	@Override
	public boolean read() {
		this.readerThread = Thread.currentThread();
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port1));
			this.logger.debug("Listening on port {}", this.port1);
			// BEGIN also loop this one?
			final SocketChannel socketChannel = serversocket.accept();
			final ByteBuffer buffer = ByteBuffer.allocateDirect(MESSAGE_BUFFER_SIZE);
			while ((socketChannel.read(buffer) != -1) && (!this.terminated)) {
				buffer.flip();
				// System.out.println("Reading, remaining:" + buffer.remaining());
				try {
					while (buffer.hasRemaining()) {
						buffer.mark();
						this.read(buffer);
					}
					buffer.clear();
				} catch (final BufferUnderflowException ex) {
					buffer.reset();
					// System.out.println("Underflow, remaining:" + buffer.remaining());
					buffer.compact();
				}
			}
			// System.out.println("Channel closing...");
			socketChannel.close();
			// END also loop this one?
		} catch (final ClosedByInterruptException ex) {
			this.logger.warn("Reader interrupted", ex);
			return this.terminated;
		} catch (final IOException ex) {
			this.logger.error("Error while reading", ex);
			return false;
		} finally {
			if (null != serversocket) {
				this.close(serversocket);
			}
		}
		return true;
	}

	private void read(final ByteBuffer buffer) {
		final int clazzId = buffer.getInt();
		final long loggingTimestamp = buffer.getLong();
		try { // NOCS (Nested try-catch)
				// final IMonitoringRecord record = AbstractMonitoringRecord.createFromByteBuffer(clazzid, buffer, this.stringRegistry);
			final String recordClassName = this.stringRegistry.get(clazzId);
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactoryCatalog.get(recordClassName);
			final IMonitoringRecord record = recordFactory.create(BinaryValueDeserializer.create(buffer, this.stringRegistry));
			record.setLoggingTimestamp(loggingTimestamp);

			super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
		} catch (final RecordInstantiationException ex) {
			this.logger.error("Failed to create record", ex);
		}
	}

	private void close(final ServerSocketChannel serversocket) {
		try {
			serversocket.close();
		} catch (final IOException e) {
			this.logger.debug("Failed to close TCP connection!", e);
		}
	}

	@Override
	public void terminate(final boolean error) {
		this.logger.info("Shutdown of TCPReader requested.");
		this.terminated = true;
		this.readerThread.interrupt();

		this.tcpStringReader.terminate();
	}

}

/**
 *
 * @author Jan Waller
 *
 * @since 1.8
 */
class TCPStringReader extends Thread {

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private static final Logger LOGGER = LoggerFactory.getLogger(TCPStringReader.class);

	private final int port;
	private final ReaderRegistry<String> stringRegistry;
	private volatile boolean terminated;
	private volatile Thread readerThread;

	public TCPStringReader(final int port, final ReaderRegistry<String> stringRegistry) {
		this.port = port;
		this.stringRegistry = stringRegistry;
	}

	public void terminate() {
		this.terminated = true;
		this.readerThread.interrupt();
	}

	@Override
	public void run() {
		this.readerThread = Thread.currentThread();
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port));
			LOGGER.debug("Listening on port {}", this.port);
			// BEGIN also loop this one?
			final SocketChannel socketChannel = serversocket.accept();
			final ByteBuffer buffer = ByteBuffer.allocateDirect(MESSAGE_BUFFER_SIZE);
			while ((socketChannel.read(buffer) != -1) && (!this.terminated)) {
				buffer.flip();
				try {
					while (buffer.hasRemaining()) {
						buffer.mark();
						RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
					}
					buffer.clear();
				} catch (final BufferUnderflowException ex) {
					buffer.reset();
					buffer.compact();
				}
			}
			socketChannel.close();
			// END also loop this one?
		} catch (final ClosedByInterruptException ex) {
			LOGGER.warn("Reader interrupted", ex);
		} catch (final IOException ex) {
			LOGGER.error("Error while reading", ex);
		} finally {
			if (null != serversocket) {
				try {
					serversocket.close();
				} catch (final IOException e) {
					LOGGER.debug("Failed to close TCP connection!", e);
				}
			}
		}
	}

}
