package kieker.analysis.plugin;

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;

/**
 *
 * @author matthias
 */
public class DummyRecordConsumer implements IMonitoringRecordConsumerPlugin {


    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
        System.out.println("DummyRecordConsumer consumed "+monitoringRecord);
        return true;
    }

    public boolean execute() {
        System.out.println("DummyRecordConsumer.execute()");
        return true;
    }

    public void terminate(final boolean error) {
        // nothing to do
    }

}
