package mySimpleKiekerExample.bookstoreTracing;

import java.util.Collection;

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordReceiverException;

public class MyConsumer implements IMonitoringRecordConsumerPlugin {

	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record) {
		boolean result = (record instanceof MyRecord);
		if (result) {
			MyRecord myRecord = (MyRecord) record;
			System.out.println("[Consumer] " + myRecord.getLoggingTimestamp()
					+ ": " + myRecord.component + ", " + myRecord.service
					+ ", " + myRecord.rt);
		}
		return result;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(boolean error) {
	}

}
