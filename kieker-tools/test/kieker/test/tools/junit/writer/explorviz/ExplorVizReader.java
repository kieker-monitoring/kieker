/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writer.explorviz;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Deque;
import java.util.LinkedList;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.registry.ILookup;
import kieker.common.util.registry.Lookup;

/**
 * @author Micky Singh Multani
 *
 * @since 1.11
 */
@Plugin(description = "A reader which reads records from a TCP port", outputPorts = {
	@OutputPort(name = ExplorVizReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the ExplorVizReader")
}, configuration = {
	@Property(name = ExplorVizReader.CONFIG_PROPERTY_NAME_PORT, defaultValue = "10555", description = "The port of the server used for the TCP connection.")
})
public class ExplorVizReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String ENCODING = "UTF-8";

	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT = "port";

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private volatile Thread readerThread;
	private volatile boolean terminated = false; // NOPMD
	private final int port;
	private final ILookup<String> stringRegistry = new Lookup<String>();
	private final Deque<Number> recordValues = new LinkedList<Number>();

	public ExplorVizReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.port = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT);
	}

	@Override
	public boolean read() {
		this.readerThread = Thread.currentThread();
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port));
			if (this.log.isDebugEnabled()) {
				this.log.debug("Listening on port " + this.port);
			}
			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e) {
				LOG.warn("An exception occurred", e);
			}
			final SocketChannel socketChannel = serversocket.accept();
			final ByteBuffer buffer = ByteBuffer.allocateDirect(MESSAGE_BUFFER_SIZE);

			while ((socketChannel.read(buffer) != -1) && (!this.terminated)) {
				buffer.flip();
				try {
					while (buffer.hasRemaining()) {
						buffer.mark();

						final byte clazzid = buffer.get();
						this.createReceivedRecord(clazzid, buffer);
					}
					buffer.clear();
				} catch (final BufferUnderflowException ex) {
					buffer.reset();
					buffer.compact();
				}
			}

			socketChannel.close();
		} catch (final ClosedByInterruptException ex) {
			this.log.warn("Reader interrupted", ex);
			return this.terminated;
		} catch (final IOException ex) {
			this.log.error("Error while reading", ex);
			return false;
		} finally {
			if (null != serversocket) {
				try {
					serversocket.close();
				} catch (final IOException e) {
					if (this.log.isDebugEnabled()) {
						this.log.debug("Failed to close TCP connection!", e);
					}
				}
			}

			this.deliverRecords();
		}
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		this.log.info("Shutdown of ExplorVizReader requested.");
		this.terminated = true;
		this.readerThread.interrupt();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT, Integer.toString(this.port));
		return configuration;
	}

	public void createReceivedRecord(final byte clazzid, final ByteBuffer buffer) {
		// HostApplicationMetaData
		if (clazzid == 0) {
			// this.recordValues.add(clazzid);
			// this.recordValues.add(buffer.getInt());
			// this.recordValues.add(buffer.getInt());
			// this.recordValues.add(buffer.getInt());
			// this.recordValues.add(buffer.getInt());
			buffer.getInt(); // not needed value
			buffer.getInt(); // not needed value
			buffer.getInt(); // not needed value
			buffer.getInt(); // not needed value

			// BeforeOperationEvent
		} else if (clazzid == 1) {
			this.recordValues.add(clazzid);
			this.recordValues.add(buffer.getLong());
			this.recordValues.add(buffer.getLong());
			this.recordValues.add(buffer.getInt());
			buffer.getInt(); // not needed value
			this.recordValues.add(buffer.getInt());
			this.recordValues.add(buffer.getInt());
			buffer.getInt(); // not needed value

			// AfterOperationFailedEvent
		} else if (clazzid == 2) {
			this.recordValues.add(clazzid);
			this.recordValues.add(buffer.getLong());
			this.recordValues.add(buffer.getLong());
			this.recordValues.add(buffer.getInt());
			this.recordValues.add(buffer.getInt());

			// AfterOperationEvent
		} else if (clazzid == 3) {
			this.recordValues.add(clazzid);
			this.recordValues.add(buffer.getLong());
			this.recordValues.add(buffer.getLong());
			this.recordValues.add(buffer.getInt());

			// RegistryRecord
		} else if (clazzid == 4) {
			RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
		}
	}

	public void deliverRecords() {
		// skip HostApplicationMetaData
		// final Number firstClassId = this.recordValues.removeFirst(); // clazzid
		// this.recordValues.removeFirst();
		// this.recordValues.removeFirst();
		// this.recordValues.removeFirst();
		// this.recordValues.removeFirst();

		while (!this.recordValues.isEmpty()) {
			final byte clazzid = (Byte) this.recordValues.removeFirst();
			final long timestamp = (Long) this.recordValues.removeFirst();
			final long traceID = (Long) this.recordValues.removeFirst();
			final int orderIndex = (Integer) this.recordValues.removeFirst();

			if (clazzid == 1) {
				final int opSinatureStringID = (Integer) this.recordValues.removeFirst();
				final int classSignatureStringID = (Integer) this.recordValues.removeFirst();
				// final int interfaceSignatureStringID = (Integer) this.recordValues.removeFirst();
				final String opSignature = this.stringRegistry.get(opSinatureStringID);
				final String classSignature = this.stringRegistry.get(classSignatureStringID);
				// final String interfaceSignature = this.stringRegistry.get(interfaceSignatureStringID);

				final BeforeOperationEvent record = new BeforeOperationEvent(timestamp, traceID, orderIndex,
						opSignature, classSignature);
				super.deliver(OUTPUT_PORT_NAME_RECORDS, record);

			} else if (clazzid == 2) {
				final int causeStringID = (Integer) this.recordValues.removeFirst();
				final String cause = this.stringRegistry.get(causeStringID);

				final CustomAfterOperationFailedEvent record = new CustomAfterOperationFailedEvent(timestamp, traceID, orderIndex, cause);
				super.deliver(OUTPUT_PORT_NAME_RECORDS, record);

			} else {
				final CustomAfterOperationEvent record = new CustomAfterOperationEvent(timestamp, traceID, orderIndex);
				super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
			}
		}
	}
}
