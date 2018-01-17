package kieker.analysisteetime.plugin.reader;

import kieker.common.record.IMonitoringRecord;

public interface IRecordReceivedListener {

	void onRecordReceived(IMonitoringRecord record);

}
