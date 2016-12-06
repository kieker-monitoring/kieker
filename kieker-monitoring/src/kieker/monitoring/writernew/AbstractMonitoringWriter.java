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

package kieker.monitoring.writernew;

import java.util.Set;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 *
 */
public abstract class AbstractMonitoringWriter {

	protected final Configuration configuration;

	// private final BlockingQueue<IMonitoringRecord> queue;
	//
	// public AbstractMonitoringWriter(final BlockingQueue<IMonitoringRecord> queue) {
	// this.queue = queue;
	// }
	//
	// protected BlockingQueue<IMonitoringRecord> getQueue() {
	// return this.queue;
	// }

	/**
	 * This event fires when Kieker has been initialized and is ready to monitor.
	 * It is executed by the {@link MonitoringWriterThread} just before reading the writer queue.
	 */
	public abstract void onStarting();

	public abstract void writeMonitoringRecord(IMonitoringRecord record);

	/**
	 * This event fires when Kieker has been notified to terminate.
	 * It is executed by the {@link MonitoringWriterThread} just after finishing the writer queue.
	 */
	public abstract void onTerminating();

	public AbstractMonitoringWriter(final Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("Writer: '");
		sb.append(this.getClass().getName());
		sb.append("'\n\tConfiguration:");
		final Set<String> keys = this.configuration.stringPropertyNames();
		if (keys.isEmpty()) {
			sb.append("\n\t\tNo Configuration");
		} else {
			for (final String property : keys) {
				sb.append("\n\t\t");
				sb.append(property);
				sb.append("='");
				sb.append(this.configuration.getStringProperty(property));
				sb.append('\'');
			}
		}
		return sb.toString();
	}

}
