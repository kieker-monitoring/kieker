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

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.AnalysisController;
import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.AbstractInputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.reader.IMonitoringReader;
import kieker.analysis.reader.jms.JMSReader;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

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

	/** Must not be used for construction */
	@SuppressWarnings("unused")
	private JMSLogReplayer() {
		this(null, null, null, null);
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
	public JMSLogReplayer(final AbstractAnalysisPlugin recordReceiver, final String jmsProviderUrl, final String jmsDestination, final String jmsFactoryLookupName) {
		this.recordReceiver = recordReceiver;
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = jmsFactoryLookupName;
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
		final IMonitoringReader logReader = new JMSReader(configuration);
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setReader(logReader);
		tpanInstance.registerPlugin(new RecordDelegationPlugin2(this.recordReceiver));
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
class RecordDelegationPlugin2 extends AbstractAnalysisPlugin {

	private static final Log LOG = LogFactory.getLog(RecordDelegationPlugin2.class);

	private final OutputPort output = new OutputPort("out", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class })));
	private final AbstractInputPort input = new AbstractInputPort("in", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }))) {
		@Override
		public void newEvent(final Object event) {
			RecordDelegationPlugin2.this.newMonitoringRecord((IMonitoringRecord) event);

			RecordDelegationPlugin2.this.output.deliver(event);
		}
	};

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private RecordDelegationPlugin2() {
		this((AbstractAnalysisPlugin) null);
	}

	public RecordDelegationPlugin2(final AbstractAnalysisPlugin rec) {
		super(new Configuration(null));

		this.output.subscribe(rec.getAllInputPorts()[0]);

		this.registerInputPort("in", this.input);
		this.registerOutputPort("out", this.output);
	}

	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.output.deliver(record);
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean execute() {
		RecordDelegationPlugin2.LOG.info(RecordDelegationPlugin.class.getName() + " starting ...");
		return true;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}

	@Override
	protected Properties getDefaultProperties() {
		return new Properties();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		// TODO: Save the current configuration

		return configuration;
	}
}
