/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.common.logReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractLogReader implements ILogReader {

    /** Contains all consumers which consume records of any type */
    private final Collection<IMonitoringRecordConsumer> subscribedToAllList =
            new Vector();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<String,Collection<IMonitoringRecordConsumer>> subscriptionMap =
            new HashMap<String,Collection<IMonitoringRecordConsumer>>();

    public final void addConsumer(IMonitoringRecordConsumer consumer, String[] recordTypeSubscriptionList) {
        if (recordTypeSubscriptionList == null) {
            this.subscribedToAllList.add(consumer);
        }else{
            for (String recordTypeName : recordTypeSubscriptionList){
                Collection<IMonitoringRecordConsumer> cList = this.subscriptionMap.get(recordTypeName);
                if (cList == null) {
                    cList = new Vector<IMonitoringRecordConsumer>(0);
                    this.subscriptionMap.put(recordTypeName, cList);
                }
                cList.add(consumer);
            }
        }
    }

    protected final void deliverRecordToConsumers (AbstractKiekerMonitoringRecord r){
        for (IMonitoringRecordConsumer c : this.subscribedToAllList){
            c.consumeMonitoringRecord(r);
        }
        Collection<IMonitoringRecordConsumer> cList = this.subscriptionMap.get(r.getClass().getName());
        if (cList != null){
            for (IMonitoringRecordConsumer c : cList) {
                c.consumeMonitoringRecord(r);
            }
        }
    }

    public abstract void run();
}
