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

package kieker.test.tools.junit.writer.explorviz;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

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
@Plugin(description = "A reader which reads records from a TCP port",
		outputPorts = {
			@OutputPort(name = ExplorVizReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class },
					description = "Output Port of the ExplorVizReader")
		},
		configuration = {
			@Property(name = ExplorVizReader.CONFIG_PROPERTY_NAME_PORT, defaultValue = "10555",
					description = "The port of the server used for the TCP connection.")
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
			byte clazzid;
			boolean flag = true;

			while ((socketChannel.read(buffer) != -1) && (!this.terminated)) {
				buffer.flip();
				try {
					while (buffer.hasRemaining()) {

						buffer.mark();

						if (flag) {
							this.skipBufferToFirstTestRecord(buffer);
							flag = false;
						}

						clazzid = buffer.get();
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
		long timestamp;
		long traceID;
		int orderIndex;
		final int opSinatureStringID;
		final int classSignatureStringID;
		final int causeStringID;
		final String opSignature;
		final String classSignature;

		// BeforeOperationEvent
		if (clazzid == 1) {
			timestamp = buffer.getLong();
			traceID = buffer.getLong();
			orderIndex = buffer.getInt();
			buffer.getInt(); // not needed value
			opSinatureStringID = buffer.getInt();
			classSignatureStringID = buffer.getInt();
			buffer.getInt(); // not needed emptyStringID
			buffer.get(); // not needed ClazzID
			RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
			buffer.get(); // not needed ClazzID
			RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
			buffer.get(); // not needed ClazzID
			RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
			opSignature = this.stringRegistry.get(opSinatureStringID);
			classSignature = this.stringRegistry.get(classSignatureStringID);

			final BeforeOperationEvent record = new BeforeOperationEvent(timestamp, traceID, orderIndex,
					opSignature, classSignature);
			super.deliver(OUTPUT_PORT_NAME_RECORDS, record);

			// AfterFailedOperationEvent
		} else if (clazzid == 2) {
			timestamp = buffer.getLong();
			traceID = buffer.getLong();
			orderIndex = buffer.getInt();
			causeStringID = buffer.getInt();
			buffer.get(); // not needed ClazzID
			RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
			final String cause = this.stringRegistry.get(causeStringID);

			final CustomAfterOperationFailedEvent record = new CustomAfterOperationFailedEvent(timestamp, traceID, orderIndex, cause);
			super.deliver(OUTPUT_PORT_NAME_RECORDS, record);

			// AfterOperationEvent
		} else if (clazzid == 3) {
			final CustomAfterOperationEvent record = new CustomAfterOperationEvent(buffer.getLong(), buffer.getLong(), buffer.getInt());
			super.deliver(OUTPUT_PORT_NAME_RECORDS, record);

			// RegistryRecord
		} else if (clazzid == 4) {
			final int id = buffer.getInt();
			final byte[] strBytes = new byte[buffer.getInt()];
			buffer.get(strBytes);
			String string;
			try {
				string = new String(strBytes, ENCODING);
			} catch (final UnsupportedEncodingException e) {
				string = "EncodingDidNotWork";
			}
			final RegistryRecord record = new RegistryRecord(id, string);
			super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
		}
	}

	public void skipBufferToFirstTestRecord(final ByteBuffer buffer) {
		int i;
		int j;
		buffer.get();
		for (i = 0; i < 4; i++) {
			buffer.getInt();
		}
		for (j = 0; j < 4; j++) {
			buffer.get();
			buffer.getInt();
			buffer.getInt();
			buffer.get();
		}
	}

}
