/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.logReplayer;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * Passes {@link IMonitoringRecord}s received via its input port {@link #INPUT_PORT_NAME_RECORD} to its own {@link IMonitoringController} instance,
 * which is created based on the {@link Configuration} file passed via the filter's property {@link #CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN}.
 * Additionally, incoming records are relayed via the output port {@link #OUTPUT_PORT_NAME_RELAYED_EVENTS}.
 * 
 * @author Andre van Hoorn
 * 
 */
// TODO: We should move this class to another package
@Plugin(description = "A filter which passes received records to the configured monitoring controller",
		outputPorts = @OutputPort(name = MonitoringRecordLoggerFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming monitoring record", eventTypes = { IMonitoringRecord.class }))
public class MonitoringRecordLoggerFilter extends AbstractFilterPlugin {
	public static final String INPUT_PORT_NAME_RECORD = "monitoringRecords";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN = "monitoringPropertiesFilename";

	private static final Log LOG = LogFactory.getLog(MonitoringRecordLoggerFilter.class);

	/**
	 * The {@link IMonitoringController} the records received via {@link #inputIMonitoringRecord(IMonitoringRecord)} are passed to.
	 */
	private final IMonitoringController monitoringController;

	/**
	 * Path to the {@link Configuration} to be used to create {@link #monitoringController}.
	 * If <code>null</code>, the {@link ConfigurationFactory#createDefaultConfiguration()} is used.
	 */
	private final String monitoringPropertiesFn;

	/**
	 * Used to cache the configuration.
	 */
	final Configuration configuration;

	public MonitoringRecordLoggerFilter(final Configuration configuration) {
		super(configuration);
		final Configuration controllerConfiguration;
		this.monitoringPropertiesFn = configuration.getStringProperty(CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN);
		if (this.monitoringPropertiesFn.length() > 0) {
			controllerConfiguration = ConfigurationFactory.createConfigurationFromFile(this.monitoringPropertiesFn);
		} else {
			LOG.info("No path to a 'monitoring.properties' file passed; using default configuration");
			controllerConfiguration = ConfigurationFactory.createDefaultConfiguration();
		}
		// flatten submitted properties
		final Configuration flatConfiguration = configuration.getPropertiesStartingWith("");
		// save before adding the defaults
		this.configuration = (Configuration) flatConfiguration.clone();
		try {
			flatConfiguration.setDefaultConfiguration(controllerConfiguration);
		} catch (final IllegalAccessException ex) {
			throw new RuntimeException(ex); // cannot happen (due to flatten)!
		}
		this.monitoringController = MonitoringController.createInstance(flatConfiguration);
	}

	public Configuration getCurrentConfiguration() {
		// clone again, so no one can change anything
		return (Configuration) this.configuration.clone();
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_MONITORING_PROPS_FN, "");
		return configuration;
	}

	@InputPort(name = INPUT_PORT_NAME_RECORD, description = "Receives records to be passed to the controller", eventTypes = { IMonitoringRecord.class })
	public final void inputIMonitoringRecord(final IMonitoringRecord record) {
		this.monitoringController.newMonitoringRecord(record);
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
	}
}
