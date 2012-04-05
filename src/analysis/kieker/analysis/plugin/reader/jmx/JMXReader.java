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

package kieker.analysis.plugin.reader.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
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

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Jan Waller
 */
@Plugin(outputPorts = @OutputPort(name = JMXReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the JMXReader"))
public final class JMXReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_SERVER = "server";
	public static final String CONFIG_PROPERTY_NAME_PORT = "port";
	public static final String CONFIG_PROPERTY_NAME_SERVICEURL = "serviceUrl";
	public static final String CONFIG_PROPERTY_NAME_DOMAIN = "domain";
	public static final String CONFIG_PROPERTY_NAME_LOGNAME = "logname";
	public static final String CONFIG_PROPERTY_NAME_SILENT = "silentReconnect";

	private static final Log LOG = LogFactory.getLog(JMXReader.class);

	final boolean silentreconnect; // NOPMD NOCS (package visible for inner class)
	private final JMXServiceURL serviceURL;
	private final ObjectName monitoringLog;
	private final CountDownLatch cdLatch = new CountDownLatch(1);
	private final String domain;
	private final String logname;
	private final int port;
	private final String server;

	public JMXReader(final Configuration configuation) throws IllegalArgumentException {
		super(configuation);
		this.server = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_SERVER);
		this.port = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_PORT);
		final String tmpServiceURL;
		if (this.port > 0) {
			tmpServiceURL = "service:jmx:rmi:///jndi/rmi://" + this.server + ":" + this.port + "/jmxrmi";
		} else {
			tmpServiceURL = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_SERVICEURL);
		}
		this.domain = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_DOMAIN);
		this.logname = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_LOGNAME);
		if (tmpServiceURL.length() == 0) {
			throw new IllegalArgumentException("JMXReader has not sufficient parameters. Set either port or serviceURL");
		}
		try {
			this.serviceURL = new JMXServiceURL(tmpServiceURL);
			this.monitoringLog = new ObjectName(this.domain, "type", this.logname);
		} catch (final MalformedObjectNameException e) {
			throw new IllegalArgumentException("Failed to parse configuration.", e);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException("Failed to parse configuration.", e);
		}
		this.silentreconnect = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SILENT);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_SERVER, "localhost");
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_PORT, "59999");
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_SERVICEURL, "");
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_DOMAIN, "kieker.monitoring");
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_LOGNAME, "MonitoringLog");
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_SILENT, "false");
		return defaultConfiguration;
	}

	public void terminate(final boolean error) {
		LOG.info("Shutdown of JMXReader requested.");
		this.unblock();
	}

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
			} catch (final IOException ex) {
				LOG.error("Unable to connect to JMX Server (" + ex.getMessage() + ")");
				if (LOG.isDebugEnabled()) {
					LOG.debug("Error in JMX connection!", ex);
				}
				return false;
			}
			serverNotificationListener = new ServerNotificationListener();
			jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
			mbServer = jmx.getMBeanServerConnection();
			logNotificationListener = new LogNotificationListener();
			mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
			LOG.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

			// Waiting
			this.block();

			// Shutdown
			LOG.info("Shutting down JMXReader");
		} catch (final InstanceNotFoundException ex) {
			LOG.error("No monitoring log found: " + this.monitoringLog.toString()); // ok to ignore ex here
			ret = false;
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Error in JMX connection!", ex);
			ret = false;
		} finally {
			try {
				if (logNotificationListener != null) {
					mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
				}
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				if (LOG.isDebugEnabled()) {
					LOG.debug("Failed to remove Listener!", e);
				}
			}
			try {
				if (serverNotificationListener != null) {
					jmx.removeConnectionNotificationListener(serverNotificationListener);
				}
			} catch (final ListenerNotFoundException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Failed to remove Listener!", e);
				}
			}
			try {
				if (jmx != null) {
					jmx.close();
				}
			} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
				if (LOG.isDebugEnabled()) {
					LOG.debug("Failed to close JMX connection!", e);
				}
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
				serverNotificationListener = new ServerNotificationListener();
				jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
				mbServer = jmx.getMBeanServerConnection();
				logNotificationListener = new LogNotificationListener();
				mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
				LOG.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

				// Waiting
				this.block();

				// Shutdown
				LOG.info("Shutting down JMXReader");

			} catch (final InstanceNotFoundException e) { // NOPMD (ignore this)
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				LOG.error("Error in JMX connection!", e);
			} finally {
				try {
					if (logNotificationListener != null) {
						mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
					}
				} catch (final Exception e) { // ignore // NOPMD NOCS (IllegalCatchCheck)
				}
				try {
					if (serverNotificationListener != null) {
						jmx.removeConnectionNotificationListener(serverNotificationListener);
					}
				} catch (final ListenerNotFoundException e) { // NOPMD (ignore)
				}
				try {
					if (jmx != null) {
						jmx.close();
					}
					Thread.sleep(10000); // NOCS
				} catch (final Exception e) { // ignore // NOPMD NOCS (IllegalCatchCheck)
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

	final void unblock() { // NOPMD (package visible for inner class)
		this.cdLatch.countDown();
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_SERVER, this.server);
		configuration.setProperty(CONFIG_PROPERTY_NAME_PORT, Integer.toString(this.port));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SERVICEURL, this.serviceURL.toString());
		configuration.setProperty(CONFIG_PROPERTY_NAME_DOMAIN, this.domain);
		configuration.setProperty(CONFIG_PROPERTY_NAME_LOGNAME, this.logname);
		configuration.setProperty(CONFIG_PROPERTY_NAME_SILENT, Boolean.toString(this.silentreconnect));

		return configuration;
	}

	private final class LogNotificationListener implements NotificationListener {

		public LogNotificationListener() {
			// nothing to do
		}

		public final void handleNotification(final Notification notification, final Object handback) {
			JMXReader.super.deliver(OUTPUT_PORT_NAME_RECORDS, notification.getUserData());
		}
	}

	private final class ServerNotificationListener implements NotificationListener {

		/**
		 * Constructs a {@link ServerNotificationListener}.
		 */
		public ServerNotificationListener() {
			// nothing to do
		}

		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType.equals(JMXConnectionNotification.CLOSED)) {
				if (!JMXReader.this.silentreconnect) {
					LOG.info("JMX connection closed.");
				}
				JMXReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.FAILED)) {
				if (!JMXReader.this.silentreconnect) {
					LOG.info("JMX connection lost.");
				}
				JMXReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.NOTIFS_LOST)) {
				LOG.error("Monitoring record lost: " + notification.getMessage());
			} else { // unknown message
				LOG.info(notificationType + ": " + notification.getMessage());
			}
		}
	}
}
