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
import kieker.analysis.configuration.GlobalConfigurationRegistry;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.exception.PluginNotFoundException;
import kieker.analysis.plugin.AbstractUpdateableFilterPlugin;
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
 * This is a test making sure that it is possible to update the configuration of specific plugins during runtime.
 * 
 * @author Markus Fischer, Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class OnlineConfigurationTest extends AbstractKiekerTest {

	public OnlineConfigurationTest() {
		// No code necessary
	}

	@Test(expected = PluginNotFoundException.class)
	public void testUpdatingNonExistingPlugin() throws PluginNotFoundException {
		GlobalConfigurationRegistry.getInstance().updateConfiguration(42, new Configuration(), true);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testUpdatingPlugin() throws PluginNotFoundException, IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController analysisController = new AnalysisController();

		final ListReader<String> reader = new ListReader<String>(new Configuration(), analysisController);
		final UpdateableFilter filter = new UpdateableFilter(new Configuration(), analysisController);

		// Make sure the default configuration is loaded
		Assert.assertEquals("original-configuration", filter.getConfiguredContent());

		analysisController.run();

		// Now update the configuration
		final int pluginID = GlobalConfigurationRegistry.getInstance().registerUpdateableFilterPlugin(filter);
	
		final Configuration newConfiguration = new Configuration();
		newConfiguration.setProperty(UpdateableFilter.PROPERTY_NAME, "new-configuration");
		GlobalConfigurationRegistry.getInstance().updateConfiguration(pluginID, newConfiguration, true);
		
		Assert.assertEquals("new-configuration", filter.getConfiguredContent());
	}

	/**
	 * A simple plugin that can be updated.
	 * 
	 * @author Markus Fischer, Nils Christian Ehmke
	 * 
	 * @since 1.10
	 */
	@Plugin(programmaticOnly = true,
			configuration = @Property(name = UpdateableFilter.PROPERTY_NAME, defaultValue = UpdateableFilter.PROPERTY_VALUE, updateable = true))
	public static class UpdateableFilter extends AbstractUpdateableFilterPlugin {

		public static final String PROPERTY_NAME = "property";
		public static final String PROPERTY_VALUE = "original-configuration";

		private String configuredContent;

		public UpdateableFilter(Configuration configuration, IProjectContext projectContext) {
			super(configuration, projectContext);

			configuredContent = configuration.getStringProperty(PROPERTY_NAME);
		}

		public String getConfiguredContent() {

			return configuredContent;
		}

		@Override
		public void setCurrentConfiguration(final Configuration configuration, final boolean update) {
			if (!update || isPropertyUpdateable(PROPERTY_NAME)) {
				this.configuredContent = configuration.getStringProperty(PROPERTY_NAME);
			}
		}

		@Override
		public Configuration getCurrentConfiguration() {
			throw new UnsupportedOperationException();
		}
	}

}
