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

package kicker.test.analysis.junit.plugin.filter.select;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import kicker.analysis.AnalysisController;
import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.analysis.plugin.filter.forward.ListCollectionFilter;
import kicker.analysis.plugin.filter.select.TypeFilter;
import kicker.analysis.plugin.reader.list.ListReader;
import kicker.common.configuration.Configuration;
import kicker.test.common.junit.AbstractKickerTest;

/**
 * A test for the class {@link TypeFilter}.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class TypeFilterTest extends AbstractKickerTest {

	private static final String ALLOWED_TYPES = String.class.getName();

	private static final Object[] MATCHING_OBJECTS = { "First String", "Second String", "Third String", "Fourth String" };
	private static final Object[] MISMATCHING_OBJECTS = { 'A', 42L, 2.5, new Object() };

	/**
	 * Default constructor.
	 */
	public TypeFilterTest() {
		// empty default constructor
	}

	@Test
	public void testFiltering() throws IllegalStateException, AnalysisConfigurationException {
		final Configuration typeFilterConfiguration = new Configuration();
		typeFilterConfiguration.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, TypeFilterTest.ALLOWED_TYPES);

		// Create the plugins
		final AnalysisController controller = new AnalysisController();
		final ListReader<Object> reader = new ListReader<Object>(new Configuration(), controller);
		final TypeFilter typeFilter = new TypeFilter(typeFilterConfiguration, controller);
		final ListCollectionFilter<Object> sinkPluginValidElements = new ListCollectionFilter<Object>(new Configuration(), controller);
		final ListCollectionFilter<Object> sinkPluginInvalidElements = new ListCollectionFilter<Object>(new Configuration(), controller);

		// Connect the plugins
		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, typeFilter, TypeFilter.INPUT_PORT_NAME_EVENTS);
		controller.connect(typeFilter, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH, sinkPluginValidElements, ListCollectionFilter.INPUT_PORT_NAME);
		controller.connect(typeFilter, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH, sinkPluginInvalidElements, ListCollectionFilter.INPUT_PORT_NAME);

		// Make sure that the reader sends valid and invalid objects interleaved
		for (int i = 0; i < Math.min(MATCHING_OBJECTS.length, MISMATCHING_OBJECTS.length); i++) {
			reader.addObject(MATCHING_OBJECTS[i]);
			reader.addObject(MISMATCHING_OBJECTS[i]);
		}

		controller.run();

		// Make sure that valid objects are sent to the valid output port and that invalid objects are sent to the invalid output port
		Assert.assertTrue("Type filter filtered matching types", sinkPluginValidElements.getList().containsAll(Arrays.asList(MATCHING_OBJECTS)));
		Assert.assertTrue("Type filter did not filter mismatching types", sinkPluginInvalidElements.getList().containsAll(Arrays.asList(MISMATCHING_OBJECTS)));
		Assert.assertEquals("Type filter sent too much to the match output port", MATCHING_OBJECTS.length, sinkPluginValidElements.getList().size());
		Assert.assertEquals("Type filter sent too much to the mismatch output port.", MISMATCHING_OBJECTS.length, sinkPluginInvalidElements.getList().size());
	}
}
