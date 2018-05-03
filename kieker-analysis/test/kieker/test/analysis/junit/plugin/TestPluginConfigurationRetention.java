/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

/**
 * This JUnit test makes sure that all plugins handle their configurations correctly. To be more precise: The listed properties in the annotation of each plugin has
 * to be in the configuration map of the {@code getCurrentConfiguration} method.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.8
 */
public class TestPluginConfigurationRetention extends AbstractDynamicKiekerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestPluginConfigurationRetention.class);

	public TestPluginConfigurationRetention() {
		// empty default constructor
	}

	@Test
	public void test() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		final Collection<Class<?>> availableClasses = super.deliverAllAvailableClassesFromSourceDirectory();
		final Collection<Class<?>> notAbstractClasses = super.filterOutAbstractClasses(availableClasses);
		final Collection<Class<?>> filteredClasses = super.filterOutClassesNotExtending(AbstractPlugin.class, notAbstractClasses);

		for (final Class<?> clazz : filteredClasses) {
			LOGGER.info("Testing '{}'...", clazz.getSimpleName());
			Assert.assertTrue(clazz.getSimpleName() + "' doesn't export all of its properties.", this.isConfigurationCorrect(clazz));
		}
	}

	private boolean isConfigurationCorrect(final Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		final IAnalysisController ac = new AnalysisController();
		final AbstractPlugin pluginInstance = (AbstractPlugin) clazz.getConstructor(Configuration.class, IProjectContext.class).newInstance(new Configuration(), ac);

		final Property[] expectedProperties = this.getExpectedProperties(clazz);
		final Configuration actualConfiguration = this.getActualProperties(pluginInstance);

		for (final Property property : expectedProperties) {
			if (!(actualConfiguration.containsKey(property.name()))) {
				return false;
			}
		}

		return true;
	}

	private Property[] getExpectedProperties(final Class<?> clazz) {
		final Plugin pluginAnnotation = clazz.getAnnotation(Plugin.class);

		if (pluginAnnotation != null) {
			return pluginAnnotation.configuration();
		} else {
			return new Property[0];
		}
	}

	private Configuration getActualProperties(final AbstractPlugin pluginInstance) {
		return pluginInstance.getCurrentConfiguration();
	}

}
