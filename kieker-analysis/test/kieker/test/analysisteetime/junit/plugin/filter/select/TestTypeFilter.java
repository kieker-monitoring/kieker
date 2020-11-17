/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.stage.select.TypeFilter;

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
		final List<Object> inputElements = new ArrayList<>();

		// Make sure that the reader sends valid and invalid objects interleaved
		for (int i = 0; i < Math.min(MATCHING_OBJECTS.length, MISMATCHING_OBJECTS.length); i++) {
			inputElements.add(MATCHING_OBJECTS[i]);
			inputElements.add(MISMATCHING_OBJECTS[i]);
		}

		StageTester.test(typeFilter).and().send(inputElements).to(typeFilter.getInputPort()).start();

		Assert.assertThat("Type filter, valid data not correctly determined.", typeFilter.getMatchingTypeOutputPort(), StageTester.produces(MATCHING_OBJECTS));
		Assert.assertThat("Type filter, invalid data not correctly determined.",
				typeFilter.getMismatchingTypeOutputPort(), StageTester.produces(MISMATCHING_OBJECTS));
	}
}
