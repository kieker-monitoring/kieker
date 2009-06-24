package kieker.loganalysis.logReader;

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
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JMSReader extends AbstractLogReader {

    private static final Log log = LogFactory.getLog(JMSReader.class);

    public void run() {
        try {
            Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
            //properties.put(Context.PROVIDER_URL, "tcp://pc-rohr.informatik.uni-oldenburg.de:3035/");
            String location = "tcp://127.0.0.1:3035/";
            properties.put(Context.PROVIDER_URL, location);
            Context context = new InitialContext(properties);
            ConnectionFactory factory =
                    (ConnectionFactory) context.lookup("ConnectionFactory");
            Connection connection = factory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            String destinationName = "queue1";
            Destination destination = (Destination) context.lookup(destinationName);
            log.info("\n\n***\nListening to destination:"+destinationName+" over "+location+" !\n***\n\n");
            MessageConsumer receiver = session.createConsumer(destination);
            receiver.setMessageListener(new MessageListener() {

                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        TextMessage text = (TextMessage) message;
                        System.out.println("Received text message: " + text);
                    } else {
                        ObjectMessage om = (ObjectMessage) message;
                        //System.out.println("Received object message: " + om.toString());
                        try {
                            AbstractKiekerMonitoringRecord id = (AbstractKiekerMonitoringRecord) om.getObject();
                            log.info("New monitoring record of type '" + id.getRecordTypeId() + "'");
                        } catch (MessageFormatException em) {
                            log.fatal("MessageFormatException:", em);
                            em.printStackTrace();
                        } catch (JMSException ex) {
                            log.fatal("JMSException ", ex);
                            ex.printStackTrace();
                        }
                    }
                }
            });

            // start the connection to enable message delivery
            connection.start();

            System.out.println("JMSTestListener1 is started and waits for incomming monitoring events!");
            while (true) {
                Thread.sleep(10);
            }
        } catch (Exception ex) {
            System.out.println("" + JMSReader.class.getName() + " " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
