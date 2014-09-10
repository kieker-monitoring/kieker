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

package kieker.tools.logReplayer;

import kieker.analysis.IAnalysisController;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;

/**
 * An implementation of the {@link AbstractLogReplayer}, using the {@link JMSReader} to replay {@link kieker.common.record.IMonitoringRecord}s from a JMS queue.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class JMSLogReplayer extends AbstractLogReplayer {

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;

	/**
	 * Creates a new JMS log replayer.
	 * 
	 * @param jmsProviderUrl
	 *            = for instance "tcp://127.0.0.1:3035/"
	 * @param jmsDestination
	 *            = for instance "queue1"
	 * @param jmsFactoryLookupName
	 *            = for instance "org.exolab.jms.jndi.InitialContextFactory" (OpenJMS)
	 * @param monitoringConfigurationFile
	 *            The path of the monitoring.properties file.
	 */
	public JMSLogReplayer(final String monitoringConfigurationFile, final String jmsProviderUrl, final String jmsDestination, final String jmsFactoryLookupName) {
		super(monitoringConfigurationFile,
				false, // realtimeMode
				1.0, // realtimeAccelerationFactor: any value will do because realtimeMode = false
				true, // keepOriginalLoggingTimestamps
				1, // numRealtimeWorkerThreads: any value will do because realtimeMode = false
				Long.MIN_VALUE,
				Long.MAX_VALUE);
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = jmsFactoryLookupName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractReaderPlugin createReader(final IAnalysisController analysisController) {
		final Configuration configuration = new Configuration();
		configuration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, this.jmsProviderUrl);
		configuration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, this.jmsDestination);
		configuration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, this.jmsFactoryLookupName);
		return new JMSReader(configuration, analysisController);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String readerOutputPortName() {
		return JMSReader.OUTPUT_PORT_NAME_RECORDS;
	}
}
