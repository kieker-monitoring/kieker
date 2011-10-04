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

import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.util.PropertyMap;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Jan Waller
 * 
 */
public final class JMXReader extends AbstractMonitoringReader {
	private static final Log log = LogFactory.getLog(JMXReader.class);

	private JMXServiceURL serviceURL;
	private ObjectName monitoringLog;
	private final boolean silentreconnect;

	/**
	 * Constructor for JMXReader. Requires a subsequent call to the init method
	 * in order to specify the input directory using the parameters
	 */
	public JMXReader() {
		this(false);
	}

	public JMXReader(final boolean silentreconnect) {
		this.silentreconnect = silentreconnect;
	}

	/**
	 * Expects an initString of the following format:
	 * "property1@value1|property2@value2|..."
	 * known properties are
	 * server, port, serviceURL, domain, logname
	 * 
	 * either port or serviceURL are mandatory
	 */
	@Override
	public final boolean init(final String initString) {
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|", "@");
			final String server = propertyMap.getProperty("server", "localhost");
			final int port = Integer.valueOf(propertyMap.getProperty("port", "0")).intValue();
			final String serviceURL;
			if (port > 0) {
				serviceURL = "service:jmx:rmi:///jndi/rmi://" + server + ":" + port + "/jmxrmi";
			} else {
				serviceURL = propertyMap.getProperty("serviceURL", null);
			}
			final String domain = propertyMap.getProperty("domain", "kieker.monitoring");
			final String logname = propertyMap.getProperty("logname", "MonitoringLog");
			this.initInstanceFromArgs(serviceURL, domain, logname);
		} catch (final IllegalArgumentException e) {
			JMXReader.log.error("Failed to parse initString '" + initString + "': " + e.getMessage());
			return false;
		} catch (final MalformedObjectNameException e) {
			JMXReader.log.error("Failed to parse initString '" + initString + "': " + e.getMessage());
			return false;
		} catch (final MalformedURLException e) {
			JMXReader.log.error("Failed to parse initString '" + initString + "': " + e.getMessage());
			return false;
		}
		return true;
	}

	private void initInstanceFromArgs(final String serviceURL, final String domain, final String logname)
			throws IllegalArgumentException, MalformedURLException, MalformedObjectNameException {
		if (serviceURL == null) {
			throw new IllegalArgumentException("JMXReader has not sufficient parameters. serviceURL is null");
		}
		this.serviceURL = new JMXServiceURL(serviceURL);
		this.monitoringLog = new ObjectName(domain, "type", logname);
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
				JMXReader.log.error("Unable to connect to JMX Server (" + e.getMessage() + ")");
				JMXReader.log.debug("Error in JMX connection!", e);
				return false;
			}
			serverNotificationListener = new ServerNotificationListener();
			jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
			mbServer = jmx.getMBeanServerConnection();
			logNotificationListener = new LogNotificationListener();
			mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
			JMXReader.log.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

			// Waiting
			this.block();

			// Shutdown
			JMXReader.log.info("Shutting down JMXReader");
		} catch (final InstanceNotFoundException e) {
			JMXReader.log.error("No monitoring log found: " + this.monitoringLog.toString());
			ret = false;
		} catch (final Exception e) {
			JMXReader.log.error("Error in JMX connection!", e);
			ret = false;
		} finally {
			try {
				if (logNotificationListener != null) {
					mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
				}
			} catch (final Exception e) {
				JMXReader.log.debug("Failed to remove Listener!", e);
			}
			try {
				if (serverNotificationListener != null) {
					jmx.removeConnectionNotificationListener(serverNotificationListener);
				}
			} catch (final ListenerNotFoundException e) {
				JMXReader.log.debug("Failed to remove Listener!", e);
			}
			try {
				if (jmx != null) {
					jmx.close();
				}
			} catch (final Exception e) {
				JMXReader.log.debug("Failed to close JMX connection!", e);
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
					Thread.sleep(10000);
					continue;
				}
				serverNotificationListener = new ServerNotificationListener();
				jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
				mbServer = jmx.getMBeanServerConnection();
				logNotificationListener = new LogNotificationListener();
				mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
				JMXReader.log.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

				// Waiting
				this.block();

				// Shutdown
				JMXReader.log.info("Shutting down JMXReader");

			} catch (final InstanceNotFoundException e) {
			} catch (final Exception e) {
				JMXReader.log.error("Error in JMX connection!", e);
			} finally {
				try {
					if (logNotificationListener != null) {
						mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
					}
				} catch (final Exception e) {// ignore
				}
				try {
					if (serverNotificationListener != null) {
						jmx.removeConnectionNotificationListener(serverNotificationListener);
					}
				} catch (final ListenerNotFoundException e) {
				}
				try {
					if (jmx != null) {
						jmx.close();
					}
					Thread.sleep(10000);
				} catch (final Exception e) {
				}
			}
		}
	}

	private final CountDownLatch cdLatch = new CountDownLatch(1);

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
		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			JMXReader.this.deliverRecord((IMonitoringRecord) notification.getUserData());
		}
	}

	private final class ServerNotificationListener implements NotificationListener {
		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType.equals(JMXConnectionNotification.CLOSED)) {
				if (!JMXReader.this.silentreconnect) {
					JMXReader.log.info("JMX connection closed.");
				}
				JMXReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.FAILED)) {
				if (!JMXReader.this.silentreconnect) {
					JMXReader.log.info("JMX connection lost.");
				}
				JMXReader.this.unblock();
			} else if (notificationType.equals(JMXConnectionNotification.NOTIFS_LOST)) {
				JMXReader.log.error("Monitoring record lost: " + notification.getMessage());
			} else { // unknown message
				JMXReader.log.info(notificationType + ": " + notification.getMessage());
			}
		}
	}

	@Override
	public void terminate() {
		JMXReader.log.info("Shutdown of JMXReader requested.");
		this.unblock();
	}
}
