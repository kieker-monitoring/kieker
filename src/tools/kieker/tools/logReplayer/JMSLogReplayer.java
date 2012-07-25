/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * Listens to a JMS queue and simply passes each record to a specified {@link kieker.common.record.IMonitoringRecordReceiver}.
 * 
 * @TODO: This class is not used within Kieker; also, it can't work properly, as the recordReceiver is not registered
 *        to the {@link AnalysisController} instance.
 * 
 * @author Andre van Hoorn
 */
public class JMSLogReplayer {

	private static final Log LOG = LogFactory.getLog(JMSLogReplayer.class);
	/** Each record is delegated to this receiver. */
	private final AbstractFilterPlugin recordReceiver;

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;
	private final String recordReceiverInputPortName;

	/**
	 * @param jmsProviderUrl
	 *            = for instance "tcp://127.0.0.1:3035/"
	 * @param jmsDestination
	 *            = for instance "queue1"
	 * @param jmsFactoryLookupName
	 *            = for instance "org.exolab.jms.jndi.InitialContextFactory" (OpenJMS)
	 * @throws IllegalArgumentException
	 *             if passed parameters are null or empty.
	 */
	public JMSLogReplayer(final AbstractFilterPlugin recordReceiver, final String recordReceiverInputPortName, final String jmsProviderUrl,
			final String jmsDestination, final String jmsFactoryLookupName) {
		this.recordReceiver = recordReceiver;
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = jmsFactoryLookupName;
		this.recordReceiverInputPortName = recordReceiverInputPortName;
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to
	 * the configured {@link kieker.common.record.IMonitoringRecordReceiver}.
	 * 
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		final Configuration configuration = new Configuration(null);
		configuration.setProperty("msProviderUrl", this.jmsProviderUrl);
		configuration.setProperty("jmsDestination", this.jmsDestination);
		configuration.setProperty("jmsFactoryLookupName", this.jmsFactoryLookupName);
		final AbstractReaderPlugin logReader = new JMSReader(configuration);
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.registerReader(logReader);

		final RecordDelegationPlugin2 tmpRecordReceiver = new RecordDelegationPlugin2(new Configuration());
		/* configure the record receiver a little bit. */
		tpanInstance.registerFilter(tmpRecordReceiver);
		try {
			tpanInstance.connect(tmpRecordReceiver, RecordDelegationPlugin2.OUTPUT_PORT_NAME_MONITORING_RECORDS, this.recordReceiver,
					this.recordReceiverInputPortName);
		} catch (final AnalysisConfigurationException ex) {
			LOG.error("Failed to connect recordReceiver to recordReceiver.", ex);
			return false;
		}
		tpanInstance.registerFilter(tmpRecordReceiver);
		try {
			tpanInstance.connect(logReader, JMSReader.OUTPUT_PORT_NAME_RECORDS, tmpRecordReceiver, RecordDelegationPlugin2.INPUT_PORT_NAME_MONITORING_RECORDS);
		} catch (final AnalysisConfigurationException ex) {
			LOG.error("Failed to connect logReader to recordReceiver", ex);
			return false;
		}

		try {
			tpanInstance.run();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Exception running analysis instance", ex);
			return false;
		}
		return true;
	}
}

/**
 * Kieker analysis plugin that delegates each record to the configured {@link kieker.common.record.IMonitoringRecordReceiver}.<br>
 * 
 * <b>Don't</b> change the visibility modificator to public. The class does not have the necessary <i>Configuration</i>-Constructor in order to be used by the
 * analysis meta model. <br>
 * TODO: We need to extract this class and merge it with that of {@link FilesystemLogReplayer} See ticket http://kieker.uni-kiel.de/trac/ticket/173
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = { @OutputPort(name = RecordDelegationPlugin2.OUTPUT_PORT_NAME_MONITORING_RECORDS, eventTypes = { IMonitoringRecord.class })
		})
class RecordDelegationPlugin2 extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_MONITORING_RECORDS = "receivedRecords";

	public static final String OUTPUT_PORT_NAME_MONITORING_RECORDS = "delegatedRecords";

	/**
	 * Creates a new instance of this class.
	 */
	public RecordDelegationPlugin2(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(
			name = RecordDelegationPlugin2.INPUT_PORT_NAME_MONITORING_RECORDS,
			eventTypes = { IMonitoringRecord.class })
	public boolean newMonitoringRecord(final Object data) {
		return super.deliver(RecordDelegationPlugin2.OUTPUT_PORT_NAME_MONITORING_RECORDS, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	/**
	 * {@inheritDoc}
	 */
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
