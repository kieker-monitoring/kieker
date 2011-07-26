package kieker.analysis.reader.jmx;

import java.io.IOException;
import java.net.MalformedURLException;

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
 * TODO: refactor analysis readers similar to monitoring writers!
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

	@Override
	public final boolean init(final String initString) {
		try {
			// TODO: "=" may be part of the serviceURL -> @
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
		} catch (final Exception e) {
			JMXReader.log.error("Failed to parse initString '" + initString + "': " + e.getMessage());
			return false;
		}
		return true;
	}

	private void initInstanceFromArgs(final String serviceURL, final String domain, final String logname) throws IllegalArgumentException,
			MalformedURLException, MalformedObjectNameException {
		if (serviceURL == null) {
			throw new IllegalArgumentException("JMXReader has not sufficient parameters. serviceURL ('" + serviceURL + "') is null");
		}
		this.serviceURL = new JMXServiceURL(serviceURL);
		this.monitoringLog = new ObjectName(domain, "type", logname);
	}

	@Override
	public final boolean read() {
		if (silentreconnect) return read2();
		boolean ret = true;
		JMXConnector jmx = null;
		try {
			// Connect to the Server
			try {
				jmx = JMXConnectorFactory.connect(serviceURL);
			} catch (final IOException e) {
				JMXReader.log.error("Unable to connect to JMX Server (" + e.getMessage() + ")");
				JMXReader.log.debug("Error in JMX connection!", e);
				return false;
			}
			final ServerNotificationListener serverNotificationListener = new ServerNotificationListener();
			jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
			final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();
			JMXReader.log.info("Connected to JMX Server, ID: " + jmx.getConnectionId());
			final LogNotificationListener logNotificationListener = new LogNotificationListener();
			mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);

			// Waiting
			block();

			// Shutdown
			JMXReader.log.info("Shutting down JMXReader");
			try {
				mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
			} catch (final Exception e) {
				JMXReader.log.debug("Failed to remove Listener!", e);
			}
			try {
				jmx.removeConnectionNotificationListener(serverNotificationListener);
			} catch (final ListenerNotFoundException e) {
				JMXReader.log.debug("Failed to remove Listener!", e);
			}
		} catch (final InstanceNotFoundException e) {
			JMXReader.log.error("No monitoring log found: " + this.monitoringLog.toString());
			ret = false;
		} catch (final Exception e) {
			JMXReader.log.error("Error in JMX connection!", e);
			ret = false;
		} finally {
			try {
				jmx.close();
			} catch (final Exception e) {
				JMXReader.log.debug("Failed to close JMX connection!", e);
			}
		}
		return ret;
	}

	private final boolean read2() {
		JMXConnector jmx = null;
		while (true) {
			try {
				// Connect to the Server
				try {
					jmx = JMXConnectorFactory.connect(serviceURL);
				} catch (final IOException e) {
					Thread.sleep(10000);
					continue;
				}
				final ServerNotificationListener serverNotificationListener = new ServerNotificationListener();
				jmx.addConnectionNotificationListener(serverNotificationListener, null, null);
				final MBeanServerConnection mbServer = jmx.getMBeanServerConnection();
				final LogNotificationListener logNotificationListener = new LogNotificationListener();
				mbServer.addNotificationListener(this.monitoringLog, logNotificationListener, null, null);
				JMXReader.log.info("Connected to JMX Server, ID: " + jmx.getConnectionId());

				// Waiting
				block();

				// Shutdown
				JMXReader.log.info("Shutting down JMXReader");
				try {
					mbServer.removeNotificationListener(this.monitoringLog, logNotificationListener);
				} catch (final Exception e) {
				}
				try {
					jmx.removeConnectionNotificationListener(serverNotificationListener);
				} catch (final ListenerNotFoundException e) {
				}
			} catch (final InstanceNotFoundException e) {
			} catch (final Exception e) {
				JMXReader.log.error("Error in JMX connection!", e);
			} finally {
				try {
					jmx.close();
					Thread.sleep(10000);
				} catch (final Exception e) {
				}
			}
		}
	}

	private final Object blockingObj = new Object();

	private final void block() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public final void run() {
				unblock();
			}
		});
		synchronized (blockingObj) {
			try {
				blockingObj.wait();
			} catch (final InterruptedException e) { // ignore
			}
		}
	}

	private final void unblock() {
		synchronized (blockingObj) {
			blockingObj.notifyAll();
		}
	}

	private final class LogNotificationListener implements NotificationListener {
		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			deliverRecord((IMonitoringRecord) notification.getUserData());
		}
	}

	private final class ServerNotificationListener implements NotificationListener {
		@Override
		public final void handleNotification(final Notification notification, final Object handback) {
			final String notificationType = notification.getType();
			if (notificationType == JMXConnectionNotification.CLOSED) {
				if (!silentreconnect) JMXReader.log.info("JMX connection closed.");
				unblock();
			} else if (notificationType == JMXConnectionNotification.FAILED) {
				if (!silentreconnect) JMXReader.log.info("JMX connection lost.");
				unblock();
			} else if (notificationType == JMXConnectionNotification.NOTIFS_LOST) {
				JMXReader.log.error("Monitoring record lost: " + notification.getMessage());
			} else { // unknown message
				JMXReader.log.info(notificationType + ": " + notification.getMessage());
			}
		}
	}
}
