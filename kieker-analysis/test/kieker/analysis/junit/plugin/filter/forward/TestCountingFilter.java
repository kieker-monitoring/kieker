/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.junit.plugin.filter.forward;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.generic.CountingFilter;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * This test is for the class {@link CountingFilter}.
 *
 * @author Nils Christian Ehmke, Jan Waller, Lars Bluemke
 *
 * @since 1.6
 */
public class TestCountingFilter extends AbstractKiekerTest {

	private CountingFilter countingFilter;
	private List<Object> testElements;

	/**
	 * Empty default constructor.
	 */
	public TestCountingFilter() {
		// empty default constructor
	}

	/**
	 * Initializes a new filter and array of test elements before each test.
	 */
	@Before
	public void initializeNewFilter() {
		this.countingFilter = new CountingFilter();
		this.testElements = new ArrayList<>();
	}

	/**
	 * A simple test for the counting filter.
	 */
	@Test
	public void testNormal() {
		this.testElements.add(new Object());
		this.testElements.add(new Object());
		this.testElements.add(new Object());
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		StageTester.test(this.countingFilter).and().send(this.testElements).to(this.countingFilter.getInputPort()).start();
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}

	/**
	 * A simple test for the counting filter using objects of different classes.
	 */
	@Test
	public void testDifferentClasses() {
		this.testElements.add(Long.valueOf(10));
		this.testElements.add(new Object());
		this.testElements.add("");
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		StageTester.test(this.countingFilter).and().send(this.testElements).to(this.countingFilter.getInputPort()).start();
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}
}
