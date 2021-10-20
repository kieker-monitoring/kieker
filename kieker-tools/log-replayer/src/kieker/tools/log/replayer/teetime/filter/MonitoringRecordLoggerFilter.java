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

package kieker.tools.log.replayer.teetime.filter;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import teetime.stage.basic.AbstractFilter;

/**
 * Passes {@link IMonitoringRecord}s received via its input port to its own {@link IMonitoringController} instance,
 * which is passed in the constructor. Additionally, incoming records are relayed via the output port.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.6
 */
public class MonitoringRecordLoggerFilter extends AbstractFilter<IMonitoringRecord> {

	private static final String CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN = "monitoringPropertiesFilename";
	private static final int WAIT_TIMEOUT_IN_MS = 1000;

	/**
	 * The {@link IMonitoringController} the received records are passed to.
	 */
	private final IMonitoringController monitoringController;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param monitoringConfigurationFile
	 *            The name of the {@code monitoring.properties} file.
	 * @param keepOriginalLoggingTimestamps
	 *            Determines whether the original logging timestamps will be used of whether the timestamps will be
	 *            modified.
	 *
	 * @since 1.7
	 */
	public MonitoringRecordLoggerFilter(final String monitoringConfigurationFile,
			final boolean keepOriginalLoggingTimestamps) {
		if (monitoringConfigurationFile == null) {
			throw new IllegalArgumentException("Parameter 'monitoringConfigurationFile' is null, but may not be null.");
		}

		// Initialize a "traditional" Configuration for initializing a monitoring controller later
		final Configuration configuration = new Configuration();
		configuration.setProperty(MonitoringRecordLoggerFilter.CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN,
				monitoringConfigurationFile);
		configuration.setProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP,
				Boolean.toString(!keepOriginalLoggingTimestamps));

		final Configuration controllerConfiguration;
		if (monitoringConfigurationFile.length() > 0) {
			controllerConfiguration = ConfigurationFactory.createConfigurationFromFile(monitoringConfigurationFile);
		} else {
			this.logger.info("No path to a 'monitoring.properties' file passed; using default configuration");
			controllerConfiguration = ConfigurationFactory.createDefaultConfiguration();
		}
		// flatten submitted properties
		final Configuration flatConfiguration = configuration.flatten();
		flatConfiguration.setDefaultConfiguration(controllerConfiguration);
		this.monitoringController = MonitoringController.createInstance(flatConfiguration);
	}

	/**
	 * This method represents the input port. The new records are send to the monitoring controller before they are
	 * delivered via the output port.
	 *
	 * @param record
	 *            The next record.
	 */
	@Override
	protected void execute(final IMonitoringRecord record) {
		this.monitoringController.newMonitoringRecord(record);
		this.outputPort.send(record);
	}

	@Override
	protected void onTerminating() {
		this.monitoringController.terminateMonitoring();
		try {
			this.monitoringController.waitForTermination(WAIT_TIMEOUT_IN_MS);
		} catch (final InterruptedException e) {
			throw new IllegalStateException(e);
		} finally {
			super.onTerminating();
		}
	}
}
