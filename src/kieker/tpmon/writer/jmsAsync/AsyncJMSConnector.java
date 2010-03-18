package kieker.tpmon.writer.jmsAsync;

import kieker.common.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.TpmonController;
import kieker.common.monitoringRecord.KiekerDummyMonitoringRecord;
import kieker.tpmon.writer.AbstractKiekerMonitoringLogWriter;

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
/**
 *
 * @author Matthias Rohr, Andre van Hoorn
 */
public final class AsyncJMSConnector extends AbstractKiekerMonitoringLogWriter {

    private static final Log log = LogFactory.getLog(AsyncJMSConnector.class);
    private Vector<AbstractWorkerThread> typeWriterAndRecordWriters = new Vector<AbstractWorkerThread>();
    private JMSWriterThread typeWriter; // publishes record type/classname mappings
    private static final MonitoringRecordTypeClassnameMapping TYPE_WRITER_END_OF_MONITORING_MARKER = new MonitoringRecordTypeClassnameMapping(-1, null);
    private static final AbstractKiekerMonitoringRecord RECORD_WRITER_END_OF_MONITORING_MARKER = new KiekerDummyMonitoringRecord();
    private final int numberOfJmsWriters = 3; // number of jms connections -- usually one (on every node)        
    private BlockingQueue<AbstractKiekerMonitoringRecord> recordQueue = null;
    private BlockingQueue<MonitoringRecordTypeClassnameMapping> typeQueue = null;
    private String contextFactoryType; // type of the jms factory implementation, e.g.
    private String providerUrl;
    private String factoryLookupName;
    private String topic;
    private long messageTimeToLive;
    private int asyncRecordQueueSize = 8000;
    private int asyncTypeQueueSize = 20;

    /**
     * Init String. Expect key=value pairs separated by |.
     * 
     * Example initString (meaning of keys explained below):
     * jmsProviderUrl=tcp://localhost:3035/ | jmsTopic=queue1 | jmsContextFactoryType=org.exolab.jms.jndi.InitialContextFactory | jmsFactoryLookupName=ConnectionFactory | jmsMessageTimeToLive = 10000
     * 
     * jmsContextFactoryType -- type of the jms factory implementation, e.g. "org.exolab.jms.jndi.InitialContextFactory" for openjms 0.7.7
     * jmsProviderUrl -- url of the jndi provider that knows the jms service
     * jmsFactoryLookupName -- service name for the jms connection factory
     * jmsTopic -- topic at the jms server which is used in the publisher/subscribe communication
     * jmsMessageTimeToLive -- time that a jms message will kepts be alive at the jms server before it is automatically deleted
     * 
     * @param initString
     * @return true on success. false on error.
     */
    @TpmonInternal
    public boolean init(String initString) {
        if (initString == null || initString.length() == 0) {
            log.error("Invalid initString. Valid example for tpmon.properties:\n"
                    + "monitoringDataWriterInitString=jmsProviderUrl=tcp://localhost:3035/ | jmsTopic=queue1 | jmsContextFactoryType=org.exolab.jms.jndi.InitialContextFactory | jmsFactoryLookupName=ConnectionFactory | jmsMessageTimeToLive = 10000");
            return false;
        }

        boolean retVal = true;
        try {
            super.initVarsFromInitString(initString);

            this.contextFactoryType = super.getInitProperty("jmsContextFactoryType");
            this.providerUrl = super.getInitProperty("jmsProviderUrl");
            this.factoryLookupName = super.getInitProperty("jmsFactoryLookupName");
            this.topic = super.getInitProperty("jmsTopic");
            this.messageTimeToLive = Long.valueOf(super.getInitProperty("jmsMessageTimeToLive"));
            this.asyncRecordQueueSize = Integer.valueOf(super.getInitProperty("asyncRecordQueueSize"));


            this.recordQueue = new ArrayBlockingQueue<AbstractKiekerMonitoringRecord>(asyncRecordQueueSize);
            this.typeQueue = new ArrayBlockingQueue<MonitoringRecordTypeClassnameMapping>(asyncTypeQueueSize);
            // init *the* record type writer
            typeWriter = new JMSWriterThread<MonitoringRecordTypeClassnameMapping>(typeQueue, AsyncJMSConnector.TYPE_WRITER_END_OF_MONITORING_MARKER, contextFactoryType, providerUrl, factoryLookupName, topic, messageTimeToLive);
            typeWriterAndRecordWriters.add(typeWriter);
            typeWriter.setDaemon(true);
            typeWriter.start();
            // init record writers
            for (int i = 1; i <= numberOfJmsWriters; i++) {
                JMSWriterThread<AbstractKiekerMonitoringRecord> recordWriter =
                        new JMSWriterThread<AbstractKiekerMonitoringRecord>(recordQueue, AsyncJMSConnector.RECORD_WRITER_END_OF_MONITORING_MARKER, contextFactoryType, providerUrl, factoryLookupName, topic, messageTimeToLive);
                typeWriterAndRecordWriters.add(recordWriter);
                recordWriter.setDaemon(true);
                recordWriter.start();
            }
            //System.out.println(">Kieker-numberOfJmsWriters: (" + numberOfFsWriters + " threads) will write to the file system");
            log.info(">Kieker-Tpmon: (" + numberOfJmsWriters + " threads) will send to the JMS server topic");
        } catch (Exception exc) {
            log.fatal("Error initiliazing JMS Connector", exc);
            retVal = false;
        }
        return retVal;
    }

    @TpmonInternal()
    public String getInfoString() {
        StringBuilder strB = new StringBuilder();

        strB.append("contextFactoryType : " + this.contextFactoryType);
        strB.append("providerUrl : " + this.providerUrl);
        strB.append("factoryLookupName : " + this.factoryLookupName);
        strB.append("topic : " + this.topic);
        strB.append("messageTimeToLive : " + this.messageTimeToLive);

        return strB.toString();
    }

    @TpmonInternal()
    public boolean writeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
        if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncJmsProducer.insertMonitoringDataNow");
        }

        try {
            if (monitoringRecord == TpmonController.END_OF_MONITORING_MARKER) {
                // "translate" END_OF_MONITORING_MARKER
                log.info("Found END_OF_MONITORING_MARKER. Notifying type and record writers");
                this.typeQueue.add(AsyncJMSConnector.TYPE_WRITER_END_OF_MONITORING_MARKER);
                this.recordQueue.add(AsyncJMSConnector.RECORD_WRITER_END_OF_MONITORING_MARKER);
            } else {
                recordQueue.add(monitoringRecord); // tries to add immediately! -- this is for production systems
            }
            //int currentQueueSize = recordQueue.size();
        } catch (Exception ex) {
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " AsyncJmsProducer() failed: Exception:", ex);
            return false;
        }
        return true;
    }

    @TpmonInternal()
    public void registerMonitoringRecordType(int id, String className) {
        log.info("Publishing record id/class mapping: " + id + "/" + className);
        this.typeQueue.add(new MonitoringRecordTypeClassnameMapping(id, className));
    }

    @TpmonInternal()
    public void setWriteRecordTypeIds(boolean writeRecordTypeIds) {
        super.setWriteRecordTypeIds(writeRecordTypeIds);
        for (AbstractWorkerThread t : typeWriterAndRecordWriters) {
            log.info("t.setWriteRecordTypeIds(" + writeRecordTypeIds + ")");
            t.setWriteRecordTypeIds(writeRecordTypeIds);
        }
    }

    @TpmonInternal
    public Vector<AbstractWorkerThread> getWorkers() {
        return this.typeWriterAndRecordWriters;
    }
}
