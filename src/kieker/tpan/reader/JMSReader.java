package kieker.tpan.reader;

import java.io.Serializable;
import java.util.Hashtable;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads tpmon messages from a (remote or local) JMS queue and processes them in tpan.
 *
 *
 * @author Andre van Hoorn, Matthias Rohr
 * History
 * 2009-07-01 (AvH) Initial version
 * 2009-07-25 (MR)
 */
public class JMSReader extends AbstractMonitoringLogReader {

    private static final Log log = LogFactory.getLog(JMSReader.class);
    private String jmsProviderUrl = null;
    private String jmsDestination = null;

    /**
     * @param jmsServerLocation = for instance "tcp://127.0.0.1:3035/"
     * @param jmsDestination = for instance "queue1"
     * @throws IllegalArgumentException if passed parameters are null or empty.
     * @return
     */
    public JMSReader(String jmsProviderUrl, String jmsDestination) {
            initInstanceFromArgs(jmsProviderUrl, jmsDestination);  // throws IllegalArgumentException
    }

    /** Constructor for JMSReader. Requires a subsequent call to the init
     *  method in order to specify the input directory using the parameters 
     *  @a jmsServerLocation and @a jmsDestination. */
    public JMSReader(){ }


   /** Valid key/value pair: jmsProviderUrl=tcp://localhost:3035/ | jmsDestination=queue1 */
    
    public void init(String initString) throws IllegalArgumentException {
        PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException

        String jmsProviderUrlP = propertyMap.getProperty("jmsProviderUrl", null);
        String jmsDestinationP = propertyMap.getProperty("jmsDestination", null);
        initInstanceFromArgs(jmsProviderUrlP, jmsDestinationP); // throws IllegalArgumentException
    }

    private void initInstanceFromArgs(final String jmsProviderUrl,
            final String jmsDestination) throws IllegalArgumentException {
        if (jmsProviderUrl == null || jmsProviderUrl.equals("")
                || jmsDestination == null || jmsDestination.equals("")) {
            throw new IllegalArgumentException("JMSReader has not sufficient parameters. jmsProviderUrl or jmsDestination is null");
        }

        this.jmsProviderUrl = jmsProviderUrl;
        this.jmsDestination = jmsDestination;
    }

    /**
     * A call to this method is a blocking call.
     */
    public boolean read() throws MonitoringLogReaderException {
        boolean retVal = false;
        try {
            Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");

            // JMS initialization
            properties.put(Context.PROVIDER_URL, jmsProviderUrl);
            Context context = new InitialContext(properties);
            ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            Connection connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) context.lookup(jmsDestination);
            log.info("\n\n***\nListening to destination:" + destination + " at " + jmsProviderUrl + " !\n***\n\n");
            MessageConsumer receiver = session.createConsumer(destination);
            receiver.setMessageListener(new MessageListener() {
                // the MessageListener will read onMessage each time a message comes in

                public void onMessage(Message jmsMessage) {
                    if (jmsMessage instanceof TextMessage) {
                        TextMessage text = (TextMessage) jmsMessage;
                        log.info("Received text message: " + text);

                    } else {
                        ObjectMessage om = (ObjectMessage) jmsMessage;
                        //System.out.println("Received object message: " + om.toString());
                        try {
                            Serializable omo = om.getObject();
                            if (omo instanceof IMonitoringRecord) {
                                IMonitoringRecord rec =
                                        (IMonitoringRecord) omo;
                                if (!deliverRecord(rec)){
                                    log.error("deliverRecord returned false");
                                    throw new MonitoringLogReaderException("deliverRecord returned false");
                                }
                            } else {
                                log.info("Unknown type of message " + om);
                            }
                        } catch (MessageFormatException em) {
                            log.fatal("MessageFormatException:", em);
                        } catch (JMSException ex) {
                            log.fatal("JMSException ", ex);
                        } catch (MonitoringLogReaderException ex) {
                            log.error("LogReaderExecutionException", ex);
                        } catch (Exception ex) {
                            log.error("Exception", ex);
                        }
                    }
                }
            });

            // start the connection to enable message delivery
            connection.start();

            System.out.println("JMSTestListener1 is started and waits for incomming monitoring events!");
            final Object blockingObj = new Object();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                public void run() {
                    synchronized (blockingObj) {
                        blockingObj.notifyAll();
                    }
                }
            }));
            synchronized (blockingObj) {
                blockingObj.wait();
            }
            log.info("Woke up by shutdown hook");
        } catch (Exception ex) {
            System.out.println("" + JMSReader.class.getName() + " " + ex.getMessage());
            ex.printStackTrace();
            retVal = false;
        } 
        return retVal;
    }
}
