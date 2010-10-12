package kieker.analysis.reader;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

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
    public JMSReader(final String jmsProviderUrl, final String jmsDestination) {
            this.initInstanceFromArgs(jmsProviderUrl, jmsDestination);  // throws IllegalArgumentException
    }

    /** Constructor for JMSReader. Requires a subsequent call to the init
     *  method in order to specify the input directory using the parameters 
     *  @a jmsServerLocation and @a jmsDestination. */
    public JMSReader(){ }


   /** Valid key/value pair: jmsProviderUrl=tcp://localhost:3035/ | jmsDestination=queue1 */
    
    @Override
	public void init(final String initString) throws IllegalArgumentException {
        final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException

        final String jmsProviderUrlP = propertyMap.getProperty("jmsProviderUrl", null);
        final String jmsDestinationP = propertyMap.getProperty("jmsDestination", null);
        this.initInstanceFromArgs(jmsProviderUrlP, jmsDestinationP); // throws IllegalArgumentException
    }

    private void initInstanceFromArgs(final String jmsProviderUrl,
            final String jmsDestination) throws IllegalArgumentException {
        if ((jmsProviderUrl == null) || jmsProviderUrl.equals("")
                || (jmsDestination == null) || jmsDestination.equals("")) {
            throw new IllegalArgumentException("JMSReader has not sufficient parameters. jmsProviderUrl or jmsDestination is null");
        }

        this.jmsProviderUrl = jmsProviderUrl;
        this.jmsDestination = jmsDestination;
    }

    /**
     * A call to this method is a blocking call.
     */
    @Override
	public boolean read() {
        boolean retVal = false;
        try {
            final Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");

            // JMS initialization
            properties.put(Context.PROVIDER_URL, this.jmsProviderUrl);
            final Context context = new InitialContext(properties);
            final ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            final Connection connection = factory.createConnection();
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final Destination destination = (Destination) context.lookup(this.jmsDestination);
            JMSReader.log.info("\n\n***\nListening to destination:" + destination + " at " + this.jmsProviderUrl + " !\n***\n\n");
            final MessageConsumer receiver = session.createConsumer(destination);
            receiver.setMessageListener(new MessageListener() {
                // the MessageListener will read onMessage each time a message comes in

                @Override
				public void onMessage(final Message jmsMessage) {
                    if (jmsMessage instanceof TextMessage) {
                        final TextMessage text = (TextMessage) jmsMessage;
                        JMSReader.log.info("Received text message: " + text);

                    } else {
                        final ObjectMessage om = (ObjectMessage) jmsMessage;
                        //System.out.println("Received object message: " + om.toString());
                        try {
                            final Serializable omo = om.getObject();
                            if (omo instanceof IMonitoringRecord) {
                                final IMonitoringRecord rec =
                                        (IMonitoringRecord) omo;
                                if (!JMSReader.this.deliverRecord(rec)){
                                    JMSReader.log.error("deliverRecord returned false");
                                    throw new MonitoringLogReaderException("deliverRecord returned false");
                                }
                            } else {
                                JMSReader.log.info("Unknown type of message " + om);
                            }
                        } catch (final MessageFormatException em) {
                            JMSReader.log.fatal("MessageFormatException:", em);
                        } catch (final JMSException ex) {
                            JMSReader.log.fatal("JMSException ", ex);
                        } catch (final MonitoringLogReaderException ex) {
                            JMSReader.log.error("LogReaderExecutionException", ex);
                        } catch (final Exception ex) {
                            JMSReader.log.error("Exception", ex);
                        }
                    }
                }
            });

            // start the connection to enable message delivery
            connection.start();

            System.out.println("JMSTestListener1 is started and waits for incomming monitoring events!");
            final Object blockingObj = new Object();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                @Override
				public void run() {
                    synchronized (blockingObj) {
                        blockingObj.notifyAll();
                    }
                }
            }));
            synchronized (blockingObj) {
                blockingObj.wait();
            }
            JMSReader.log.info("Woke up by shutdown hook");
        } catch (final Exception ex) {
            System.out.println("" + JMSReader.class.getName() + " " + ex.getMessage());
            ex.printStackTrace();
            retVal = false;
        } 
        return retVal;
    }
}
