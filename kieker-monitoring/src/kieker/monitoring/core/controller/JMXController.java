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

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * @author Jan Waller
 * 
 * @since 1.4
 */
public final class JMXController extends AbstractController implements IJMXController {
	static final Log LOG = LogFactory.getLog(JMXController.class); // NOPMD package for inner class

	private final boolean jmxEnabled;
	private final String domain;
	private final ObjectName controllerObjectName;
	private final ObjectName serverObjectName;
	private final JMXConnectorServer server;
	private final ServerNotificationListener serverNotificationListener;
	private final String port;

	private final JMXImplementation usedJMXImplementation;

	private static enum JMXImplementation {
		Fallback, Sun
	}

	/**
	 * Create a new JMX controller.
	 * Note: The error handling in this block is correct, see ticket #293
	 * 
	 * @param configuration
	 *            the Kieker configuration facitlity
	 */
	protected JMXController(final Configuration configuration) {
		super(configuration);
		ObjectName controllerObjectNameTmp = null;
		ObjectName serverObjectNameTmp = null;
		JMXConnectorServer serverTmp = null;
		ServerNotificationListener serverNotificationListenerTmp = null;
		String portTmp = "0";
		JMXImplementation usedJMXImplementationTmp = JMXImplementation.Fallback;
		this.domain = configuration.getStringProperty(ConfigurationFactory.ACTIVATE_JMX_DOMAIN);
		this.jmxEnabled = configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_JMX);
		if (this.jmxEnabled) {
			if (configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE)) {
				try {
					portTmp = configuration.getStringProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_PORT);
					try {
						// Try using the "secret" SUN implementation
						// Reflection to suppress compiler warnings
						final Properties jmxProperties = configuration.getPropertiesStartingWith("com.sun.management.jmxremote");
						serverTmp = (JMXConnectorServer) Class.forName("sun.management.jmxremote.ConnectorBootstrap")
								.getMethod("initialize", String.class, Properties.class).invoke(null, portTmp, jmxProperties);
						usedJMXImplementationTmp = JMXImplementation.Sun;
					} catch (final Exception ignoreErrors) { // NOPMD NOCS (IllegalCatchCheck)
						if (configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_FALLBACK)) { // NOCS (NestedIf)
							LOG.warn("Failed to initialize remote JMX server, falling back to default implementation");
							// Fallback to default Implementation
							final JMXServiceURL url = new JMXServiceURL("rmi", null, Integer.parseInt(portTmp));
							final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
							serverTmp = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
							serverTmp.start();
						} else {
							LOG.warn("Failed to initialize remote JMX server and fallback is deactivated");
						}
					}
					if ((serverTmp != null) && (serverTmp.isActive())) { // NOCS (NestedIf)
						serverObjectNameTmp = new ObjectName(this.domain, "type",
								configuration.getStringProperty(ConfigurationFactory.ACTIVATE_JMX_REMOTE_NAME));
						serverNotificationListenerTmp = new ServerNotificationListener();
					}
				} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
					LOG.warn("Failed to initialize remote JMX server", e);
				}
			}
			if (configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER)) {
				try {
					controllerObjectNameTmp = new ObjectName(this.domain, "type",
							configuration.getStringProperty(ConfigurationFactory.ACTIVATE_JMX_CONTROLLER_NAME));
				} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
					LOG.warn("Failed to initialize MonitoringController MBean", e);
				}
			}
		}
		this.usedJMXImplementation = usedJMXImplementationTmp;
		this.port = portTmp;
		this.server = serverTmp;
		this.controllerObjectName = controllerObjectNameTmp;
		this.serverObjectName = serverObjectNameTmp;
		this.serverNotificationListener = serverNotificationListenerTmp;
	}

	// The error handling in this block is correct, see ticket #293

	@Override
	protected final void init() {
		synchronized (this) {
			if (this.jmxEnabled && !this.isTerminated()) {
				final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
				if (this.serverObjectName != null) {
					try {
						mbs.registerMBean(this.server, this.serverObjectName);
					} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
						LOG.warn("Unable to register JMXServer MBean", e);
					}
				}
				if (this.controllerObjectName != null) {
					try {
						// MXBeans is currently not possible (getClasses in IRecord)
						final StandardMBean mbean = new StandardMBean(this.monitoringController, IMonitoringController.class);
						mbs.registerMBean(mbean, this.controllerObjectName);
					} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
						LOG.warn("Unable to register Monitoring Controller MBean", e);
					}
				}
				if ((this.server != null) && this.server.isActive()) {
					this.server.addNotificationListener(this.serverNotificationListener, null, null);
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
					} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
						LOG.error("Failed to terminate MBean", e);
					}
				}
				if (this.serverObjectName != null) {
					try {
						mbs.unregisterMBean(this.serverObjectName);
					} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
						LOG.error("Failed to terminate MBean", e);
					}
				}
				if (this.server != null) {
					try {
						this.server.removeNotificationListener(this.serverNotificationListener);
					} catch (final ListenerNotFoundException e) {
						LOG.error("Failed to remove ServerNotificationListener", e);
					}
					try {
						this.server.stop();
					} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
						LOG.error("Failed to terminate JMX Server", e);
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
		final StringBuilder sb = new StringBuilder(255);
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
			sb.append("\tJMX remote access available:\n\t\tService URL: '");
			final JMXServiceURL url = this.server.getAddress();
			switch (this.usedJMXImplementation) { // NOPMD (extend in the future)
			case Sun:
				try {
					sb.append(new JMXServiceURL(url.getProtocol(), url.getHost(), url.getPort(), "/jndi/rmi://" + url.getHost() + ":" + this.port + "/" + "jmxrmi")
							.toString());
				} catch (final MalformedURLException ignoreErrors) {
					sb.append("unable to determine JMXServiceURL (" + ignoreErrors.toString() + ")");
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
				for (final String connection : connections) {
					sb.append("\t\tRemote connection: '");
					sb.append(connection);
					sb.append("'\n");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @author Jan Waller
	 */
	private static final class ServerNotificationListener implements NotificationListener {

		/**
		 * Constructs a {@link ServerNotificationListener}.
		 */
		public ServerNotificationListener() {
			// nothing to do
		}

		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType.equals(JMXConnectionNotification.OPENED)) {
				LOG.info("New JMX remote connection initialized. Connection ID: "
						+ (notification instanceof JMXConnectionNotification ? ((JMXConnectionNotification) notification).getConnectionId() : "unknown")); // NOCS
			} else if (notificationType.equals(JMXConnectionNotification.CLOSED)) {
				LOG.info("JMX remote connection closed. Connection ID: "
						+ (notification instanceof JMXConnectionNotification ? ((JMXConnectionNotification) notification).getConnectionId() : "unknown")); // NOCS
			} else { // unknown message
				LOG.info(notificationType + ": " + notification.getMessage() + " (ID: "
						+ (notification instanceof JMXConnectionNotification ? ((JMXConnectionNotification) notification).getConnectionId() : "unknown") + ")"); // NOCS
			}
		}
	}
}
