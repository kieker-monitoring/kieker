package kieker.monitoring.writer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 */
public interface IMonitoringWriter extends IMonitoringRecordReceiver {

	@Override
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	/**
	 * Called by the Monitoring Controller to announce a shutdown of monitoring.
	 * Writers should return as soon as it is safe to terminate Kieker.
	 */
	public abstract void terminate();

	/**
	 * Set the <code>IMonitoringController</code> controlling this writer.
	 * 
	 * @param monitoringController
	 * @throws Exception
	 */
	public abstract void setController(final IMonitoringController monitoringController) throws Exception;

	@Override
	public abstract String toString();
}
