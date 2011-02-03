package kieker.monitoring.writer.jms;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordTypeClassnameMapping;
import kieker.common.util.PropertyMap;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.writer.IMonitoringWriter;

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
 * @author Matthias Rohr, Andre van Hoorn
 */
public final class AsyncJMSWriter implements IMonitoringWriter {

	private static final Log log = LogFactory.getLog(AsyncJMSWriter.class);
	private final Vector<Thread> typeWriterAndRecordWriters = new Vector<Thread>();
	private static final MonitoringRecordTypeClassnameMapping TYPE_WRITER_END_OF_MONITORING_MARKER = new MonitoringRecordTypeClassnameMapping(
			-1, null);
	private static final IMonitoringRecord RECORD_WRITER_END_OF_MONITORING_MARKER = new DummyMonitoringRecord();
	private final int numberOfJmsWriters = 3; // number of jms connections --
																						// usually one (on every node)
	private BlockingQueue<IMonitoringRecord> recordQueue = null;
	private BlockingQueue<MonitoringRecordTypeClassnameMapping> typeQueue = null;
	private String contextFactoryType; // type of the jms factory implementation,
																			// e.g.
	private String providerUrl;
	private String factoryLookupName;
	private String topic;
	private long messageTimeToLive;
	private int asyncRecordQueueSize = 8000;
	private final int asyncTypeQueueSize = 20;

	/**
	 * Init String. Expect key=value pairs separated by |.
	 * 
	 * Example initString (meaning of keys explained below):
	 * jmsProviderUrl=tcp://localhost:3035/ | jmsTopic=queue1 |
	 * jmsContextFactoryType=org.exolab.jms.jndi.InitialContextFactory |
	 * jmsFactoryLookupName=ConnectionFactory | jmsMessageTimeToLive = 10000
	 * 
	 * jmsContextFactoryType -- type of the jms factory implementation, e.g.
	 * "org.exolab.jms.jndi.InitialContextFactory" for openjms 0.7.7
	 * jmsProviderUrl -- url of the jndi provider that knows the jms service
	 * jmsFactoryLookupName -- service name for the jms connection factory
	 * jmsTopic -- topic at the jms server which is used in the
	 * publisher/subscribe communication
	 * jmsMessageTimeToLive -- time that a jms message will kepts be alive at the
	 * jms server before it is automatically deleted
	 * 
	 * @param initString
	 * @return true on success. false on error.
	 */
	@Override
	public boolean init(final String initString) {
		if ((initString == null) || (initString.length() == 0)) {
			AsyncJMSWriter.log
					.error("Invalid initString. Valid example for kieker.monitoring.properties:\n"
							+ "monitoringDataWriterInitString=jmsProviderUrl=tcp://localhost:3035/ | jmsTopic=queue1 | jmsContextFactoryType=org.exolab.jms.jndi.InitialContextFactory | jmsFactoryLookupName=ConnectionFactory | jmsMessageTimeToLive = 10000");
			return false;
		}

		boolean retVal = true;
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|", "=");

			this.contextFactoryType = propertyMap.getProperty("jmsContextFactoryType");
			this.providerUrl = propertyMap.getProperty("jmsProviderUrl");
			this.factoryLookupName = propertyMap.getProperty("jmsFactoryLookupName");
			this.topic = propertyMap.getProperty("jmsTopic");
			this.messageTimeToLive = Long.valueOf(propertyMap.getProperty("jmsMessageTimeToLive"));
			this.asyncRecordQueueSize = Integer.valueOf(propertyMap.getProperty("asyncRecordQueueSize"));

			this.recordQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.asyncRecordQueueSize);
			this.typeQueue = new ArrayBlockingQueue<MonitoringRecordTypeClassnameMapping>(this.asyncTypeQueueSize);
			for (int i = 1; i <= this.numberOfJmsWriters; i++) {
				final JMSWriterThread<IMonitoringRecord> recordWriter = new JMSWriterThread<IMonitoringRecord>(
						this.recordQueue, AsyncJMSWriter.RECORD_WRITER_END_OF_MONITORING_MARKER, this.contextFactoryType,
						this.providerUrl, this.factoryLookupName, this.topic, this.messageTimeToLive);
				this.typeWriterAndRecordWriters.add(recordWriter);
				recordWriter.setDaemon(true);
				recordWriter.start();
			}
			AsyncJMSWriter.log.info("(" + this.numberOfJmsWriters + " threads) will send to the JMS server topic");
		} catch (final Exception exc) {
			AsyncJMSWriter.log.fatal("Error initiliazing JMS Connector", exc);
			retVal = false;
		}
		return retVal;
	}

	@Override
	public String getInfoString() {
		final StringBuilder strB = new StringBuilder();

		strB.append("contextFactoryType : " + this.contextFactoryType);
		strB.append(", providerUrl : " + this.providerUrl);
		strB.append(", factoryLookupName : " + this.factoryLookupName);
		strB.append(", topic : " + this.topic);
		strB.append(", messageTimeToLive : " + this.messageTimeToLive);
		strB.append(", asyncRecordQueueSize :" + this.asyncRecordQueueSize);
		return strB.toString();
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			if (monitoringRecord == MonitoringController.END_OF_MONITORING_MARKER) {
				// "translate" END_OF_MONITORING_MARKER
				AsyncJMSWriter.log.info("Found END_OF_MONITORING_MARKER. Notifying type and record writers");
				this.typeQueue.add(AsyncJMSWriter.TYPE_WRITER_END_OF_MONITORING_MARKER);
				this.recordQueue.add(AsyncJMSWriter.RECORD_WRITER_END_OF_MONITORING_MARKER);
			} else {
				this.recordQueue.add(monitoringRecord); // tries to add immediately! --
																								// this is for production
																								// systems
			}
			// int currentQueueSize = recordQueue.size();
		} catch (final Exception ex) {
			AsyncJMSWriter.log.error(System.currentTimeMillis() + " AsyncJmsProducer() failed: Exception:", ex);
			return false;
		}
		return true;
	}

	@Override
	public Vector<AbstractWorkerThread> getWorkers() {
		return this.typeWriterAndRecordWriters;
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub

	}
}
