/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * This is a reader which reads the records from a JMX queue.
 * 
 * @author Jan Waller
 * 
 * @since 1.4
 */
@Plugin(description = "A reader which reads records from a JMX queue",
		outputPorts = {
			@OutputPort(name = JMXReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the JMXReader")
		},
		configuration = {
			@Property(name = JMXReader.CONFIG_PROPERTY_NAME_SERVER, defaultValue = "localhost",
					description = "The address of the server used for the JMX connection."),
			@Property(name = JMXReader.CONFIG_PROPERTY_NAME_PORT, defaultValue = "59999",
					description = "The port of the server used for the JMX connection."),
			@Property(name = JMXReader.CONFIG_PROPERTY_NAME_SERVICEURL, defaultValue = "",
					description = "As an alternative to specifiying server and port, a service URL can be given. This value is ignored if port > 0."),
			@Property(name = JMXReader.CONFIG_PROPERTY_NAME_DOMAIN, defaultValue = "kieker.monitoring",
					description = "The JMX domain used by the JMXWriter."),
			@Property(name = JMXReader.CONFIG_PROPERTY_NAME_LOGNAME, defaultValue = "MonitoringLog",
					description = "The logname used by the JMXWriter."),
			@Property(name = JMXReader.CONFIG_PROPERTY_NAME_SILENT, defaultValue = "false",
					description = "Whether the JMXReader should silently reconnect on any errors. This prevents termination of the reader!")
		})
public final class JMXReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_SERVER = "server";
	public static final String CONFIG_PROPERTY_NAME_PORT = "port";
	public static final String CONFIG_PROPERTY_NAME_SERVICEURL = "serviceUrl";
	public static final String CONFIG_PROPERTY_NAME_DOMAIN = "domain";
	public static final String CONFIG_PROPERTY_NAME_LOGNAME = "logname";
	public static final String CONFIG_PROPERTY_NAME_SILENT = "silentReconnect";

	static final Log LOG = LogFactory.getLog(JMXReader.class); // NOPMD package for inner class

	final boolean silentreconnect; // NOPMD NOCS (package visible for inner class)
	private final JMXServiceURL serviceURL;
	private final ObjectName monitoringLog;
	private final CountDownLatch cdLatch = new CountDownLatch(1);
	private final String domain;
	private final String logname;
	private final int port;
	private final String server;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 * 
	 * @throws IllegalArgumentException
	 *             If the arguments are invalid.
	 */
	public JMXReader(final Configuration configuration, final IProjectContext projectContext) throws IllegalArgumentException {
		super(configuration, projectContext);

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

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @throws IllegalArgumentException
	 *             If the arguments are invalid.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public JMXReader(final Configuration configuration) throws IllegalArgumentException {
		this(configuration, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public void terminate(final boolean error) {
		LOG.info("Shutdown of JMXReader requested.");
		this.unblock();
	}

	/**
	 * {@inheritDoc}
	 */
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

	final boolean deliverIndirect(final String outputPortName, final Object data) { // NOPMD (package visible for inner class)
		return super.deliver(outputPortName, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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

	/**
	 * @author Jan waller
	 */
	private final class LogNotificationListener implements NotificationListener {

		public LogNotificationListener() {
			// nothing to do
		}

		public final void handleNotification(final Notification notification, final Object handback) {
			JMXReader.this.deliverIndirect(OUTPUT_PORT_NAME_RECORDS, notification.getUserData());
		}
	}

	/**
	 * @author Jan waller
	 */
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
