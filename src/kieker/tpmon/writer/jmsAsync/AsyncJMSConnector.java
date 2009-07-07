/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon.writer.jmsAsync;

import java.util.HashMap;
import java.util.StringTokenizer;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.writer.AbstractMonitoringDataWriter;

/**
 *
 * @author Matthias Rohr, Andre van Hoorn
 */
public class AsyncJMSConnector extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(AsyncJMSConnector.class);
    private Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();
    private final int numberOfJmsWriters = 3; // number of jms connections -- usually one (on every node)        
    private BlockingQueue<AbstractKiekerMonitoringRecord> blockingQueue = null;
    private String contextFactoryType; // type of the jms factory implementation, e.g.
    private String providerUrl;
    private String factoryLookupName;
    private String topic;
    private long messageTimeToLive;
    private boolean writeRecordTypeIds;

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
            log.error("Invalid initString. Valid example for tpmon.properties:\n" +
                    "monitoringDataWriterInitString=jmsProviderUrl=tcp://localhost:3035/ | jmsTopic=queue1 | jmsContextFactoryType=org.exolab.jms.jndi.InitialContextFactory | jmsFactoryLookupName=ConnectionFactory | jmsMessageTimeToLive = 10000");
            return false;
        }

        if (!this.initVarsFromInitString(initString)) {
            log.error("init failed");
            return false;
        }
        log.info("XX Init successful!");

        this.blockingQueue = new ArrayBlockingQueue<AbstractKiekerMonitoringRecord>(8000);
        for (int i = 0; i < numberOfJmsWriters; i++) {
            JMSWriterThread dbw = new JMSWriterThread(blockingQueue, contextFactoryType, providerUrl, factoryLookupName, topic, messageTimeToLive);
            workers.add(dbw);
            dbw.start();
        }
        //System.out.println(">Kieker-numberOfJmsWriters: (" + numberOfFsWriters + " threads) will write to the file system");
        log.info(">Kieker-Tpmon: (" + numberOfJmsWriters + " threads) will send to the JMS server topic");
        return true;
    }

    @TpmonInternal
    private boolean initVarsFromInitString(String initString) {
        HashMap<String, String> map = new HashMap<String, String>();
        StringTokenizer keyValListTokens = new StringTokenizer(initString, "|");
        while (keyValListTokens.hasMoreTokens()) {
            String curKeyValToken = keyValListTokens.nextToken().trim();
            StringTokenizer keyValTokens = new StringTokenizer(curKeyValToken, "=");
            if (keyValTokens.countTokens() != 2) {
                log.error("Expected key=value pair, found " + curKeyValToken);
                return false;
            }
            String key = keyValTokens.nextToken().trim();
            String val = keyValTokens.nextToken().trim();
            log.info("Found key/value pair: " + key + "=" + val);
            map.put(key, val);
        }

        this.contextFactoryType = map.get("jmsContextFactoryType");
        this.providerUrl = map.get("jmsProviderUrl");
        this.factoryLookupName = map.get("jmsFactoryLookupName");
        this.topic = map.get("jmsTopic");
        this.messageTimeToLive = Long.valueOf(map.get("jmsMessageTimeToLive"));

        return true;
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
            blockingQueue.add(monitoringRecord); // tries to add immediately! -- this is for production systems
        //int currentQueueSize = blockingQueue.size();
        } catch (Exception ex) {
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " AsyncJmsProducer() failed: Exception:", ex);
            return false;
        }
        return true;
    }

   @TpmonInternal()
    public void registerMonitoringRecordType(int id, String className) {
        log.warn("I am just predending to register classname "+className+" with id " + id);
    }

   @TpmonInternal()
    public boolean isWriteRecordTypeIds() {
        return this.writeRecordTypeIds;
    }

    @TpmonInternal()
    public void setWriteRecordTypeIds(boolean writeRecordTypeIds) {
        for (AbstractWorkerThread t : workers) {
            log.info("t.setWriteRecordTypeIds(" + writeRecordTypeIds + ")");
            t.setWriteRecordTypeIds(writeRecordTypeIds);
        }
        this.writeRecordTypeIds = writeRecordTypeIds;
    }

    public Vector<AbstractWorkerThread> getWorkers() {
        return this.workers;
    }
}
