/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.consumer;

import kieker.common.record.IMonitoringRecord;

/**
 *
 * @author matthias
 */
public class DummyRecordConsumer implements IMonitoringRecordConsumer {


    public Class<? extends IMonitoringRecord>[] getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public void consumeMonitoringRecord(IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException {
        System.out.println("DummyRecordConsumer consumed "+monitoringRecord);
    }

    public boolean execute() throws MonitoringRecordConsumerExecutionException {
        System.out.println("DummyRecordConsumer.execute()");
        return true;
    }

    public void terminate(final boolean error) {
        // nothing to do
    }

}
