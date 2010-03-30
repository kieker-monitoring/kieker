/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.consumer;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.common.record.AbstractMonitoringRecord;

/**
 *
 * @author matthias
 */
public class DummyRecordConsumer implements IKiekerRecordConsumer {


    public String[] getRecordTypeSubscriptionList() {
        return null; // null gets it all
    }

    public void consumeMonitoringRecord(AbstractMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
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
