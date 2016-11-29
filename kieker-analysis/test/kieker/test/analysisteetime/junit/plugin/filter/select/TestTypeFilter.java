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

package kieker.test.analysisteetime.junit.plugin.filter.select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.select.TypeFilter;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * A test for the class {@link TypeFilter}.
 *
 * @author Nils Christian Ehmke, Lars Bluemke
 *
 * @since 1.8
 */
public class TestTypeFilter extends AbstractKiekerTest {

	private static final Class<?>[] ALLOWED_TYPES = { String.class };

	private static final Object[] MATCHING_OBJECTS = { "First String", "Second String", "Third String", "Fourth String" };
	private static final Object[] MISMATCHING_OBJECTS = { 'A', 42L, 2.5, new Object() };

	/**
	 * Default constructor.
	 */
	public TestTypeFilter() {
		// empty default constructor
	}

	@Test
	public void testFiltering() {
		final TypeFilter typeFilter = new TypeFilter(ALLOWED_TYPES);
		final List<Object> inputElements = new ArrayList<Object>();
		final List<Object> validOutputElements = new ArrayList<Object>();
		final List<Object> invalidOutputElements = new ArrayList<Object>();

		// Make sure that the reader sends valid and invalid objects interleaved
		for (int i = 0; i < Math.min(MATCHING_OBJECTS.length, MISMATCHING_OBJECTS.length); i++) {
			inputElements.add(MATCHING_OBJECTS[i]);
			inputElements.add(MISMATCHING_OBJECTS[i]);
		}

		StageTester.test(typeFilter)
				.and().send(inputElements).to(typeFilter.getInputPort())
				.and().receive(validOutputElements).from(typeFilter.getMatchingTypeOutputPort())
				.and().receive(invalidOutputElements).from(typeFilter.getMismatchingTypeOutputPort())
				.start();

		// Make sure that valid objects are sent to the valid output port and that invalid objects are sent to the invalid output port
		Assert.assertTrue("Type filter filtered matching types", validOutputElements.containsAll(Arrays.asList(MATCHING_OBJECTS)));
		Assert.assertTrue("Type filter did not filter mismatching types", invalidOutputElements.containsAll(Arrays.asList(MISMATCHING_OBJECTS)));
		Assert.assertEquals("Type filter sent too much to the match output port", MATCHING_OBJECTS.length, validOutputElements.size());
		Assert.assertEquals("Type filter sent too much to the mismatch output port.", MISMATCHING_OBJECTS.length, invalidOutputElements.size());
	}
}
