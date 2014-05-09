/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.composite;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Composite Filter Plug-in Example, not for actual use.
 * 
 * @author Markus Fischer
 * @version 0.1
 */
@Plugin(description = "CompositeFilterPluginExample",
		outputPorts = {
			@OutputPort(name = CompositeFilterPluginExample.OUPUT_PORT_MONITORING_RECORD, eventTypes = { IMonitoringRecord.class }) },
		configuration = {
			@Property(name = CompositeFilterPluginExample.PROPERTY_NAME, defaultValue = CompositeFilterPluginExample.PROPERTY_DEFAULT) })
public class CompositeFilterPluginExample extends AbstractCompositeFilterPlugin {

	/**
	 * Exemplary ports.
	 */
	public static final String OUPUT_PORT_MONITORING_RECORD = "AbstractCompositeFilterPlugin_Example_MonitoringRecord_Output_Port";
	/**
	 * PropertyName.
	 */
	public static final String PROPERTY_NAME = "4the_answer";
	/**
	 * PropertyDefault.
	 */
	public static final String PROPERTY_DEFAULT = "42";

	private static final String INPUT_PORT = "AbstractCompositeFilterPlugin_Example_MonitoringRecord_Input_Port";

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            - configuration
	 * @param projectContext
	 *            - IProjectContext
	 */
	public CompositeFilterPluginExample(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// create Filters to be used in this composite filter
		final Configuration teeConfig = new Configuration();

		// update the configuration with values from the composites configuration
		this.updateConfiguration(teeConfig, TeeFilter.class);
		final TeeFilter tee = new TeeFilter(teeConfig, this.controller);

		try {

			// connect the plugins.

			// connect InputRelay to TeeFilter
			this.controller.connect(this.inputRelay, CompositeInputRelay.INPUTRELAY_OUTPUTPORT, tee, TeeFilter.INPUT_PORT_NAME_EVENTS);
			// connect TeeFilter to OutputRelay
			this.controller.connect(tee, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, this.outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);

		} catch (final IllegalStateException e) { // NOPMD
			// doNothing
		} catch (final AnalysisConfigurationException e) { // NOPMD
			// doNothing

		}

	}

	/**
	 * Inputport. Called when a message arrives at the defined Inputport.
	 * 
	 * @param monitoringRecord
	 *            - Incoming IMonitoringRecord.
	 */
	@InputPort(name = CompositeFilterPluginExample.INPUT_PORT, eventTypes = { IMonitoringRecord.class })
	public void startAnalysis(final IMonitoringRecord monitoringRecord) {
		this.inputRelay.relayMessage(monitoringRecord);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		if (!this.configuration.containsKey(PROPERTY_NAME)) {
			this.configuration.setProperty(PROPERTY_NAME, PROPERTY_DEFAULT);
		}
		return this.configuration;
	}

}
