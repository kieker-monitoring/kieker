package kieker.tpan;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.reader.IMonitoringLogReader;

import kieker.tpan.reader.LogReaderExecutionException;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * 2010-03-06 Matthias:
 *      Added exception handling for the case that the log reader is missing.
 *          (I had lot of pain because of this).
 *
 * 2010-03-04 Andre: Release of Kieker 1.1
 *
 *
 * TODOs:
 *  - In the context of realizing a event driven architecture for the model
 *    synthesis layer, it makes sense to refactor the KiekerRecordConsumers to
 *    KiekerRecordFilters. Consumers are only about how data goes in - but we
 *    also have now a concept what should happen if the data goes out: its
 *    again a publisher, to which other filters or plugins can subscribe to.
 *
 * @author Andre van Hoorn, Matthias Rohr
 */
public class TpanInstance {

    private static final Log log = LogFactory.getLog(TpanInstance.class);
    private IMonitoringLogReader logReader;
    // this are the consumers for data that are comming into kieker by readers (files or system under monitoring)
    private final Vector<IMonitoringRecordConsumer> consumers = new Vector<IMonitoringRecordConsumer>();
    /** Contains all consumers which consume records of any type */
    private final Collection<IMonitoringRecordConsumer> anyTypeConsumers =
            new Vector<IMonitoringRecordConsumer>();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<String, Collection<IMonitoringRecordConsumer>> specificTypeConsumers =
            new HashMap<String, Collection<IMonitoringRecordConsumer>>();

    public void run() throws LogReaderExecutionException, MonitoringRecordConsumerExecutionException {
        for (IMonitoringRecordConsumer c : this.consumers) {
            c.execute();
        }
        try {
            if (logReader == null) {
                log.error("Error: LogReader is missing - cannot execute run() without it!");
                throw new LogReaderExecutionException(" LogReader is missing - cannot execute run() without it!");
            } else {
                this.logReader.addRecordReceiver(new IMonitoringRecordReceiver() {

                    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
                        try {
                            deliverRecordToConsumers(monitoringRecord);
                        } catch (MonitoringRecordConsumerExecutionException ex) {
                            log.error("Caught MonitoringRecordConsumerExecutionException", ex);
                            return false;
                        }
                        return true;
                    }
                });
                if (!this.logReader.read()) {
                    log.error("Calling execute() on logReader returned false");
                    throw new LogReaderExecutionException("Calling execute() on logReader returned false");
                }
            }
        } catch (LogReaderExecutionException exc) {
            log.fatal("LogReaderException! Will terminate consumers.");
            for (IMonitoringRecordConsumer c : this.consumers) {
                c.terminate(true); // terminate due to an error
            }
            throw exc;
        }
        for (IMonitoringRecordConsumer c : this.consumers) {
            log.info("Terminating consumer " + c);
            c.terminate(false); // terminate after successful execution
        }
    }

    public void setLogReader(IMonitoringLogReader reader) {
        this.logReader = reader;
    }

    public void addRecordConsumer(IMonitoringRecordConsumer consumer) {
        this.consumers.add(consumer);
        final String[] recordTypeSubscriptionList = consumer.getRecordTypeSubscriptionList();
        if (recordTypeSubscriptionList == null) {
            this.anyTypeConsumers.add(consumer);
        } else {
            for (String recordTypeName : recordTypeSubscriptionList) {
                Collection<IMonitoringRecordConsumer> cList = this.specificTypeConsumers.get(recordTypeName);
                if (cList == null) {
                    cList = new Vector<IMonitoringRecordConsumer>(0);
                    this.specificTypeConsumers.put(recordTypeName, cList);
                }
                cList.add(consumer);
            }
        }
    }

    /**
     * Delivers the given record to the consumers that are registered for this
     * type of records.
     *
     * @param monitoringRecord the record
     * @throws LogReaderExecutionException if an error occurs
     */
    private final void deliverRecordToConsumers(final IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException {
            for (IMonitoringRecordConsumer c : this.anyTypeConsumers) {
                c.consumeMonitoringRecord(monitoringRecord);
            }
            Collection<IMonitoringRecordConsumer> cList = this.specificTypeConsumers.get(monitoringRecord.getClass().getName());
            if (cList != null) {
                for (IMonitoringRecordConsumer c : cList) {
                    c.consumeMonitoringRecord(monitoringRecord);
                }
            }
    }
}
