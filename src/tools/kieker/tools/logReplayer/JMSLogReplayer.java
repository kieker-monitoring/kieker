/***************************************************************************
 * Copyright 2011 by
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

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.analysis.reader.jms.JMSReader;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * Listens to a JMS queue and simply passes each record to a specified {@link IMonitoringRecordReceiver}.
 * 
 * @author Andre van Hoorn
 */
public class JMSLogReplayer {

	private static final Log LOG = LogFactory.getLog(JMSLogReplayer.class);
	/** Each record is delegated to this receiver. */
	private final AbstractAnalysisPlugin recordReceiver;

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;
	private final String recordReceiverInputPortName;

	/**
	 * Must not be used for construction<br>
	 * TODO: Do we need this?
	 */
	@SuppressWarnings("unused")
	private JMSLogReplayer() {
		this(null, null, null, null, null);
	}

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
	public JMSLogReplayer(final AbstractAnalysisPlugin recordReceiver, final String recordReceiverInputPortName, final String jmsProviderUrl,
			final String jmsDestination, final String jmsFactoryLookupName) {
		this.recordReceiver = recordReceiver;
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = jmsFactoryLookupName;
		this.recordReceiverInputPortName = recordReceiverInputPortName;
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to
	 * the configured {@link IMonitoringRecordReceiver}.
	 * 
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		boolean success = true;

		final Configuration configuration = new Configuration(null);
		configuration.setProperty("msProviderUrl", this.jmsProviderUrl);
		configuration.setProperty("jmsDestination", this.jmsDestination);
		configuration.setProperty("jmsFactoryLookupName", this.jmsFactoryLookupName);
		final IMonitoringReader logReader = new JMSReader(configuration, new HashMap<String, AbstractRepository>());
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setReader(logReader);
		final RecordDelegationPlugin2 recordReceiver = new RecordDelegationPlugin2(this.recordReceiver, this.recordReceiverInputPortName);
		tpanInstance.registerPlugin(recordReceiver);
		AbstractPlugin.connect((AbstractPlugin) logReader, JMSReader.OUTPUT_PORT_NAME, recordReceiver, RecordDelegationPlugin2.INPUT_PORT);
		try {
			tpanInstance.run();
			success = true;
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			JMSLogReplayer.LOG.error("Exception", ex);
			success = false;
		}
		return success;
	}
}

/**
 * Kieker analysis plugin that delegates each record to the configured {@link IMonitoringRecordReceiver}.<br>
 * 
 * <b>Don't</b> change the visibility modificator to public. The class does not have the necessary <i>Configuration</i>-Constructor in order to be used by the
 * analysis meta model. <br>
 * TODO: We need to extract this class and merge it with that of {@link FilesystemLogReplayer} See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/173
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(
		outputPorts = { @OutputPort(name = RecordDelegationPlugin2.OUTPUT_PORT_NAME, eventTypes = { IMonitoringRecord.class })
		})
class RecordDelegationPlugin2 extends AbstractAnalysisPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String INPUT_PORT = "newMonitoringRecord";
	private static final Log LOG = LogFactory.getLog(RecordDelegationPlugin2.class);

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private RecordDelegationPlugin2() {
		this((AbstractAnalysisPlugin) null, "");
	}

	public RecordDelegationPlugin2(final AbstractAnalysisPlugin rec, final String inputPortName) {
		super(new Configuration(null), new HashMap<String, AbstractRepository>());

		AbstractPlugin.connect(this, RecordDelegationPlugin2.OUTPUT_PORT_NAME, rec, inputPortName);
	}

	@InputPort(eventTypes = { IMonitoringRecord.class })
	public boolean newMonitoringRecord(final Object data) {
		return super.deliver(RecordDelegationPlugin2.OUTPUT_PORT_NAME, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute() {
		RecordDelegationPlugin2.LOG.info(RecordDelegationPlugin.class.getName() + " starting ...");
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		// nothing to do
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
	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@Override
	protected Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
