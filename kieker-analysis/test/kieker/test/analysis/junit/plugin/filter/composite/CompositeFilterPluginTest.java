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

// CHECKSTYLE:OFF
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.composite.AbstractCompositeFilterPlugin;
import kieker.analysis.plugin.filter.composite.PortWrapper;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;

import kieker.test.analysis.junit.plugin.filter.composite.CompositeImplementation.Distributor;
import kieker.test.common.junit.AbstractKiekerTest;

//CHECKSTYLE:ON

/**
 * This test is set up with {@link CompositeImplementation}, Within the CompositeImplementation
 * every incoming IMonitoringRecord is passed through and for every IMonitoringRecord a String and an
 * Integer are broadcasted via their respective Outputports.
 * 
 * @author Markus Fischer
 * @since 1.10
 * 
 */
public class CompositeFilterPluginTest extends AbstractKiekerTest {

	private static String operationSignature = "signature";
	private static String hostName = "hostname";
	private static long timeOut = 2000L;
	private static long timeIn = 1337L;
	private static final String NEW_PROPERTY = "testProperty";

	private final IAnalysisController analysisController;
	private final ListReader<OperationExecutionRecord> simpleListReader;
	private final ListCollectionFilter<OperationExecutionRecord> listCollectionfilterOER;
	private final ListCollectionFilter<String> listCollectionfilterSTR;
	private final ListCollectionFilter<Integer> listCollectionfilterINT;
	private final CompositeImplementation compositePlugin;

	/**
	 * Contructor.
	 */
	public CompositeFilterPluginTest() {
		this.analysisController = new AnalysisController();
		this.simpleListReader = new ListReader<OperationExecutionRecord>(new Configuration(), this.analysisController);
		this.listCollectionfilterOER = new ListCollectionFilter<OperationExecutionRecord>(new Configuration(), this.analysisController);
		this.listCollectionfilterSTR = new ListCollectionFilter<String>(new Configuration(), this.analysisController);
		this.listCollectionfilterINT = new ListCollectionFilter<Integer>(new Configuration(), this.analysisController);

		final Configuration compositeConfiguration = new Configuration();
		compositeConfiguration.setProperty(Distributor.class.getSimpleName() + "." + Distributor.TEST_PROPERTY_ONE, CompositeFilterPluginTest.NEW_PROPERTY);
		this.compositePlugin = new CompositeImplementation(compositeConfiguration, this.analysisController);
	}

	/**
	 * Sets up the TestEnvironment.
	 */
	@Before
	public void setUp() {
		try {
			this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, this.compositePlugin, CompositeImplementation.INPUT_PORT);
			this.analysisController.connect(this.compositePlugin, CompositeImplementation.OUPUT_PORT_MR, this.listCollectionfilterOER,
					ListCollectionFilter.INPUT_PORT_NAME);
			this.analysisController.connect(this.compositePlugin, CompositeImplementation.OUPUT_PORT_STRING, this.listCollectionfilterSTR,
					ListCollectionFilter.INPUT_PORT_NAME);
			this.analysisController.connect(this.compositePlugin, CompositeImplementation.OUPUT_PORT_INT, this.listCollectionfilterINT,
					ListCollectionFilter.INPUT_PORT_NAME);

			this.simulate();
		} catch (final IllegalStateException e) {
			this.nothing();
		} catch (final AnalysisConfigurationException e) {
			this.nothing();
		}
	}

	/**
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 * 
	 */
	private void simulate() throws IllegalStateException, AnalysisConfigurationException {
		final OperationExecutionRecord oer = new OperationExecutionRecord(operationSignature,
				OperationExecutionRecord.NO_SESSION_ID,
				OperationExecutionRecord.NO_TRACE_ID,
				timeIn,
				timeOut,
				hostName,
				OperationExecutionRecord.NO_EOI_ESS,
				OperationExecutionRecord.NO_EOI_ESS);

		this.simpleListReader.addObject(oer);
		this.analysisController.run();
	}

	/**
	 * The Configuration of the CompositeImplementation has one property for the inner Filter 'Distributor".<br>
	 * So the inner configuration has to be updated.
	 */
	@Test
	public void testInternallyUpdatedConfiguration() {
		Assert.assertEquals("Configuration of Distributor was not updated", CompositeFilterPluginTest.NEW_PROPERTY,
				this.compositePlugin.getDistributorConfiguration().getStringProperty(Distributor.TEST_PROPERTY_ONE));
	}

	/**
	 * Tests if the right number of messages were sent.
	 */
	@Test
	public void testListCollectionSizeInt() {
		Assert.assertEquals("Wrong number of Integer messages", 1, this.listCollectionfilterINT.size());
	}

	/**
	 * Tests if the right number of messages were sent.
	 */
	@Test
	public void testListCollectionSizeStr() {
		Assert.assertEquals("Wrong number of String messages", 1, this.listCollectionfilterSTR.size());
	}

	/**
	 * Tests if the right number of messages were sent.
	 */
	@Test
	public void testListCollectionSizeOer() {
		Assert.assertEquals("Wrong number of OER messages", 1, this.listCollectionfilterOER.size());
	}

	/**
	 * Tests the configuration update.
	 */
	@Test
	public void testConfigUpdate() { // NOPMD-multiple assertions
		// create a default Configuration
		Configuration testConfig = new Configuration();
		testConfig.setProperty(PropertyPlugin.PROPERTY_DO_CHANGE, PropertyPlugin.DEFAULT_PROPERTY_DO_CHANGE);
		testConfig.setProperty(PropertyPlugin.PROPERTY_DO_NOT_CHANGE, PropertyPlugin.DEFAULT_PROPERTY_DO_NOT_CHANGE);
		testConfig.setProperty(PropertyPlugin.PROPERTY_DO_NOT_GET_LOST, PropertyPlugin.DEFAULT_PROPERTY_DO_NOT_GET_LOST);

		// clear the "master configuration"
		this.compositePlugin.getCurrentConfiguration().clear();
		// check if truly empty
		Assert.assertTrue("The CompositePlugins Config is not empty", this.compositePlugin.getCurrentConfiguration().isEmpty());

		final String change = "AENDERUNG";
		// refill "master configuration"
		this.compositePlugin.getCurrentConfiguration().setProperty(
				PropertyPlugin.class.getSimpleName() + "." + PropertyPlugin.PROPERTY_DO_CHANGE, change);
		this.compositePlugin.getCurrentConfiguration().setProperty(
				PropertyPlugin.class.getSimpleName() + PropertyPlugin.PROPERTY_DO_NOT_CHANGE, "huehuae");

		testConfig = this.compositePlugin.updateConfiguration(testConfig, PropertyPlugin.class);
		Assert.assertEquals("The Value was not changed", testConfig.getStringProperty(PropertyPlugin.PROPERTY_DO_CHANGE), change);
		Assert.assertEquals("The Value was changed", testConfig.getStringProperty(PropertyPlugin.PROPERTY_DO_NOT_CHANGE),
				PropertyPlugin.DEFAULT_PROPERTY_DO_NOT_CHANGE);
		Assert.assertEquals("Something happened", testConfig.getStringProperty(PropertyPlugin.PROPERTY_DO_NOT_GET_LOST),
				PropertyPlugin.DEFAULT_PROPERTY_DO_NOT_GET_LOST);

	}

	/**
	 * Tests the selection of outputports.
	 */
	@Test
	public void testOutputportAssociation() { // NOPMD-multiple assertions
		final OutputportsPlugin testPlugin = new OutputportsPlugin(new Configuration(), new AnalysisController());
		final List<PortWrapper> t1 = testPlugin.getOutputPorts(new T1());
		final List<PortWrapper> t2 = testPlugin.getOutputPorts(new T2());
		final List<PortWrapper> t6 = testPlugin.getOutputPorts(new T6());
		final List<PortWrapper> t3 = testPlugin.getOutputPorts(new T3());

		Assert.assertTrue("T1 is not in the list", this.containing(t1, OutputportsPlugin.T1_T3, T1.class));
		Assert.assertFalse("T3 is in the list falsefully", this.containing(t1, OutputportsPlugin.T1_T3, T3.class));
		Assert.assertEquals("Wrong number of Ports found!", 1, t1.size());

		Assert.assertTrue("T2 is not in the list", this.containing(t2, OutputportsPlugin.T2, T2.class));
		Assert.assertEquals("Wrong number of Ports found!", 1, t2.size());

		Assert.assertTrue("T6 is not in the list", this.containing(t6, OutputportsPlugin.T3_T6, T6.class));
		Assert.assertTrue("T6 is not in the list", this.containing(t6, OutputportsPlugin.T5_T6, T6.class));
		Assert.assertEquals("Wrong number of Ports found!", 2, t6.size());

		Assert.assertTrue("T3 is not in the list", this.containing(t3, OutputportsPlugin.T1_T3, T3.class));
		Assert.assertTrue("T3 is not in the list", this.containing(t3, OutputportsPlugin.T3_T6, T3.class));
		Assert.assertEquals("Wrong number of Ports found!", 2, t3.size());

	}

	/**
	 * Checks whether or not the container contains the given port and eventtype.
	 * 
	 * @param container
	 *            container
	 * @param portname
	 *            portname
	 * @param eventType
	 *            eventtype
	 * @return true if the container contains the given port and eventtype
	 */
	public boolean containing(final List<PortWrapper> container, final String portname, final Class<?> eventType) { // NOPMD-multiple assertions
		for (final PortWrapper pw : container) {
			if (pw.getOutputPortName().equals(portname) && pw.getEventClass().isAssignableFrom(eventType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * empty Method to avoid empty catch-blocks(PMD).
	 */
	private void nothing() {
		// doNothing
	}

	/**
	 * Small implementation of AbstractFilterPlugin to test Configuration-updating.
	 * 
	 * @author Markus Fischer
	 * @since 1.10
	 * 
	 */
	@Plugin(description = "Properties",
			configuration = {
				@Property(name = PropertyPlugin.PROPERTY_DO_CHANGE, defaultValue = PropertyPlugin.DEFAULT_PROPERTY_DO_CHANGE),
				@Property(name = PropertyPlugin.PROPERTY_DO_NOT_CHANGE, defaultValue = PropertyPlugin.DEFAULT_PROPERTY_DO_NOT_CHANGE),
				@Property(name = PropertyPlugin.PROPERTY_DO_NOT_GET_LOST, defaultValue = PropertyPlugin.DEFAULT_PROPERTY_DO_NOT_GET_LOST)
			})
	class PropertyPlugin extends AbstractFilterPlugin {

		public static final String PROPERTY_DO_CHANGE = "change";
		public static final String PROPERTY_DO_NOT_CHANGE = "dontChange";
		public static final String PROPERTY_DO_NOT_GET_LOST = "dontGetLost";

		public static final String DEFAULT_PROPERTY_DO_CHANGE = "default_change";
		public static final String DEFAULT_PROPERTY_DO_NOT_CHANGE = "default_dontChange";
		public static final String DEFAULT_PROPERTY_DO_NOT_GET_LOST = "defaul_dontGetLost";

		/**
		 * Constructor.
		 * 
		 * @param configuration
		 *            configuration
		 * @param projectContext
		 *            projectContext
		 */
		public PropertyPlugin(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see kieker.analysis.analysisComponent.AbstractAnalysisComponent#getCurrentConfiguration()
		 */
		@Override
		public Configuration getCurrentConfiguration() {
			return this.configuration;
		}

	}

	/**
	 * Implementation of AbstractCompositeFilterPlugin to test the selection of Outputports.
	 * 
	 * @author Markus Fischer
	 * @since 1.10
	 */
	@Plugin(description = "Outputports",
			outputPorts = {
				@OutputPort(name = OutputportsPlugin.T1_T3, eventTypes = { T1.class, T3.class }),
				@OutputPort(name = OutputportsPlugin.T2, eventTypes = { T2.class }),
				@OutputPort(name = OutputportsPlugin.T3_T6, eventTypes = { T3.class, T6.class }),
				@OutputPort(name = OutputportsPlugin.T4, eventTypes = { T4.class }),
				@OutputPort(name = OutputportsPlugin.T5_T6, eventTypes = { T5.class, T6.class }) })
	static class OutputportsPlugin extends AbstractCompositeFilterPlugin {

		public static final String T1_T3 = "T1_T3";
		public static final String T2 = "T2";
		public static final String T3_T6 = "T3_T6";
		public static final String T4 = "T4";
		public static final String T5_T6 = "T5_T6";

		/**
		 * Constructor.
		 * 
		 * @param configuration
		 *            configuration
		 * @param projectContext
		 *            projectContext
		 */
		public OutputportsPlugin(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return this.configuration;
		}

	}

	// just some classes for test reasons.
	// CHECKSTYLE:OFF
	/**
	 * Dummy class used within the CompositeFilterPluginTest.
	 * 
	 * @since 1.10
	 */
	static class T1 {// NOPMD
		T1() {}// NOPMD
	}// NOPMD

	/**
	 * Dummy class used within the CompositeFilterPluginTest.
	 * 
	 * @since 1.10
	 */
	static class T2 {// NOPMD
		T2() {}// NOPMD
	}// NOPMD

	/**
	 * Dummy class used within the CompositeFilterPluginTest.
	 * 
	 * @since 1.10
	 */
	static class T3 {// NOPMD
		T3() {}// NOPMD
	}// NOPMD

	/**
	 * Dummy class used within the CompositeFilterPluginTest.
	 * 
	 * @since 1.10
	 */
	static class T4 {// NOPMD
		T4() {}// NOPMD
	}// NOPMD

	/**
	 * Dummy class used within the CompositeFilterPluginTest.
	 * 
	 * @since 1.10
	 */
	static class T5 {// NOPMD
		T5() {}// NOPMD
	}// NOPMD

	/**
	 * Dummy class used within the CompositeFilterPluginTest.
	 * 
	 * @since 1.10
	 */
	static class T6 {// NOPMD
		T6() {}// NOPMD
	}// NOPMD
		// CHECKSTYLE:ON

}
