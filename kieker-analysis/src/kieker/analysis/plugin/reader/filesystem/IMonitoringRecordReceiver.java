package kieker.analysis.plugin.reader.filesystem;

import kieker.common.record.IMonitoringRecord;

/**
 * This is a simple interface showing that the {@link FSReader} can receive records. This is mostly a relict from an older version.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
interface IMonitoringRecordReceiver {

	/**
	 * This method is called for each new record by each ReaderThread.
	 *
	 * @param record
	 *            The record to be processed.
	 * @return true if and only if the record has been handled correctly.
	 *
	 * @since 1.2
	 */
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	/**
	 * @since 2.0
	 */
	void newEndOfFileRecord();
}
