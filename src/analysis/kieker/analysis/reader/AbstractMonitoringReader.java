package kieker.analysis.reader;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractMonitoringReader implements IMonitoringReader {

	private static final Log log = LogFactory.getLog(AbstractMonitoringReader.class);

	private final Vector<IMonitoringRecordReceiver> recordReceivers = new Vector<IMonitoringRecordReceiver>();

	@Override
	public final void addRecordReceiver(final IMonitoringRecordReceiver receiver) {
		this.recordReceivers.add(receiver);
	}

	/**
	 * Delivers the given record to the consumers that are registered for this
	 * type of records.
	 * 
	 * This method should be used by implementing classes.
	 * 
	 * @param monitoringRecord
	 *            the record
	 * @return true on success; false in case of an error.
	 * @throws LogReaderExecutionException
	 *             if an error occurs
	 */
	protected final boolean deliverRecord(final IMonitoringRecord record) {
		try {
			for (final IMonitoringRecordReceiver c : this.recordReceivers) {
				if (!c.newMonitoringRecord(record)) {
					AbstractMonitoringReader.log.error("Consumer returned with error");
					return false;
				}
			}
		} catch (final Exception ex) {
			AbstractMonitoringReader.log.fatal("Caught Exception while delivering record", ex);
			return false;
		}
		return true;
	}
}
