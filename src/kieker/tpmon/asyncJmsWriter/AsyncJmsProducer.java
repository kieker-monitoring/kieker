/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.asyncJmsWriter;

import kieker.tpmon.AbstractMonitoringDataWriter;
import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.asyncDbconnector.InsertData;
import kieker.tpmon.asyncDbconnector.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author matthias
 */
public class AsyncJmsProducer  extends AbstractMonitoringDataWriter {
    private static final Log log = LogFactory.getLog(AsyncJmsProducer.class);        
    private Vector<Worker> workers = new Vector<Worker>();
    private final int numberOfJmsWriters = 3; // number of jms connections -- usually one (on every node)        
    private BlockingQueue<InsertData> blockingQueue = null;    
    
    private String contextFactoryType; // type of the jms factory implementation, e.g.
    private String providerUrl; 
    private String factoryLookupName; 
    private String topic;
    private long messageTimeToLive;
    
    
    private final String NOSESSIONID = "nosession";

    public Vector<Worker> getWorkers() {
        return workers;
    }
    /**
     * Use this method to insert data into the database.
     */
    @TpmonInternal
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String traceid, long tin, long tout, int executionOrderIndex, int executionStackSize) {
        return this.insertMonitoringDataNow(experimentId, vmName, opname, NOSESSIONID, traceid, tin, tout, executionOrderIndex, executionStackSize);
    }
    
    @TpmonInternal
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String sessionID, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize) {
         if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncJmsProducer.insertMonitoringDataNow");
        }

        try {
            InsertData id = new InsertData(experimentId, vmName, opname, sessionID, requestID, tin, tout, executionOrderIndex, executionStackSize);
               
            blockingQueue.add(id); // tries to add immediately! -- this is for production systems            
            
            //int currentQueueSize = blockingQueue.size();
        } catch (Exception ex) {            
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " AsyncJmsProducer() failed: Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @param contextFactoryType -- type of the jms factory implementation, e.g. "org.exolab.jms.jndi.InitialContextFactory" for openjms 0.7.7
     * @param providerUrl -- url of the jndi provider that knows the jms service
     * @param factoryLookupName -- service name for the jms connection factory
     * @param topic -- topic at the jms server which is used in the publisher/subscribe communication
     * @param messageTimeToLive -- time that a jms message will kepts be alive at the jms server before it is automatically deleted
     */
    public AsyncJmsProducer(String contextFactoryType, String providerUrl, String factoryLookupName, String topic, long messageTimeToLive) {        
        this.contextFactoryType = contextFactoryType;
        this.providerUrl = providerUrl;
        this.factoryLookupName = factoryLookupName;
        this.topic = topic;
        this.messageTimeToLive = messageTimeToLive;
        this.init();
    }        
    
    @TpmonInternal
    public void init() {
        blockingQueue = new ArrayBlockingQueue<InsertData>(8000);
        for (int i = 0; i < numberOfJmsWriters; i++) {
            AsyncJmsWorker dbw = new AsyncJmsWorker(blockingQueue, contextFactoryType, providerUrl, factoryLookupName, topic, messageTimeToLive);                       
            new Thread(dbw).start();                       
            workers.add(dbw);
        }
        //System.out.println(">Kieker-numberOfJmsWriters: (" + numberOfFsWriters + " threads) will write to the file system");
        log.info(">Kieker-Tpmon: (" + numberOfJmsWriters + " threads) will send to the JMS server topic");
    }

}
