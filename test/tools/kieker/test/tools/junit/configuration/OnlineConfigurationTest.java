/**
 * 
 */
package kieker.test.tools.junit.configuration;

import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals; // NOCS
import static org.junit.Assert.assertTrue; // NOCS

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;

import kieker.tools.configuration.AbstractUpdateableFilterPlugin;

import kieker.tools.configuration.GlobalConfigurationRegistry;
import kieker.tools.configuration.exception.PluginNotFoundException;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Test for Configuration plugins during runtime.
 * 
 * @author Markus Fischer
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

	private IAnalysisController analysisController;
	private ListReader<String> simpleListReader;
	private ListCollectionFilter<String> listCollectionfilterSTR;

	private Updateable updateable;

	/**
	 * Mandatory Constructor.
	 */
	public OnlineConfigurationTest() {
		//
	}

	/**
	 * Set up the tests.
	 */
	@Before
	public void setUp() {
		this.analysisController = new AnalysisController();
		this.simpleListReader = new ListReader<String>(new Configuration(), this.analysisController);
		this.listCollectionfilterSTR = new ListCollectionFilter<String>(new Configuration(), this.analysisController);

		final Updater updater = new Updater(new Configuration(), this.analysisController);
		final Configuration updateableConfig = new Configuration();
		this.updateable = new Updateable(updateableConfig, this.analysisController);
		GlobalConfigurationRegistry.getInstance().registerUpdateableFilterPlugin(UPDATEABLE_FILTER_ID, this.updateable);
		try {
			this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, updater, Updater.INPUT);
			this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, this.updateable, Updateable.INPUT);
			this.analysisController.connect(this.updateable, Updateable.OUPUT_STRING, this.listCollectionfilterSTR, ListCollectionFilter.INPUT_PORT_NAME);
			this.simulate();
		} catch (IllegalStateException e) {
			this.nothing();
			// e.printStackTrace();
		} catch (AnalysisConfigurationException e) {
			this.nothing();
			// e.printStackTrace();
		}
	}

	/**
	 * Tests if the right number of exceptions was thrown.
	 */
	@Test
	public void numberOfExceptionsTest() {
		assertEquals("Wrong number of PNF-Exceptions thrown", 1, this.noPNFExceptions);
	}

	/**
	 * Tests if properties were set properly.
	 */
	@Test
	public void testNewProperty() {
		final boolean notYetUpdated = this.listCollectionfilterSTR.getList().get(0).equals(Updateable.TEST_PROPERTY_UPDATEABLE);
		final boolean updated = this.listCollectionfilterSTR.getList().get(1)
				.equals(this.updateable.getCurrentConfiguration().getStringProperty(Updateable.TEST_PROPERTY_UPDATEABLE));
		assertTrue("Updating wrent wrong ", notYetUpdated && updated);
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
			return configuration;
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
			} catch (PluginNotFoundException e) {
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
	 * 
	 */
	@Plugin(description = "Updateable",
			outputPorts = {
				@OutputPort(name = Updateable.OUPUT_STRING, eventTypes = { String.class }) },
			configuration = {
				@Property(name = Updateable.TEST_PROPERTY_UPDATEABLE, defaultValue = Updateable.TEST_PROPERTY_UPDATEABLE, updateable = true),
				@Property(name = Updateable.TEST_PROPERTY_TWO, defaultValue = Updateable.TEST_PROPERTY_TWO)
			})
	public class Updateable extends AbstractUpdateableFilterPlugin {

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
			return configuration;
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
				if (isPropertyUpdateable(propertyName)) {
					this.configuration.setProperty(propertyName, config.getStringProperty(propertyName));
				}
			}
		}
	}

}
