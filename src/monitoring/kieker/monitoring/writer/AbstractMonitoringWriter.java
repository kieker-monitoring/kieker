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

package kieker.monitoring.writer;

import java.util.Properties;
import java.util.Set;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller, Robert von Massow
 */
public abstract class AbstractMonitoringWriter implements IMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(AbstractMonitoringWriter.class);

	protected final Configuration configuration;
	protected IMonitoringController monitoringController;

	/**
	 * 
	 * @param IWriterController
	 * @param configuration
	 */
	protected AbstractMonitoringWriter(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Properties defaultProps = this.getDefaultProperties(); // NOPMD
			if (defaultProps != null) {
				configuration.setDefaultProperties(defaultProps);
			}
		} catch (final IllegalAccessException ex) {
			AbstractMonitoringWriter.LOG.error("Unable to set writer custom default properties");
		}
		this.configuration = configuration;
	}

	/**
	 * This method should be overwritten, iff the writer is external to Kieker and
	 * thus its default configuration is not included in the default config file.
	 * 
	 * @return
	 */
	protected Properties getDefaultProperties() { // NOPMD
		return null;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
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
				sb.append(this.configuration.getProperty(property));
				sb.append("'");
			}
		}
		return sb.toString();
	}

	@Override
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	@Override
	public abstract void terminate();

	@Override
	public final void setController(final IMonitoringController monitoringController) throws Exception {
		this.monitoringController = monitoringController;
		this.init();
	}

	public final Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * Implementing classes should indicate an initialization
	 * error by throwing an {@link Exception}.
	 * 
	 * @throws Exception
	 */
	protected abstract void init() throws Exception;
}
