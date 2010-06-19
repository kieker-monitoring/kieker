package mySimpleKiekerExample.bookstoreTracing;

import java.util.Collection;

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.common.record.IMonitoringRecord;

public class MyConsumer implements IMonitoringRecordConsumerPlugin {

	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record) {
		/* Has the record the correct form? */
		boolean result = (record instanceof MyRecord);
		if (result) {
			/* Seems like yes. Write the content to the default output. */
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
