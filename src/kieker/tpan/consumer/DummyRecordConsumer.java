/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.consumer;

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;

/**
 *
 * @author matthias
 */
public class DummyRecordConsumer implements IMonitoringRecordConsumer {


    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
        System.out.println("DummyRecordConsumer consumed "+monitoringRecord);
        return true;
    }

    public boolean invoke() throws MonitoringRecordConsumerException {
        System.out.println("DummyRecordConsumer.execute()");
        return true;
    }

    public void terminate(final boolean error) {
        // nothing to do
    }

}
