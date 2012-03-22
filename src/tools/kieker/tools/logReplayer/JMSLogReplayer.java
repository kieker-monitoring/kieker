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

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractFilterPlugin;
import kieker.analysis.plugin.AbstractReaderPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * Listens to a JMS queue and simply passes each record to a specified {@link kieker.common.record.IMonitoringRecordReceiver.IMonitoringRecordReceiver}.
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
	 * the configured {@link kieker.common.record.IMonitoringRecordReceiver.IMonitoringRecordReceiver}.
	 * 
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		boolean success = true;

		final Configuration configuration = new Configuration(null);
		configuration.setProperty("msProviderUrl", this.jmsProviderUrl);
		configuration.setProperty("jmsDestination", this.jmsDestination);
		configuration.setProperty("jmsFactoryLookupName", this.jmsFactoryLookupName);
		final AbstractReaderPlugin logReader = new JMSReader(configuration);
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.registerReader(logReader);

		final RecordDelegationPlugin2 recordReceiver = new RecordDelegationPlugin2(new Configuration());
		/* configure the record receiver a little bit. */
		recordReceiver.setController(tpanInstance);
		recordReceiver.setInputPortName(this.recordReceiverInputPortName);
		recordReceiver.setRec(this.recordReceiver);
		recordReceiver.initialize();

		tpanInstance.registerFilter(recordReceiver);
		tpanInstance.connect(logReader, JMSReader.OUTPUT_PORT_NAME, recordReceiver, RecordDelegationPlugin2.INPUT_PORT);
		try {
			tpanInstance.run();
			success = true;
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			JMSLogReplayer.LOG.error("Exception running analysis instance", ex);
			success = false;
		}
		return success;
	}
}

/**
 * Kieker analysis plugin that delegates each record to the configured {@link kieker.common.record.IMonitoringRecordReceiver.IMonitoringRecordReceiver}.<br>
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
class RecordDelegationPlugin2 extends AbstractFilterPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String INPUT_PORT = "newMonitoringRecord";

	// private static final Log LOG = LogFactory.getLog(RecordDelegationPlugin2.class);
	private AbstractFilterPlugin rec;
	private String inputPortName;
	private AnalysisController controller;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param rec
	 *            The target of this plugin.
	 * @param inputPortName
	 *            The input port of the given plugin.
	 * @param controller
	 *            The controller instance to be used for connecting the filters.
	 */
	public RecordDelegationPlugin2(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * This method should only be called <b>after</b> <i>setRec</i>, <i>setController</i> and <i>setInputPortName</i> have been called, to register this plugin with
	 * the given components.
	 */
	public void initialize() {
		this.controller.registerFilter(this);
		this.controller.connect(this, RecordDelegationPlugin2.OUTPUT_PORT_NAME, this.rec, this.inputPortName);
	}

	/**
	 * This method sets the receiver of this instance.
	 * 
	 * @param rec
	 *            The new receiver.
	 */
	public void setRec(final AbstractFilterPlugin rec) {
		this.rec = rec;
	}

	/**
	 * This method sets the input port name to be used from the receiver component.
	 * 
	 * @param inputPortName
	 *            The name of the input port to be used.
	 */
	public void setInputPortName(final String inputPortName) {
		this.inputPortName = inputPortName;
	}

	/**
	 * Sets the controller to be used for registering.
	 * 
	 * @param controller
	 *            The analysis controller.
	 */
	public void setController(final AnalysisController controller) {
		this.controller = controller;
	}

	@InputPort(
			name = RecordDelegationPlugin2.INPUT_PORT,
			eventTypes = { IMonitoringRecord.class })
	public boolean newMonitoringRecord(final Object data) {
		return super.deliver(RecordDelegationPlugin2.OUTPUT_PORT_NAME, data);
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
}
