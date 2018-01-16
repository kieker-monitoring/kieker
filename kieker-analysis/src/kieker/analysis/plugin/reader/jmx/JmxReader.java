/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
			@OutputPort(name = JmxReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the JmxReader")
		},
		configuration = {
			@Property(name = JmxReader.CONFIG_PROPERTY_NAME_SERVER, defaultValue = "localhost",
					description = "The address of the server used for the JMX connection."),
			@Property(name = JmxReader.CONFIG_PROPERTY_NAME_PORT, defaultValue = "59999",
					description = "The port of the server used for the JMX connection."),
			@Property(name = JmxReader.CONFIG_PROPERTY_NAME_SERVICEURL, defaultValue = "",
					description = "As an alternative to specifiying server and port, a service URL can be given. This value is ignored if port > 0."),
			@Property(name = JmxReader.CONFIG_PROPERTY_NAME_DOMAIN, defaultValue = "kieker.monitoring",
					description = "The JMX domain used by the JMXWriter."),
			@Property(name = JmxReader.CONFIG_PROPERTY_NAME_LOGNAME, defaultValue = "MonitoringLog",
					description = "The logname used by the JMXWriter."),
			@Property(name = JmxReader.CONFIG_PROPERTY_NAME_SILENT, defaultValue = "false",
					description = "Whether the JmxReader should silently reconnect on any errors. This prevents termination of the reader!")
		})
public final class JmxReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the JMX server. */
	public static final String CONFIG_PROPERTY_NAME_SERVER = "server";
	/** The name of the configuration determining the JMX port. */
	public static final String CONFIG_PROPERTY_NAME_PORT = "port";
	/** The name of the configuration determining the optional service URL. */
	public static final String CONFIG_PROPERTY_NAME_SERVICEURL = "serviceUrl";
	/** The name of the configuration determining the JMX domain. */
	public static final String CONFIG_PROPERTY_NAME_DOMAIN = "domain";
	/** The name of the configuration determining the logname used by the reader. */
	public static final String CONFIG_PROPERTY_NAME_LOGNAME = "logname";
	/** The name of the configuration determining whether the reader silently reconnects on any errors. */
	public static final String CONFIG_PROPERTY_NAME_SILENT = "silentReconnect";

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
	public JmxReader(final Configuration configuration, final IProjectContext projectContext) throws IllegalArgumentException {
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
			throw new IllegalArgumentException("JmxReader has not sufficient parameters. Set either port or serviceURL");
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
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		this.log.info("Shutdown of JmxReader requested.");
		this.unblock();
	}

	/**
	 * {@inheritDoc}
	 */
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
			} catch (final IOException ex) {
				this.log.error("Unable to connect to JMX Server (" + ex.getMessage() + ")");
				if (this.log.isDebugEnabled()) {
					this.log.debug("Error in JMX connection!", ex);
				}
				return false;
			}
			serverNotificationListener = new ServerNotificationListener();
			jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
			mbServer = jmx.getMBeanServerConnection();
			logNotificationListener = new LogNotificationListener();
			mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
			this.log.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

			// Waiting
			this.block();

			// Shutdown
			this.log.info("Shutting down JmxReader");
		} catch (final InstanceNotFoundException ex) {
			this.log.error("No monitoring log found: " + this.monitoringLog.toString()); // ok to ignore ex here
			ret = false;
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			this.log.error("Error in JMX connection!", ex);
			ret = false;
		} finally {
			try {
				if (logNotificationListener != null) {
					mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
				}
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				if (this.log.isDebugEnabled()) {
					this.log.debug("Failed to remove Listener!", e);
				}
			}
			try {
				if (serverNotificationListener != null) {
					jmx.removeConnectionNotificationListener(serverNotificationListener);
				}
			} catch (final ListenerNotFoundException e) {
				if (this.log.isDebugEnabled()) {
					this.log.debug("Failed to remove Listener!", e);
				}
			}
			try {
				if (jmx != null) {
					jmx.close();
				}
			} catch (final Exception e) { // NOCS (IllegalCatchCheck) // NOPMD
				if (this.log.isDebugEnabled()) {
					this.log.debug("Failed to close JMX connection!", e);
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
				this.log.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

				// Waiting
				this.block();

				// Shutdown
				this.log.info("Shutting down JmxReader");

			} catch (final InstanceNotFoundException e) { // NOPMD (ignore this)
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				this.log.error("Error in JMX connection!", e);
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
				JmxReader.this.unblock();
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

	protected Log getLog() {
		return super.log;
	}

	/**
	 * @author Jan waller
	 */
	private final class LogNotificationListener implements NotificationListener {

		public LogNotificationListener() {
			// nothing to do
		}

		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			JmxReader.this.deliverIndirect(OUTPUT_PORT_NAME_RECORDS, notification.getUserData());
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

		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType.equals(JMXConnectionNotification.CLOSED)) {
				if (!JmxReader.this.silentreconnect) {
					JmxReader.this.getLog().info("JMX connection closed.");
				}
				JmxReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.FAILED)) {
				if (!JmxReader.this.silentreconnect) {
					JmxReader.this.getLog().info("JMX connection lost.");
				}
				JmxReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.NOTIFS_LOST)) {
				JmxReader.this.getLog().error("Monitoring record lost: " + notification.getMessage());
			} else { // unknown message
				JmxReader.this.getLog().info(notificationType + ": " + notification.getMessage());
			}
		}
	}
}
