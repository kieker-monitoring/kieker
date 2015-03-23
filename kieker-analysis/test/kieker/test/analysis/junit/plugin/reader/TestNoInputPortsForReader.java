/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.reader;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

/**
 * This JUnit test makes sure that there are no readers with input ports in Kieker.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class TestNoInputPortsForReader extends AbstractDynamicKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestNoInputPortsForReader.class);

	public TestNoInputPortsForReader() {
		// empty default constructor
	}

	@Test
	public void test() throws ClassNotFoundException {
		final Collection<Class<?>> availableClasses = super.deliverAllAvailableClassesFromSourceDirectory();
		final Collection<Class<?>> notAbstractClasses = super.filterOutAbstractClasses(availableClasses);
		final Collection<Class<?>> filteredClasses = super.filterOutClassesNotExtending(AbstractReaderPlugin.class, notAbstractClasses);

		for (final Class<?> clazz : filteredClasses) {
			LOG.info("Testing '" + clazz.getSimpleName() + "'...");
			Assert.assertFalse(clazz.getSimpleName() + "' is a reader with input ports.", TestNoInputPortsForReader.containsInputPorts(clazz));
		}
	}

	private static boolean containsInputPorts(final Class<?> clazz) {
		for (final Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(InputPort.class)) {
				return true;
			}
		}
		return false;
	}

}
