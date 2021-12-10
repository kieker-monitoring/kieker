/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.jmx;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Jan Waller
 *
 * @since 1.4
 */
public final class KiekerJmxMonitoringLog extends NotificationBroadcasterSupport implements KiekerJmxMonitoringLogMBean {

	private static final String MESSAGE_TYPE = null;
	private final ObjectName kiekerMonitoringLogName;

	public KiekerJmxMonitoringLog(final ObjectName kiekerMonitoringLogName) {
		this.kiekerMonitoringLogName = kiekerMonitoringLogName;
	}

	/**
	 * Consumes the given record by sending a notification.
	 *
	 * @param record
	 *            The record to consume.
	 *
	 * @return Always true.
	 */
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		final Notification notification = new Notification(MESSAGE_TYPE, this.kiekerMonitoringLogName, 0L, 0L);
		notification.setUserData(record);
		super.sendNotification(notification);
		return true;
	}
}
