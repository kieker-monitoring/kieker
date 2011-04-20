package kieker.common.record;

/**
 * @author Andre van Hoorn
 */
public interface IMonitoringRecordReceiver {

	/**
	 * Called for each new record.
	 * 
	 * Notice, that this method should not throw an exception,
	 * but indicate an error by the return value false.
	 * 
	 * @param record the record.
	 * @return true on success; false in case of an error.
	 */
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);
}
