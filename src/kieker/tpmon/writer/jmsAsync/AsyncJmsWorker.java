package kieker.tpmon.writer.jmsAsync;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.naming.NamingException;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;

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
public class AsyncJmsWorker extends AbstractWorkerThread {
    // configuration parameters                

    private Session session;
    private Connection connection;
    private MessageProducer sender;
    private static final Log log = LogFactory.getLog(AsyncJmsWorker.class);
    private BlockingQueue<AbstractKiekerMonitoringRecord> writeQueue = null;
    private boolean finished = false;
    private static boolean shutdown = false;

    public AsyncJmsWorker(BlockingQueue<AbstractKiekerMonitoringRecord> writeQueue, String contextFactoryType, String providerUrl, String factoryLookupName, String topic, long messageTimeToLive) {
        System.out.printf("JMS connect with factorytype:%s, providerurl: %s, factorylookupname: %s, topic: %s \n", contextFactoryType, providerUrl, factoryLookupName, topic);
        System.out.println("JMS connect with messagetimetolive:" + messageTimeToLive);
        this.writeQueue = writeQueue;

        try {
            Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryType);
            properties.put(Context.PROVIDER_URL, providerUrl);

            Context context = new InitialContext(properties);
            ConnectionFactory factory = (ConnectionFactory) context.lookup(factoryLookupName);
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("Looking for topic " + topic);
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
    private void publish(AbstractKiekerMonitoringRecord execData) {
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
        publish((AbstractKiekerMonitoringRecord) traceidObject);
    }

    @TpmonInternal
    public void run() {
        log.info("JmsWriter thread running");
        //System.out.println("FsWriter thread running");
        try {
            while (!finished) {
                AbstractKiekerMonitoringRecord monitoringRecord = writeQueue.take();
                if (monitoringRecord == TpmonController.END_OF_MONITORING_MARKER) {
                    log.info("Found END_OF_MONITORING_MARKER. Will terminate");
                    // need to put the marker back into the queue to notify other threads
                    writeQueue.add(TpmonController.END_OF_MONITORING_MARKER);
                    finished = true;
                    break;
                }
                if (monitoringRecord != null) {
                    consume(monitoringRecord);
                //System.out.println("FSW "+writeQueue.size());
                } else {
                    // timeout ... 
                    if (shutdown && writeQueue.isEmpty()) {
                        finished = true;
                    }
                }
            }
            log.info("JMS writer finished");
        } catch (Exception ex) {
            // e.g. Interrupted Exception or IOException
            log.error("JMX writer will halt", ex);
        } finally {
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
            } catch (Exception e) {
            }
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
