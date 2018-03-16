/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.amqp;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * Reader stage that reads monitoring records from an AMQP queue.
 *
 * @author Holger Knoche, Lars Bluemke, Sören Henning
 *
 * @since 1.12
 */
public class AMQPReaderStage extends AbstractProducerStage<IMonitoringRecord> {

	private final AMQPReader readerLogic;

	/**
	 * Creates a new AMQP reader.
	 *
	 * @param uri
	 *            The name of the configuration property for the server URI.
	 * @param queueName
	 *            The name of the configuration property for the AMQP queue name.
	 * @param heartbeat
	 *            The name of the configuration property for the heartbeat timeout.
	 */
	public AMQPReaderStage(final String uri, final String queueName, final int heartbeat) {
		this.readerLogic = new AMQPReader(uri, queueName, heartbeat, this::deliverRecord);
	}

	@Override
	protected void execute() {
		this.readerLogic.read();
	}

	/**
	 * Terminates the reader logic by returning from read method and terminates the execution of the stage.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void terminateStage() {
		this.readerLogic.terminate();
		super.terminateStage();
	}

	/**
	 * Send a record to the stage's output port.
	 *
	 * @param monitoringRecord
	 *            The record to deliver.
	 */
	private void deliverRecord(final IMonitoringRecord monitoringRecord) {
		this.outputPort.send(monitoringRecord);
	}

}
