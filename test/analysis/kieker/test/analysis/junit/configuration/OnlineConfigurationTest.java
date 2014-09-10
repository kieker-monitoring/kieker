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

package kieker.test.analysis.junit.configuration;

import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.configuration.AbstractUpdateableFilterPlugin;
import kieker.analysis.configuration.GlobalConfigurationRegistry;
import kieker.analysis.configuration.exception.PluginNotFoundException;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Test for Configuration plugins during runtime.
 * 
 * @author Markus Fischer
 * @since 1.10
 * 
 */
public class OnlineConfigurationTest extends AbstractKiekerTest {

	/**
	 * Id for a Filter.
	 */
	private static final String UPDATEABLE_FILTER_ID = "updateableFilter-ID";

	/**
	 * value for a filters property.
	 */
	private static final String UPDATED = "updated";

	private int noPNFExceptions;

	private final IAnalysisController analysisController;
	private final ListReader<String> simpleListReader;
	private final ListCollectionFilter<String> listCollectionfilterSTR;

	private final Updater updater;
	private final Updateable updateable;

	/**
	 * Mandatory Constructor.
	 */
	public OnlineConfigurationTest() {
		this.analysisController = new AnalysisController();
		this.simpleListReader = new ListReader<String>(new Configuration(), this.analysisController);
		this.listCollectionfilterSTR = new ListCollectionFilter<String>(new Configuration(), this.analysisController);

		this.updater = new Updater(new Configuration(), this.analysisController);
		final Configuration updateableConfig = new Configuration();
		this.updateable = new Updateable(updateableConfig, this.analysisController);
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {

		GlobalConfigurationRegistry.getInstance().registerUpdateableFilterPlugin(UPDATEABLE_FILTER_ID, this.updateable);
		try {
			this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, this.updater, Updater.INPUT);
			this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, this.updateable, Updateable.INPUT);
			this.analysisController.connect(this.updateable, Updateable.OUPUT_STRING, this.listCollectionfilterSTR, ListCollectionFilter.INPUT_PORT_NAME);
			this.simulate();
		} catch (final IllegalStateException e) {
			this.nothing();
			// e.printStackTrace();
		} catch (final AnalysisConfigurationException e) {
			this.nothing();
			// e.printStackTrace();
		}
	}

	/**
	 * Tests if the right number of exceptions was thrown.
	 */
	@Test
	public void numberOfExceptionsTest() {
		Assert.assertEquals("Wrong number of PNF-Exceptions thrown", 1, this.noPNFExceptions);
	}

	/**
	 * Tests if properties were set properly.
	 */
	@Test
	public void testNewProperty() {
		final boolean notYetUpdated = this.listCollectionfilterSTR.getList().get(0).equals(Updateable.TEST_PROPERTY_UPDATEABLE);
		final boolean updated = this.listCollectionfilterSTR.getList().get(1)
				.equals(this.updateable.getCurrentConfiguration().getStringProperty(Updateable.TEST_PROPERTY_UPDATEABLE));
		Assert.assertTrue("Updating wrent wrong ", notYetUpdated && updated);
	}

	/**
	 * Increases exception count.
	 */
	public void increaseExceptionCount() {
		this.noPNFExceptions++;
	}

	/**
	 * empty Method.
	 */
	private void nothing() {
		// empty Method
	}

	/**
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 * 
	 */
	private void simulate() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject("a");
		this.simpleListReader.addObject("b");
		this.simpleListReader.addObject("c");
		this.analysisController.run();
	}

	/**
	 * An inner class for testing purposes. <br>
	 * Reconfigures a Plugin every second message.
	 * 
	 * @author Markus Fischer
	 * @since 1.10
	 * 
	 */
	@Plugin(description = "Updater")
	public class Updater extends AbstractFilterPlugin {

		/**
		 * Inputportname.
		 */
		public static final String INPUT = "updater-input";
		private boolean update;

		/**
		 * Constructor.
		 * 
		 * @param configuration
		 *            configuration
		 * @param projectContext
		 *            projectContext
		 */
		public Updater(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return this.configuration;
		}

		/**
		 * inputport.
		 * 
		 * @param string
		 *            string
		 */
		@InputPort(name = Updater.INPUT, eventTypes = { String.class })
		public void startAnalysis(final String string) {
			final Configuration config = new Configuration();
			config.setProperty(Updateable.TEST_PROPERTY_UPDATEABLE, UPDATED);
			try {
				if (this.update) {
					GlobalConfigurationRegistry.getInstance().updateConfiguration(UPDATEABLE_FILTER_ID, config, true);
					GlobalConfigurationRegistry.getInstance().updateConfiguration("non-Existent-ID", config, true);
				}
			} catch (final PluginNotFoundException e) {
				OnlineConfigurationTest.this.increaseExceptionCount();
				// e.printStackTrace();
			} finally {
				this.update = !this.update;
			}
		}
	}

	/**
	 * Plugin that can be updated.
	 * Passes through its current property value as message.
	 * 
	 * @author Markus Fischer
	 * @since 1.10
	 * 
	 */
	@Plugin(description = "Updateable",
			outputPorts = {
				@OutputPort(name = Updateable.OUPUT_STRING, eventTypes = { String.class }) },
			configuration = {
				@Property(name = Updateable.TEST_PROPERTY_UPDATEABLE, defaultValue = Updateable.TEST_PROPERTY_UPDATEABLE, updateable = true),
				@Property(name = Updateable.TEST_PROPERTY_TWO, defaultValue = Updateable.TEST_PROPERTY_TWO)
			})
	public static class Updateable extends AbstractUpdateableFilterPlugin {

		/**
		 * Inputportname.
		 */
		public static final String INPUT = "updateable-input";

		/**
		 * Outputportname.
		 */
		public static final String OUPUT_STRING = "updateable-string";

		/**
		 * Propertyname.
		 */
		public static final String TEST_PROPERTY_UPDATEABLE = "property-one";

		/**
		 * Propertyname.
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
		public Updateable(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return this.configuration;
		}

		/**
		 * inputportmethod.
		 * 
		 * @param string
		 *            string
		 */
		@InputPort(name = Updateable.INPUT, eventTypes = { String.class })
		public void startAnalysis(final String string) {
			// aktuelle property-werte weitergeben
			final String s = this.getCurrentConfiguration().getStringProperty(TEST_PROPERTY_UPDATEABLE);
			super.deliver(Updateable.OUPUT_STRING, s);
		}

		@Override
		public void setCurrentConfiguration(final Configuration config, final boolean update) {
			for (final Entry<Object, Object> entry : this.configuration.getPropertiesStartingWith("").entrySet()) {
				final String propertyName = (String) entry.getKey();
				if (this.isPropertyUpdateable(propertyName)) {
					this.configuration.setProperty(propertyName, config.getStringProperty(propertyName));
				}
			}
		}
	}

}
