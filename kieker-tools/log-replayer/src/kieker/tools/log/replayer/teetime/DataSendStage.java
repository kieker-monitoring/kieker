/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.log.replayer.teetime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

import teetime.framework.AbstractConsumerStage;

/**
 * Send data via TCP.
 *
 * @author Reiner Jung
 * @since 1.16
 *
 */
public class DataSendStage extends AbstractConsumerStage<IMonitoringRecord> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSendStage.class);

	private static final String WRITER_NAME = SingleSocketTcpWriter.class.getCanonicalName();

	private final IMonitoringController ctrl;

	private long count;

	/**
	 * Configure and setup the Kieker writer.
	 *
	 * @param hostname
	 *            host where the data is send to
	 * @param port
	 *            port where the data is send to
	 */
	public DataSendStage(final String hostname, final int port) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationConstants.CONTROLLER_NAME, "Kieker Logreplayer");
		configuration.setProperty(ConfigurationConstants.WRITER_CLASSNAME, DataSendStage.WRITER_NAME);

		configuration.setProperty(SingleSocketTcpWriter.CONFIG_FLUSH, "true");
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_BUFFERSIZE, "25000");
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, hostname);
		configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, port);

		DataSendStage.LOGGER.debug("Configuration complete.");

		this.ctrl = MonitoringController.createInstance(configuration);
	}

	@Override
	protected void execute(final IMonitoringRecord record) {
		this.count++;
		this.ctrl.newMonitoringRecord(record);
		if (this.count % 1000 == 0) {
			DataSendStage.LOGGER.info("Saved {} records", this.count);
		}
	}

	public long getCount() {
		return this.count;
	}

	public boolean isOutputConnected() {
		return !this.ctrl.isMonitoringTerminated();
	}
}
