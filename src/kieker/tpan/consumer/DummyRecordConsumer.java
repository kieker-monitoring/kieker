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
public class DummyRecordConsumer implements IRecordConsumer {


    public String[] getRecordTypeSubscriptionList() {
        return null; // null gets it all
    }

    public void consumeMonitoringRecord(IMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
        System.out.println("DummyRecordConsumer consumed "+monitoringRecord);
    }

    public boolean execute() throws RecordConsumerExecutionException {
        System.out.println("DummyRecordConsumer.execute()");
        return true;
    }

    public void terminate() {
        // nothing to do
    }

}
