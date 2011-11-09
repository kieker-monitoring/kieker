/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysis.reader.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractMonitoringReader;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.reader.jms.JMSReader;
import kieker.analysis.util.PropertyMap;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Jan Waller
 * 
 */
public final class JMXReader extends AbstractMonitoringReader {
	private static final Log LOG = LogFactory.getLog(JMXReader.class);

	private JMXServiceURL serviceURL;
	private ObjectName monitoringLog;
	private final boolean silentreconnect;
	private final CountDownLatch cdLatch = new CountDownLatch(1);
	private final OutputPort outputPort;
	private static final Collection<Class<?>> OUT_CLASSES = Collections
			.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(new Class<?>[] { IMonitoringRecord.class }));

	public JMXReader(final Configuration configuation) {
		// TODO: Load from configuration.
		super(configuation);
		silentreconnect = false;

		this.outputPort = new OutputPort("Output Port of the JMSReader", JMXReader.OUT_CLASSES);
		super.registerOutputPort("out", this.outputPort);

		init(configuation);
	}

	/**
	 * Constructor for JMXReader. Requires a subsequent call to the init method
	 * in order to specify the input directory using the parameters
	 */
	public JMXReader() {
		this(false);
	}

	public JMXReader(final boolean silentreconnect) {
		super(new Configuration(null));
		this.silentreconnect = silentreconnect;

		this.outputPort = new OutputPort("Output Port of the JMSReader", JMXReader.OUT_CLASSES);
		super.registerOutputPort("out", this.outputPort);
	}

	/**
	 * known properties are
	 * server, port, serviceURL, domain, logname
	 * 
	 * either port or serviceURL are mandatory
	 */
	private final boolean init(Configuration configuration) {
		try {
			final String server = configuration.getProperty("server", "localhost");
			final int port = Integer.parseInt(configuration.getProperty("port", "0"));
			final String tmpServiceURL;
			if (port > 0) {
				tmpServiceURL = "service:jmx:rmi:///jndi/rmi://" + server + ":" + port + "/jmxrmi";
			} else {
				tmpServiceURL = configuration.getProperty("serviceURL", null);
			}
			final String domain = configuration.getProperty("domain", "kieker.monitoring");
			final String logname = configuration.getProperty("logname", "MonitoringLog");
			if (tmpServiceURL == null) {
				throw new IllegalArgumentException("JMXReader has not sufficient parameters. serviceURL is null");
			}
			this.serviceURL = new JMXServiceURL(tmpServiceURL);
			this.monitoringLog = new ObjectName(domain, "type", logname);
		} catch (final MalformedObjectNameException e) {
			JMXReader.LOG.error("Failed to parse configuration: " + e.getMessage()); // NOCS (MultipleStringLiteralsCheck)
			return false;
		} catch (final MalformedURLException e) {
			JMXReader.LOG.error("Failed to parse configuration: " + e.getMessage()); // NOCS (MultipleStringLiteralsCheck)
			return false;
		}
		return true;
	}

	@Override
	public void terminate() {
		JMXReader.LOG.info("Shutdown of JMXReader requested.");
		this.unblock();
	}

	@Override
	public final boolean read() {
		if (this.silentreconnect) {
			return this.read2();
		}
		boolean ret = true;
		JMXConnector jmx = null;
		MBeanServerConnection mbServer = null;
		ServerNotificationListener serverNotificationListener = null;
		LogNotificationListener logNotificationListener = null;
		try {
			// Connect to the Server
			try {
				jmx = JMXConnectorFactory.connect(this.serviceURL);
			} catch (final IOException e) {
				JMXReader.LOG.error("Unable to connect to JMX Server (" + e.getMessage() + ")");
				JMXReader.LOG.debug("Error in JMX connection!", e); // NOCS (MultipleStringLiteralsCheck)
				return false;
			}
			serverNotificationListener = new ServerNotificationListener();
			jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
			mbServer = jmx.getMBeanServerConnection();
			logNotificationListener = new LogNotificationListener();
			mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
			JMXReader.LOG.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

			// Waiting
			this.block();

			// Shutdown
			JMXReader.LOG.info("Shutting down JMXReader");
		} catch (final InstanceNotFoundException e) {
			JMXReader.LOG.error("No monitoring log found: " + this.monitoringLog.toString());
			ret = false;
		} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
			JMXReader.LOG.error("Error in JMX connection!", e); // NOCS (MultipleStringLiteralsCheck)
			ret = false;
		} finally {
			try {
				if (logNotificationListener != null) {
					mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
				}
			} catch (final Exception e) { // NOCS // NOPMD
				JMXReader.LOG.debug("Failed to remove Listener!", e); // NOCS (MultipleStringLiteralsCheck)
			}
			try {
				if (serverNotificationListener != null) {
					jmx.removeConnectionNotificationListener(serverNotificationListener);
				}
			} catch (final ListenerNotFoundException e) {
				JMXReader.LOG.debug("Failed to remove Listener!", e); // NOCS (MultipleStringLiteralsCheck)
			}
			try {
				if (jmx != null) {
					jmx.close();
				}
			} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
				JMXReader.LOG.debug("Failed to close JMX connection!", e);
			}
		}
		return ret;
	}

	private final boolean read2() {
		while (true) {
			JMXConnector jmx = null;
			MBeanServerConnection mbServer = null;
			ServerNotificationListener serverNotificationListener = null;
			LogNotificationListener logNotificationListener = null;
			try {
				// Connect to the Server
				try {
					jmx = JMXConnectorFactory.connect(this.serviceURL);
				} catch (final IOException e) {
					Thread.sleep(10000); // NOCS
					continue;
				}
				serverNotificationListener = new ServerNotificationListener(); // NOPMD
				jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
				mbServer = jmx.getMBeanServerConnection();
				logNotificationListener = new LogNotificationListener(); // NOPMD
				mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
				JMXReader.LOG.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

				// Waiting
				this.block();

				// Shutdown
				JMXReader.LOG.info("Shutting down JMXReader");

			} catch (final InstanceNotFoundException e) { // ignore // NOPMD
			} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
				JMXReader.LOG.error("Error in JMX connection!", e); // NOCS (MultipleStringLiteralsCheck)
			} finally {
				try {
					if (logNotificationListener != null) {
						mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
					}
				} catch (final Exception e) { // ignore // NOCS (IllegalCatchCheck) // NOPMD
				}
				try {
					if (serverNotificationListener != null) {
						jmx.removeConnectionNotificationListener(serverNotificationListener);
					}
				} catch (final ListenerNotFoundException e) { // ignore // NOPMD
				}
				try {
					if (jmx != null) {
						jmx.close();
					}
					Thread.sleep(10000); // NOCS
				} catch (final Exception e) { // ignore // NOCS (IllegalCatchCheck) // NOPMD
				}
			}
		}
	}

	private final void block() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public final void run() {
				JMXReader.this.unblock();
			}
		});
		try {
			this.cdLatch.await();
		} catch (final InterruptedException e) { // ignore
		}
	}

	private final void unblock() {
		this.cdLatch.countDown();
	}

	private final class LogNotificationListener implements NotificationListener {

		public LogNotificationListener() {
			// nothing to do
		}

		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			JMXReader.this.outputPort.deliver((IMonitoringRecord) notification.getUserData());
		}
	}

	private final class ServerNotificationListener implements NotificationListener {

		/**
		 * Constructs a {@link ServerNotificationListener}.
		 */
		public ServerNotificationListener() {
			// nothing to do
		}

		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType.equals(JMXConnectionNotification.CLOSED)) {
				if (!JMXReader.this.silentreconnect) {
					JMXReader.LOG.info("JMX connection closed.");
				}
				JMXReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.FAILED)) {
				if (!JMXReader.this.silentreconnect) {
					JMXReader.LOG.info("JMX connection lost.");
				}
				JMXReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.NOTIFS_LOST)) {
				JMXReader.LOG.error("Monitoring record lost: " + notification.getMessage());
			} else { // unknown message
				JMXReader.LOG.info(notificationType + ": " + notification.getMessage());
			}
		}
	}
}
