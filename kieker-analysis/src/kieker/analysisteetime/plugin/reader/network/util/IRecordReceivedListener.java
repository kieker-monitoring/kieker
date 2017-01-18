package kieker.analysisteetime.plugin.reader.network.util;

import kieker.common.record.IMonitoringRecord;

public interface IRecordReceivedListener {

	void onRecordReceived(IMonitoringRecord record);

}
