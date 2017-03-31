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

package kieker.analysisteetime.plugin.reader.jmx;

import javax.management.remote.JMXServiceURL;

import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * This is a reader which reads the records from a JMX queue.
 *
 * @author Jan Waller, Lars Bluemke
 *
 * @since 1.4
 */
public class JMXReader extends AbstractProducerStage<IMonitoringRecord> {

	private final JMXReaderLogic readerLogic;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param silentreconnect
	 *            Determines whether the reader silently reconnects on any errors.
	 * @param serviceURL
	 *            Optional service URL.
	 * @param domain
	 *            Determines the JMX domain.
	 * @param logname
	 *            Determines the logname used by the reader.
	 * @param port
	 *            Determines the JMX port.
	 * @param server
	 *            Determines the JMX server.
	 */
	public JMXReader(final boolean silentreconnect, final JMXServiceURL serviceURL, final String domain, final String logname,
			final int port, final String server) {
		this.readerLogic = new JMXReaderLogic(silentreconnect, serviceURL, domain, logname, port, server, LogFactory.getLog(JMXReader.class), this);
	}

	@Override
	protected void execute() {
		this.readerLogic.read();
	}

	/**
	 * Terminates the reader logic by returning from read method and terminates the execution of the stage.
	 */
	@Override
	public void terminateStage() {
		this.readerLogic.terminate();
		super.terminateStage();
	}

	/**
	 * Called from reader logic to send the read records to the output port.
	 *
	 * @param monitoringRecord
	 *            The record to deliver.
	 */
	public void deliverRecord(final IMonitoringRecord monitoringRecord) {
		this.outputPort.send(monitoringRecord);
	}

}
