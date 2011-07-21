package kieker.monitoring.core.controller;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
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

	/** Name of the MBean. */
	private final boolean jmxEnabled;
	private final String domain;
	private final ObjectName controllerObjectName;
	private final ObjectName remoteObjectName;
	private final JMXConnectorServer jmxserver;

	protected JMXController(final Configuration configuration) {
		ObjectName controllerObjectName = null;
		ObjectName remoteObjectName = null;
		JMXConnectorServer jmxserver = null;
		this.domain = configuration.getStringProperty(Configuration.ACTIVATE_JMX_DOMAIN);
		this.jmxEnabled = configuration.getBooleanProperty(Configuration.ACTIVATE_JMX);
		if (this.jmxEnabled) {
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_CONTROLLER)) {
				try {
					controllerObjectName = new ObjectName(this.domain, "type", configuration.getStringProperty(Configuration.ACTIVATE_JMX_CONTROLLER_NAME));
				} catch (final Exception e) {
					JMXController.log.warn("Failed to initialize MonitoringController MBean", e);
				}
			}
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_REMOTE)) {
				try {
					final JMXServiceURL serviceURL = new JMXServiceURL(configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_URL));
					jmxserver = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, null, null);
					remoteObjectName = new ObjectName(this.domain, "type", configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_NAME));
				} catch (final Exception e) {
					JMXController.log.warn("Failed to initialize JMXServer", e);
				}
			}
		}
		this.controllerObjectName = controllerObjectName;
		this.remoteObjectName = remoteObjectName;
		this.jmxserver = jmxserver;
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
						mbs.registerMBean(mbean, controllerObjectName);
					} catch (final Exception e) {
						JMXController.log.warn("Unable to register Monitoring Controller MBean", e);
					}
				}
				if (this.jmxserver != null) {
					try {
						mbs.registerMBean(this.jmxserver, this.remoteObjectName);
						this.jmxserver.start();
					} catch (final Exception e) {
						JMXController.log.warn("Unable to register JMXServer", e);
					}
				}
			}
		}

	}

	@Override
	protected final void cleanup() {
		synchronized (this) {
			if (this.controllerObjectName != null) {
				try {
					ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.controllerObjectName);
				} catch (final Exception e) {
					JMXController.log.error("Failed to terminate MBean", e);
				}
			}
			if (this.jmxserver != null) {
				try {
					this.jmxserver.stop();
				} catch (final IOException e) {
					JMXController.log.error("Failed to terminate JMXServer", e);
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
		if ((this.jmxserver != null) && (this.jmxserver.isActive())) {
			sb.append("\tJMX remote access available:\n");
			sb.append("\t\tService URL: '");
			sb.append(this.jmxserver.getAddress().toString());
			sb.append("'\n");
			final String[] connections = this.jmxserver.getConnectionIds();
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
