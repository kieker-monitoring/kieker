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
			this.controller.connect(inputRelay, CompositeInputRelay.INPUTRELAY_OUTPUTPORT, tee, TeeFilter.INPUT_PORT_NAME_EVENTS);
			// connect TeeFilter to OutputRelay
			this.controller.connect(tee, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);

		} catch (IllegalStateException e) { // NOPMD
			// doNothing
		} catch (AnalysisConfigurationException e) { // NOPMD
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
		if (!configuration.containsKey(PROPERTY_NAME)) {
			configuration.setProperty(PROPERTY_NAME, PROPERTY_DEFAULT);
		}
		return configuration;
	}

}
