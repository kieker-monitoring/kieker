/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.common.logReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractKiekerLogReader implements IKiekerLogReader {
    private static final Log log = LogFactory.getLog(AbstractKiekerLogReader.class);

    /** Contains all consumers which consume records of any type */
    private final Collection<IKiekerRecordConsumer> subscribedToAllList =
            new Vector<IKiekerRecordConsumer>();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<String, Collection<IKiekerRecordConsumer>> subscribedToTypeMap =
            new HashMap<String, Collection<IKiekerRecordConsumer>>();

    public final void addConsumer(final IKiekerRecordConsumer consumer, final String[] recordTypeSubscriptionList) {
        if (recordTypeSubscriptionList == null) {
            this.subscribedToAllList.add(consumer);
        } else {
            for (String recordTypeName : recordTypeSubscriptionList) {
                Collection<IKiekerRecordConsumer> cList = this.subscribedToTypeMap.get(recordTypeName);
                if (cList == null) {
                    cList = new Vector<IKiekerRecordConsumer>(0);
                    this.subscribedToTypeMap.put(recordTypeName, cList);
                }
                cList.add(consumer);
            }
        }
    }

    protected final void deliverRecordToConsumers(final AbstractKiekerMonitoringRecord r) throws LogReaderExecutionException {
        try {
            for (IKiekerRecordConsumer c : this.subscribedToAllList) {
                c.consumeMonitoringRecord(r);
            }
            Collection<IKiekerRecordConsumer> cList = this.subscribedToTypeMap.get(r.getClass().getName());
            if (cList != null) {
                for (IKiekerRecordConsumer c : cList) {
                    c.consumeMonitoringRecord(r);
                }
            }
        } catch (RecordConsumerExecutionException ex) {
            log.fatal("RecordConsumerExecutionException", ex);
            throw new LogReaderExecutionException("A RecordConsumerExecutionException "+
                    "was caught -- now being rethrown as LogReaderExecutionException", ex);
        }

    }

    public final void terminate() {
        for (IKiekerRecordConsumer c : this.subscribedToAllList) {
            c.terminate();
        }
        for (Collection<IKiekerRecordConsumer> cList : this.subscribedToTypeMap.values()) {
            if (cList != null) {
                for (IKiekerRecordConsumer c : cList) {
                    c.terminate();
                }
            }
        }
        synchronized (this) {
            this.notifyAll();
        }
    }

    public abstract boolean execute() throws LogReaderExecutionException;
}
