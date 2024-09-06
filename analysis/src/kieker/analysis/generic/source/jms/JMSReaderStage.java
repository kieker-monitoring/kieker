/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.generic.source.jms;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * Reads monitoring records from a (remote or local) JMS queue by using the
 * read-method from JMSReaderLogicModule. JMSReaderLogicModule also delivers
 * read records to the output port.
 *
 * @author Andre van Hoorn, Matthias Rohr, Lars Bluemke
 *
 * @since 0.95a
 */
public class JMSReaderStage extends AbstractProducerStage<IMonitoringRecord> {

	private final JMSReader readerLogic;

	/**
	 * Creates a new JMSReader.
	 *
	 * @param jmsProviderUrl
	 *            The name of the configuration determining the JMS provider URL,
	 *            e.g. {@code tcp://localhost:3035/}
	 * @param jmsDestination
	 *            The name of the configuration determining the JMS destination,
	 *            e.g. {@code queue1}.
	 * @param jmsFactoryLookupName
	 *            The name of the configuration determining the name of the used JMS
	 *            factory, e.g. {@code org.exolab.jms.jndi.InitialContextFactory}.
	 * @throws AnalysisConfigurationException
	 *             on errors when configuring the JMSReader
	 */
	public JMSReaderStage(final String jmsProviderUrl, final String jmsDestination, final String jmsFactoryLookupName) throws AnalysisConfigurationException {
		this.readerLogic = new JMSReader(jmsProviderUrl, jmsDestination, jmsFactoryLookupName, this::deliverRecord);
	}

	@Override
	protected void execute() {
		this.readerLogic.read();
	}

	/**
	 * Terminates the reader logic in addition to the following.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	protected void workCompleted() {
		this.readerLogic.terminate();
		super.workCompleted();
	}

	/**
	 * Send the read records to the output port.
	 *
	 * @param monitoringRecord
	 *            The record to deliver.
	 */
	private void deliverRecord(final IMonitoringRecord monitoringRecord) {
		this.outputPort.send(monitoringRecord);
	}
}
