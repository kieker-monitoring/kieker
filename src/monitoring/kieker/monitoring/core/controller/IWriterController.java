package kieker.monitoring.core.controller;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.timer.ITimeSource;
import kieker.monitoring.writer.IMonitoringWriter;

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
	 * Returns the configured monitoring writer.
	 * 
	 * @return
	 */
	public abstract IMonitoringWriter getMonitoringWriter();

	/**
	 * Shows how many inserts have been performed since last restart of the
	 * execution environment.
	 * 
	 * @return long
	 */
	public abstract long getNumberOfInserts();

	/**
	 * Returns the ITimeSource used in this controller.
	 * 
	 * @return ITimeSource
	 */
	public abstract ITimeSource getTimeSource();
}
