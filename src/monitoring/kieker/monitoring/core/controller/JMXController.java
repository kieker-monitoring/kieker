package kieker.monitoring.core.controller;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;

import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.management.jmxremote.ConnectorBootstrap;

/**
 * @author Jan Waller
 */
public final class JMXController extends AbstractController implements IJMXController {
	private final static Log log = LogFactory.getLog(JMXController.class);

	/** Name of the MBean. */
	private final boolean jmxEnabled;
	private final String domain;
	private final ObjectName controllerObjectName;
	private final JMXConnectorServer server;
	private final String port;

	protected JMXController(final Configuration configuration) {
		ObjectName controllerObjectName = null;
		JMXConnectorServer server = null;
		String port = "0";
		this.domain = configuration.getStringProperty(Configuration.ACTIVATE_JMX_DOMAIN);
		this.jmxEnabled = configuration.getBooleanProperty(Configuration.ACTIVATE_JMX);
		if (this.jmxEnabled) {
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_REMOTE)) {
				final Properties jmxProperties = configuration.getPropertiesStartingWith("com.sun.management.jmxremote");
				try {
					// TODO: careful! This will probably only work in SUN VMs, what happens in other VMs?
					port = configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_PORT);
					server = ConnectorBootstrap.initialize(port, jmxProperties);
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
		this.port = port;
		this.server = server;
		this.controllerObjectName = controllerObjectName;
	}

	@Override
	protected void init() {
		synchronized (this) {
			if (this.jmxEnabled && !this.isTerminated()) {
				final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
				if (this.controllerObjectName != null) {
					try {
						// MXBeans is currently not possible (getClasses in IRecord)
						final StandardMBean mbean = new StandardMBean(monitoringController, IMonitoringController.class, false);
						mbs.registerMBean(mbean, this.controllerObjectName);
					} catch (final Exception e) {
						JMXController.log.warn("Unable to register Monitoring Controller MBean", e);
					}
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
				if (this.server != null) {
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
		if ((this.server != null) && (this.server.isActive())) {
			sb.append("\tJMX remote access available:\n");
			sb.append("\t\tService URL: '");
			final JMXServiceURL url = this.server.getAddress();
			try {
				sb.append(new JMXServiceURL(url.getProtocol(), url.getHost(), url.getPort(), "/jndi/rmi://" + url.getHost() + ":" + this.port + "/" + "jmxrmi")
						.toString());
			} catch (final MalformedURLException e) {
				// ignore, should not happen anyway
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
}
