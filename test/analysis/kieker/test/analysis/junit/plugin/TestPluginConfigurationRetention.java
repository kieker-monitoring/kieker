/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.cxf.helpers.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This JUnit test makes sure that all plugins handle their configurations correctly. To be more precise: The listed properties in the annotation of each plugin has
 * to be in the configuration map of the {@code getCurrentConfiguration} method.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class TestPluginConfigurationRetention extends AbstractKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestPluginConfigurationRetention.class);

	private static final String DIR_NAME_SOURCES = "src";
	private static final String PATTERN_JAVA_SOURCE_FILES = ".*java";

	/**
	 * Default constructor.
	 */
	public TestPluginConfigurationRetention() {
		// empty default constructor
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		final List<File> sourceFiles = TestPluginConfigurationRetention.listJavaSourceFiles();
		for (final File sourceFile : sourceFiles) {
			final String className = TestPluginConfigurationRetention.sourceFileToClassName(sourceFile);
			final Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
			if (TestPluginConfigurationRetention.doesClassExtendAbstractPlugin(clazz) && !(this.isClassAbstract(clazz))) {
				LOG.info("Testing class '" + className + "'...");
				if (!this.isConfigurationCorrect((Class<? extends AbstractPlugin>) clazz)) {
					Assert.fail("Class '" + className + "' doesn't export all of its properties.");
				}
			}
		}
	}

	private boolean isConfigurationCorrect(final Class<? extends AbstractPlugin> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final IAnalysisController ac = new AnalysisController();
		final AbstractPlugin pluginInstance = clazz.getConstructor(Configuration.class, IProjectContext.class).newInstance(new Configuration(), ac);

		final Property[] expectedProperties = this.getExpectedPropertiesFromAnnotation(clazz);
		final Configuration actualConfiguration = pluginInstance.getCurrentConfiguration();

		for (final Property property : expectedProperties) {
			if (!(actualConfiguration.containsKey(property.name()))) {
				return false;
			}
		}

		return true;
	}

	private Property[] getExpectedPropertiesFromAnnotation(final Class<?> clazz) {
		final Plugin pluginAnnotation = clazz.getAnnotation(Plugin.class);

		if (pluginAnnotation != null) {
			return pluginAnnotation.configuration();
		} else {
			return new Property[0];
		}
	}

	private boolean isClassAbstract(final Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	private static List<File> listJavaSourceFiles() {
		return FileUtils.getFilesRecurse(new File(DIR_NAME_SOURCES), PATTERN_JAVA_SOURCE_FILES);
	}

	private static boolean doesClassExtendAbstractPlugin(final Class<?> clazz) {
		return AbstractPlugin.class.isAssignableFrom(clazz);
	}

	private static String sourceFileToClassName(final File file) {
		final String pathName = file.getPath();
		String className = pathName.substring(0, pathName.length() - 5).replace(File.separator, ".");
		final int secondPointPos = className.indexOf('.', 5);
		className = className.substring(secondPointPos + 1);

		return className;
	}
}
