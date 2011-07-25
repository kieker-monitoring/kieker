package kieker.monitoring.writer.jmx;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Jan Waller
 */
public final class KiekerJMXMonitoringLog extends NotificationBroadcasterSupport implements KiekerJMXMonitoringLogMBean {

	private final static String MESSAGE_TYPE = null;
	private final ObjectName kiekerMonitoringLogName;

	public KiekerJMXMonitoringLog(final ObjectName kiekerMonitoringLogName) {
		this.kiekerMonitoringLogName = kiekerMonitoringLogName;
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		// TODO: are there problems with these extremely short messages?
		final Notification notification = new Notification(MESSAGE_TYPE, kiekerMonitoringLogName, 0L, 0L);
		notification.setUserData(record);
		super.sendNotification(notification);
		return true;
	}
}
