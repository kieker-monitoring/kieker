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

package kieker.monitoring.writer;

import java.util.Set;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Jan Waller, Robert von Massow
 * 
 * @since 1.3
 */
public abstract class AbstractMonitoringWriter implements IMonitoringWriter {

	/** The controller of this writer. */
	protected IMonitoringController monitoringController;
	private final Configuration configuration;

	/**
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 */
	protected AbstractMonitoringWriter(final Configuration configuration) {
		// somewhat dirty hack...
		final Configuration defaultConfiguration = this.getDefaultConfiguration(); // NOPMD (overrideable)
		if (defaultConfiguration != null) {
			configuration.setDefaultConfiguration(defaultConfiguration);
		}
		this.configuration = configuration;
	}

	/**
	 * This method should be overwritten, iff the writer is external to Kieker and
	 * thus its default configuration is not included in the default config file.
	 * 
	 * @return The configuration object containing the default configuration.
	 */
	protected Configuration getDefaultConfiguration() { // NOPMD (default implementation)
		return null;
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

	@Override
	public final void setController(final IMonitoringController controller) throws Exception {
		this.monitoringController = controller;
		this.init();
	}

	/**
	 * Implementing classes should indicate an initialization error by throwing an {@link Exception}.
	 * 
	 * @throws Exception
	 *             If something during the initialization went wrong.
	 */
	protected abstract void init() throws Exception;

	/**
	 * Overwrite this method if necessary.
	 */
	@Override
	public boolean newMonitoringRecordNonBlocking(final IMonitoringRecord record) {
		return this.newMonitoringRecord(record);
	}
}
