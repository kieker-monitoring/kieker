package kieker.monitoring.writer.jms;

import java.io.Serializable;
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
 * @author Matthias Rohr
 */
public final class JMSWriterThread<T> extends Thread {
	private static final Log log = LogFactory.getLog(JMSWriterThread.class);
	// configuration parameters

	private Session session;
	private Connection connection;
	private MessageProducer sender;
	private BlockingQueue<T> writeQueue = null;
	private boolean finished = false;
	private static boolean shutdown = false;
	private final T END_OF_MONITORING_MARKER;

	public JMSWriterThread(BlockingQueue<T> writeQueue, T endOfMonitoringMarker, String contextFactoryType, String providerUrl, String factoryLookupName, String topic, long messageTimeToLive) {
		log.info("JMS connect with factorytype: " + contextFactoryType + ", providerurl: " + providerUrl + ", factorylookupname: " + factoryLookupName + ", topic: " + topic + " \n");
		log.info("JMS connect with messagetimetolive: " + messageTimeToLive);
		this.writeQueue = writeQueue;
		this.END_OF_MONITORING_MARKER = endOfMonitoringMarker;
		if (this.END_OF_MONITORING_MARKER == null) {
			log.error("endofMonitoringMarker must not be null!");
		} else {
			log.info("Constructing JmsWriter (" + this.END_OF_MONITORING_MARKER.getClass().getName() + ")");
		}
		try {
			Hashtable<String, String> properties = new Hashtable<String, String>();
			properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryType);
			properties.put(Context.PROVIDER_URL, providerUrl);

			Context context = new InitialContext(properties);
			ConnectionFactory factory = (ConnectionFactory) context.lookup(factoryLookupName);
			connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			log.info("Looking for topic " + topic);
			Destination destination = (Destination) context.lookup(topic);
			connection.start();

			sender = session.createProducer(destination);
			sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sender.setDisableMessageID(false);
			sender.setTimeToLive(messageTimeToLive); // time to live defaults in millisecs
		} catch (JMSException ex) {
			log.error("JMS Exception while constructing JMS Writer", ex);
			throw new RuntimeException(ex);
		} catch (NamingException ex) {
			log.error("NamingException while constructing JMS Writer", ex);
			throw new RuntimeException(ex);
		}
	}

	private void consume(T execData) throws Exception {
		try {
			// TODO: casting to serializable may throw an exception!
			ObjectMessage messageObject = session.createObjectMessage((Serializable) execData);
			// TextMessage message = session.createTextMessage("Hello World!");
			sender.send(messageObject);
			// System.out.println("sent execution "+insertData.opname+" "+insertData.tin);
			// Object so = messageObject.getObject();
			// InsertData id2 = (InsertData) so;
			// System.out.println("id2 "+id2.opname);
		} catch (JMSException ex) {
			log.error(Level.SEVERE, ex);
		}
	}

	public void run() {
		log.info("JmsWriter thread (" + this.END_OF_MONITORING_MARKER.getClass().getName() + ") running");
		// System.out.println("FsWriter thread running");
		try {
			while (!finished) {
				T monitoringRecord = writeQueue.take();
				if (monitoringRecord == this.END_OF_MONITORING_MARKER) {
					log.info("Found END_OF_MONITORING_MARKER. Will terminate");
					// need to put the marker back into the queue to notify other threads
					writeQueue.add(this.END_OF_MONITORING_MARKER);
					this.closeConnection();
					finished = true;
					break;
				}
				if (monitoringRecord != null) {
					consume(monitoringRecord);
					// System.out.println("FSW "+writeQueue.size());
				} else { // timeout ...
					if (shutdown && writeQueue.isEmpty()) {
						this.closeConnection();
						finished = true;
					}
				}
			}
			log.info("JMS writer finished");
		} catch (Exception ex) {
			// TODO: terminate monitoring?
			// e.g. Interrupted Exception or IOException
			log.error("JMS writer will halt", ex);
		} finally {
			this.finished = true;
		}
	}

	public boolean isFinished() {
		//TODO: ???? getter!!!
		if (finished) {
			this.closeConnection();
		}
		return finished;
	}

	private void closeConnection() {
		log.info("Closing JMS connection");
		try {
			sender.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			log.error("Error closing connection", e);
		}
	}

	/**
	 * Initials the shutdown
	 */
	public void initShutdown() {
		// the variable might be set to false more often than required
		JMSWriterThread.shutdown = true;
	}
}
