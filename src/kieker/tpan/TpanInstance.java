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
import kieker.tpan.plugins.IAnalysisPlugin;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.tpan.consumer.IMonitoringRecordConsumerPlugin;
import kieker.tpan.reader.IMonitoringLogReader;

import kieker.tpan.reader.MonitoringLogReaderException;
import kieker.tpan.consumer.MonitoringRecordConsumerException;
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
    /** this are the consumers for data that are comming into kieker by readers (files or system under monitoring)*/
    private final Vector<IMonitoringRecordConsumerPlugin> consumers = new Vector<IMonitoringRecordConsumerPlugin>();
    /** Contains all consumers which consume records of any type */
    private final Collection<IMonitoringRecordConsumerPlugin> anyTypeConsumers =
            new Vector<IMonitoringRecordConsumerPlugin>();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<Class<? extends IMonitoringRecord>, Collection<IMonitoringRecordConsumerPlugin>> specificTypeConsumers =
            new HashMap<Class<? extends IMonitoringRecord>, Collection<IMonitoringRecordConsumerPlugin>>();
    private final Collection<IAnalysisPlugin> controlledComponents =
            new Vector<IAnalysisPlugin>();

    public void run() throws MonitoringLogReaderException, MonitoringRecordConsumerException {
        for (IAnalysisPlugin c : this.controlledComponents) {
            c.execute();
        }
        try {
            if (logReader == null) {
                log.error("Error: LogReader is missing - cannot execute run() without it!");
                throw new MonitoringLogReaderException(" LogReader is missing - cannot execute run() without it!");
            } else {
                this.logReader.addRecordReceiver(new IMonitoringRecordReceiver() {

                    public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
                        try {
                            deliverRecordToConsumers(monitoringRecord);
                        } catch (MonitoringRecordReceiverException ex) {
                            log.error("Caught MonitoringRecordConsumerExecutionException", ex);
                            return false;
                        }
                        return true;
                    }
                });
                if (!this.logReader.read()) {
                    log.error("Calling execute() on logReader returned false");
                    throw new MonitoringLogReaderException("Calling execute() on logReader returned false");
                }
            }
        } catch (MonitoringLogReaderException exc) {
            log.fatal("LogReaderException! Will terminate consumers.");
            for (IAnalysisPlugin c : this.controlledComponents) {
                c.terminate(true); // terminate due to an error
            }
            throw exc;
        }
        for (IAnalysisPlugin c : this.controlledComponents) {
            c.terminate(false); // terminate due to an error
        }
    }

    public void setLogReader(IMonitoringLogReader reader) {
        this.logReader = reader;
    }

    private void addRecordConsumer(IMonitoringRecordConsumerPlugin consumer) {
        this.consumers.add(consumer);
        final Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList = consumer.getRecordTypeSubscriptionList();
        if (recordTypeSubscriptionList == null) {
            this.anyTypeConsumers.add(consumer);
        } else {
            for (Class<? extends IMonitoringRecord> recordType : recordTypeSubscriptionList) {
                Collection<IMonitoringRecordConsumerPlugin> cList = this.specificTypeConsumers.get(recordType);
                if (cList == null) {
                    cList = new Vector<IMonitoringRecordConsumerPlugin>(0);
                    this.specificTypeConsumers.put(recordType, cList);
                }
                cList.add(consumer);
            }
        }
    }

    /**
     * Registers the passed plugin <i>c<i>. If <i>c</i> is an
     * instance of the interface <i>IMonitoringRecordConsumerPlugin</i>
     * it is also registered as a record consumer.
     */
    public void registerPlugin(IAnalysisPlugin c) {
        this.controlledComponents.add(c);
        log.info("Registered plugin " + c);

        if (c instanceof IMonitoringRecordConsumerPlugin){
            log.info("Plugin " + c + " also registered as record consumer");
            this.addRecordConsumer((IMonitoringRecordConsumerPlugin)c);
        }
    }

    /**
     * Delivers the given record to the consumers that are registered for this
     * type of records.
     *
     * @param monitoringRecord the record
     * @throws LogReaderExecutionException if an error occurs
     */
    private final void deliverRecordToConsumers(final IMonitoringRecord monitoringRecord) throws MonitoringRecordReceiverException {

        for (IMonitoringRecordConsumerPlugin c : this.anyTypeConsumers) {
            c.newMonitoringRecord(monitoringRecord);
        }
        Collection<IMonitoringRecordConsumerPlugin> cList = this.specificTypeConsumers.get(monitoringRecord.getClass());
        if (cList != null) {
            for (IMonitoringRecordConsumerPlugin c : cList) {
                c.newMonitoringRecord(monitoringRecord);
            }
        }
    }
}
