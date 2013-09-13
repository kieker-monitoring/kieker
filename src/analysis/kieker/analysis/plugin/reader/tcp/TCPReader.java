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

package kieker.analysis.plugin.reader.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.registry.ILookup;
import kieker.common.util.registry.Lookup;

/**
 * This is a reader which reads the records from a TCP port.
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
@Plugin(description = "A reader which reads records from a TCP port",
		outputPorts = {
			@OutputPort(name = TCPReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the TCPReader")
		},
		configuration = {
			@Property(name = TCPReader.CONFIG_PROPERTY_NAME_PORT1, defaultValue = "10133",
					description = "The first port of the server used for the TCP connection."),
			@Property(name = TCPReader.CONFIG_PROPERTY_NAME_PORT2, defaultValue = "10134",
					description = "The second port of the server used for the TCP connection."),
		})
public final class TCPReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT1 = "port1";
	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT2 = "port2";

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private static final Log LOG = LogFactory.getLog(TCPReader.class);

	private final int port1;
	private final int port2;
	final ILookup<String> stringRegistry = new Lookup<String>();

	public TCPReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.port1 = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT1);
		this.port2 = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT2);
	}

	@Override
	public boolean init() {
		final TCPStringReader tcpStringReader = new TCPStringReader(this.port2, this.stringRegistry);
		// tcpStringReader.setDaemon(true);
		tcpStringReader.start();
		return super.init();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT1, Integer.toString(this.port1));
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT2, Integer.toString(this.port2));
		return configuration;
	}

	public boolean read() {
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port1));
			if (LOG.isDebugEnabled()) {
				LOG.debug("Listening on port " + this.port1);
			}
			// BEGIN also loop this one?
			final SocketChannel socketChannel = serversocket.accept();
			final ByteBuffer buffer = ByteBuffer.allocateDirect(MESSAGE_BUFFER_SIZE);
			while (socketChannel.read(buffer) != -1) {
				// System.out.println("Reading ...");
				buffer.flip();
				while (buffer.hasRemaining()) {
					// TODO: what if the message is not completely within the buffer (try catch BufferUnderflowException)
					final int clazzid = buffer.getInt();
					// System.out.println("ClassId: " + clazzid);
					final long loggingTimestamp = buffer.getLong();
					final IMonitoringRecord record;
					try {
						final String str = this.stringRegistry.get(clazzid);
						// System.out.println("Class: " + str);
						record = AbstractMonitoringRecord.createFromByteBuffer(str, buffer, this.stringRegistry);
						record.setLoggingTimestamp(loggingTimestamp);
						// System.out.println("Deliver record: " + record.getClass().getName() + " : " + record.toString());
						super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
					} catch (final MonitoringRecordException ex) {
						LOG.error("Failed to create record.", ex);
					}
				}
				buffer.clear();
			}
			// System.out.println("Channel closing...");
			socketChannel.close();
			// END also loop this one?
		} catch (final IOException ex) {
			LOG.error("Error while reading", ex);
			return false;
		} finally {
			if (null != serversocket) {
				try {
					serversocket.close();
				} catch (final IOException e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Failed to close TCP connection!", e);
					}
				}
			}
		}
		return true;
	}

	public void terminate(final boolean error) {
		LOG.info("Shutdown of TCPReader requested.");
		// TODO actually implement terminate!
	}

}

class TCPStringReader extends Thread {

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private static final Log LOG = LogFactory.getLog(TCPReader.class);

	private final int port;
	final ILookup<String> stringRegistry;

	public TCPStringReader(final int port, final ILookup<String> stringRegistry) {
		this.port = port;
		this.stringRegistry = stringRegistry;
	}

	@Override
	public void run() {
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port));
			if (LOG.isDebugEnabled()) {
				LOG.debug("Listening on port " + this.port);
			}
			// BEGIN also loop this one?
			final SocketChannel socketChannel = serversocket.accept();
			final ByteBuffer buffer = ByteBuffer.allocateDirect(MESSAGE_BUFFER_SIZE);
			while (socketChannel.read(buffer) != -1) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					// TODO: what if the message is not completely within the buffer (try catch BufferUnderflowException)
					// System.out.println("Reading from StringChannel ...");
					RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
				}
				buffer.clear();
			}
			// System.out.println("StringChannel closing...");
			socketChannel.close();
			// END also loop this one?
		} catch (final IOException ex) {
			LOG.error("Error while reading", ex);
		} finally {
			if (null != serversocket) {
				try {
					serversocket.close();
				} catch (final IOException e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Failed to close TCP connection!", e);
					}
				}
			}
		}
	}

}
