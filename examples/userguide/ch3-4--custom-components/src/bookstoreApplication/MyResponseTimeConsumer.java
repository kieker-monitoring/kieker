package bookstoreApplication;

import java.util.Collection;

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.common.record.IMonitoringRecord;

public class MyResponseTimeConsumer implements IMonitoringRecordConsumerPlugin {

    @Override
    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null;
    }

    @Override
    public boolean newMonitoringRecord(IMonitoringRecord record) {
        if (record instanceof MyResponseTimeRecord) {
            /* Write the content to the standard output stream. */
            MyResponseTimeRecord myRecord = (MyResponseTimeRecord) record;
            System.out.println("[Consumer] " + myRecord.getLoggingTimestamp()
                    + ": " + myRecord.className + ", " + myRecord.methodName
                    + ", " + myRecord.responseTimeNanos);
        }
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
