package kieker.monitoring.writer.jms;

import java.util.concurrent.BlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public final class AsyncJMSWriter extends AbstractAsyncWriter {
	private static final String PREFIX = AsyncJMSWriter.class.getName() + ".";
	public static final String CONFIG__PROVIDERURL = AsyncJMSWriter.PREFIX + "ProviderUrl";
	public static final String CONFIG__TOPIC = AsyncJMSWriter.PREFIX + "Topic";
	public static final String CONFIG__CONTEXTFACTORYTYPE = AsyncJMSWriter.PREFIX + "ContextFactoryType";
	public static final String CONFIG__FACTORYLOOKUPNAME = AsyncJMSWriter.PREFIX + "FactoryLookupName";
	public static final String CONFIG__MESSAGETTL = AsyncJMSWriter.PREFIX + "MessageTimeToLive";

	public AsyncJMSWriter(final IMonitoringController monitoringController, final Configuration configuration) throws NamingException, JMSException {
		super(configuration);
		this.addWorker(new JMSWriterThread(monitoringController, this.blockingQueue, this.configuration.getStringProperty(AsyncJMSWriter.CONFIG__CONTEXTFACTORYTYPE),
				this.configuration.getStringProperty(AsyncJMSWriter.CONFIG__PROVIDERURL), this.configuration.getStringProperty(AsyncJMSWriter.CONFIG__FACTORYLOOKUPNAME),
				this.configuration.getStringProperty(AsyncJMSWriter.CONFIG__TOPIC), this.configuration.getLongProperty(AsyncJMSWriter.CONFIG__MESSAGETTL)));
	}

	@Override
	protected void init() {
	}
}

/**
 * The writer moves monitoring data via messaging to a JMS server. This uses the
 * publishRecord/subscribe pattern. The JMS server will only keep each
 * monitoring event for messageTimeToLive milliseconds presents before it is
 * deleted.
 * 
 * At the moment, JmsWorkers do not share any connections, sessions or other
 * objects.
 * 
 * History: 2008-09-13: Initial prototype
 * 
 * @author Matthias Rohr, Jan Waller
 */
final class JMSWriterThread extends AbstractAsyncThread {
	private static final Log log = LogFactory.getLog(JMSWriterThread.class);

	// configuration parameters
	private Session session;
	private Connection connection;
	private MessageProducer sender;

	public JMSWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final String contextFactoryType, final String providerUrl, final String factoryLookupName, final String topic, final long messageTimeToLive)
			throws NamingException, JMSException {
		super(monitoringController, writeQueue);
		try {
			final Context context = new InitialContext();
			context.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, contextFactoryType);
			context.addToEnvironment(Context.PROVIDER_URL, providerUrl);
			final ConnectionFactory factory = (ConnectionFactory) context.lookup(factoryLookupName);
			this.connection = factory.createConnection();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.connection.start();
			final Destination destination = (Destination) context.lookup(topic);
			this.sender = this.session.createProducer(destination);
			this.sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			this.sender.setDisableMessageID(false);
			this.sender.setTimeToLive(messageTimeToLive);
		} catch (final NamingException ex) {
			JMSWriterThread.log.error("NamingException Exception while initializing JMS Writer");
			throw ex;
		} catch (final JMSException ex) {
			JMSWriterThread.log.error("JMSException Exception while initializing JMS Writer");
			throw ex;
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("; Session: '");
		sb.append(this.session.toString());
		sb.append("'; Connection: '");
		sb.append(this.connection.toString());
		sb.append("'; MessageProducer: '");
		sb.append(this.sender.toString());
		sb.append("'");
		return sb.toString();
	}

	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws JMSException {
		try {
			this.sender.send(this.session.createObjectMessage(monitoringRecord));
		} catch (final JMSException ex) {
			JMSWriterThread.log.error("Error sending jms message");
			throw ex;
		}
	}

	@Override
	protected final void cleanup() {
		try {
			if (this.sender != null) {
				this.sender.close();
			}
			if (this.session != null) {
				this.session.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (final JMSException ex) {
			JMSWriterThread.log.error("Error closing connection", ex);
		}
	}
}
