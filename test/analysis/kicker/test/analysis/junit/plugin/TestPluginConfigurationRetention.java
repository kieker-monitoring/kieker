/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.analysis.junit.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import kicker.analysis.AnalysisController;
import kicker.analysis.IAnalysisController;
import kicker.analysis.IProjectContext;
import kicker.analysis.plugin.AbstractPlugin;
import kicker.analysis.plugin.annotation.Plugin;
import kicker.analysis.plugin.annotation.Property;
import kicker.common.configuration.Configuration;
import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.test.common.junit.AbstractDynamicKickerTest;

/**
 * This JUnit test makes sure that all plugins handle their configurations correctly. To be more precise: The listed properties in the annotation of each plugin has
 * to be in the configuration map of the {@code getCurrentConfiguration} method.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class TestPluginConfigurationRetention extends AbstractDynamicKickerTest {

	private static final Log LOG = LogFactory.getLog(TestPluginConfigurationRetention.class);

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
			LOG.info("Testing '" + clazz.getSimpleName() + "'...");
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
