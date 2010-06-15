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
	public boolean newMonitoringRecord(IMonitoringRecord arg0)
			throws MonitoringRecordReceiverException {
		System.out.println(arg0.toString());
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(boolean error) {
	}

}
