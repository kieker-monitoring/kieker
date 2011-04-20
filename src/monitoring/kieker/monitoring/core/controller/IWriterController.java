package kieker.monitoring.core.controller;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

/**
 * The methods must not throw any exceptions but indicate problems via its
 * respective return value.
 * 
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 */
public interface IWriterController extends IMonitoringRecordReceiver {

	@Override
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	/**
	 * Shows how many inserts have been performed since last restart of the
	 * execution environment.
	 * 
	 * @return long
	 */
	public abstract long getNumberOfInserts();
}
