package kieker.tools.logReplayer;

import java.util.Collection;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.analysis.reader.JMSReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Listens to a JMS queue and simply passes each record to a specified
 * {@link IMonitoringRecordReceiver}.
 * 
 * @author Andre van Hoorn
 */
public class JMSLogReplayer {

	private static final Log log = LogFactory.getLog(JMSLogReplayer.class);
	/** Each record is delegated to this receiver. */
	private final IMonitoringRecordReceiver recordReceiver;

	private final String jmsProviderUrl;
	private final String jmsDestination;

	/** Must not be used for construction */
	@SuppressWarnings("unused")
	private JMSLogReplayer() {
		this(null, null, null);
	}

	/**
	 * @param jmsProviderUrl
	 *            = for instance "tcp://127.0.0.1:3035/"
	 * @param jmsDestination
	 *            = for instance "queue1"
	 * @throws IllegalArgumentException
	 *             if passed parameters are null or empty.
	 * @return
	 */
	public JMSLogReplayer(final IMonitoringRecordReceiver recordReceiver,
			final String jmsProviderUrl, final String jmsDestination) {
		this.recordReceiver = recordReceiver;
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to
	 * the configured {@link IMonitoringRecordReceiver}.
	 * 
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		boolean success = true;

		final IMonitoringReader logReader =
				new JMSReader(this.jmsProviderUrl, this.jmsDestination);
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setReader(logReader);
		tpanInstance.registerPlugin(new RecordDelegationPlugin2(
				this.recordReceiver));
		try {
			tpanInstance.run();
			success = true;
		} catch (final Exception ex) {
			JMSLogReplayer.log.error("Exception", ex);
			success = false;
		}
		return success;
	}
}

/**
 * Kieker analysis plugin that delegates each record to the configured
 * {@link IMonitoringRecordReceiver}.
 * 
 * TODO: We need to extract this class and merge it with that of
 * {@link FilesystemLogReplayer}
 * 
 * @author Andre van Hoorn
 * 
 */
class RecordDelegationPlugin2 implements IMonitoringRecordConsumerPlugin {

	private static final Log log = LogFactory
			.getLog(RecordDelegationPlugin.class);

	private final IMonitoringRecordReceiver rec;

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private RecordDelegationPlugin2() {
		this(null);
	}

	public RecordDelegationPlugin2(final IMonitoringRecordReceiver rec) {
		this.rec = rec;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.rec.newMonitoringRecord(record);
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean execute() {
		RecordDelegationPlugin2.log.info(RecordDelegationPlugin.class.getName()
				+ " starting ...");
		return true;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public void terminate(final boolean error) {
		return;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null; // receive records of any type
	}
}