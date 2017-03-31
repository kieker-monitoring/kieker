/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.jmx;

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

import kieker.common.logging.Log;
import kieker.common.record.IMonitoringRecord;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This is a reader which reads the records from a JMX queue.
 *
 * @author Jan Waller, Lars Bluemke
 *
 * @since 1.4
 */
public class JMXReaderLogic {

	final boolean silentreconnect; // NOPMD NOCS (package visible for inner class)
	private final JMXServiceURL serviceURL;
	private final ObjectName monitoringLog;
	private final CountDownLatch cdLatch = new CountDownLatch(1);

	private final Log log;
	private final JMXReader jmxReaderStage;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param silentreconnect
	 *            Determines whether the reader silently reconnects on any errors.
	 * @param serviceURL
	 *            Optional service URL.
	 * @param domain
	 *            Determines the JMX domain.
	 * @param logname
	 *            Determines the logname used by the reader.
	 * @param port
	 *            Determines the JMX port.
	 * @param server
	 *            Determines the JMX server.
	 * @param log
	 *            Kieker log.
	 * @param jmxReaderStage
	 *            The actual teetime stage which uses this class.
	 */
	public JMXReaderLogic(final boolean silentreconnect, final JMXServiceURL serviceURL, final String domain, final String logname,
			final int port, final String server, final Log log, final JMXReader jmxReaderStage) {
		final String tmpServiceURL;
		if (port > 0) {
			tmpServiceURL = "service:jmx:rmi:///jndi/rmi://" + server + ":" + port + "/jmxrmi";
		} else {
			tmpServiceURL = serviceURL.toString();
		}
		if (tmpServiceURL.length() == 0) {
			throw new IllegalArgumentException("JMXReader has not sufficient parameters. Set either port or serviceURL");
		}
		try {
			this.serviceURL = new JMXServiceURL(tmpServiceURL);
			this.monitoringLog = new ObjectName(domain, "type", logname);
		} catch (final MalformedObjectNameException e) {
			throw new IllegalArgumentException("Failed to parse configuration.", e);
		} catch (final MalformedURLException e) {
			throw new IllegalArgumentException("Failed to parse configuration.", e);
		}
		this.silentreconnect = silentreconnect;

		this.log = log;
		this.jmxReaderStage = jmxReaderStage;
	}

	public void terminate() {
		this.log.info("Shutdown of JMXReader requested.");
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
			this.log.info("Shutting down JMXReader");
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

	@SuppressFBWarnings("DE_MIGHT_IGNORE")
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
				this.log.info("Shutting down JMXReader");

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
				JMXReaderLogic.this.unblock();
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

	final void deliverIndirect(final IMonitoringRecord record) { // NOPMD (package visible for inner class)
		this.jmxReaderStage.deliverRecord(record);
	}

	protected Log getLog() {
		return this.log;
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
			final Object data = notification.getUserData();
			if (data instanceof IMonitoringRecord) {
				JMXReaderLogic.this.deliverIndirect((IMonitoringRecord) data);
			}
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
				if (!JMXReaderLogic.this.silentreconnect) {
					JMXReaderLogic.this.getLog().info("JMX connection closed.");
				}
				JMXReaderLogic.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.FAILED)) {
				if (!JMXReaderLogic.this.silentreconnect) {
					JMXReaderLogic.this.getLog().info("JMX connection lost.");
				}
				JMXReaderLogic.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.NOTIFS_LOST)) {
				JMXReaderLogic.this.getLog().error("Monitoring record lost: " + notification.getMessage());
			} else { // unknown message
				JMXReaderLogic.this.getLog().info(notificationType + ": " + notification.getMessage());
			}
		}
	}

}
