package kieker.tpmon.asyncJmsWriter;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.jms.*;
import javax.lang.model.type.ExecutableType;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.naming.NamingException;
import kieker.tpmon.ExecutionData;
import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.asyncDbconnector.Worker;

/**
 * The writer moves monitoring data via messaging to a JMS server. This uses the
 * publish/subscribe pattern. The JMS server will only keep each monitoring event
 * for messageTimeToLive milliseconds presents before it is deleted. 
 * 
 * At the moment, JmsWorkers do not share any connections, 
 * sessions or other objects.
 * 
 * History:
 * 2008-09-13: Initial prototype
 * 
 * @author matthias
 */
public class AsyncJmsWorker implements Runnable, Worker {
    // configuration parameters                
    private Session session;
    private Connection connection;
    private MessageProducer sender;
    private static final Log log = LogFactory.getLog(AsyncJmsWorker.class);
    private static final long pollingIntervallInMillisecs = 400L;    
    private BlockingQueue writeQueue = null;    
    private ExecutableType execData = null;
    private boolean finished = false;
    private static boolean shutdown = false;
    
    
    public AsyncJmsWorker(BlockingQueue writeQueue, String contextFactoryType, String providerUrl, String factoryLookupName, String topic, long messageTimeToLive) {
        System.out.printf("JMS connect with factorytype:%s, providerurl: %s, factorylookupname: %s, topic: %s \n",contextFactoryType,providerUrl,factoryLookupName,topic);
        System.out.println("JMS connect with messagetimetolive:"+messageTimeToLive);
        this.writeQueue = writeQueue;
        
        try {        
            Hashtable<String,String> properties = new Hashtable<String,String>();    
            properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryType);
            properties.put(Context.PROVIDER_URL, providerUrl);
     
            Context context = new InitialContext(properties);
            ConnectionFactory factory = (ConnectionFactory) context.lookup(factoryLookupName);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Looking for topic "+topic);
            Destination destination = (Destination) context.lookup(topic);
            connection.start();

            sender = session.createProducer(destination);
            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            sender.setDisableMessageID(false);
            sender.setTimeToLive(messageTimeToLive); // time to live defaults in millisecs
        } catch (JMSException ex) {
            log.error(Level.SEVERE, ex);
        } catch (NamingException ex) {
            log.error(Level.SEVERE, ex);
        }
    }
    
    @TpmonInternal
    private void publish(ExecutionData execData) {
        try {           
            ObjectMessage messageObject = session.createObjectMessage(execData);           
            //TextMessage message = session.createTextMessage("Hello World!");
            sender.send(messageObject);                        
            //System.out.println("sended execution "+insertData.opname+" "+insertData.tin);
            
//            Object so = messageObject.getObject();
//            InsertData id2 = (InsertData) so;
//            System.out.println("id2 "+id2.opname);
            
        } catch (JMSException ex) {
            log.error(Level.SEVERE, ex);
        }
    }

    @TpmonInternal
    private void consume(Object traceidObject) throws Exception {
        publish((ExecutionData)traceidObject);        
    }
    
    @TpmonInternal
    public void run() {
        log.info("JmsWriter thread running");
        //System.out.println("FsWriter thread running");
        try {
            while (!finished) {
                Object data = writeQueue.poll(pollingIntervallInMillisecs, TimeUnit.MILLISECONDS);
                if (data != null) {
                    consume(data);
                //System.out.println("FSW "+writeQueue.size());
                } else {
                    // timeout ... 
                    if (shutdown && writeQueue.isEmpty()) {
                        finished = true;
                    }
                }
            }
            log.info("FsWriter finished");
        } catch (Exception ex) {
            // e.g. Interrupted Exception or IOException
            log.error("FS Writer will halt", ex);
        } finally{
            this.finished = true;
        }
    }        

    @TpmonInternal
    public boolean isFinished() {
        if (finished) {
            try {
            sender.close();
            session.close();
            connection.close();
            } catch (Exception e) {}
        }
        return finished;
    }

    /**
     * Initials the shutdown
     */
    @TpmonInternal
    public void initShutdown() {
        //the variable might be set to false more often than required
        AsyncJmsWorker.shutdown = true;
    }
}
