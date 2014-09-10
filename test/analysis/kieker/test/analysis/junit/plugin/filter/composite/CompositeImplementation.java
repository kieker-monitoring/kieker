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

package kieker.test.analysis.junit.plugin.filter.composite;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.composite.AbstractCompositeFilterPlugin;
import kieker.analysis.plugin.filter.composite.CompositeInputRelay;
import kieker.analysis.plugin.filter.composite.CompositeOutputRelay;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Markus Fischer
 * @since 1.10
 * 
 */
@Plugin(description = "CompositeTestPlugin",
		outputPorts = {
			@OutputPort(name = CompositeImplementation.OUPUT_PORT_MR, eventTypes = { IMonitoringRecord.class }),
			@OutputPort(name = CompositeImplementation.OUPUT_PORT_STRING, eventTypes = { String.class }),
			@OutputPort(name = CompositeImplementation.OUPUT_PORT_INT, eventTypes = { Integer.class }) })
public class CompositeImplementation extends AbstractCompositeFilterPlugin {

	/**
	 * InputportName.
	 */
	public static final String INPUT_PORT = "composite-test-inputport";

	/**
	 * OutputportName.
	 */
	public static final String OUPUT_PORT_MR = "composite-test-inputport-mr";

	/**
	 * OutputportName.
	 */
	public static final String OUPUT_PORT_STRING = "composite-test-inputport-string";

	/**
	 * OutputportName.
	 */
	public static final String OUPUT_PORT_INT = "composite-test-inputport-int";
	private final Distributor distributor;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            Configuratione
	 * @param projectContext
	 *            IProjectContext
	 */
	public CompositeImplementation(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		Configuration distributorConfiguration = new Configuration();
		distributorConfiguration = this.updateConfiguration(distributorConfiguration, Distributor.class);
		this.distributor = new Distributor(distributorConfiguration, this.controller);

		try {
			// only relay messages through the plugin
			this.controller.connect(this.inputRelay, CompositeInputRelay.INPUTRELAY_OUTPUTPORT, this.outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);
			this.controller.connect(this.inputRelay, CompositeInputRelay.INPUTRELAY_OUTPUTPORT, this.distributor, Distributor.INPUT);
			this.controller.connect(this.distributor, Distributor.OUPUT_INT, this.outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);
			this.controller.connect(this.distributor, Distributor.OUPUT_STRING, this.outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);
		} catch (final IllegalStateException e) {
			this.nothing();
			// e.printStackTrace();
		} catch (final AnalysisConfigurationException e) {
			this.nothing();
			// e.printStackTrace();
		}
	}

	/**
	 * empty Method to avoid empty catch-blocks(PMD).
	 */
	private void nothing() {
		// does nothing
	}

	public Configuration getDistributorConfiguration() {
		return this.distributor.getCurrentConfiguration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return this.configuration;
	}

	/**
	 * Inputportmethod.
	 * 
	 * @param monitoringRecord
	 *            IMonitoringRecord
	 */
	@InputPort(name = CompositeImplementation.INPUT_PORT, eventTypes = { IMonitoringRecord.class })
	public void startAnalysis(final IMonitoringRecord monitoringRecord) {
		this.inputRelay.relayMessage(monitoringRecord);
	}

	/**
	 * Innerclass for Testing.<br>
	 * 
	 * For every incoming IMonitoringRecord sends a string and an integer on its way.
	 * 
	 * @author Markus Fischer
	 * @since 1.10
	 * 
	 */
	@Plugin(description = "Distributor",
			outputPorts = {
				@OutputPort(name = Distributor.OUPUT_STRING, eventTypes = { String.class }),
				@OutputPort(name = Distributor.OUPUT_INT, eventTypes = { Integer.class }) },
			configuration = {
				@Property(name = Distributor.TEST_PROPERTY_ONE, defaultValue = Distributor.TEST_PROPERTY_ONE),
				@Property(name = Distributor.TEST_PROPERTY_TWO, defaultValue = Distributor.TEST_PROPERTY_TWO)
			})
	public class Distributor extends AbstractFilterPlugin {

		/**
		 * InputPortname.
		 */
		public static final String INPUT = "dist-input";

		/**
		 * OutputPortname.
		 */
		public static final String OUPUT_STRING = "dist-string";

		/**
		 * OutputPortname.
		 */
		public static final String OUPUT_INT = "dist-int";

		/**
		 * PopertyName.
		 */
		public static final String TEST_PROPERTY_ONE = "property-one";

		/**
		 * PopertyName.
		 */
		public static final String TEST_PROPERTY_TWO = "property-two";

		/**
		 * Constructor.
		 * 
		 * @param configuration
		 *            configuration
		 * @param projectContext
		 *            projectContext
		 */
		public Distributor(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return this.configuration;
		}

		/**
		 * Inputport-Method.
		 * 
		 * @param monitoringRecord
		 *            IMonitoringRecord
		 */
		@InputPort(name = Distributor.INPUT, eventTypes = { IMonitoringRecord.class })
		public void startAnalysis(final IMonitoringRecord monitoringRecord) {
			this.deliver(OUPUT_STRING, "");
			this.deliver(OUPUT_INT, 1337);

		}

	}

}
