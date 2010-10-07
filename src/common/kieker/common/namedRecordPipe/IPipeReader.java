package kieker.common.namedRecordPipe;

import kieker.common.record.IMonitoringRecordReceiver;

/**
 *
 * @author Andre van Hoorn
 */
public interface IPipeReader extends IMonitoringRecordReceiver {
	/**
	 * Called to notify the reader that the pipe is closed. 
	 */
	public void notifyPipeClosed();
}
