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
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public final class AsyncJMSWriter extends AbstractAsyncWriter {
	private static final String PREFIX = "kieker.monitoring.writer.jms.AsyncJMSWriter.";
	private static final String PROVIDERURL = PREFIX + "ProviderUrl";
	private static final String TOPIC = PREFIX + "Topic";
	private static final String CONTEXTFACTORYTYPE = PREFIX + "ContextFactoryType";
	private static final String FACTORYLOOKUPNAME = PREFIX + "FactoryLookupName";
	private static final String MESSAGETTL = PREFIX + "MessageTimeToLive";

	public AsyncJMSWriter(final IMonitoringController ctrl, final Configuration configuration) throws NamingException, JMSException {
		super(ctrl, configuration);
		this.addWorker(new JMSWriterThread(
				ctrl,
				this.blockingQueue,
				this.configuration.getStringProperty(CONTEXTFACTORYTYPE),
				this.configuration.getStringProperty(PROVIDERURL), 
				this.configuration.getStringProperty(FACTORYLOOKUPNAME), 
				this.configuration.getStringProperty(TOPIC), 
				this.configuration.getLongProperty(MESSAGETTL)
			));
	}
}

/**
 * The writer moves monitoring data via messaging to a JMS server. This uses the
 * publishRecord/subscribe pattern. The JMS server will only keep each
 * monitoring event
 * for messageTimeToLive milliseconds presents before it is deleted.
 * 
 * At the moment, JmsWorkers do not share any connections,
 * sessions or other objects.
 * 
 * History:
 * 2008-09-13: Initial prototype
 * 
 * @author Matthias Rohr, Jan Waller
 */
final class JMSWriterThread extends AbstractAsyncThread {
	private static final Log log = LogFactory.getLog(JMSWriterThread.class);

	// configuration parameters
	private Session session;
	private Connection connection;
	private MessageProducer sender;

	public JMSWriterThread(final IMonitoringController ctrl, final BlockingQueue<IMonitoringRecord> writeQueue,
			final String contextFactoryType, final String providerUrl, final String factoryLookupName, final String topic, final long messageTimeToLive) 
			throws NamingException, JMSException {
		super(ctrl, writeQueue);
		Context context;
		try {
			context = new InitialContext();
			context.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, contextFactoryType);
			context.addToEnvironment(Context.PROVIDER_URL, providerUrl);
			final ConnectionFactory factory = (ConnectionFactory) context.lookup(factoryLookupName);
			this.connection = factory.createConnection();
			this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.connection.start();
			final Destination destination = (Destination) context.lookup(topic);
			this.sender = session.createProducer(destination);
			this.sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			this.sender.setDisableMessageID(false);
			this.sender.setTimeToLive(messageTimeToLive);
		} catch (final NamingException ex) {
			log.error("NamingException Exception while initializing JMS Writer");
			throw ex;
		} catch (final JMSException ex) {
			log.error("JMSException Exception while initializing JMS Writer");
			throw ex;
		}
	}
	
	@Override
	public final String getInfoString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getInfoString());
		sb.append("; Session: '");
		sb.append(session.toString());
		sb.append("'; Connection: '");
		sb.append(connection.toString());
		sb.append("'; MessageProducer: '");
		sb.append(sender.toString());
		sb.append("'");
		return sb.toString();
	}
	
	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws JMSException {
		try {
			sender.send(session.createObjectMessage(monitoringRecord));
		} catch (final JMSException ex) {
			log.error("Error sending jms message");
			throw ex;
		}
	}
	
	@Override
	protected final void cleanup() {
		try {
			sender.close();
			session.close();
			connection.close();
		} catch (final JMSException ex) {
			log.error("Error closing connection", ex);
		}
	}
}
