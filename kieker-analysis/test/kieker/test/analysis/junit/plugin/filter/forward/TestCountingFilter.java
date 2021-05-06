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

package kieker.test.analysis.junit.plugin.filter.forward;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This test is for the class {@link CountingFilter}.
 *
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.6
 */
public class TestCountingFilter extends AbstractKiekerTest {

	private IAnalysisController analysisController;
	private ListReader<Object> simpleListReader;
	private CountingFilter countingFilter;

	/**
	 * Default constructor.
	 */
	public TestCountingFilter() {
		// empty default constructor
	}

	/**
	 * This method prepares the test setup.
	 *
	 * @throws IllegalStateException
	 *             If something went wrong during the test setup (should not happen).
	 * @throws AnalysisConfigurationException
	 *             If something went wrong during the test setup (should not happen).
	 */
	@Before
	public void before() throws IllegalStateException, AnalysisConfigurationException {
		this.analysisController = new AnalysisController();
		this.countingFilter = new CountingFilter(new Configuration(), this.analysisController);
		this.simpleListReader = new ListReader<>(new Configuration(), this.analysisController);

		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, this.countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
	}

	/**
	 * A simple test for the counting filter.
	 *
	 * @throws IllegalStateException
	 *             If the test setup is somehow invalid (should not happen).
	 * @throws AnalysisConfigurationException
	 *             If the test setup is somehow invalid (should not happen).
	 */
	@Test
	public void testNormal() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(new Object());
		this.simpleListReader.addObject(new Object());
		this.simpleListReader.addObject(new Object());
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		this.analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}

	/**
	 * A test for the counting filter with multiple readers.
	 *
	 * @throws IllegalStateException
	 *             If the test setup is somehow invalid (should not happen).
	 * @throws AnalysisConfigurationException
	 *             If the test setup is somehow invalid (should not happen).
	 */
	@Test
	public void testConcurrently() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(new Object());
		// register multiple readers (they fire concurrently)
		final ListReader<Object> reader2 = new ListReader<>(new Configuration(), this.analysisController);
		this.analysisController.connect(reader2, ListReader.OUTPUT_PORT_NAME, this.countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
		reader2.addObject(new Object());
		final ListReader<Object> reader3 = new ListReader<>(new Configuration(), this.analysisController);
		this.analysisController.connect(reader3, ListReader.OUTPUT_PORT_NAME, this.countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
		reader3.addObject(new Object());
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		this.analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}

	/**
	 * A simple test for the counting filter using objects of different classes.
	 *
	 * @throws IllegalStateException
	 *             If the test setup is somehow invalid (should not happen).
	 * @throws AnalysisConfigurationException
	 *             If the test setup is somehow invalid (should not happen).
	 */
	@Test
	public void testDifferentClasses() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(Long.valueOf(10));
		this.simpleListReader.addObject(null);
		this.simpleListReader.addObject("");
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		this.analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());
		Assert.assertEquals(2, this.countingFilter.getMessageCount());
	}
}
