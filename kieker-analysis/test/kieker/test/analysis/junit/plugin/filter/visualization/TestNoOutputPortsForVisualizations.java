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

package kieker.test.analysis.junit.plugin.filter.visualization;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.visualization.AbstractWebVisualizationFilterPlugin;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

/**
 * This JUnit test makes sure that there are no visualizations with output ports in Kieker.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.9
 */
public class TestNoOutputPortsForVisualizations extends AbstractDynamicKiekerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestNoOutputPortsForVisualizations.class);

	public TestNoOutputPortsForVisualizations() {
		// empty default constructor
	}

	@Test
	public void test() throws ClassNotFoundException {
		final Collection<Class<?>> availableClasses = super.deliverAllAvailableClassesFromSourceDirectory();
		final Collection<Class<?>> notAbstractClasses = super.filterOutAbstractClasses(availableClasses);
		final Collection<Class<?>> filteredClasses = super.filterOutClassesNotExtending(AbstractWebVisualizationFilterPlugin.class, notAbstractClasses);

		for (final Class<?> clazz : filteredClasses) {
			LOGGER.info("Testing '{}'...", clazz.getSimpleName());
			Assert.assertFalse(clazz.getSimpleName() + "' is a visualization filter with output ports.",
					TestNoOutputPortsForVisualizations.containsOutputPorts(clazz));
		}
	}

	private static boolean containsOutputPorts(final Class<?> clazz) {
		final Plugin annotation = clazz.getAnnotation(Plugin.class);

		return annotation.outputPorts().length != 0;
	}

}
