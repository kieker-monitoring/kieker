package kieker.analysis.reader;

import kieker.common.record.IMonitoringRecordReceiver;

/**
 * This reader reads a folder or an single tpmon file and
 * transforms it to monitoring events that are stored in the file system
 * again, written to a database, or whatever tpmon is configured to do
 * with the monitoring data.
 *
 * @author Andre van Hoorn
 */
public interface IMonitoringReader {

	/**
	 * Initialize instance from passed initialization string which is typically
	 * a list of separated parameter/values pairs. The implementing class
	 * AbstractMonitoringLogWriter includes convenient methods to extract
	 * configuration values from an initString.
	 * 
	 * @param initString
	 *            the initialization string
	 * @return true if the initialization was successful; false if an error occurred
	 */
	public boolean init(String initString);

	/**
	 * Adds the given record receiver. This method is only used by the framework
	 * and should not be called manually to register a receiver. Use an
	 * AnalysisInstance instead.
	 * 
	 * @param receiver
	 *            the receiver
	 */
	public void addRecordReceiver(IMonitoringRecordReceiver receiver);

	/**
	 * Starts the reader. This method is intended to be a blocking operation,
	 * i.e., it is assumed that reading has finished before this method returns.
	 * The method should indicate an error by the return value false.
	 * 
	 * @return true if reading was successful; false if an error occurred
	 */
	public boolean read();
}
