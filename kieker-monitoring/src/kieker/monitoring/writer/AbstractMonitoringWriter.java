/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer;

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

	/** The writer's configuration. */
	protected final Configuration configuration;

	public AbstractMonitoringWriter(final Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * This event fires when Kieker has been initialized and is ready to monitor.
	 * It is executed by the {@link MonitoringWriterThread} just before reading the writer queue.
	 */
	public abstract void onStarting();

	/**
	 * This event fires when Kieker has received a new record.
	 *
	 * @param record
	 *            a monitoring record
	 */
	public abstract void writeMonitoringRecord(IMonitoringRecord record);

	/**
	 * This event fires when Kieker has been notified to terminate.
	 * It is executed by the {@link MonitoringWriterThread} just after finishing the writer queue.
	 */
	public abstract void onTerminating();

	/**
	 * Returns a textual representation of the writer's configuration.
	 *
	 * @return a textual representation of the writer's configuration
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(128)
				.append("Writer: '")
				.append(this.getClass().getName())
				.append("'\n\tConfiguration:");
		final Set<String> keys = this.configuration.stringPropertyNames();
		if (keys.isEmpty()) {
			sb.append("\n\t\tNo Configuration");
		} else {
			for (final String property : keys) {
				if (property.startsWith(this.getClass().getName())) {
					sb.append("\n\t\t");
					sb.append(property);
					sb.append("='");
					sb.append(this.configuration.getStringProperty(property));
					sb.append('\'');
				}
			}
		}
		return sb.toString();
	}

}
