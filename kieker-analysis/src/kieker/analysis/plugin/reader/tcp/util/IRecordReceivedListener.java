package kieker.analysis.plugin.reader.tcp.util;

import kieker.common.record.IMonitoringRecord;

public interface IRecordReceivedListener {

	void onRecordReceived(IMonitoringRecord record);

}
