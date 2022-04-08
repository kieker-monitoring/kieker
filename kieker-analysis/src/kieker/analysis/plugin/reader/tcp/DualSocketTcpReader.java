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
package kieker.analysis.plugin.reader.tcp;

import java.nio.ByteBuffer;

import org.slf4j.Logger;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.monitoring.core.controller.tcp.AbstractTcpReader;

/**
 * This is a reader which reads the records from a TCP port. Compared to the {@link TCPReader}, it is more modular and faster in reading.
 *
 * @author Jan Waller, Christian Wulf
 *
 * @since 1.13
 * @deprecated 1.15 will no longer support dual socket in future
 */
@Deprecated
@Plugin(description = "A reader which reads records from a TCP port", outputPorts = {
	@OutputPort(name = DualSocketTcpReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class,
			description = "Output Port of the DualSocketTcpReader")
}, configuration = {
	@Property(name = DualSocketTcpReader.CONFIG_PROPERTY_NAME_PORT1, defaultValue = "10133",
			description = "The first port of the server used for the TCP connection."),
	@Property(name = DualSocketTcpReader.CONFIG_PROPERTY_NAME_PORT2, defaultValue = "10134",
			description = "The second port of the server used for the TCP connection.")
})
public class DualSocketTcpReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT1 = "port1";
	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT2 = "port2";

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private final int port1;
	private final int port2;

	private final ReaderRegistry<String> stringRegistry = new ReaderRegistry<>();

	private final AbstractRecordTcpReader tcpMonitoringRecordReader;
	private final AbstractTcpReader tcpStringRecordReader;

	private Thread tcpStringRecordReaderThread;

	public DualSocketTcpReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.port1 = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT1);
		this.port2 = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT2);

		this.tcpMonitoringRecordReader = this.createTcpMonitoringRecordReader(this.port1, MESSAGE_BUFFER_SIZE, this.logger, this.stringRegistry);

		this.tcpStringRecordReader = new AbstractTcpReader(this.port2, MESSAGE_BUFFER_SIZE, this.logger) {
			@SuppressWarnings("synthetic-access")
			@Override
			protected boolean onBufferReceived(final ByteBuffer buffer) {
				RegistryRecord.registerRecordInRegistry(buffer, DualSocketTcpReader.this.stringRegistry);
				return true;
			}
		};
	}

	protected AbstractRecordTcpReader createTcpMonitoringRecordReader(final int port, final int bufferCapacity, final Logger logger,
			final ReaderRegistry<String> registry) {
		return new AbstractRecordTcpReader(port, bufferCapacity, logger, registry, new CachedRecordFactoryCatalog()) {
			@SuppressWarnings("synthetic-access")
			@Override
			protected void onRecordReceived(final IMonitoringRecord record) {
				final boolean success = DualSocketTcpReader.this.deliver(OUTPUT_PORT_NAME_RECORDS, record);
				if (!success) {
					this.logger.warn("Failed to deliver record: {}", record);
				}
			}
		};
	}

	@Override
	public boolean init() {
		this.tcpStringRecordReaderThread = new Thread(this.tcpStringRecordReader); // BETTER move to ctor
		this.tcpStringRecordReaderThread.start();
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
		this.tcpMonitoringRecordReader.run();
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		this.logger.info("Shutdown requested.");
		this.tcpMonitoringRecordReader.terminate();

		this.tcpStringRecordReader.terminate();
		this.tcpStringRecordReaderThread.interrupt();
	}

	public int getPort1() {
		return this.port1;
	}

	public int getPort2() {
		return this.port2;
	}
}
