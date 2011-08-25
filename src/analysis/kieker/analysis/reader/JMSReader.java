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
import javax.naming.NameNotFoundException;

import kieker.analysis.util.PropertyMap;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads monitoring records from a (remote or local) JMS queue.
 * 
 * 
 * @author Andre van Hoorn, Matthias Rohr History 2009-07-01 (AvH) Initial
 *         version 2009-07-25 (MR)
 */
public class JMSReader extends AbstractMonitoringReader {

	private static final Log log = LogFactory.getLog(JMSReader.class);
	private String jmsProviderUrl = null;
	private String jmsDestination = null;
	private String jmsFactoryLookupName = null;

	/**
	 * @param jmsProviderUrl
	 *            = for instance "tcp://127.0.0.1:3035/"
	 * @param jmsDestination
	 *            = for instance "queue1"
	 * @param jmsFactoryLookupName
	 * 			 = for instance "org.exolab.jms.jndi.InitialContextFactory" (OpenJMS)
	 * @throws IllegalArgumentException
	 *             if passed parameters are null or empty.
	 */
	public JMSReader(final String jmsProviderUrl, final String jmsDestination,
			final String jmsFactoryLookupName) {
		this.initInstanceFromArgs(jmsProviderUrl, jmsDestination,
				jmsFactoryLookupName); // throws
		// IllegalArgumentException
	}

	/**
	 * Constructor for JMSReader. Requires a subsequent call to the init method.
	 */
	public JMSReader() {
	}

	/**
	 * Valid key/value pair: jmsProviderUrl=tcp://localhost:3035/ |
	 * jmsDestination=queue1 | jmsFactoryLookupName=org.exolab.jms.jndi.InitialContextFactory
	 */

	@Override
	public boolean init(final String initString) {
		try {
			final PropertyMap propertyMap =
					new PropertyMap(initString, "|", "="); // throws
															// IllegalArgumentException

			final String jmsProviderUrlP =
					propertyMap.getProperty("jmsProviderUrl", null);
			final String jmsDestinationP =
					propertyMap.getProperty("jmsDestination", null);
			final String jmsFactoryLookupNameP =
				propertyMap.getProperty("jmsFactoryLookupName", null);
			this.initInstanceFromArgs(jmsProviderUrlP, jmsDestinationP,
					jmsFactoryLookupNameP); // throws
			// IllegalArgumentException
		} catch (final Exception exc) {
			JMSReader.log.error("Failed to parse initString '" + initString
					+ "': " + exc.getMessage());
			return false;
		}
		return true;
	}

	private void initInstanceFromArgs(final String jmsProviderUrl,
			final String jmsDestination, final String factoryLookupName)
			throws IllegalArgumentException {
		if ((jmsProviderUrl == null) || jmsProviderUrl.equals("")
				|| (jmsDestination == null) || jmsDestination.equals("")
				|| (factoryLookupName == null)
				|| (factoryLookupName.equals(""))) {
			throw new IllegalArgumentException(
					"JMSReader has not sufficient parameters. jmsProviderUrl ('"
							+ jmsProviderUrl + "'), jmsDestination ('"
							+ jmsDestination + "'), or factoryLookupName ('"
							+ factoryLookupName + "') is null");
		}

		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = factoryLookupName;
	}

	/**
	 * A call to this method is a blocking call.
	 */
	@Override
	public boolean read() {
		boolean retVal = false;
		try {
			final Hashtable<String, String> properties =
					new Hashtable<String, String>();
			properties.put(Context.INITIAL_CONTEXT_FACTORY,
					this.jmsFactoryLookupName);

			// JMS initialization
			properties.put(Context.PROVIDER_URL, this.jmsProviderUrl);
			final Context context = new InitialContext(properties);
			final ConnectionFactory factory =
					(ConnectionFactory) context.lookup("ConnectionFactory");
			final Connection connection = factory.createConnection();
			final Session session =
					connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination;
			try {
				/*
				 * As a first step, try a JNDI lookup (this seems to fail with
				 * ActiveMQ sometimes)
				 */
				destination = (Destination) context.lookup(this.jmsDestination);
			} catch (final NameNotFoundException exc) {
				/*
				 * JNDI lookup failed, try manual creation (this seems to fail
				 * with ActiveMQ sometimes)
				 */
				JMSReader.log.warn("Failed to lookup queue '" + this.jmsDestination
						+ "' via JNDI: " + exc.getMessage());
				JMSReader.log.info("Attempting to create queue ...");
				destination = session.createQueue(this.jmsDestination);
				// Is the following required?
				//context.bind(this.jmsDestination, destination);
			}

			JMSReader.log.info("Listening to destination:"
					+ destination + " at " + this.jmsProviderUrl
					+ " !\n***\n\n");
			final MessageConsumer receiver =
					session.createConsumer(destination);
			receiver.setMessageListener(new MessageListener() {
				// the MessageListener will read onMessage each time a message
				// comes in

				@Override
				public void onMessage(final Message jmsMessage) {
					if (jmsMessage instanceof TextMessage) {
						final TextMessage text = (TextMessage) jmsMessage;
						JMSReader.log.info("Received text message: " + text);

					} else {
						try {
							final ObjectMessage om = (ObjectMessage) jmsMessage;
							// System.out.println("Received object message: " +
							// om.toString());
							final Serializable omo = om.getObject();
							if (omo instanceof IMonitoringRecord) {
								final IMonitoringRecord rec =
										(IMonitoringRecord) omo;
								if (!JMSReader.this.deliverRecord(rec)) {
									JMSReader.log
											.error("deliverRecord returned false");
									throw new MonitoringReaderException(
											"deliverRecord returned false");
								}
							} else {
								JMSReader.log.info("Unknown type of message "
										+ om);
							}
						} catch (final MessageFormatException em) {
							JMSReader.log.fatal(
									"MessageFormatException:" + em.getMessage(),
									em);
						} catch (final JMSException ex) {
							JMSReader.log.fatal(
									"JMSException: " + ex.getMessage(), ex);
						} catch (final MonitoringReaderException ex) {
							JMSReader.log.error("LogReaderExecutionException: "
									+ ex.getMessage(), ex);
						} catch (final Exception ex) {
							JMSReader.log.error("Exception", ex);
						}
					}
				}
			});

			// start the connection to enable message delivery
			connection.start();

			JMSReader.log.info("JMSReader started and waits for incomming monitoring events!");
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
			JMSReader.log.fatal(ex.getMessage(), ex);
			retVal = false;
		}
		return retVal;
	}
}
