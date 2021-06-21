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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.monitoring.core.controller.tcp.SingleSocketRecordReader;

/**
 * This is a reader which reads the records from a TCP port.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 * @deprecated 1.15 replaced in the TeeTime port by a generic TCP stage
 */
@Deprecated
@Plugin(description = "A reader which reads records from a TCP port", outputPorts = {
	@OutputPort(name = SingleSocketTcpReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class },
			description = "Output Port of the TCPReader")
}, configuration = {
	@Property(name = SingleSocketTcpReader.CONFIG_PROPERTY_NAME_PORT, defaultValue = "10133",
			description = "The first port of the server used for the TCP connection.")
})
public final class SingleSocketTcpReader extends AbstractReaderPlugin implements IRecordReceivedListener {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the TCP port. */
	public static final String CONFIG_PROPERTY_NAME_PORT = "port";

	private static final int MESSAGE_BUFFER_SIZE = 65535;

	private final int port;

	private final SingleSocketRecordReader recordReader;

	public SingleSocketTcpReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.port = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT);
		this.recordReader = new SingleSocketRecordReader(this.port, MESSAGE_BUFFER_SIZE, this.logger, this);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT, Integer.toString(this.port));
		return configuration;
	}

	@Override
	public boolean read() {
		this.recordReader.run();
		return true;
	}

	@Override
	public void onRecordReceived(final IMonitoringRecord record) {
		super.deliver(OUTPUT_PORT_NAME_RECORDS, record);
	}

	@Override
	public void terminate(final boolean error) {
		this.logger.info("Shutdown of TCPReader requested.");
	}

}
