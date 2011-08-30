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

package kieker.monitoring.core.controller;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public final class JMXController extends AbstractController implements IJMXController {
	private final static Log log = LogFactory.getLog(JMXController.class);

	private final boolean jmxEnabled;
	private final String domain;
	private final ObjectName controllerObjectName;
	private final ObjectName serverObjectName;
	private final JMXConnectorServer server;
	private final ServerNotificationListener serverNotificationListener;
	private final String port;

	private final static int FallbackJMXImplementation = 0;
	private final static int SunJMXImplementation = 1;
	private final int usedJMXImplementation;

	protected JMXController(final Configuration configuration) {
		ObjectName controllerObjectName = null;
		ObjectName serverObjectName = null;
		JMXConnectorServer server = null;
		ServerNotificationListener serverNotificationListener = null;
		String port = "0";
		int usedJMXImplementation = FallbackJMXImplementation;
		this.domain = configuration.getStringProperty(Configuration.ACTIVATE_JMX_DOMAIN);
		this.jmxEnabled = configuration.getBooleanProperty(Configuration.ACTIVATE_JMX);
		if (this.jmxEnabled) {
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_REMOTE)) {
				try {
					port = configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_PORT);
					try {
						// Try using the "secret" SUN implementation
						// Reflection to suppress compiler warnings
						final Properties jmxProperties = configuration.getPropertiesStartingWith("com.sun.management.jmxremote");
						server = (JMXConnectorServer) Class.forName("sun.management.jmxremote.ConnectorBootstrap")
								.getMethod("initialize", String.class, Properties.class).invoke(null, port, jmxProperties);
						usedJMXImplementation = SunJMXImplementation;
					} catch (final Throwable ignoreErrors) {
						if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_REMOTE_FALLBACK)) {
							JMXController.log.warn("Failed to initialize remote JMX server, falling back to default implementation");
							// Fallback to default Implementation
							final JMXServiceURL url = new JMXServiceURL("rmi", null, Integer.parseInt(port));
							final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
							server = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
							server.start();
						} else {
							JMXController.log.warn("Failed to initialize remote JMX server and fallback is deactivated");
						}
					}
					if ((server != null) && (server.isActive())) {
						serverObjectName = new ObjectName(this.domain, "type", configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_NAME));
						serverNotificationListener = new ServerNotificationListener();
					}
				} catch (final Throwable e) {
					JMXController.log.warn("Failed to initialize remote JMX server", e);
				}
			}
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_CONTROLLER)) {
				try {
					controllerObjectName = new ObjectName(this.domain, "type", configuration.getStringProperty(Configuration.ACTIVATE_JMX_CONTROLLER_NAME));
				} catch (final Exception e) {
					JMXController.log.warn("Failed to initialize MonitoringController MBean", e);
				}
			}
		}
		this.usedJMXImplementation = usedJMXImplementation;
		this.port = port;
		this.server = server;
		this.controllerObjectName = controllerObjectName;
		this.serverObjectName = serverObjectName;
		this.serverNotificationListener = serverNotificationListener;
	}

	@Override
	protected void init() {
		synchronized (this) {
			if (this.jmxEnabled && !this.isTerminated()) {
				final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
				if (this.serverObjectName != null) {
					try {
						mbs.registerMBean(server, this.serverObjectName);
					} catch (final Exception e) {
						JMXController.log.warn("Unable to register JMXServer MBean", e);
					}
				}
				if (this.controllerObjectName != null) {
					try {
						// MXBeans is currently not possible (getClasses in IRecord)
						final StandardMBean mbean = new StandardMBean(monitoringController, IMonitoringController.class, false);
						mbs.registerMBean(mbean, this.controllerObjectName);
					} catch (final Exception e) {
						JMXController.log.warn("Unable to register Monitoring Controller MBean", e);
					}
				}
				if ((this.server != null) && this.server.isActive()) {
					this.server.addNotificationListener(serverNotificationListener, null, null);
				}
			}
		}

	}

	@Override
	protected final void cleanup() {
		synchronized (this) {
			if (this.jmxEnabled) {
				final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
				if (this.controllerObjectName != null) {
					try {
						mbs.unregisterMBean(this.controllerObjectName);
					} catch (final Exception e) {
						JMXController.log.error("Failed to terminate MBean", e);
					}
				}
				if (this.serverObjectName != null) {
					try {
						mbs.unregisterMBean(this.serverObjectName);
					} catch (final Exception e) {
						JMXController.log.error("Failed to terminate MBean", e);
					}
				}
				if (this.server != null) {
					try {
						server.removeNotificationListener(serverNotificationListener);
					} catch (final ListenerNotFoundException e) {
						JMXController.log.error("Failed to remove ServerNotificationListener", e);
					}
					try {
						server.stop();
					} catch (final Exception e) {
						JMXController.log.error("Failed to terminate JMX Server", e);
					}
				}
			}
		}
	}

	@Override
	public final String getJMXDomain() {
		return this.domain;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("JMXController: ");
		if (this.jmxEnabled) {
			sb.append("JMX enabled (Domain: '");
			sb.append(this.domain);
			sb.append("')\n");
		} else {
			sb.append("JMX disabled\n");
		}
		if (this.controllerObjectName != null) {
			sb.append("\tMonitoringController MBean available: '");
			sb.append(this.controllerObjectName.getCanonicalName());
			sb.append("'\n");
		}
		if ((this.server != null) && this.server.isActive()) {
			sb.append("\tJMX remote access available:\n");
			sb.append("\t\tService URL: '");
			final JMXServiceURL url = this.server.getAddress();
			switch (this.usedJMXImplementation) {
				case SunJMXImplementation:
					try {
						sb.append(new JMXServiceURL(url.getProtocol(), url.getHost(), url.getPort(), "/jndi/rmi://" + url.getHost() + ":" + this.port + "/"
								+ "jmxrmi").toString());
					} catch (final MalformedURLException ignoreErrors) {
						// ignore, should not happen anyway
					}
					break;
				default:
					sb.append(url.toString());
					break;
			}
			sb.append("'\n");
			final String[] connections = this.server.getConnectionIds();
			if (connections.length == 0) {
				sb.append("\t\tNo current remote connections\n");
			} else {
				for (String connection : connections) {
					sb.append("\t\tRemote connection: '");
					sb.append(connection);
					sb.append("'\n");
				}
			}
		}
		return sb.toString();
	}

	private final static class ServerNotificationListener implements NotificationListener {
		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType == JMXConnectionNotification.OPENED) {
				JMXController.log.info("New JMX remote connection initialized. Connection ID: " + ((JMXConnectionNotification) notification).getConnectionId());
			} else if (notificationType == JMXConnectionNotification.CLOSED) {
				JMXController.log.info("JMX remote connection closed. Connection ID: " + ((JMXConnectionNotification) notification).getConnectionId());
			} else { // unknown message
				JMXController.log.info(notificationType + ": " + notification.getMessage() + " (ID: "
						+ ((JMXConnectionNotification) notification).getConnectionId() + ")");
			}
		}
	}
}
